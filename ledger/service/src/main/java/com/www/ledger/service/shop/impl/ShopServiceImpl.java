package com.www.ledger.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.config.security.entity.SysUserEntity;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.common.utils.BigDecimalUtils;
import com.www.common.utils.UidGeneratorUtils;
import com.www.ledger.data.dao.IShopSalesDAO;
import com.www.ledger.data.dao.IUserShopDAO;
import com.www.ledger.data.dao.IYearSalesDAO;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.entity.ShopSalesEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.properties.LedgerProperties;
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
    private IUserShopDAO userShopDAO;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IShopSalesDAO shopSalesDAO;
    @Autowired
    private IYearSalesDAO yearSalesDAO;
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
    public Result<String> saveAndCountShopData(String userId) {
        //统计的店销售额
        List<ShopDTO> countList = yearSalesDAO.countShopData(userId);
        //查询的店销售额
        List<ShopSalesEntity> shopList = shopSalesDAO.findShopSalesList(userId);
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
                        .setSaleAmount(BigDecimalUtils.nullToZero(shopDTO.getSaleAmount()))
                        .setCostAmount(BigDecimalUtils.nullToZero(shopDTO.getCostAmount()))
                        .setAdvertAmount(BigDecimalUtils.nullToZero(shopDTO.getAdvertAmount()))
                        .setServiceAmount(BigDecimalUtils.nullToZero(shopDTO.getServiceAmount()))
                        .setVirtualAmount(BigDecimalUtils.nullToZero(shopDTO.getVirtualAmount()));
            }else {//如果查下的店销售额(shopList)在统计出的店销售额(dtoMap)中没有数据，则对应数据字段设置为0
                entity.setTotalOrder(0L).setSucceedOrder(0L).setFailedOrder(0L).setSaleAmount(BigDecimal.ZERO)
                      .setCostAmount(BigDecimal.ZERO).setAdvertAmount(BigDecimal.ZERO)
                      .setServiceAmount(BigDecimal.ZERO).setVirtualAmount(BigDecimal.ZERO);
            }
            entity.setUpdateTime(DateUtils.getCurrentDateTime());
            //计算店支出费=店成本费+店推广费+店服务费+店刷单费
            entity.setTotalCost(entity.getCostAmount().add(entity.getAdvertAmount()).add(entity.getServiceAmount()).add(entity.getVirtualAmount()));
            //计算店毛利润=店销售额-店成本费
            entity.setGrossProfit(entity.getSaleAmount().subtract(entity.getCostAmount()));
            //计算店店毛利率=店毛利润/店成本费 * 100%
            entity.setGrossProfitRate( entity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    : (entity.getGrossProfit().divide(entity.getCostAmount(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
            //店净利润=店销售额-店支出费
            entity.setRetainedProfits((entity.getGrossProfit().subtract(entity.getAdvertAmount())).subtract(entity.getServiceAmount()).subtract(entity.getVirtualAmount()));
            //店净利率=店净利润/店支出费 * 100%
            entity.setRetainedProfitsRate(entity.getTotalCost().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    :(entity.getRetainedProfits().divide(entity.getTotalCost(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        });
        shopSalesDAO.updateBatchById(shopList,100);
        return new Result<>("统计完成");
    }
    /**
     * <p>@Description 新增店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return Response<java.lang.String>
     */
    @Override
    public Result<String> createShop(ShopDTO dto) {
        if(CodeDict.isIllegalValue(CodeTypeEnum.ShopPlatform_Pdd.getType(), dto.getShopType())){
            throw new BusinessException("店铺平台信息有误，新增失败");
        }
        SysUserEntity userEntity = userInfoService.findUserById(dto.getUserId());
        if(userEntity == null){
            throw new BusinessException("查询不到该用户信息，新增失败");
        }
        UserShopEntity shopEntity = new UserShopEntity();
        shopEntity.setShopId(UidGeneratorUtils.getRedisUid(ledgerProperties.getShopidRedisKey(),5))
                .setShopName(dto.getShopName())
                .setUserId(dto.getUserId())
                .setShopType(dto.getShopType());
        userShopDAO.save(shopEntity);
        ShopSalesEntity salesEntity = new ShopSalesEntity();
        salesEntity.setShopId(shopEntity.getShopId())
                .setUserId(shopEntity.getUserId());
        shopSalesDAO.save(salesEntity);
        return new Result<>("新增成功");
    }

    /**
     * <p>@Description 修改店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return Response<java.lang.String>
     */
    @Override
    public Result<String> updateShop(ShopDTO dto) {
        if(dto == null || dto.getShopId() == null || StringUtils.isAnyBlank(dto.getShopName(),dto.getShopType())
                || CodeDict.isIllegalValue(CodeTypeEnum.ShopPlatform_Pdd.getType(), dto.getShopType())){
            throw new BusinessException("店铺名称和店铺平台信息有误，修改失败");
        }
        UpdateWrapper<UserShopEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserShopEntity::getShopId,dto.getShopId())
                .set(UserShopEntity::getShopName,dto.getShopName())
                .set(UserShopEntity::getShopType,dto.getShopType())
                .set(UserShopEntity::getUpdateTime,DateUtils.getCurrentDateTime());
        if(userShopDAO.update(wrapper)){
             return new Result<>("修改成功");
        }
        return new Result<>("修改失败");
    }

    /**
     * <p>@Description 删除店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param shopId 店铺ID
     * @return Response<java.lang.String>
     */
    @Override
    public Result<String> deleteShop(String shopId) {
        UpdateWrapper<UserShopEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserShopEntity::getShopId,shopId)
                .set(UserShopEntity::getShopState,CodeDict.getValue(CodeTypeEnum.ShopState_Logout.getType(), CodeTypeEnum.ShopState_Logout.getKey()))
                .set(UserShopEntity::getUpdateTime,DateUtils.getCurrentDateTime());
        if(userShopDAO.update(wrapper)){
            return new Result<>("删除成功");
        }
        return new Result<>("删除失败");
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
    public Result<List<ShopDTO>> findShopList(ShopDTO dto, int pageNum, long pageSize) {
        Page<ShopDTO> page = new Page<>(pageNum,pageSize);
        page = shopSalesDAO.findShopList(page,dto);
        List<ShopDTO> shopList =  page.getRecords();
        if(CollectionUtils.isNotEmpty(shopList)){
            shopList.forEach(d -> {
                d.setShopTypeName(CodeDict.getCodeValueName(CodeTypeEnum.ShopPlatform_Pdd.getType(), d.getShopType()));
            });
        }
        Result<List<ShopDTO>> result = new Result<>(shopList);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalNum(page.getTotal());
        return result;
    }

    /**
     * <p>@Description 查询用户的所有店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Result<List<ShopDTO>> finUserShop(String userId) {
        QueryWrapper<UserShopEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserShopEntity::getUserId,userId)
                .eq(UserShopEntity::getShopState,CodeDict.getValue(CodeTypeEnum.ShopState_Valid.getType(), CodeTypeEnum.ShopState_Valid.getKey()));
        List<UserShopEntity> shopList = userShopDAO.list(wrapper);
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
        return new Result<>(dtoList);
    }
}
