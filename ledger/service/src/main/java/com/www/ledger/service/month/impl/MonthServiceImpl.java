package com.www.ledger.service.month.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.exception.BusinessException;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.common.utils.MoneyUtils;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dao.IMonthSalesDAO;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dao.IUserShopDAO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.MonthSalesEntity;
import com.www.ledger.data.entity.UserShopEntity;
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
    private IOrderInfoDAO orderInfoDAO;
    @Autowired
    private IUserShopDAO userShopDAO;
    @Autowired
    private IMonthSalesDAO monthSalesDAO;
    @Autowired
    private IDaySalesDAO daySalesDAO;


    /**
     * <p>@Description 增加/减少月销售推广及服务费用  </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:09 </p>
     * @param monthDTO 费用信息
     * @return
     */
    @Override
    public Result<String> updateMonthAmt(MonthDTO monthDTO) {
        monthDTO.setAdvertAmount(MoneyUtils.nullToZero(monthDTO.getAdvertAmount()));
        monthDTO.setServiceAmount(MoneyUtils.nullToZero(monthDTO.getServiceAmount()));
        if(monthDTO.getAdvertAmount().compareTo(BigDecimal.ZERO) == 0 && monthDTO.getServiceAmount().compareTo(BigDecimal.ZERO) == 0){
            throw new BusinessException("推广费和服务费不能都为0");
        }
        MonthSalesEntity monthEntity = monthSalesDAO.findMonthSales(monthDTO.getUserId(),monthDTO.getMsId());
        if(monthEntity == null){
            throw new BusinessException("月销售数据不存在");
        }
        //增加月销售推广及服务费用
        monthEntity.setAdvertAmount((monthEntity.getAdvertAmount().add(monthDTO.getAdvertAmount())).setScale(2,RoundingMode.HALF_UP))
                .setServiceAmount((monthEntity.getServiceAmount().add(monthDTO.getServiceAmount())).setScale(2,RoundingMode.HALF_UP));
        if(monthEntity.getAdvertAmount().compareTo(BigDecimal.ZERO) == -1){
            throw new BusinessException("推广费不能为负数");
        }
        if(monthEntity.getServiceAmount().compareTo(BigDecimal.ZERO) == -1){
            throw new BusinessException("服务费不能为负数");
        }
        //计算月销售额数据
        this.computeMonthData(monthEntity);
        if(monthSalesDAO.updateById(monthEntity)){
            return new Result<>("修改成功");
        }
        return new Result<>("修改失败");
    }
    /**
     * <p>@Description 编辑月销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 18:26 </p>
     * @param monthDTO 月销售额数据
     * @return
     */
    @Override
    public Result<String> saveMonthSales(MonthDTO monthDTO) {
        Date monthDate = DateUtils.parse(monthDTO.getMonthDateStr() + "-01",DateFormatEnum.YYYYMMDD1);
        if(monthDate == null){
            throw new BusinessException("月份格式错误");
        }
        //判断当前店铺是否属于用户的
        UserShopEntity shopEntity = userShopDAO.findUserShop(monthDTO.getUserId(),monthDTO.getShopId());
        MonthSalesEntity monthEntity = null;
        //月销售数据修改
        if(monthDTO.getMsId() != null){
            monthEntity = monthSalesDAO.findMonthSales(monthDTO.getUserId(),monthDTO.getMsId());
            if(monthEntity == null){
                throw new BusinessException("店铺月销售数据不存在");
            }
        }else {//月销售数据新增
            monthEntity = monthSalesDAO.findMonthSales(monthDTO.getUserId(),monthDTO.getShopId(),monthDate);
            if(monthEntity != null){
                throw new BusinessException("已存在店铺月销售数据");
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
        if(monthSalesDAO.saveOrUpdate(monthEntity)){
            return new Result<>("编辑成功");
        }
        return new Result<>("编辑失败");
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
    public Result<String> deleteMonthData(String userId,Long msId) {
        if(monthSalesDAO.deleteMonthSales(userId,msId)){
            return new Result<>("删除成功");
        }
        return new Result<>("删除失败");
    }
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
        //统计出所有店铺汇总的月销售没有数据，则需要删除存在的数据
        if(isShop == false && CollectionUtils.isEmpty(countList) && CollectionUtils.isNotEmpty(monthList)){
            //没有统计的年销售额但有年销售额数据，则需要删除年销售数据
            if(monthSalesDAO.deleteMonthList(userId,isShop)){
                return true;
            }
            return false;
        }
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
                        .setFailedOrder(v.getFailedOrder() == null ? 0L : v.getFailedOrder())
                        .setUpdateTime(DateUtils.getCurrentDateTime())
                        .setSaleAmount(MoneyUtils.nullToZero(v.getSaleAmount()))
                        .setCostAmount(MoneyUtils.nullToZero(v.getCostAmount()))
                        .setVirtualAmount(MoneyUtils.nullToZero(v.getVirtualAmount()));
                //统计所有店铺汇总的月销售额的推广费和服务费需要更新
                if(isShop == false){
                    monthEntity.setAdvertAmount(MoneyUtils.nullToZero(v.getAdvertAmount()))
                            .setServiceAmount(MoneyUtils.nullToZero(v.getServiceAmount()));
                }
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
                        .setSaleAmount(MoneyUtils.nullToZero(v.getSaleAmount()))
                        .setCostAmount(MoneyUtils.nullToZero(v.getCostAmount()))
                        .setVirtualAmount(MoneyUtils.nullToZero(v.getVirtualAmount()));
                //统计所有店铺汇总的月销售额的推广费和服务费需要更新
                if(isShop == false){
                    monthEntity.setAdvertAmount(MoneyUtils.nullToZero(v.getAdvertAmount()))
                            .setServiceAmount(MoneyUtils.nullToZero(v.getServiceAmount()));
                }else {//统计店铺的月销售额的推广费和服务费需要赋值0
                    monthEntity.setAdvertAmount(BigDecimal.ZERO).setServiceAmount(BigDecimal.ZERO);
                }
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
        monthSalesDAO.updateBatchById(updateList,100);
        monthSalesDAO.saveBatch(insertList,100);
        //统计出所有店铺汇总的月销售数据在数据库中不存在，则需要删除存在的数据
        if(isShop == false){
            List<Long> deleteList = new ArrayList<>();//待删除的数据
            //查询已存在的年销售额数据（yearList）的主键在统计的年销售额（dtoMap）不存在，则说明没有年销售额数据，需要删除已存在的数据
            monthList.forEach(e -> {
                if(!dtoMap.containsKey(e.getShopId() + DateUtils.format(e.getMonthDate(), DateFormatEnum.YYYYMMDD1))){
                    deleteList.add(e.getMsId());
                }
            });
            monthSalesDAO.removeByIds(deleteList);
        }
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
