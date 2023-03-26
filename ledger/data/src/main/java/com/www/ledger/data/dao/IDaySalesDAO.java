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
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 月销售额
     */
    List<MonthDTO> countMonthSale(String userId);
    /**
     * <p>@Description 根据用户ID查询用户日销售额entity数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:20 </p>
     * @param userId 用户ID
     * @return 日销售额entity数据
     */
    List<DaySalesEntity> findDaySalesList(String userId);
    /**
     * <p>@Description 删除用户的所有日销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:26 </p>
     * @param userId 用户ID
     * @return true删除成功，false删除失败
     */
    boolean deleteDayList(String userId);
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
