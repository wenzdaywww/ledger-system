package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.DaySalesEntity;
import com.www.ledger.data.mapper.DaySalesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>@Description 日销售额信息DAO实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class DaySalesDAOImpl extends ServiceImpl<DaySalesMapper, DaySalesEntity> implements IDaySalesDAO {
    @Autowired
    private DaySalesMapper daySalesMapper;

    /**
     * <p>@Description 统计月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 月销售额
     */
    @Override
    public List<MonthDTO> countMonthSale(String userId) {
        return daySalesMapper.countMonthSale(userId);
    }
    /**
     * <p>@Description 根据用户ID查询用户日销售额entity数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:20 </p>
     * @param userId 用户ID
     * @return 日销售额entity数据
     */
    @Override
    public List<DaySalesEntity> findDaySalesList(String userId) {
        return daySalesMapper.findDaySalesList(userId);
    }
    /**
     * <p>@Description 删除用户的所有日销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:26 </p>
     * @param userId 用户ID
     * @return true删除成功，false删除失败
     */
    @Override
    public boolean deleteDayList(String userId) {
        QueryWrapper<DaySalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DaySalesEntity::getUserId,userId);
        return daySalesMapper.delete(wrapper) > 0;
    }
    /**
     * <p>@Description 查询日销售额列表信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto  查询条件
     * @return Page<ShopDTO>
     */
    @Override
    public Page<DayDTO> findDayList(Page<DayDTO> page, DayDTO dto) {
        return daySalesMapper.findDayList(page,dto);
    }
}
