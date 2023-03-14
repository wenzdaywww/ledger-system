package com.www.ledger.service.shop;

import com.www.common.data.dto.response.ResponseDTO;
import com.www.ledger.data.dto.ShopDTO;

import java.util.List;

/**
 * <p>@Description 我的店铺service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
public interface IShopService {
    /**
     * <p>@Description 新增店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    ResponseDTO<String> createShop(ShopDTO dto);
    /**
     * <p>@Description 修改店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    ResponseDTO<String> updateShop(ShopDTO dto);
    /**
     * <p>@Description 删除店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param shopId 店铺ID
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    ResponseDTO<String> deleteShop(String shopId);
    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param dto 店铺信息查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return com.www.common.data.dto.response.ResponseDTO<java.util.List < com.www.ledger.data.dto.ShopDTO>>
     */
    ResponseDTO<List<ShopDTO>> findShopList(ShopDTO dto, int pageNum, long pageSize);
}
