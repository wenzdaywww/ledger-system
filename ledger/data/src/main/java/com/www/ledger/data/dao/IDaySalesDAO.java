package com.www.ledger.data.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.DaySalesEntity;

import java.util.List;

/**
 * <p>@Description 日销售额信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IDaySalesDAO extends IService<DaySalesEntity> {
    /**
     * <p>@Description 导出店铺日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @param isShop true查询店铺的日销售额，false查询店铺汇总的日销售额
     * @return 店铺月销售额
     */
    List<DayDTO> exportDaySaleData(String userId, boolean isShop);
    /**
     * <p>@Description 查询日期区间的日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/28 21:46 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID 为空则查询用户店铺汇总日销售额，不为空则查询指定店铺日销售额
     * @param startData 起始日期
     * @param endDate 截止日期
     * @return 日销售额
     */
    List<DayDTO> findLastDaySales(String userId,Long shopId,String startData,String endDate);
    /**
     * <p>@Description 查询日销售数据排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/28 21:46 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param descField 排序的字段
     * @return 日销售数据排行榜
     */
    List<DayDTO> findDayRank(String userId, Long shopId, String descField);
    /**
     * <p>@Description 统计店铺月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺月销售额
     */
    List<MonthDTO> countShopMonthSale(String userId);
    /**
     * <p>@Description 统计店铺汇总日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺汇总日销售额
     */
    List<DayDTO> countTotalDaySale(String userId);
    /**
     * <p>@Description 根据用户ID查询用户店铺日销售额entity数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:20 </p>
     * @param userId 用户ID
     * @return 店铺日销售额entity数据
     */
    List<DaySalesEntity> findShopDaySalesList(String userId);
    /**
     * <p>@Description 根据用户ID查询用户店铺汇总日销售额entity数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:20 </p>
     * @param userId 用户ID
     * @return 店铺汇总日销售额entity数据
     */
    List<DaySalesEntity> findTotalDaySalesList(String userId);
    /**
     * <p>@Description 删除用户的所有日销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:26 </p>
     * @param userId 用户ID
     * @param isShop true删除店铺的日销售额，false删除所有店铺汇总的日销售额
     * @return true删除成功，false删除失败
     */
    boolean deleteDayList(String userId,boolean isShop);
    /**
     * <p>@Description 查询日销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return Page<ShopDTO>
     */
    Page<DayDTO> findDayList(Page<DayDTO> page, DayDTO dto);
}
