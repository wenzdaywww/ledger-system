package com.www.ledger.service.day.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.common.utils.BigDecimalUtils;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.entity.DaySalesEntity;
import com.www.ledger.service.day.IDayService;
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
 * <p>@Description 日销售额业务处理接口实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/26 18:06 </p>
 */
@Service
public class DayServiceImpl implements IDayService {
    @Autowired
    private IDaySalesDAO daySalesDAO;
    @Autowired
    private IOrderInfoDAO orderInfoDAO;

    /**
     * <p>@Description 统计日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 20:09 </p>
     * @param userId 用户ID
     * @return
     */
    @Override
    public Result<String> saveAndCountDayData(String userId) {
        //统计的店铺日销售额
        List<DayDTO> countShopList = orderInfoDAO.countShopDaySale(userId);
        //查询存在的店铺日销售额数据
        List<DaySalesEntity> dayShopList = daySalesDAO.findShopDaySalesList(userId);
        //保存店铺日销售数据
        boolean shopOk = this.saveDayData(countShopList,dayShopList,userId,true);
        if (shopOk == false){
            return new Result<>("统计失败");
        }
        //统计的店铺汇总日销售额
        List<DayDTO> countTotalList = daySalesDAO.countTotalDaySale(userId);
        //查询存在的店铺汇总日销售额数据
        List<DaySalesEntity> dayTotalList = daySalesDAO.findTotalDaySalesList(userId);
        //保存店铺汇总日销售数据
        this.saveDayData(countTotalList,dayTotalList,userId,false);
        return new Result<>("统计完成");
    }
    /**
     * <p>@Description 根据统计出的日销售数据和已存在的日销售数据处理保存成新的日销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/27 20:26 </p>
     * @param countList 统计出的日销售数据
     * @param dayList 已存在的日销售数据
     * @param userId 用户ID
     * @param isShop true统计店铺的日销售额，false统计所有店铺汇总的日销售额
     * @return true保存成功，false保存失败
     */
    private boolean saveDayData(List<DayDTO> countList,List<DaySalesEntity> dayList,String userId,boolean isShop){
        if(CollectionUtils.isEmpty(countList) && CollectionUtils.isNotEmpty(dayList)){
            //没有统计的日销售额但有日销售额数据，则需要删除日销售数据
            if(daySalesDAO.deleteDayList(userId,isShop)){
                return true;
            }
            return false;
        }
        List<DaySalesEntity> insertList = new ArrayList<>();//待插入的数据
        List<DaySalesEntity> updateList = new ArrayList<>();//待更新的数据
        List<Long> deleteList = new ArrayList<>();//待删除的数据
        //数据转换处理，key=店铺ID+日期，如：10132023-01-01
        Map<String,DaySalesEntity> entityMap = CollectionUtils.isEmpty(dayList) ? new HashMap<>()
                : dayList.stream().collect(Collectors.toMap(k -> k.getShopId() + DateUtils.format(k.getDayDate(), DateFormatEnum.YYYYMMDD1), month -> month));
        Map<String, DayDTO> dtoMap = CollectionUtils.isEmpty(countList) ? new HashMap<>()
                : countList.stream().collect(Collectors.toMap(k -> k.getShopId() + k.getDayDateStr(), month -> month));
        //处理统计的日销售额
        dtoMap.forEach((k,v) -> {
            //统计出的日销售额中已有存在的entity数据，则更新数据
            if(entityMap.containsKey(k)){
                DaySalesEntity dayEntity = entityMap.get(k);
                dayEntity.setTotalOrder(v.getTotalOrder() == null ? 0L : v.getTotalOrder())
                        .setSucceedOrder(v.getSucceedOrder() == null ? 0L : v.getSucceedOrder())
                        .setSaleAmount(BigDecimalUtils.nullToZero(v.getSaleAmount())).setCostAmount(BigDecimalUtils.nullToZero(v.getCostAmount()))
                        .setVirtualAmount(BigDecimalUtils.nullToZero(v.getVirtualAmount()));
                //计算月销售额数据
                this.computeDayData(dayEntity);
                updateList.add(dayEntity);
            }else {//统计出的日销售额中没有存在的entity数据，则插入数据
                DaySalesEntity dayEntity = new DaySalesEntity();
                dayEntity.setShopId(v.getShopId()).setUserId(userId).setDayDate(v.getDayDate())
                        .setTotalOrder(v.getTotalOrder() == null ? 0L : v.getTotalOrder())
                        .setSucceedOrder(v.getSucceedOrder() == null ? 0L : v.getSucceedOrder())
                        .setSaleAmount(BigDecimalUtils.nullToZero(v.getSaleAmount())).setCostAmount(BigDecimalUtils.nullToZero(v.getCostAmount()))
                        .setVirtualAmount(BigDecimalUtils.nullToZero(v.getVirtualAmount()));
                //计算日销售额数据
                this.computeDayData(dayEntity);
                insertList.add(dayEntity);
            }
        });
        //查询已存在的日销售额数据（yearList）的主键在统计的日销售额（dtoMap）不存在，则说明没有日销售额数据，需要删除已存在的数据
        dayList.forEach(e -> {
            if(!dtoMap.containsKey(e.getShopId() + DateUtils.format(e.getDayDate(), DateFormatEnum.YYYYMMDD1))){
                deleteList.add(e.getDsId());
            }
        });
        daySalesDAO.updateBatchById(updateList,100);
        daySalesDAO.saveBatch(insertList,100);
        daySalesDAO.removeByIds(deleteList);
        return true;
    }
    /**
     * <p>@Description 计算日销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 16:20 </p>
     * @param dayEntity 日销售额数据
     * @return
     */
    private void computeDayData(DaySalesEntity dayEntity){
        //日失败单量=日订单量-日成功单量
        dayEntity.setFailedOrder(dayEntity.getTotalOrder() - dayEntity.getSucceedOrder());
        //计算日支出费=日成本费+日刷单费
        dayEntity.setTotalCost(dayEntity.getCostAmount().add(dayEntity.getVirtualAmount()));
        //计算日毛利润=日销售额-日成本费
        dayEntity.setGrossProfit(dayEntity.getSaleAmount().subtract(dayEntity.getCostAmount()));
        //计算日毛利率=日毛利润/日成本费 * 100%
        dayEntity.setGrossProfitRate( dayEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (dayEntity.getGrossProfit().divide(dayEntity.getCostAmount(),5, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
        //日净利润=日销售额-日支出费
        dayEntity.setRetainedProfits(dayEntity.getSaleAmount().subtract(dayEntity.getTotalCost()));
        //日净利率=日净利润/日支出费 * 100%
        dayEntity.setRetainedProfitsRate(dayEntity.getTotalCost().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                :(dayEntity.getRetainedProfits().divide(dayEntity.getTotalCost(),5,RoundingMode.HALF_UP).multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        //更新时间
        dayEntity.setUpdateTime(DateUtils.getCurrentDateTime());
    }
    /**
     * <p>@Description 查询我的日销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 18:36 </p>
     * @param dayDTO   查询条件
     * @param pageNum  分页数量
     * @param pageSize 页码
     * @return 日销售额列表数据
     */
    @Override
    public Result<List<DayDTO>> findDayList(DayDTO dayDTO, int pageNum, long pageSize) {
        Page<DayDTO> page = new Page<>(pageNum,pageSize);
        page = daySalesDAO.findDayList(page,dayDTO);
        List<DayDTO> dayList =  page.getRecords();
        Result<List<DayDTO>> result = new Result<>(dayList);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalNum(page.getTotal());
        return result;
    }
}
