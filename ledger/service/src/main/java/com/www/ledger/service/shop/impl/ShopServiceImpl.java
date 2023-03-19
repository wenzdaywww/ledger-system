package com.www.ledger.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.code.CodeDict;
import com.www.common.config.security.entity.SysUserEntity;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.common.utils.UidGeneratorUtils;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.entity.ShopSalesEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.mapper.ShopSalesMapper;
import com.www.ledger.data.mapper.YearSalesMapper;
import com.www.ledger.data.properties.LedgerProperties;
import com.www.ledger.service.entity.IShopSalesService;
import com.www.ledger.service.entity.IUserShopService;
import com.www.ledger.service.shop.IShopService;
import com.www.ledger.service.user.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>@Description 我的店铺service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
@Slf4j
@Service
public class ShopServiceImpl implements IShopService {
    @Autowired
    private YearSalesMapper yearSalesMapper;
    @Autowired
    private ShopSalesMapper shopSalesMapper;
    @Autowired
    private IUserShopService userShopService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IShopSalesService shopSalesService;
    @Autowired
    private LedgerProperties ledgerProperties;

    /**
     * <p>@Description 统计店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param userId 用户ID
     * @return Response<java.lang.String>
     */
    @Override
    public Response<String> saveAndCountShopData(String userId) {
        //统计的店销售额
        List<ShopDTO> countList = yearSalesMapper.countShopData(userId);
        //查询的店销售额
        QueryWrapper<ShopSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ShopSalesEntity::getUserId,userId);
        List<ShopSalesEntity> shopList = shopSalesMapper.selectList(wrapper);
        //数据转换处理，key=店铺ID，如：1013
        Map<Long, ShopSalesEntity> entityMap = CollectionUtils.isEmpty(shopList) ? new HashMap<>()
                : shopList.stream().collect(Collectors.toMap(k -> k.getShopId(), month -> month));
        Map<Long, ShopDTO> dtoMap =  CollectionUtils.isEmpty(countList) ? new HashMap<>()
                : countList.stream().collect(Collectors.toMap(k -> k.getShopId(), month -> month));
        //处理查询的店销售额
        shopList.forEach(entity -> {
            //如果查下的店销售额(shopList)在统计出的店销售额(dtoMap)中有数据，则更新对应数据字段
            if(dtoMap.containsKey(entity.getShopId())){
                ShopDTO shopDTO = dtoMap.get(entity.getShopId());
                entity.setTotalOrder(shopDTO.getTotalOrder() == null ? 0L : shopDTO.getTotalOrder())
                        .setSucceedOrder(shopDTO.getSucceedOrder() == null ? 0L : shopDTO.getSucceedOrder())
                        .setFailedOrder(shopDTO.getFailedOrder() == null ? 0L : shopDTO.getFailedOrder())
                        .setSaleAmount(MoneyUtils.nullToZero(shopDTO.getSaleAmount()))
                        .setCostAmount(MoneyUtils.nullToZero(shopDTO.getCostAmount()))
                        .setAdvertAmount(MoneyUtils.nullToZero(shopDTO.getAdvertAmount()))
                        .setServiceAmount(MoneyUtils.nullToZero(shopDTO.getServiceAmount()))
                        .setVirtualAmount(MoneyUtils.nullToZero(shopDTO.getVirtualAmount()));
            }else {//如果查下的店销售额(shopList)在统计出的店销售额(dtoMap)中没有数据，则对应数据字段设置为0
                entity.setTotalOrder(0L).setSucceedOrder(0L).setFailedOrder(0L).setSaleAmount(BigDecimal.ZERO)
                      .setCostAmount(BigDecimal.ZERO).setAdvertAmount(BigDecimal.ZERO)
                      .setServiceAmount(BigDecimal.ZERO).setVirtualAmount(BigDecimal.ZERO);
            }
            entity.setUpdateTime(DateUtils.getCurrentDateTime());
            //计算月毛利润=月销售额-月成本费
            entity.setGrossProfit((entity.getSaleAmount().subtract(entity.getCostAmount())).setScale(2, RoundingMode.HALF_UP));
            //计算月毛利率=月毛利润/月成本费
            entity.setGrossProfitRate( entity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    : (entity.getGrossProfit().divide(entity.getCostAmount(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
            //月净利润=月毛利润-月推广费-月服务费-月刷单费
            entity.setRetainedProfits((entity.getGrossProfit().subtract(entity.getAdvertAmount())).subtract(entity.getServiceAmount()).subtract(entity.getVirtualAmount()));
            //月净利率=月净利润/(月成本+月推广费+月服务费+月刷单费)
            BigDecimal totalAmt = entity.getCostAmount().add(entity.getAdvertAmount()).add(entity.getServiceAmount()).add(entity.getVirtualAmount());
            entity.setRetainedProfitsRate(totalAmt.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    :(entity.getRetainedProfits().divide(totalAmt,5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        });
        shopSalesService.updateBatchById(shopList,100);
        return new Response<>(ResponseEnum.SUCCESS,"统计完成");
    }
    /**
     * <p>@Description 新增店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return Response<java.lang.String>
     */
    @Override
    public Response<String> createShop(ShopDTO dto) {
        Response<String> response = new Response<>();
        if(CodeDict.isIllegalValue(CodeTypeEnum.ShopPlatform_Pdd.getType(), dto.getShopType())){
            response.setResponse(ResponseEnum.FAIL,"店铺平台信息有误，新增失败");
            return response;
        }
        SysUserEntity userEntity = userInfoService.findUserById(dto.getUserId());
        if(userEntity == null){
            response.setResponse(ResponseEnum.FAIL,"查询不到该用户信息，新增失败");
            return response;
        }
        UserShopEntity shopEntity = new UserShopEntity();
        shopEntity.setShopId(UidGeneratorUtils.getRedisUid(ledgerProperties.getShopidRedisKey(),5))
                .setShopName(dto.getShopName())
                .setUserId(dto.getUserId())
                .setShopType(dto.getShopType());
        userShopService.save(shopEntity);
            ShopSalesEntity salesEntity = new ShopSalesEntity();
            salesEntity.setShopId(shopEntity.getShopId())
                    .setUserId(shopEntity.getUserId());
        shopSalesService.save(salesEntity);
        response.setResponse(ResponseEnum.SUCCESS,"新增成功");
        return response;
    }

    /**
     * <p>@Description 修改店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return Response<java.lang.String>
     */
    @Override
    public Response<String> updateShop(ShopDTO dto) {
        Response<String> response = new Response<>();
        if(dto == null || dto.getShopId() == null || StringUtils.isAnyBlank(dto.getShopName(),dto.getShopType())
                || CodeDict.isIllegalValue(CodeTypeEnum.ShopPlatform_Pdd.getType(), dto.getShopType())){
            response.setResponse(ResponseEnum.FAIL,"店铺名称和店铺平台信息有误，修改失败");
            return response;
        }
        UpdateWrapper<UserShopEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserShopEntity::getShopId,dto.getShopId())
                .set(UserShopEntity::getShopName,dto.getShopName())
                .set(UserShopEntity::getShopType,dto.getShopType())
                .set(UserShopEntity::getUpdateTime,DateUtils.getCurrentDateTime());
        if(userShopService.update(wrapper)){
            response.setResponse(ResponseEnum.SUCCESS,"修改成功");
        }else {
            response.setResponse(ResponseEnum.FAIL,"修改失败");
        }
        return response;
    }

    /**
     * <p>@Description 删除店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param shopId 店铺ID
     * @return Response<java.lang.String>
     */
    @Override
    public Response<String> deleteShop(String shopId) {
        Response<String> response = new Response<>();
        UpdateWrapper<UserShopEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserShopEntity::getShopId,shopId)
                .set(UserShopEntity::getShopState,CodeDict.getValue(CodeTypeEnum.ShopState_Logout.getType(), CodeTypeEnum.ShopState_Logout.getKey()))
                .set(UserShopEntity::getUpdateTime,DateUtils.getCurrentDateTime());
        if(userShopService.update(wrapper)){
            response.setResponse(ResponseEnum.SUCCESS,"删除成功");
        }else {
            response.setResponse(ResponseEnum.FAIL,"删除失败");
        }
        return response;
    }

    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param dto 店铺信息查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return Response<java.util.List < com.www.ledger.data.dto.ShopDTO>>
     */
    @Override
    public Response<List<ShopDTO>> findShopList(ShopDTO dto, int pageNum, long pageSize) {
        Page<ShopDTO> page = new Page<>(pageNum,pageSize);
        page = shopSalesMapper.findShopList(page,dto);
        List<ShopDTO> shopList =  page.getRecords();
        if(CollectionUtils.isNotEmpty(shopList)){
            shopList.forEach(d -> {
                d.setShopTypeName(CodeDict.getCodeValueName(CodeTypeEnum.ShopPlatform_Pdd.getType(), d.getShopType()));
            });
        }
        Response<List<ShopDTO>> response = new Response<>(ResponseEnum.SUCCESS,shopList);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalNum(page.getTotal());
        return response;
    }

    /**
     * <p>@Description 查询用户的所有店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Response<List<ShopDTO>> finUserShop(String userId) {
        QueryWrapper<UserShopEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserShopEntity::getUserId,userId)
                .eq(UserShopEntity::getShopState,CodeDict.getValue(CodeTypeEnum.ShopState_Valid.getType(), CodeTypeEnum.ShopState_Valid.getKey()));
        List<UserShopEntity> shopList = userShopService.list(wrapper);
        List<ShopDTO> dtoList = Optional.ofNullable(shopList).filter(e -> CollectionUtils.isNotEmpty(shopList))
                .map(list -> {
                    List<ShopDTO> temList = new ArrayList<>();
                    list.forEach(entity -> {
                        ShopDTO dto = new ShopDTO();
                        dto.setShopId(entity.getShopId()).setShopName(entity.getShopName());
                        temList.add(dto);
                    });
                    return temList;
                }).orElse(null);
        return new Response<>(ResponseEnum.SUCCESS,dtoList);
    }
}
