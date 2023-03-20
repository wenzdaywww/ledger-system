package com.www.ledger.service.entity;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.MonthSalesEntity;

import java.util.Date;
import java.util.List;

/**
 * <p>@Description 月销售额信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IMonthSalesService extends IService<MonthSalesEntity> {
    /**
     * <p>@Description 删除用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @param msId 月销售ID
     * @return true成功，false失败
     */
    boolean deleteMonthSales(String userId,Long msId);
    /**
     * <p>@Description 查询用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @return 月销售数据
     */
    List<MonthSalesEntity> findMonthSalesList(String userId);
    /**
     * <p>@Description 查询用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @param msId 月销售ID
     * @return 月销售数据
     */
    MonthSalesEntity findMonthSales(String userId,Long msId);
    /**
     * <p>@Description 查询用户的月销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:40 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param month 月份（每月1日）
     * @return 月销售数据
     */
    MonthSalesEntity findMonthSales(String userId, Long shopId, Date month);
    /**
     * <p>@Description 查询用户monthStr月份销量前1的店铺 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:13 </p>
     * @param userId 用户ID
     * @param monthStr 月份
     * @return 店铺ID
     */
    List<Long> findMaxSalesShop(String userId, String monthStr);
    /**
     * <p>@Description 查询用户近一年的销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @param date 统计的最近日期
     * @return
     */
    List<MonthDTO> findLastYearSales(String userId, String date);
    /**
     * <p>@Description 统计年销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 21:40 </p>
     * @param userId 用户ID
     * @return
     */
    List<YearDTO> countYearData(String userId);
    /**
     * <p>@Description 查询月销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return Page<ShopDTO>
     */
    Page<MonthDTO> findMonthList(Page<MonthDTO> page, MonthDTO dto);
}
