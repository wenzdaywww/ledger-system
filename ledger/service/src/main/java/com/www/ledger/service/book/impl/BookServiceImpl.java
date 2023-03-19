package com.www.ledger.service.book.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.www.common.config.code.CodeDict;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.entity.ShopGoodsEntity;
import com.www.ledger.data.entity.UserBookEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.mapper.ShopGoodsMapper;
import com.www.ledger.data.mapper.ShopSalesMapper;
import com.www.ledger.data.mapper.UserBookMapper;
import com.www.ledger.data.mapper.UserShopMapper;
import com.www.ledger.service.book.IBookService;
import com.www.ledger.service.month.IMonthService;
import com.www.ledger.service.shop.IShopService;
import com.www.ledger.service.year.IYearService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p>@Description 我的账本service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
@Slf4j
@Service
public class BookServiceImpl implements IBookService {
    @Autowired
    private IYearService yearService;
    @Autowired
    private IShopService shopService;
    @Autowired
    private IMonthService monthService;
    @Autowired
    private UserShopMapper userShopMapper;
    @Autowired
    private ShopGoodsMapper shopGoodsMapper;
    @Autowired
    private UserBookMapper userBookMapper;
    @Autowired
    private ShopSalesMapper shopSalesMapper;

    /**
     * <p>@Description 统计用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:29 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Response<String> saveAndCountBookData(String userId) {
        //统计月销售额
        monthService.saveAndCountMonthData(userId);
        //统计年销售额
        yearService.saveAndCountYearData(userId);
        //统计店铺销售额
        shopService.saveAndCountShopData(userId);
        //统计用户账簿信息
        this.countAllShopData(userId);
        return new Response<>(ResponseEnum.SUCCESS,"统计完成");
    }
    /**
     * <p>@Description 根据用户的店销售数据统计用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 11:14 </p>
     * @param userId
     * @return
     */
    private void countAllShopData(String userId){
        //统计所有店铺销售额
        BookDTO bookDTO = shopSalesMapper.countAllShopData(userId);
        //查询用户账簿销售额
        QueryWrapper<UserBookEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserBookEntity::getUserId,userId);
        UserBookEntity bookEntity = userBookMapper.selectOne(wrapper);
        bookEntity.setTotalOrder(bookDTO.getTotalOrder() == null ? 0L : bookDTO.getTotalOrder())
                .setSucceedOrder(bookDTO.getSucceedOrder() == null ? 0L : bookDTO.getSucceedOrder())
                .setFailedOrder(bookDTO.getFailedOrder() == null ? 0L : bookDTO.getFailedOrder())
                .setSaleAmount(MoneyUtils.nullToZero(bookDTO.getSaleAmount()))
                .setCostAmount(MoneyUtils.nullToZero(bookDTO.getCostAmount())).setAdvertAmount(MoneyUtils.nullToZero(bookDTO.getAdvertAmount()))
                .setServiceAmount(MoneyUtils.nullToZero(bookDTO.getServiceAmount())).setVirtualAmount(MoneyUtils.nullToZero(bookDTO.getVirtualAmount()))
                .setUpdateTime(DateUtils.getCurrentDateTime());
        //计算月毛利润=月销售额-月成本费
        bookEntity.setGrossProfit((bookEntity.getSaleAmount().subtract(bookEntity.getCostAmount())).setScale(2, RoundingMode.HALF_UP));
        //计算月毛利率=月毛利润/月成本费
        bookEntity.setGrossProfitRate( bookEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (bookEntity.getGrossProfit().divide(bookEntity.getCostAmount(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        //月净利润=月毛利润-月推广费-月服务费-月刷单费
        bookEntity.setRetainedProfits((bookEntity.getGrossProfit().subtract(bookEntity.getAdvertAmount())).
                subtract(bookEntity.getServiceAmount()).subtract(bookEntity.getVirtualAmount()));
        //月净利率=月净利润/(月成本+月推广费+月服务费+月刷单费)
        BigDecimal totalAmt = bookEntity.getCostAmount().add(bookEntity.getAdvertAmount()).add(bookEntity.getServiceAmount()).add(bookEntity.getVirtualAmount());
        bookEntity.setRetainedProfitsRate(totalAmt.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                :(bookEntity.getRetainedProfits().divide(totalAmt,5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        userBookMapper.updateById(bookEntity);
    }
    /**
     * <p>@Description 查询用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:29 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Response<BookDTO> findBookData(String userId) {
        QueryWrapper<UserBookEntity> bookWrapper = new QueryWrapper<>();
        bookWrapper.lambda().eq(UserBookEntity::getUserId,userId);
        UserBookEntity bookEntity = userBookMapper.selectOne(bookWrapper);
        if(bookEntity == null){
            return new Response<>(ResponseEnum.FAIL,null);
        }
        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(bookEntity,bookDTO);
        BigDecimal succeedNum = new BigDecimal(bookDTO.getSucceedOrder());
        BigDecimal totalNum = new BigDecimal(bookDTO.getTotalOrder());
        bookDTO.setSucceedOrderRate((succeedNum.divide(totalNum,4,BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        //查询店铺数量
        QueryWrapper<UserShopEntity> shopWrapper = new QueryWrapper<>();
        shopWrapper.lambda().eq(UserShopEntity::getUserId,userId)
                .eq(UserShopEntity::getShopState, CodeDict.getValue(CodeTypeEnum.ShopState_Valid.getType(),CodeTypeEnum.ShopState_Valid.getKey()));
        bookDTO.setShopNum(userShopMapper.selectCount(shopWrapper));
        //查询商品数
        QueryWrapper<ShopGoodsEntity> goodsWrapper = new QueryWrapper<>();
        goodsWrapper.lambda().eq(ShopGoodsEntity::getUserId,userId)
                .ne(ShopGoodsEntity::getGoodsState,CodeDict.getValue(CodeTypeEnum.GoodsState_Del.getType(),CodeTypeEnum.GoodsState_Del.getKey()));
        bookDTO.setGoodsNum(shopGoodsMapper.selectCount(goodsWrapper));
        return new Response<>(ResponseEnum.SUCCESS,bookDTO);
    }
}
