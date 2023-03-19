package com.www.ledger.service.book.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.www.common.config.code.CodeDict;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.entity.ShopGoodsEntity;
import com.www.ledger.data.entity.UserBookEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.mapper.MonthSalesMapper;
import com.www.ledger.data.mapper.OrderInfoMapper;
import com.www.ledger.data.mapper.ShopGoodsMapper;
import com.www.ledger.data.mapper.ShopSalesMapper;
import com.www.ledger.data.mapper.UserBookMapper;
import com.www.ledger.data.mapper.UserShopMapper;
import com.www.ledger.service.book.IBookService;
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
    private UserShopMapper userShopMapper;
    @Autowired
    private UserBookMapper userBookMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ShopGoodsMapper shopGoodsMapper;
    @Autowired
    private ShopSalesMapper shopSalesMapper;
    @Autowired
    private MonthSalesMapper monthSalesMapper;


    /**
     * <p>@Description 查询近10天销售额前3名店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 18:00 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Response<List<List<OrderDTO>>> findLast10DaySales(String userId) {
        int lastDays = 10;
        //获取订单中最大的日期
        String maxDateStr = orderInfoMapper.getMaxOrderDate(userId);
        if(StringUtils.isBlank(maxDateStr)){
            return new Response<>(ResponseEnum.SUCCESS,null);
        }
        //查询销量前3店铺近10日的订单信息
        Date maxDate = DateUtils.parse(maxDateStr,DateFormatEnum.YYYYMMDD1);
        // 查询订单中最大的日期的月份销量前1的店铺
        List<Long> shopList = monthSalesMapper.findMaxSalesShop(userId,DateUtils.format(maxDate,DateFormatEnum.YYYYMM1)+"-01");
        if(CollectionUtils.isEmpty(shopList)){
            return new Response<>(ResponseEnum.SUCCESS,null);
        }
        //获取maxDateStr的10天前日期
        int dayStep = -1*lastDays;
        String minData = DateUtils.format(DateUtils.stepDay(maxDate,dayStep),DateFormatEnum.YYYYMMDD1);
        List<OrderDTO> orderList = orderInfoMapper.findMaxSalesOrder(userId,shopList,minData,maxDateStr);
        if(CollectionUtils.isEmpty(orderList)){
            return new Response<>(ResponseEnum.SUCCESS,null);
        }
        //orderList转为map，用于判断近10日哪天没有销售额
        //Map<店铺ID, Map<日期，当日销售额>>
        Map<Long, Map<String,OrderDTO>> ordMap = new HashMap<>();
        //销量前1店铺近10日的订单信息处理
        orderList.forEach(dto -> {
            Map<String,OrderDTO> tempMap = null;
            //已添加key
            if(ordMap.containsKey(dto.getShopId())){
                tempMap = ordMap.get(dto.getShopId());
            }else {
                tempMap = new HashMap<>();
                ordMap.put(dto.getShopId(),tempMap);
            }
            OrderDTO tempDTO = new OrderDTO();
            tempDTO.setShopId(dto.getShopId()).setShopName(dto.getShopName()).setOrderDate(dto.getOrderDate())
                    .setOrderDateStr(dto.getOrderDateStr()).setSaleAmount(dto.getSaleAmount());
            tempMap.put(tempDTO.getOrderDateStr(),tempDTO);
        });
        //循环判断近10日是否都有销售额，没有则补齐数据,从最早日期开始
        ordMap.forEach((k,v) -> {
            for (int i = (-1*(lastDays-1)); i <= 0; i++){
                Date day = DateUtils.stepDay(maxDate,i);
                String dayStr = DateUtils.format(day,DateFormatEnum.YYYYMD4);
                if(!v.containsKey(dayStr)){
                    OrderDTO tempDTO = new OrderDTO();
                    UserShopEntity shopEntity = userShopMapper.selectById(k);
                    tempDTO.setShopId(k).setShopName(shopEntity.getShopName()).setOrderDate(day)
                            .setOrderDateStr(dayStr).setSaleAmount(BigDecimal.ZERO);
                    v.put(dayStr,tempDTO);
                }
            }
        });
        List<List<OrderDTO>> resultList = new ArrayList<>();
        ordMap.forEach((k,v) -> {
            List<OrderDTO> dayList = new ArrayList<>(v.values());
            dayList.sort((a,b) -> a.getOrderDate().compareTo(b.getOrderDate()));
            resultList.add(dayList);
        });
        return new Response<>(ResponseEnum.SUCCESS,resultList);
    }
    /**
     * <p>@Description 查询用户近一年的销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:46 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Response<List<MonthDTO>> findLastYearSales(String userId) {
        //获取订单中最大的日期
        String maxDateStr = orderInfoMapper.getMaxOrderDate(userId);
        //查询用户近一年的销售额
        List<MonthDTO> dtoList = monthSalesMapper.findLastYearSales(userId,maxDateStr);
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
        return new Response<>(ResponseEnum.SUCCESS,resultList);
    }

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
