package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.entity.DaySalesEntity;
import com.www.ledger.data.mapper.DaySalesMapper;
import org.apache.commons.lang3.StringUtils;
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
     * <p>@Description 导出店铺日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @param isShop true查询店铺的日销售额，false查询店铺汇总的日销售额
     * @return 店铺月销售额
     */
    @Override
    public List<DayDTO> exportDaySaleData(String userId, boolean isShop) {
        return daySalesMapper.exportDaySaleData(userId, isShop);
    }
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
    @Override
    public List<DayDTO> findLastDaySales(String userId,Long shopId, String startData, String endDate) {
        return daySalesMapper.findLastDaySales(userId,shopId,startData,endDate);
    }
    /**
     * <p>@Description 查询日销售数据排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/28 21:46 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID
     * @param descField 排序的字段
     * @return 日销售数据排行榜
     */
    @Override
    public List<DayDTO> findDayRank(String userId, Long shopId, String descField) {
        return daySalesMapper.findDayRank(userId, shopId, descField);
    }
    /**
     * <p>@Description 统计店铺月销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺月销售额
     */
    @Override
    public List<MonthDTO> countShopMonthSale(String userId) {
        return daySalesMapper.countShopMonthSale(userId);
    }
    /**
     * <p>@Description 统计店铺汇总日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺汇总日销售额
     */
    @Override
    public List<DayDTO> countTotalDaySale(String userId) {
        return daySalesMapper.countTotalDaySale(userId);
    }
    /**
     * <p>@Description 根据用户ID查询用户店铺日销售额entity数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:20 </p>
     * @param userId 用户ID
     * @return 店铺日销售额entity数据
     */
    @Override
    public List<DaySalesEntity> findShopDaySalesList(String userId) {
        return StringUtils.isBlank(userId) ? null : daySalesMapper.findShopDaySalesList(userId);
    }
    /**
     * <p>@Description 根据用户ID查询用户店铺汇总日销售额entity数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:20 </p>
     * @param userId 用户ID
     * @return 店铺汇总日销售额entity数据
     */
    @Override
    public List<DaySalesEntity> findTotalDaySalesList(String userId) {
        return StringUtils.isBlank(userId) ? null : daySalesMapper.findTotalDaySalesList(userId);
    }
    /**
     * <p>@Description 删除用户的所有日销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 19:26 </p>
     * @param userId 用户ID
     * @param isShop true删除店铺的日销售额，false删除所有店铺汇总的日销售额
     * @return true删除成功，false删除失败
     */
    @Override
    public boolean deleteDayList(String userId,boolean isShop) {
        QueryWrapper<DaySalesEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(DaySalesEntity::getUserId,userId);
        if(isShop){
            wrapper.lambda().isNotNull(DaySalesEntity::getShopId);
        }else {
            wrapper.lambda().isNull(DaySalesEntity::getShopId);
        }
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
