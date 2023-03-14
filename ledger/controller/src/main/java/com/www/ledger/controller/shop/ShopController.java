package com.www.ledger.controller.shop;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.dto.response.ResponseDTO;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.service.shop.IShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>@Description 我的店铺controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@RestController
@RequestMapping("shop")
public class ShopController {
    @Autowired
    private IShopService shopService;

    /**
     * <p>@Description 新增店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    @PostMapping("new")
    public ResponseDTO<String> createShop(ShopDTO dto){
        dto.setUserId(JwtAuthorizationTokenFilter.getUserId());
        return shopService.createShop(dto);
    }
    /**
     * <p>@Description 修改店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param dto 店铺信息
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    @PostMapping("info")
    public ResponseDTO<String> updateShop(ShopDTO dto){
        dto.setUserId(JwtAuthorizationTokenFilter.getUserId());
        return shopService.updateShop(dto);
    }
    /**
     * <p>@Description 删除店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param shopId 店铺ID
     * @return com.www.common.data.dto.response.ResponseDTO<java.lang.String>
     */
    @PostMapping("dlt/{shopId}")
    public ResponseDTO<String> deleteShop(@PathVariable("shopId") String shopId){
        return shopService.deleteShop(shopId);
    }
    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param shopId 店铺ID
     * @param shopNm 店铺名称
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return com.www.common.data.dto.response.ResponseDTO<java.util.List < com.www.ledger.data.dto.ShopDTO>>
     */
    @GetMapping("list")
    public ResponseDTO<List<ShopDTO>> findShopList(Long shopId,String shopNm, int pageNum, long pageSize){
        ShopDTO dto = new ShopDTO();
        dto.setShopId(shopId).setShopNm(shopNm)
           .setUserId(JwtAuthorizationTokenFilter.getUserId());
        return shopService.findShopList(dto,pageNum,pageSize);
    }
}
