package com.www.ledger.data.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.YearSalesEntity;

import java.util.List;

/**
 * <p>@Description 年销售额信息DAO接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IYearSalesDAO extends IService<YearSalesEntity> {
    /**
     * <p>@Description 根据所有店铺的年销售额汇总每年所有店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/27 20:12 </p>
     * @param userId 用户ID
     * @return 每年所有店铺销售额
     */
    List<YearDTO> countTotalYearData(String userId);
    /**
     * <p>@Description 删除用户的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:26 </p>
     * @param userId 用户ID
     * @param isShop true统计店铺的年销售额，false统计所有店铺的年销售额
     * @return true删除成功，false删除失败
     */
    boolean deleteYearList(String userId,boolean isShop);
    /**
     * <p>@Description 查询用户店铺的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:23 </p>
     * @param userId 用户ID
     * @return 用户店铺的年销售数据
     */
    List<YearSalesEntity> findShopYearSalesList(String userId);
    /**
     * <p>@Description 查询用户店铺汇总的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:23 </p>
     * @param userId 用户ID
     * @return 用户店铺汇总的年销售数据
     */
    List<YearSalesEntity> findTotalYearSalesList(String userId);
    /**
     * <p>@Description 统计店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 23:50 </p>
     * @param userId 用户ID
     * @return
     */
    List<ShopDTO> countShopData(String userId);
    /**
     * <p>@Description 查询我的年销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return
     */
    Page<YearDTO> findShopYearSalesList(Page<YearDTO> page, YearDTO dto);
}
