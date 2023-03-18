package com.www.ledger.service.shop;

import com.www.common.data.response.Response;
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
     * <p>@Description 统计店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param userId 用户ID
     * @return Response<java.lang.String>
     */
    Response<String> saveAndCountShopData(String userId);
    /**
     * <p>@Description 新增店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return Response<java.lang.String>
     */
    Response<String> createShop(ShopDTO dto);
    /**
     * <p>@Description 修改店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return Response<java.lang.String>
     */
    Response<String> updateShop(ShopDTO dto);
    /**
     * <p>@Description 删除店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param shopId 店铺ID
     * @return Response<java.lang.String>
     */
    Response<String> deleteShop(String shopId);
    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param dto 店铺信息查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return Response<java.util.List < com.www.ledger.data.dto.ShopDTO>>
     */
    Response<List<ShopDTO>> findShopList(ShopDTO dto, int pageNum, long pageSize);
    /**
     * <p>@Description 查询用户的所有店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param userId 用户ID
     * @return
     */
    Response<List<ShopDTO>> finUserShop(String userId);
}
