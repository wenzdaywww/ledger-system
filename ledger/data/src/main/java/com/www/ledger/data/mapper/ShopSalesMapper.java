package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.mybatis.annotation.RowLimitInterceptor;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.entity.ShopSalesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>@Description 店铺销售额信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface ShopSalesMapper extends BaseMapper<ShopSalesEntity> {
    /**
     * <p>@Description 查询导出店铺销售额数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:58 </p>
     * @param userId 用户ID
     * @return 店铺销售额数据
     */
    @RowLimitInterceptor
    List<ShopDTO> exportShopSalesData(@Param("userId") String userId);
    /**
     * <p>@Description 查询店销销售数据排行榜 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:58 </p>
     * @param userId 用户ID
     * @param descField 排序的字段
     * @return 店销销售数据排行榜
     */
    List<ShopDTO> findShopRank(@Param("userId") String userId,@Param("descField") String descField);
    /**
     * <p>@Description 查询用户的店销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:56 </p>
     * @param userId 用户ID
     * @return 店销售数据
     */
    List<ShopSalesEntity> findShopSalesList(@Param("userId") String userId);
    /**
     * <p>@Description 统计所有店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:46 </p>
     * @param userId 用户ID
     * @return
     */
    BookDTO countAllShopData(@Param("userId") String userId);
    /**
     * <p>@Description 查询我的店铺信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 店铺信息查询条件
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.www.ledger.data.dto.ShopDTO>
     */
    Page<ShopDTO> findShopList(Page<ShopDTO> page, @Param("obj") ShopDTO dto);
}
