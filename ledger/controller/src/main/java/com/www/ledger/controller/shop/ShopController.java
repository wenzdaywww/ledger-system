package com.www.ledger.controller.shop;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.vo.shop.ShopAllResponse;
import com.www.ledger.data.vo.shop.ShopInfoRequest;
import com.www.ledger.data.vo.shop.ShopListRequest;
import com.www.ledger.data.vo.shop.ShopListResponse;
import com.www.ledger.data.vo.shop.ShopNewRequest;
import com.www.ledger.service.shop.IShopService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
     * @param newRequest 新增店铺信息
     * @return Response<java.lang.String>
     */
    @PostMapping("new")
    public Response<String> createShop(@Validated ShopNewRequest newRequest){
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setShopName(newRequest.getShopNm())
                .setShopType(newRequest.getShopTp());
        return shopService.createShop(shopDTO);
    }
    /**
     * <p>@Description 统计店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @return Response<java.lang.String>
     */
    @PostMapping("tal")
    public Response<String> updateShopData(){
        return shopService.saveAndCountShopData(JwtAuthorizationTokenFilter.getUserId());
    }
    /**
     * <p>@Description 修改店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param infoRequest 店铺信息
     * @return Response<java.lang.String>
     */
    @PostMapping("info")
    public Response<String> updateShop(@Validated ShopInfoRequest infoRequest){
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setUserId(JwtAuthorizationTokenFilter.getUserId())
                .setShopId(infoRequest.getShopId())
                .setShopName(infoRequest.getShopNm())
                .setShopType(infoRequest.getShopTp());
        return shopService.updateShop(shopDTO);
    }
    /**
     * <p>@Description 删除店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param shopId 店铺ID
     * @return Response<java.lang.String>
     */
    @PostMapping("dlt/{shopId}")
    public Response<String> deleteShop(@PathVariable("shopId") String shopId){
        return shopService.deleteShop(shopId);
    }
    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param shopRequest 店铺信息查询条件
     * @return Response<java.util.List < com.www.ledger.data.dto.ShopDTO>>
     */
    @GetMapping("list")
    public Response<List<ShopListResponse>> findShopList(@Validated ShopListRequest shopRequest){
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setShopId(shopRequest.getShopId()).setShopName(shopRequest.getShopNm())
           .setUserId(JwtAuthorizationTokenFilter.getUserId()).setShopType(shopRequest.getShopTp());
        Response<List<ShopDTO>> dtoResponse = shopService.findShopList(shopDTO,shopRequest.getPageNum(),shopRequest.getPageSize());
        List<ShopListResponse> shopList = Optional.ofNullable(dtoResponse.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResponse.getData()))
                .map(list -> {
                    List<ShopListResponse> tempList = new ArrayList<>();
                    list.forEach(shop -> {
                        ShopListResponse ruslut = new ShopListResponse();
                        ruslut.setShopId(shop.getShopId()).setShopNm(shop.getShopName())
                              .setShopTp(shop.getShopType()).setShopTpNm(shop.getShopTypeName())
                              .setRetPro(shop.getRetainedProfits()).setRetProRat(shop.getRetainedProfitsRate())
                              .setGroPro(shop.getGrossProfit()).setGroProRat(shop.getGrossProfitRate())
                              .setTalOrd(shop.getTotalOrder()).setSucOrd(shop.getSucceedOrder())
                              .setFaiOrd(shop.getFailedOrder()).setSalAmt(shop.getSaleAmount())
                              .setCosAmt(shop.getCostAmount()).setAdvAmt(shop.getAdvertAmount())
                              .setSerAmt(shop.getServiceAmount()).setVirAmt(shop.getVirtualAmount());
                        tempList.add(ruslut);
                    });
                    return tempList;
                }).orElse(null);
        return new Response<>(dtoResponse,shopList);
    }
    /**
     * <p>@Description 查询用户的所有店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 00:46 </p>
     * @return
     */
    @GetMapping("all")
    public Response<List<ShopAllResponse>> finUserShop(){
        Response<List<ShopDTO>> dtoResponse = shopService.finUserShop(JwtAuthorizationTokenFilter.getUserId());
        List<ShopAllResponse> shopList = Optional.ofNullable(dtoResponse.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResponse.getData()))
                .map(list -> {
                    List<ShopAllResponse> tempList = new ArrayList<>();
                    list.forEach(shop -> {
                        ShopAllResponse ruslut = new ShopAllResponse();
                        ruslut.setValue(shop.getShopId()).setName(shop.getShopName());
                        tempList.add(ruslut);
                    });
                    return tempList;
                }).orElse(null);
        return new Response<>(dtoResponse,shopList);
    }

}
