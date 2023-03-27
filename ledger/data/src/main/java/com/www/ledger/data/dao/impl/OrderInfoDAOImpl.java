package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.entity.OrderInfoEntity;
import com.www.ledger.data.mapper.OrderInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>@Description 订单信息Service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class OrderInfoDAOImpl extends ServiceImpl<OrderInfoMapper, OrderInfoEntity> implements IOrderInfoDAO {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /**
     * <p>@Description 删除用户的订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 21:00 </p>
     * @param userId 用户ID
     * @param oiId   订单主键id
     * @return 订单信息
     */
    @Override
    public boolean deleteOrderInfo(String userId, Long oiId) {
        if(StringUtils.isBlank(userId) || oiId == null){
            return false;
        }
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderInfoEntity::getOiId,oiId)
                .eq(OrderInfoEntity::getUserId,userId);
        return orderInfoMapper.delete(wrapper) != 0;
    }
    /**
     * <p>@Description 查询用户的订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 21:00 </p>
     * @param userId 用户ID
     * @param oiId   订单主键id
     * @return 订单信息
     */
    @Override
    public OrderInfoEntity findOrderInfo(String userId, Long oiId) {
        if(StringUtils.isBlank(userId) || oiId == null){
            return null;
        }
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(OrderInfoEntity::getUserId,userId)
                .eq(OrderInfoEntity::getOiId,oiId);
        return orderInfoMapper.selectOne(wrapper);
    }
    /**
     * <p>@Description 查询销量店铺近些日的销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:19 </p>
     * @param userId 用户ID
     * @param shopList 待统计的店铺ID
     * @param minDate 订单统计起始日期
     * @param maxDate 订单统计截止日期
     * @return 店铺近些日的销售额
     */
    @Override
    public List<OrderDTO> findMaxSalesOrder(String userId, List<Long> shopList, String minDate, String maxDate) {
        return orderInfoMapper.findMaxSalesOrder(userId,shopList,minDate,maxDate);
    }
    /**
     * <p>@Description 获取订单中最大的日期 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:08 </p>
     * @param userId 用户ID
     * @return 最大的日期
     */
    @Override
    public String getMaxOrderDate(String userId) {
        return orderInfoMapper.getMaxOrderDate(userId);
    }
    /**
     * <p>@Description 统计店铺日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺日销售额
     */
    @Override
    public List<DayDTO> countShopDaySale(String userId) {
        return orderInfoMapper.countShopDaySale(userId);
    }
    /**
     * <p>@Description 查询订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto  订单信息查询条件
     * @return Page<ShopDTO>
     */
    @Override
    public Page<OrderDTO> findOrdeList(Page<OrderDTO> page, OrderDTO dto) {
        return orderInfoMapper.findOrdeList(page,dto);
    }
}
