package com.www.ledger.service.book.impl;

import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dao.IMonthSalesDAO;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dao.IPayInfoDAO;
import com.www.ledger.data.dao.IShopSalesDAO;
import com.www.ledger.data.dao.IUserBookDAO;
import com.www.ledger.data.dao.IUserShopDAO;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.UserBookEntity;
import com.www.ledger.service.book.IBookService;
import com.www.ledger.service.day.IDayService;
import com.www.ledger.service.month.IMonthService;
import com.www.ledger.service.shop.IShopService;
import com.www.ledger.service.year.IYearService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private IOrderInfoDAO orderInfoDAO;
    @Autowired
    private IDaySalesDAO daySalesDAO;
    @Autowired
    private IShopSalesDAO shopSalesDAO;
    @Autowired
    private IMonthSalesDAO monthSalesDAO;
    @Autowired
    private IUserBookDAO userBookDAO;
    @Autowired
    private IPayInfoDAO payInfoDAO;


    /**
     * <p>@Description 查询日期区间的店铺汇总日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 18:00 </p>
     * @param userId 用户ID
     * @return 店铺汇总日销售额
     */
    @Override
    public Result<List<DayDTO>> findLastDaySales(String userId) {
        int lastDays = 10;//统计的天数
        //获取订单中最大的日期
        String maxDateStr = orderInfoDAO.getMaxOrderDate(userId);
        if(StringUtils.isBlank(maxDateStr)){
            return new Result<>();
        }
        //查询销量前3店铺近10日的订单信息
        Date maxDate = DateUtils.parse(maxDateStr,DateFormatEnum.YYYYMMDD1);
        //获取maxDateStr的10天前日期
        int dayStep = -1*lastDays;
        String minData = DateUtils.format(DateUtils.stepDay(maxDate,dayStep),DateFormatEnum.YYYYMMDD1);
        //查询日期区间的店铺汇总日销售额
        List<DayDTO> dayList = daySalesDAO.findLastDaySales(userId,minData,maxDateStr);
        if(CollectionUtils.isEmpty(dayList)){
            return new Result<>();
        }
        //dayList转为map，用于判断近10日哪天没有销售额
        //Map<日期，当日销售额>
        Map<String,DayDTO> ordMap = dayList.stream().collect(Collectors.toMap(k -> k.getDayDateStr(),day -> day));
        //循环判断近10日是否都有销售额，没有则补齐数据,从最早日期开始
        for (int i = (-1*(lastDays-1)); i <= 0; i++){
            Date day = DateUtils.stepDay(maxDate,i);
            String dayStr = DateUtils.format(day,DateFormatEnum.YYYYMD4);
            if(!ordMap.containsKey(dayStr)){
                DayDTO tempDTO = new DayDTO();
                tempDTO.setDayDate(day).setDayDateStr(dayStr).setSaleAmount(BigDecimal.ZERO);
                ordMap.put(dayStr,tempDTO);
            }
        }
        List<DayDTO> resultList = new ArrayList<>(ordMap.values());
        resultList.sort((a,b) -> a.getDayDate().compareTo(b.getDayDate()));
        return new Result<>(resultList);
    }
    /**
     * <p>@Description 查询用户最近一年所有店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:46 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Result<List<MonthDTO>> findLastYearSales(String userId) {
        //获取订单中最大的日期
        String maxDateStr = orderInfoDAO.getMaxOrderDate(userId);
        //查询用户近一年的销售额
        List<MonthDTO> dtoList = monthSalesDAO.findLastYearSales(userId,maxDateStr);
        Map<String,MonthDTO> dtoMap = CollectionUtils.isEmpty(dtoList) ? new HashMap<>()
                : dtoList.stream().collect(Collectors.toMap(k -> k.getMonthDateStr(),month -> month));
        //近一年的销售额数据
        List<MonthDTO> resultList = new ArrayList<>();
        for (int i = -11; i <= 0; i++){
            //获取前i个月日期
            Date lastMonth = DateUtils.stepMonth(DateUtils.getCurrentDateTime(),i);
            //获取前i个月月份
            String monthStr = DateUtils.format(lastMonth, DateFormatEnum.YYYYMM3);
            MonthDTO monthDTO = new MonthDTO();
            monthDTO.setMonthDateStr(monthStr);
            if(dtoMap.containsKey(monthStr)){
                MonthDTO dto = dtoMap.get(monthStr);
                monthDTO.setSaleAmount(dto.getSaleAmount());
            }else {
                monthDTO.setSaleAmount(BigDecimal.ZERO);
            }
            resultList.add(monthDTO);
        }
        return new Result<>(resultList);
    }

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
        UserBookEntity bookEntity = userBookDAO.findUserBook(userId);
        bookEntity.setTotalOrder(bookDTO.getTotalOrder() == null ? 0L : bookDTO.getTotalOrder())
                .setSucceedOrder(bookDTO.getSucceedOrder() == null ? 0L : bookDTO.getSucceedOrder())
                .setFailedOrder(bookDTO.getFailedOrder() == null ? 0L : bookDTO.getFailedOrder())
                .setSaleAmount(MoneyUtils.nullToZero(bookDTO.getSaleAmount()))
                .setCostAmount(MoneyUtils.nullToZero(bookDTO.getCostAmount())).setAdvertAmount(MoneyUtils.nullToZero(bookDTO.getAdvertAmount()))
                .setServiceAmount(MoneyUtils.nullToZero(bookDTO.getServiceAmount())).setVirtualAmount(MoneyUtils.nullToZero(bookDTO.getVirtualAmount()))
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
     * @return
     */
    @Override
    public Result<BookDTO> findBookData(String userId) {
        UserBookEntity bookEntity = userBookDAO.findUserBook(userId);
        if(bookEntity == null){
            return new Result<>();
        }
        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(bookEntity,bookDTO);
        BigDecimal succeedNum = new BigDecimal(bookDTO.getSucceedOrder());
        BigDecimal totalNum = new BigDecimal(bookDTO.getTotalOrder());
        bookDTO.setSucceedOrderRate(totalNum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (succeedNum.divide(totalNum,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        //查询店铺数量
        bookDTO.setGuarantee(payInfoDAO.findShopGuarantee(userId));
        return new Result<>(bookDTO);
    }
}
