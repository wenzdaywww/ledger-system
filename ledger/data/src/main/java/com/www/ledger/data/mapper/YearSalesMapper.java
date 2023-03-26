package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.YearSalesEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>@Description 月销售额信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface YearSalesMapper extends BaseMapper<YearSalesEntity> {
    /**
     * <p>@Description 查询用户的年销售数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/20 20:23 </p>
     * @param userId 用户ID
     * @return 用户的年销售数据
     */
    List<YearSalesEntity> findYearSalesList(@Param("userId") String userId);
    /**
     * <p>@Description 统计店铺销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 23:50 </p>
     * @param userId 用户ID
     * @return
     */
    List<ShopDTO> countShopData(@Param("userId") String userId);
    /**
     * <p>@Description 查询我的年销售额列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/13 22:55 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return
     */
    Page<YearDTO> findYearList(Page<YearDTO> page, @Param("obj") YearDTO dto);
}
