package com.www.ledger.service.month.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.enums.ResponseEnum;
import com.www.common.data.response.Response;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.MonthSalesEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.mapper.MonthSalesMapper;
import com.www.ledger.data.mapper.OrderInfoMapper;
import com.www.ledger.data.mapper.UserShopMapper;
import com.www.ledger.service.entity.IMonthSalesService;
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
    private UserShopMapper userShopMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private MonthSalesMapper monthSalesMapper;
    @Autowired
    private IMonthSalesService monthSalesService;

    /**
     * <p>@Description 编辑月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:26 </p>
     * @param monthDTO 月销售额数据
     * @return
     */
    @Override
    public Response<String> saveMonthSales(MonthDTO monthDTO) {
        Date monthDate = DateUtils.parse(monthDTO.getMonthDateStr() + "-01",DateFormatEnum.YYYY_MM_DD);
        if(monthDate == null){
            return new Response<>(ResponseEnum.FAIL,"月份格式错误");
        }
        UserShopEntity shopEntity = userShopMapper.selectById(monthDTO.getShopId());
        if(shopEntity == null){
            return new Response<>(ResponseEnum.FAIL,"店铺ID错误");
        }
        QueryWrapper<MonthSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MonthSalesEntity::getUserId,monthDTO.getUserId())
                .eq(MonthSalesEntity::getShopId,monthDTO.getShopId())
                .eq(MonthSalesEntity::getMonthDate,monthDate);
        MonthSalesEntity monthEntity = monthSalesMapper.selectOne(wrapper);
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
        UpdateWrapper<MonthSalesEntity> wrapper = new UpdateWrapper<>();
        wrapper.lambda().eq(MonthSalesEntity::getUserId,userId)
                .eq(MonthSalesEntity::getMsId,msId);
        if(monthSalesMapper.delete(wrapper) != 0){
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
        //没有统计月销售额数据，则统计完成
        if(CollectionUtils.isEmpty(countList)){
            return new Response<>(ResponseEnum.SUCCESS,"统计完成");
        }
        //查询存在的月销售额数据
        QueryWrapper<MonthSalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(MonthSalesEntity::getUserId,userId);
        List<MonthSalesEntity> monthList = monthSalesMapper.selectList(wrapper);
        List<MonthSalesEntity> insertList = new ArrayList<>();//待插入的数据
        List<MonthSalesEntity> updateList = new ArrayList<>();//待更新的数据
        Map<String,MonthSalesEntity> entityMap = CollectionUtils.isEmpty(monthList) ? new HashMap<>()
                : monthList.stream().collect(Collectors.toMap(k -> k.getShopId() + DateUtils.format(k.getMonthDate(), DateFormatEnum.YYYY_MM_DD), month -> month));
        Map<String,MonthDTO> dtoMap = countList.stream().collect(Collectors.toMap(k -> k.getShopId() + k.getMonthDateStr(), month -> month));
        dtoMap.forEach((k,v) -> {
            //dtoMap中有entity数据，则更新数据
            if(entityMap.containsKey(k)){
                MonthSalesEntity monthEntity = entityMap.get(k);
                monthEntity.setTotalOrder(v.getTotalOrder()).setSucceedOrder(v.getSucceedOrder()).setUpdateTime(DateUtils.getCurrentDateTime())
                        .setSaleAmount(v.getSaleAmount()).setCostAmount(v.getCostAmount()).setVirtualAmount(v.getVirtualAmount());
                //计算月销售额数据
                this.computeMonthData(monthEntity);
                updateList.add(monthEntity);
            }else {//dtoMap中没有entity数据，则插入数据
                MonthSalesEntity monthEntity = new MonthSalesEntity();
                monthEntity.setShopId(v.getShopId()).setUserId(userId)
                        .setMonthDate(DateUtils.parse(v.getMonthDateStr(),DateFormatEnum.YYYY_MM_DD))
                        .setTotalOrder(v.getTotalOrder()).setSucceedOrder(v.getSucceedOrder())
                        .setAdvertAmount(BigDecimal.ZERO).setServiceAmount(BigDecimal.ZERO)
                        .setSaleAmount(v.getSaleAmount()).setCostAmount(v.getCostAmount())
                        .setVirtualAmount(v.getVirtualAmount());
                //计算月销售额数据
                this.computeMonthData(monthEntity);
                insertList.add(monthEntity);
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
        //计算月失败单数=月订单数-月成交单数
        monthEntity.setFailedOrder(monthEntity.getTotalOrder()-monthEntity.getSucceedOrder());
        //计算月毛利润=月销售额-月成本费
        monthEntity.setGrossProfit((monthEntity.getSaleAmount().subtract(monthEntity.getCostAmount())).setScale(2, RoundingMode.HALF_UP));
        //计算月毛利率=月毛利润/月成本费
        monthEntity.setGrossProfitRate( monthEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (monthEntity.getSaleAmount().divide(monthEntity.getCostAmount(),4,RoundingMode.HALF_UP)).setScale(2, RoundingMode.HALF_UP));
        //月净利润=月毛利润-月推广费-月服务费-月刷单费
        monthEntity.setRetainedProfits((monthEntity.getGrossProfit().subtract(monthEntity.getAdvertAmount())).subtract(monthEntity.getServiceAmount()).subtract(monthEntity.getVirtualAmount()));
        //月净利率=月净利润/(月成本+月推广费+月服务费+月刷单费)
        BigDecimal totalAmt = monthEntity.getCostAmount().add(monthEntity.getAdvertAmount()).add(monthEntity.getServiceAmount()).add(monthEntity.getVirtualAmount());
        monthEntity.setRetainedProfitsRate(totalAmt.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                :(monthEntity.getRetainedProfits().divide(totalAmt,4,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP));
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
        page = monthSalesMapper.findMonthList(page,monthDTO);
        List<MonthDTO> shopList =  page.getRecords();
        Response<List<MonthDTO>> response = new Response<>(ResponseEnum.SUCCESS,shopList);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalNum(page.getTotal());
        return response;
    }
}
