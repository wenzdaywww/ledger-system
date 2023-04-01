package com.www.ledger.service.month.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.common.utils.BigDecimalUtils;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dao.IMonthSalesDAO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.MonthSalesEntity;
import com.www.ledger.service.month.IMonthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
    private IMonthSalesDAO monthSalesDAO;
    @Autowired
    private IDaySalesDAO daySalesDAO;


    /**
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 14:01 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Result<String> saveAndCountMonthData(String userId) {
        //统计店铺月销售额
        List<MonthDTO> countShopList = daySalesDAO.countShopMonthSale(userId);
        //查询存在的店铺月销售额数据
        List<MonthSalesEntity> monthShopList = monthSalesDAO.findShopMonthSalesList(userId);
        //保存店铺的月销售额
        boolean shopOk = this.saveMonthData(countShopList,monthShopList,userId,true);
        if(shopOk == false){
            return new Result<>("统计失败");
        }
        //统计店铺汇总的月销售额
        List<MonthDTO> countToatlList = monthSalesDAO.countTotalMonthData(userId);
        //查询存在店铺汇总的月销售额数据
        List<MonthSalesEntity> monthToatlList = monthSalesDAO.findTotalMonthSalesList(userId);
        //保存店铺的月销售额
        this.saveMonthData(countToatlList,monthToatlList,userId,false);
        return new Result<>("统计完成");
    }
    /**
     * <p>@Description 根据统计出的月销售数据和已存在的月销售数据处理保存成新的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/27 21:17 </p>
     * @param countList 统计出的月销售数据
     * @param monthList 已存在的月销售数据
     * @param userId 用户ID
     * @param isShop true统计店铺的月销售额，false统计所有店铺汇总的月销售额
     * @return true保存成功，false保存失败
     */
    private boolean saveMonthData(List<MonthDTO> countList,List<MonthSalesEntity> monthList,String userId,boolean isShop){
        //没有统计的年销售额但有年销售额数据，则需要删除年销售数据
        if(CollectionUtils.isEmpty(countList) && CollectionUtils.isNotEmpty(monthList)){
            if(monthSalesDAO.deleteMonthList(userId,isShop)){
                return true;
            }
            return false;
        }
        List<MonthSalesEntity> insertList = new ArrayList<>();//待插入的数据
        List<MonthSalesEntity> updateList = new ArrayList<>();//待更新的数据
        List<Long> deleteList = new ArrayList<>();//待删除的数据
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
                        .setFailedOrder(v.getFailedOrder() == null ? 0L : v.getFailedOrder())
                        .setUpdateTime(DateUtils.getCurrentDateTime())
                        .setSaleAmount(BigDecimalUtils.nullToZero(v.getSaleAmount()))
                        .setCostAmount(BigDecimalUtils.nullToZero(v.getCostAmount()))
                        .setAdvertAmount(BigDecimalUtils.nullToZero(v.getAdvertAmount()))
                        .setServiceAmount(BigDecimalUtils.nullToZero(v.getServiceAmount()))
                        .setVirtualAmount(BigDecimalUtils.nullToZero(v.getVirtualAmount()));
                //计算月销售额数据
                this.computeMonthData(monthEntity);
                updateList.add(monthEntity);
            }else {//统计出的月销售额中没有存在的entity数据，则插入数据
                MonthSalesEntity monthEntity = new MonthSalesEntity();
                monthEntity.setShopId(v.getShopId()).setUserId(userId)
                        .setMonthDate(DateUtils.parse(v.getMonthDateStr(),DateFormatEnum.YYYYMMDD1))
                        .setTotalOrder(v.getTotalOrder() == null ? 0L : v.getTotalOrder())
                        .setSucceedOrder(v.getSucceedOrder() == null ? 0L : v.getSucceedOrder())
                        .setFailedOrder(v.getFailedOrder() == null ? 0L : v.getFailedOrder())
                        .setSaleAmount(BigDecimalUtils.nullToZero(v.getSaleAmount()))
                        .setCostAmount(BigDecimalUtils.nullToZero(v.getCostAmount()))
                        .setAdvertAmount(BigDecimalUtils.nullToZero(v.getAdvertAmount()))
                        .setServiceAmount(BigDecimalUtils.nullToZero(v.getServiceAmount()))
                        .setVirtualAmount(BigDecimalUtils.nullToZero(v.getVirtualAmount()));
                //计算月销售额数据
                this.computeMonthData(monthEntity);
                insertList.add(monthEntity);
            }
        });
        //查询已存在的年销售额数据（yearList）的主键在统计的年销售额（dtoMap）不存在，则说明没有年销售额数据，需要删除已存在的数据
        monthList.forEach(e -> {
            if(!dtoMap.containsKey(e.getShopId() + DateUtils.format(e.getMonthDate(), DateFormatEnum.YYYYMMDD1))){
                deleteList.add(e.getMsId());
            }
        });
        monthSalesDAO.updateBatchById(updateList,100);
        monthSalesDAO.saveBatch(insertList,100);
        monthSalesDAO.removeByIds(deleteList);
        return true;
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
    public Result<List<MonthDTO>> findMonthList(MonthDTO monthDTO, int pageNum, long pageSize) {
        Page<MonthDTO> page = new Page<>(pageNum,pageSize);
        page = monthSalesDAO.findMonthList(page,monthDTO);
        List<MonthDTO> shopList =  page.getRecords();
        Result<List<MonthDTO>> result = new Result<>(shopList);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalNum(page.getTotal());
        return result;
    }
}
