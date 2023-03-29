package com.www.ledger.service.year.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.YearSalesEntity;
import com.www.ledger.data.dao.IMonthSalesDAO;
import com.www.ledger.data.dao.IYearSalesDAO;
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
    private IMonthSalesDAO monthSalesDAO;
    @Autowired
    private IYearSalesDAO yearSalesDAO;

    /**
     * <p>@Description 统计年销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:10 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Result<String> saveAndCountYearData(String userId) {
        //统计店铺的年销售额
        List<YearDTO> countShopList = monthSalesDAO.countShopYearData(userId);
        //查询存在店铺的年销售额数据
        List<YearSalesEntity> shopYearList = yearSalesDAO.findShopYearSalesList(userId);
        //保存成新店铺的年销售数据
        boolean shopOk = this.saveYearData(countShopList,shopYearList,userId,true);
        if(shopOk == false){
            return new Result<>("统计失败");
        }
        //统计店铺汇总的年销售额
        List<YearDTO> countTotalList = yearSalesDAO.countTotalYearData(userId);
        //查询存在店铺汇总的年销售额数据
        List<YearSalesEntity> totalYearList = yearSalesDAO.findTotalYearSalesList(userId);
        //保存成新店铺汇总的年销售数据
        this.saveYearData(countTotalList,totalYearList,userId,false);
        return new Result<>("统计完成");
    }
    /**
     * <p>@Description 根据统计出的年销售数据和已存在的年销售数据处理保存成新的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/27 20:26 </p>
     * @param countList 统计出的年销售数据
     * @param yearList 已存在的年销售数据
     * @param userId 用户ID
     * @param isShop true统计店铺的年销售额，false统计所有店铺汇总的年销售额
     * @return true保存成功，false保存失败
     */
    private boolean saveYearData(List<YearDTO> countList,List<YearSalesEntity> yearList,String userId,boolean isShop){
        //没有统计的年销售额但有年销售额数据，则需要删除年销售数据
        if(CollectionUtils.isEmpty(countList) && CollectionUtils.isNotEmpty(yearList)){
            if(yearSalesDAO.deleteYearList(userId,isShop)){
                return true;
            }
            return false;
        }
        List<YearSalesEntity> insertList = new ArrayList<>();//待插入的数据
        List<YearSalesEntity> updateList = new ArrayList<>();//待更新的数据
        List<Long> deleteList = new ArrayList<>();//待删除的数据
        //数据转换处理，key=店铺ID+年份日期（默认为年份01月01日），如：101320230101
        Map<String,YearSalesEntity> entityMap = CollectionUtils.isEmpty(yearList) ? new HashMap<>()
                : yearList.stream().collect(Collectors.toMap(k -> k.getShopId() + DateUtils.format(k.getYear(), DateFormatEnum.YYYYMMDD1), month -> month));
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
                        .setVirtualAmount(MoneyUtils.nullToZero(v.getVirtualAmount()));
                //计算月销售额数据
                this.computeYearData(yearEntity);
                updateList.add(yearEntity);
            }else {//统计出的年销售额中没有存在的entity数据，则插入数据
                YearSalesEntity yearEntity = new YearSalesEntity();
                yearEntity.setShopId(v.getShopId()).setUserId(userId).setYear(DateUtils.parse(v.getYearStr(),DateFormatEnum.YYYYMMDD1))
                        .setTotalOrder(v.getTotalOrder() == null ? 0L : v.getTotalOrder())
                        .setSucceedOrder(v.getSucceedOrder() == null ? 0L : v.getSucceedOrder())
                        .setFailedOrder(v.getFailedOrder() == null ? 0L : v.getFailedOrder())
                        .setSaleAmount(MoneyUtils.nullToZero(v.getSaleAmount())).setCostAmount(MoneyUtils.nullToZero(v.getCostAmount()))
                        .setAdvertAmount(MoneyUtils.nullToZero(v.getAdvertAmount())).setServiceAmount(MoneyUtils.nullToZero(v.getServiceAmount()))
                        .setVirtualAmount(MoneyUtils.nullToZero(v.getVirtualAmount()));
                //计算年销售额数据
                this.computeYearData(yearEntity);
                insertList.add(yearEntity);
            }
        });
        //查询已存在的年销售额数据（yearList）的主键在统计的年销售额（dtoMap）不存在，则说明没有年销售额数据，需要删除已存在的数据
        yearList.forEach(e -> {
            if(!dtoMap.containsKey(e.getShopId() + DateUtils.format(e.getYear(), DateFormatEnum.YYYYMMDD1))){
                deleteList.add(e.getYsId());
            }
        });
        yearSalesDAO.updateBatchById(updateList,100);
        yearSalesDAO.saveBatch(insertList,100);
        yearSalesDAO.removeByIds(deleteList);
        return true;
    }
    /**
     * <p>@Description 计算年销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 16:20 </p>
     * @param yearEntity 年销售额数据
     * @return
     */
    private void computeYearData(YearSalesEntity yearEntity){
        //计算年支出费=年成本费+年推广费+年服务费+年刷单费
        yearEntity.setTotalCost(yearEntity.getCostAmount().add(yearEntity.getAdvertAmount()).add(yearEntity.getServiceAmount()).add(yearEntity.getVirtualAmount()));
        //计算年毛利润=年销售额-年成本费
        yearEntity.setGrossProfit(yearEntity.getSaleAmount().subtract(yearEntity.getCostAmount()));
        //计算年毛利率=年毛利润/年成本费 * 100%
        yearEntity.setGrossProfitRate( yearEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (yearEntity.getGrossProfit().divide(yearEntity.getCostAmount(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        //年净利润=年销售额-年支出费
        yearEntity.setRetainedProfits((yearEntity.getSaleAmount().subtract(yearEntity.getTotalCost())));
        //年净利率=年净利润/年支出费 * 100%
        yearEntity.setRetainedProfitsRate(yearEntity.getTotalCost().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                :(yearEntity.getRetainedProfits().divide(yearEntity.getTotalCost(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        //更新时间
        yearEntity.setUpdateTime(DateUtils.getCurrentDateTime());
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
    public Result<List<YearDTO>> findYearList(YearDTO yearDTO, int pageNum, long pageSize) {
        Page<YearDTO> page = new Page<>(pageNum,pageSize);
        page = yearSalesDAO.findShopYearSalesList(page,yearDTO);
        List<YearDTO> shopList =  page.getRecords();
        Result<List<YearDTO>> listResult = new Result<>(shopList);
        listResult.setPageNum(pageNum);
        listResult.setPageSize(pageSize);
        listResult.setTotalNum(page.getTotal());
        return listResult;
    }
}
