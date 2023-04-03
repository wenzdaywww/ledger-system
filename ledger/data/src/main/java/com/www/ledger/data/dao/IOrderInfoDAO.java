package com.www.ledger.data.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.entity.OrderInfoEntity;

import java.util.Date;
import java.util.List;

/**
 * <p>@Description 订单信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IOrderInfoDAO extends IService<OrderInfoEntity> {
    /**
     * <p>@Description 导出订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 22:27 </p>
     * @param userId 用户ID
     * @return 订单信息
     */
    List<OrderDTO> exportOrderData(String userId);
    /**
     * <p>@Description 删除用户的订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 21:00 </p>
     * @param userId 用户ID
     * @param oiId 订单主键id
     * @return 订单信息
     */
    boolean deleteOrderInfo(String userId,Long oiId);
    /**
     * <p>@Description 查询用户的订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 21:00 </p>
     * @param userId 用户ID
     * @param oiId 订单主键id
     * @return 订单信息
     */
    OrderInfoEntity findOrderInfo(String userId,Long oiId);
    /**
     * <p>@Description 获取订单中最大的日期 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 19:08 </p>
     * @param userId 用户ID
     * @param shopId 店铺ID 为空则查询用户下所有店铺订单，不为空则查询指定店铺下订单
     * @return 最大的日期
     */
    Date getMaxOrderDate(String userId, Long shopId);
    /**
     * <p>@Description 统计店铺日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 15:19 </p>
     * @param userId 用户ID
     * @return 店铺日销售额
     */
    List<DayDTO> countShopDaySale(String userId);
    /**
     * <p>@Description 查询订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 订单信息查询条件
     * @return Page<ShopDTO>
     */
    Page<OrderDTO> findOrdeList(Page<OrderDTO> page, OrderDTO dto);
}
