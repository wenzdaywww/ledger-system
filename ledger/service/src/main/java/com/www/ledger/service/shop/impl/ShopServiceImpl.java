package com.www.ledger.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.code.CodeDict;
import com.www.common.config.code.enums.CodeKeyEnum;
import com.www.common.config.security.entity.SysUserEntity;
import com.www.common.data.dto.response.ResponseDTO;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.entity.ShopSalesEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.mapper.ShopSalesMapper;
import com.www.ledger.service.entity.IShopSalesService;
import com.www.ledger.service.entity.IUserShopService;
import com.www.ledger.service.shop.IShopService;
import com.www.ledger.service.user.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private ShopSalesMapper shopSalesMapper;
    @Autowired
    private IUserShopService userShopService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private IShopSalesService shopSalesService;

    /**
     * <p>@Description 新增店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    @Override
    public ResponseDTO<String> createShop(ShopDTO dto) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        if(dto == null || StringUtils.isAnyBlank(dto.getShopNm(),dto.getShopTp())
            || CodeDict.isIllegalValue(CodeTypeEnum.ShopPlatform.getCodeType(), dto.getShopTp())){
            responseDTO.setResponse(ResponseEnum.FAIL,"用户名、店铺名称和店铺平台信息有误，新增失败");
            return responseDTO;
        }
        SysUserEntity userEntity = userInfoService.findUserById(dto.getUserId());
        if(userEntity == null){
            responseDTO.setResponse(ResponseEnum.FAIL,"查询不到该用户信息，新增失败");
            return responseDTO;
        }
        UserShopEntity shopEntity = new UserShopEntity();
        shopEntity.setShopName(dto.getShopNm())
                .setUserId(dto.getUserId())
                .setShopType(dto.getShopTp());
        userShopService.save(shopEntity);
            ShopSalesEntity salesEntity = new ShopSalesEntity();
            salesEntity.setShopId(shopEntity.getShopId())
                    .setUserId(shopEntity.getUserId());
        shopSalesService.save(salesEntity);
        responseDTO.setResponse(ResponseEnum.SUCCESS,"新增成功");
        return responseDTO;
    }

    /**
     * <p>@Description 修改店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    @Override
    public ResponseDTO<String> updateShop(ShopDTO dto) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        if(dto == null || dto.getShopId() == null || StringUtils.isAnyBlank(dto.getShopNm(),dto.getShopTp())
                || CodeDict.isIllegalValue(CodeTypeEnum.ShopPlatform.getCodeType(), dto.getShopTp())){
            responseDTO.setResponse(ResponseEnum.FAIL,"店铺名称和店铺平台信息有误，修改失败");
            return responseDTO;
        }
        UpdateWrapper<UserShopEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserShopEntity::getShopId,dto.getShopId())
                .set(UserShopEntity::getShopName,dto.getShopNm())
                .set(UserShopEntity::getShopType,dto.getShopTp())
                .set(UserShopEntity::getUpdateTime,DateUtils.getCurrentDateTime());
        if(userShopService.update(wrapper)){
            responseDTO.setResponse(ResponseEnum.SUCCESS,"修改成功");
        }else {
            responseDTO.setResponse(ResponseEnum.FAIL,"修改失败");
        }
        return responseDTO;
    }

    /**
     * <p>@Description 删除店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param shopId 店铺ID
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    @Override
    public ResponseDTO<String> deleteShop(String shopId) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        UpdateWrapper<UserShopEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(UserShopEntity::getShopId,shopId)
                .set(UserShopEntity::getShopState,CodeDict.getValue(CodeTypeEnum.ShopState.getCodeType(), CodeKeyEnum.K0.getKey()))
                .set(UserShopEntity::getUpdateTime,DateUtils.getCurrentDateTime());
        if(userShopService.update(wrapper)){
            responseDTO.setResponse(ResponseEnum.SUCCESS,"删除成功");
        }else {
            responseDTO.setResponse(ResponseEnum.FAIL,"删除失败");
        }
        return responseDTO;
    }

    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param dto 店铺信息查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return com.www.common.data.dto.response.ResponseDTO<java.util.List < com.www.ledger.data.dto.ShopDTO>>
     */
    @Override
    public ResponseDTO<List<ShopDTO>> findShopList(ShopDTO dto, int pageNum, long pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 5 : pageSize;
        Page<ShopDTO> page = new Page<>(pageNum,pageSize);
        page = shopSalesMapper.findShopList(page,dto);
        List<ShopDTO> shopList =  page.getRecords();
        if(CollectionUtils.isNotEmpty(shopList)){
            shopList.forEach(d -> {
                d.setShopTp(CodeDict.getCodeValueName(CodeTypeEnum.ShopPlatform.getCodeType(), d.getShopTp()));
            });
        }
        ResponseDTO<List<ShopDTO>> responseDTO = new ResponseDTO<>(ResponseEnum.SUCCESS,shopList);
        responseDTO.setPageNum(pageNum);
        responseDTO.setPageSize(pageSize);
        responseDTO.setTotalNum(page.getTotal());
        return responseDTO;
    }
}
