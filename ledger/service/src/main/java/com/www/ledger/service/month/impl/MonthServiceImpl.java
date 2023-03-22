package com.www.ledger.service.month.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.MonthSalesEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.mapper.OrderInfoMapper;
import com.www.ledger.service.entity.IMonthSalesService;
import com.www.ledger.service.entity.IUserShopService;
import com.www.ledger.service.month.IMonthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
 * <p>@Description 月销售额service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
@Slf4j
@Service
public class MonthServiceImpl implements IMonthService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private IUserShopService userShopService;
    @Autowired
    private IMonthSalesService monthSalesService;


    /**
     * <p>@Description 增加/减少月销售推广及服务费用  </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:09 </p>
     * @param monthDTO 费用信息
     * @return
     */
    @Override
    public Response<String> updateMonthAmt(MonthDTO monthDTO) {
        monthDTO.setAdvertAmount(MoneyUtils.nullToZero(monthDTO.getAdvertAmount()));
        monthDTO.setServiceAmount(MoneyUtils.nullToZero(monthDTO.getServiceAmount()));
        if(monthDTO.getAdvertAmount().compareTo(BigDecimal.ZERO) == 0 && monthDTO.getServiceAmount().compareTo(BigDecimal.ZERO) == 0){
            return new Response<>(ResponseEnum.FAIL,"推广费和服务费不能都为0");
        }
        MonthSalesEntity monthEntity = monthSalesService.findMonthSales(monthDTO.getUserId(),monthDTO.getMsId());
        if(monthEntity == null){
            return new Response<>(ResponseEnum.FAIL,"月销售数据不存在");
        }
        //增加月销售推广及服务费用
        monthEntity.setAdvertAmount((monthEntity.getAdvertAmount().add(monthDTO.getAdvertAmount())).setScale(2,RoundingMode.HALF_UP))
                .setServiceAmount((monthEntity.getServiceAmount().add(monthDTO.getServiceAmount())).setScale(2,RoundingMode.HALF_UP));
        if(monthEntity.getAdvertAmount().compareTo(BigDecimal.ZERO) == -1){
            return new Response<>(ResponseEnum.FAIL,"推广费不能为负数");
        }
        if(monthEntity.getServiceAmount().compareTo(BigDecimal.ZERO) == -1){
            return new Response<>(ResponseEnum.FAIL,"服务费不能为负数");
        }
        //计算月销售额数据
        this.computeMonthData(monthEntity);
        if(monthSalesService.updateById(monthEntity)){
            return new Response<>(ResponseEnum.SUCCESS,"修改成功");
        }
        return new Response<>(ResponseEnum.FAIL,"修改失败");
    }
    /**
     * <p>@Description 编辑月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:26 </p>
     * @param monthDTO 月销售额数据
     * @return
     */
    @Override
    public Response<String> saveMonthSales(MonthDTO monthDTO) {
        Date monthDate = DateUtils.parse(monthDTO.getMonthDateStr() + "-01",DateFormatEnum.YYYYMMDD1);
        if(monthDate == null){
            return new Response<>(ResponseEnum.FAIL,"月份格式错误");
        }
        //判断当前店铺是否属于用户的
        UserShopEntity shopEntity = userShopService.findUserShop(monthDTO.getUserId(),monthDTO.getShopId());
        if(shopEntity == null){
            return new Response<>(ResponseEnum.FAIL,"店铺不存在");
        }
        MonthSalesEntity monthEntity = null;
        //月销售数据修改
        if(monthDTO.getMsId() != null){
            monthEntity = monthSalesService.findMonthSales(monthDTO.getUserId(),monthDTO.getMsId());
            if(monthEntity == null){
                return new Response<>(ResponseEnum.FAIL,"店铺月销售数据不存在");
            }
        }else {//月销售数据新增
            monthEntity = monthSalesService.findMonthSales(monthDTO.getUserId(),monthDTO.getShopId(),monthDate);
            if(monthEntity != null){
                return new Response<>(ResponseEnum.FAIL,"已存在店铺月销售数据");
            }
        }
        //月销售数据新增
        if(monthEntity == null){
            monthEntity = new MonthSalesEntity();
            monthEntity.setShopId(monthDTO.getShopId()).setUserId(monthDTO.getUserId())
                    .setMonthDate(monthDate).setTotalOrder(0L).setCreateTime(DateUtils.getCurrentDateTime())
                    .setSucceedOrder(0L).setSaleAmount(BigDecimal.ZERO)
                    .setCostAmount(BigDecimal.ZERO).setVirtualAmount(BigDecimal.ZERO);
        }
        monthEntity.setUpdateTime(DateUtils.getCurrentDateTime())
                .setAdvertAmount(MoneyUtils.nullToZero(monthDTO.getAdvertAmount()))
                .setServiceAmount(MoneyUtils.nullToZero(monthDTO.getServiceAmount()));
        //计算月销售额数据
        this.computeMonthData(monthEntity);
        if(monthSalesService.saveOrUpdate(monthEntity)){
            return new Response<>(ResponseEnum.SUCCESS,"编辑成功");
        }
        return new Response<>(ResponseEnum.FAIL,"编辑失败");
    }
    /**
     * <p>@Description 删除月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:21 </p>
     * @param userId 用户ID
     * @param msId 月销售额ID
     * @return
     */
    @Override
    public Response<String> deleteMonthData(String userId,Long msId) {
        if(monthSalesService.deleteMonthSales(userId,msId)){
            return new Response<>(ResponseEnum.SUCCESS,"删除成功");
        }
        return new Response<>(ResponseEnum.FAIL,"删除失败");
    }
    /**
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:01 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Response<String> saveAndCountMonthData(String userId) {
        //统计月销售额
        List<MonthDTO> countList = orderInfoMapper.countMonthSale(userId);
        //查询存在的月销售额数据
        List<MonthSalesEntity> monthList = monthSalesService.findMonthSalesList(userId);
        List<MonthSalesEntity> insertList = new ArrayList<>();//待插入的数据
        List<MonthSalesEntity> updateList = new ArrayList<>();//待更新的数据
        //数据转换处理，key=店铺ID+月份日期（默认为月份01日），如：101320230201
        Map<String,MonthSalesEntity> entityMap = CollectionUtils.isEmpty(monthList) ? new HashMap<>()
                : monthList.stream().collect(Collectors.toMap(k -> k.getShopId() + DateUtils.format(k.getMonthDate(), DateFormatEnum.YYYYMMDD1), month -> month));
        Map<String,MonthDTO> dtoMap = CollectionUtils.isEmpty(countList) ? new HashMap<>()
                : countList.stream().collect(Collectors.toMap(k -> k.getShopId() + k.getMonthDateStr(), month -> month));
        //处理统计出的月销售额
        dtoMap.forEach((k,v) -> {
            //统计出的月销售额中已有存在的entity数据，则更新数据
            if(entityMap.containsKey(k)){
                MonthSalesEntity monthEntity = entityMap.get(k);
                monthEntity.setTotalOrder(v.getTotalOrder() == null ? 0L : v.getTotalOrder())
                        .setSucceedOrder(v.getSucceedOrder() == null ? 0L : v.getSucceedOrder())
                        .setUpdateTime(DateUtils.getCurrentDateTime())
                        .setSaleAmount(MoneyUtils.nullToZero(v.getSaleAmount()))
                        .setCostAmount(MoneyUtils.nullToZero(v.getCostAmount()))
                        .setVirtualAmount(MoneyUtils.nullToZero(v.getVirtualAmount()));
                //计算月销售额数据
                this.computeMonthData(monthEntity);
                updateList.add(monthEntity);
            }else {//统计出的月销售额中没有存在的entity数据，则插入数据
                MonthSalesEntity monthEntity = new MonthSalesEntity();
                monthEntity.setShopId(v.getShopId()).setUserId(userId)
                        .setMonthDate(DateUtils.parse(v.getMonthDateStr(),DateFormatEnum.YYYYMMDD1))
                        .setTotalOrder(v.getTotalOrder() == null ? 0L : v.getTotalOrder())
                        .setSucceedOrder(v.getSucceedOrder() == null ? 0L : v.getSucceedOrder())
                        .setAdvertAmount(BigDecimal.ZERO).setServiceAmount(BigDecimal.ZERO)
                        .setSaleAmount(MoneyUtils.nullToZero(v.getSaleAmount()))
                        .setCostAmount(MoneyUtils.nullToZero(v.getCostAmount()))
                        .setVirtualAmount(MoneyUtils.nullToZero(v.getVirtualAmount()));
                //计算月销售额数据
                this.computeMonthData(monthEntity);
                insertList.add(monthEntity);
            }
        });
        //待更新的数据的数据转换
        Map<String,MonthSalesEntity> updateMap = CollectionUtils.isEmpty(updateList) ? new HashMap<>()
                : updateList.stream().collect(Collectors.toMap(k -> k.getShopId() + DateUtils.format(k.getMonthDate(), DateFormatEnum.YYYYMMDD1), month -> month));
        //处理已存在的entity数据
        entityMap.forEach((k,v) -> {
            //待更新的是数据中没有当前entity，说明没有当前店铺的月销售数据，则需要更新数据
            if(!updateMap.containsKey(k)){
                v.setTotalOrder(0L).setSucceedOrder(0L)
                  .setSaleAmount(BigDecimal.ZERO).setCostAmount(BigDecimal.ZERO)
                  .setVirtualAmount(BigDecimal.ZERO).setUpdateTime(DateUtils.getCurrentDateTime());
                //计算月销售额数据
                this.computeMonthData(v);
                updateList.add(v);
            }
        });
        monthSalesService.updateBatchById(updateList,100);
        monthSalesService.saveBatch(insertList,100);
        return new Response<>(ResponseEnum.SUCCESS,"统计完成");
    }
    /**
     * <p>@Description 计算月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 16:20 </p>
     * @param monthEntity 月销售额数据
     * @return
     */
    private void computeMonthData(MonthSalesEntity monthEntity){
        //计算月支出费=月成本费+月推广费+月服务费+月刷单费
        monthEntity.setTotalCost(monthEntity.getCostAmount().add(monthEntity.getAdvertAmount()).add(monthEntity.getServiceAmount()).add(monthEntity.getVirtualAmount()));
        //计算月失败单数=月订单数-月成交单数
        monthEntity.setFailedOrder(monthEntity.getTotalOrder()-monthEntity.getSucceedOrder());
        //计算月毛利润=月销售额-月成本费
        monthEntity.setGrossProfit(monthEntity.getSaleAmount().subtract(monthEntity.getCostAmount()));
        //计算月毛利率=月毛利润/月成本费 * 100%
        monthEntity.setGrossProfitRate(monthEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (monthEntity.getGrossProfit().divide(monthEntity.getCostAmount(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        //月净利润=月销售额-月支出费
        monthEntity.setRetainedProfits((monthEntity.getSaleAmount().subtract(monthEntity.getTotalCost())));
        //月净利率=月净利润/月支出费 * 100%
        monthEntity.setRetainedProfitsRate(monthEntity.getTotalCost().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                :(monthEntity.getRetainedProfits().divide(monthEntity.getTotalCost(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        //更新时间
        monthEntity.setUpdateTime(DateUtils.getCurrentDateTime());
    }
    /**
     * <p>@Description 查询月销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:09 </p>
     * @param monthDTO 查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return
     */
    @Override
    public Response<List<MonthDTO>> findMonthList(MonthDTO monthDTO, int pageNum, long pageSize) {
        Page<MonthDTO> page = new Page<>(pageNum,pageSize);
        page = monthSalesService.findMonthList(page,monthDTO);
        List<MonthDTO> shopList =  page.getRecords();
        Response<List<MonthDTO>> response = new Response<>(ResponseEnum.SUCCESS,shopList);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalNum(page.getTotal());
        return response;
    }
}
