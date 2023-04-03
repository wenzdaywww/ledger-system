package com.www.ledger.service.book.impl;

import com.www.common.data.response.Result;
import com.www.common.utils.BigDecimalUtils;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dao.IPayInfoDAO;
import com.www.ledger.data.dao.IShopSalesDAO;
import com.www.ledger.data.dao.IUserBookDAO;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.entity.UserBookEntity;
import com.www.ledger.service.book.IBookService;
import com.www.ledger.service.day.IDayService;
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
    private IDayService dayService;
    @Autowired
    private IShopSalesDAO shopSalesDAO;
    @Autowired
    private IUserBookDAO userBookDAO;
    @Autowired
    private IPayInfoDAO payInfoDAO;



    /**
     * <p>@Description 统计用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:29 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Result<String> saveAndCountBookData(String userId) {
        //统计月销售额
        dayService.saveAndCountDayData(userId);
        //统计月销售额
        monthService.saveAndCountMonthData(userId);
        //统计年销售额
        yearService.saveAndCountYearData(userId);
        //统计店铺销售额
        shopService.saveAndCountShopData(userId);
        //统计用户账簿信息
        this.countAllShopData(userId);
        return new Result<>("统计完成");
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
        BookDTO bookDTO = shopSalesDAO.countAllShopData(userId);
        if(bookDTO == null){
            return;
        }
        //查询用户账簿销售额
        UserBookEntity bookEntity = userBookDAO.findUserBook(userId,true);
        bookEntity.setTotalOrder(bookDTO.getTotalOrder() == null ? 0L : bookDTO.getTotalOrder())
                .setSucceedOrder(bookDTO.getSucceedOrder() == null ? 0L : bookDTO.getSucceedOrder())
                .setFailedOrder(bookDTO.getFailedOrder() == null ? 0L : bookDTO.getFailedOrder())
                .setSaleAmount(BigDecimalUtils.nullToZero(bookDTO.getSaleAmount()))
                .setCostAmount(BigDecimalUtils.nullToZero(bookDTO.getCostAmount())).setAdvertAmount(BigDecimalUtils.nullToZero(bookDTO.getAdvertAmount()))
                .setServiceAmount(BigDecimalUtils.nullToZero(bookDTO.getServiceAmount())).setVirtualAmount(BigDecimalUtils.nullToZero(bookDTO.getVirtualAmount()))
                .setUpdateTime(DateUtils.getCurrentDateTime());
        //计算总支出费=总成本费+总推广费+总服务费+总刷单费
        bookEntity.setTotalCost(bookEntity.getCostAmount().add(bookEntity.getAdvertAmount()).add(bookEntity.getServiceAmount()).add(bookEntity.getVirtualAmount()));
        //计算总毛利润=总销售额-总成本费
        bookEntity.setGrossProfit(bookEntity.getSaleAmount().subtract(bookEntity.getCostAmount()));
        //计算总毛利率=总毛利润/总成本费 * 100%
        bookEntity.setGrossProfitRate( bookEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (bookEntity.getGrossProfit().divide(bookEntity.getCostAmount(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        //总净利润=总销售额-总支出费
        bookEntity.setRetainedProfits((bookEntity.getGrossProfit().subtract(bookEntity.getAdvertAmount())).
                subtract(bookEntity.getServiceAmount()).subtract(bookEntity.getVirtualAmount()));
        //总净利率=总净利润/总支出费 * 100%
        bookEntity.setRetainedProfitsRate(bookEntity.getTotalCost().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                :(bookEntity.getRetainedProfits().divide(bookEntity.getTotalCost(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        userBookDAO.updateById(bookEntity);
    }
    /**
     * <p>@Description 查询用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:29 </p>
     * @param userId 用户ID
     * @param isThrow 查不到数据是否抛出业务异常，true抛出，false不抛出
     * @return 用户账簿信息
     */
    @Override
    public BookDTO findBookData(String userId,boolean isThrow) {
        UserBookEntity bookEntity = userBookDAO.findUserBook(userId,isThrow);
        BookDTO bookDTO = null;
        if(bookEntity != null){
            bookDTO = new BookDTO();
            BeanUtils.copyProperties(bookEntity,bookDTO);
            BigDecimal succeedNum = new BigDecimal(bookDTO.getSucceedOrder());
            BigDecimal totalNum = new BigDecimal(bookDTO.getTotalOrder());
            bookDTO.setSucceedOrderRate(totalNum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    : (succeedNum.divide(totalNum,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
            //查询店铺数量
            bookDTO.setGuarantee(payInfoDAO.findShopGuarantee(userId));
        }
        return bookDTO;
    }
}
