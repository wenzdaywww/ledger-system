package com.www.ledger.controller.shop;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.vo.shop.ShopAllOutVO;
import com.www.ledger.data.vo.shop.ShopInfoInVO;
import com.www.ledger.data.vo.shop.ShopListInVO;
import com.www.ledger.data.vo.shop.ShopListOutVO;
import com.www.ledger.data.vo.shop.ShopNewInVO;
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
     * @param newInVO 新增店铺信息
     * @return Response<java.lang.String>
     */
    @PostMapping("new")
    public Result<String> createShop(@Validated ShopNewInVO newInVO){
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setShopName(newInVO.getShopNm()).setShopType(newInVO.getShopTp())
               .setUserId(JwtAuthorizationTokenFilter.getUserId());
        return shopService.createShop(shopDTO);
    }
    /**
     * <p>@Description 统计店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @return Response<java.lang.String>
     */
    @PostMapping("tal")
    public Result<String> updateShopData(){
        return shopService.saveAndCountShopData(JwtAuthorizationTokenFilter.getUserId());
    }
    /**
     * <p>@Description 修改店铺信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:18 </p>
     * @param infoInVO 店铺信息
     * @return Response<java.lang.String>
     */
    @PostMapping("info")
    public Result<String> updateShop(@Validated ShopInfoInVO infoInVO){
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setShopId(infoInVO.getShopId())
               .setShopName(infoInVO.getShopNm())
               .setShopType(infoInVO.getShopTp())
               .setUserId(JwtAuthorizationTokenFilter.getUserId());
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
    public Result<String> deleteShop(@PathVariable("shopId") String shopId){
        return shopService.deleteShop(shopId);
    }
    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:49 </p>
     * @param shopInVO 店铺信息查询条件
     * @return Response<java.util.List < com.www.ledger.data.dto.ShopDTO>>
     */
    @GetMapping("list")
    public Result<List<ShopListOutVO>> findShopList(@Validated ShopListInVO shopInVO){
        ShopDTO shopDTO = new ShopDTO();
        shopDTO.setShopId(shopInVO.getShopId()).setShopName(shopInVO.getShopNm())
           .setShopType(shopInVO.getShopTp()).setUserId(JwtAuthorizationTokenFilter.getUserId());
        Result<List<ShopDTO>> listResult = shopService.findShopList(shopDTO,shopInVO.getPageNum(),shopInVO.getPageSize());
        List<ShopListOutVO> shopList = Optional.ofNullable(listResult.getData()).filter(e -> CollectionUtils.isNotEmpty(listResult.getData()))
                .map(list -> {
                    List<ShopListOutVO> tempList = new ArrayList<>();
                    list.forEach(shop -> {
                        ShopListOutVO ruslut = new ShopListOutVO();
                        ruslut.setShopId(shop.getShopId()).setShopNm(shop.getShopName())
                              .setShopTp(shop.getShopType()).setShopTpNm(shop.getShopTypeName())
                              .setRetPro(shop.getRetainedProfits()).setRetProRat(shop.getRetainedProfitsRate())
                              .setGroPro(shop.getGrossProfit()).setGroProRat(shop.getGrossProfitRate())
                              .setTalOrd(shop.getTotalOrder()).setSucOrd(shop.getSucceedOrder())
                              .setFaiOrd(shop.getFailedOrder()).setSalAmt(shop.getSaleAmount())
                              .setCosAmt(shop.getCostAmount()).setAdvAmt(shop.getAdvertAmount())
                              .setSerAmt(shop.getServiceAmount()).setVirAmt(shop.getVirtualAmount())
                              .setTalCos(shop.getTotalCost());
                        tempList.add(ruslut);
                    });
                    return tempList;
                }).orElse(null);
        return new Result<>(listResult,shopList);
    }
    /**
     * <p>@Description 查询用户的所有店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 00:46 </p>
     * @return
     */
    @GetMapping("all")
    public Result<List<ShopAllOutVO>> finUserShop(){
        Result<List<ShopDTO>> listResult = shopService.finUserShop(JwtAuthorizationTokenFilter.getUserId());
        List<ShopAllOutVO> shopList = Optional.ofNullable(listResult.getData()).filter(e -> CollectionUtils.isNotEmpty(listResult.getData()))
                .map(list -> {
                    List<ShopAllOutVO> tempList = new ArrayList<>();
                    list.forEach(shop -> {
                        ShopAllOutVO ruslut = new ShopAllOutVO();
                        ruslut.setValue(shop.getShopId()).setName(shop.getShopName());
                        tempList.add(ruslut);
                    });
                    return tempList;
                }).orElse(null);
        return new Result<>(listResult,shopList);
    }

}
