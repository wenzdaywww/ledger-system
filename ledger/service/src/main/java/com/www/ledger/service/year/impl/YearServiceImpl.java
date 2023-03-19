package com.www.ledger.service.year.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.YearSalesEntity;
import com.www.ledger.data.mapper.MonthSalesMapper;
import com.www.ledger.data.mapper.YearSalesMapper;
import com.www.ledger.service.entity.IYearSalesService;
import com.www.ledger.service.year.IYearService;
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
 * <p>@Description 年销售额service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
@Slf4j
@Service
public class YearServiceImpl implements IYearService {
    @Autowired
    private YearSalesMapper yearSalesMapper;
    @Autowired
    private MonthSalesMapper monthSalesMapper;
    @Autowired
    private IYearSalesService yearSalesService;

    /**
     * <p>@Description 统计年销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:10 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Response<String> saveAndCountYearData(String userId) {
        //统计的年销售额
        List<YearDTO> countList = monthSalesMapper.countYearData(userId);
        //查询存在的年销售额数据
        QueryWrapper<YearSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(YearSalesEntity::getUserId,userId);
        List<YearSalesEntity> yearList = yearSalesMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(countList) && CollectionUtils.isNotEmpty(yearList)){
            //没有统计的年销售额但有年销售额数据，则需要删除年销售数据
            if(yearSalesMapper.delete(wrapper) != 0){
                return new Response<>(ResponseEnum.SUCCESS,"统计完成");
            }
            return new Response<>(ResponseEnum.FAIL,"统计失败");
        }
        List<YearSalesEntity> insertList = new ArrayList<>();//待插入的数据
        List<YearSalesEntity> updateList = new ArrayList<>();//待更新的数据
        List<Long> deleteList = new ArrayList<>();//待删除的数据
        //数据转换处理，key=店铺ID+年份日期（默认为年份01月01日），如：101320230101
        Map<String,YearSalesEntity> entityMap = CollectionUtils.isEmpty(yearList) ? new HashMap<>()
                : yearList.stream().collect(Collectors.toMap(k -> k.getShopId() + DateUtils.format(k.getYear(), DateFormatEnum.YYYYMMDD), month -> month));
        Map<String,YearDTO> dtoMap = CollectionUtils.isEmpty(countList) ? new HashMap<>()
                : countList.stream().collect(Collectors.toMap(k -> k.getShopId() + k.getYearStr(), month -> month));
        //处理统计的年销售额
        dtoMap.forEach((k,v) -> {
            //统计出的年销售额中已有存在的entity数据，则更新数据
            if(entityMap.containsKey(k)){
                YearSalesEntity yearEntity = entityMap.get(k);
                yearEntity.setTotalOrder(v.getTotalOrder() == null ? 0L : v.getTotalOrder())
                        .setSucceedOrder(v.getSucceedOrder() == null ? 0L : v.getSucceedOrder())
                        .setFailedOrder(v.getFailedOrder() == null ? 0L : v.getFailedOrder())
                        .setSaleAmount(MoneyUtils.nullToZero(v.getSaleAmount())).setCostAmount(MoneyUtils.nullToZero(v.getCostAmount()))
                        .setAdvertAmount(MoneyUtils.nullToZero(v.getAdvertAmount())).setServiceAmount(MoneyUtils.nullToZero(v.getServiceAmount()))
                        .setVirtualAmount(MoneyUtils.nullToZero(v.getVirtualAmount())).setUpdateTime(DateUtils.getCurrentDateTime());
                //计算月销售额数据
                this.computeYearData(yearEntity);
                updateList.add(yearEntity);
            }else {//统计出的年销售额中没有存在的entity数据，则插入数据
                YearSalesEntity yearEntity = new YearSalesEntity();
                yearEntity.setShopId(v.getShopId()).setUserId(userId).setYear(DateUtils.parse(v.getYearStr(),DateFormatEnum.YYYYMMDD))
                        .setTotalOrder(v.getTotalOrder() == null ? 0L : v.getTotalOrder())
                        .setSucceedOrder(v.getSucceedOrder() == null ? 0L : v.getSucceedOrder())
                        .setFailedOrder(v.getFailedOrder() == null ? 0L : v.getFailedOrder())
                        .setSaleAmount(MoneyUtils.nullToZero(v.getSaleAmount())).setCostAmount(MoneyUtils.nullToZero(v.getCostAmount()))
                        .setAdvertAmount(MoneyUtils.nullToZero(v.getAdvertAmount())).setServiceAmount(MoneyUtils.nullToZero(v.getServiceAmount()))
                        .setVirtualAmount(MoneyUtils.nullToZero(v.getVirtualAmount())).setUpdateTime(DateUtils.getCurrentDateTime());
                //计算月销售额数据
                this.computeYearData(yearEntity);
                insertList.add(yearEntity);
            }
        });
        //查询已存在的年销售额数据（yearList）的主键在统计的年销售额（dtoMap）不存在，则说明没有年销售额数据，需要删除已存在的数据
        yearList.forEach(e -> {
            if(!dtoMap.containsKey(e.getShopId() + DateUtils.format(e.getYear(), DateFormatEnum.YYYYMMDD))){
                deleteList.add(e.getYsId());
            }
        });
        yearSalesService.updateBatchById(updateList,100);
        yearSalesService.saveBatch(insertList,100);
        yearSalesService.removeByIds(deleteList);
        return new Response<>(ResponseEnum.SUCCESS,"统计完成");
    }
    /**
     * <p>@Description 计算年销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 16:20 </p>
     * @param yearEntity 年销售额数据
     * @return
     */
    private void computeYearData(YearSalesEntity yearEntity){
        //计算月毛利润=月销售额-月成本费
        yearEntity.setGrossProfit((yearEntity.getSaleAmount().subtract(yearEntity.getCostAmount())).setScale(2, RoundingMode.HALF_UP));
        //计算月毛利率=月毛利润/月成本费
        yearEntity.setGrossProfitRate( yearEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (yearEntity.getGrossProfit().divide(yearEntity.getCostAmount(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        //月净利润=月毛利润-月推广费-月服务费-月刷单费
        yearEntity.setRetainedProfits((yearEntity.getGrossProfit().subtract(yearEntity.getAdvertAmount())).subtract(yearEntity.getServiceAmount()).subtract(yearEntity.getVirtualAmount()));
        //月净利率=月净利润/(月成本+月推广费+月服务费+月刷单费)
        BigDecimal totalAmt = yearEntity.getCostAmount().add(yearEntity.getAdvertAmount()).add(yearEntity.getServiceAmount()).add(yearEntity.getVirtualAmount());
        yearEntity.setRetainedProfitsRate(totalAmt.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                :(yearEntity.getRetainedProfits().divide(totalAmt,5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
    }
    /**
     * <p>@Description 查询我的年销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:12 </p>
     * @param yearDTO  查询条件
     * @param pageNum  分页数量
     * @param pageSize 页码
     * @return
     */
    @Override
    public Response<List<YearDTO>> findYearList(YearDTO yearDTO, int pageNum, long pageSize) {
        Page<YearDTO> page = new Page<>(pageNum,pageSize);
        page = yearSalesMapper.findYearList(page,yearDTO);
        List<YearDTO> shopList =  page.getRecords();
        Response<List<YearDTO>> response = new Response<>(ResponseEnum.SUCCESS,shopList);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalNum(page.getTotal());
        return response;
    }
}
