package com.www.ledger.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.mybatis.annotation.RowLimitInterceptor;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.PayDTO;
import com.www.ledger.data.entity.PayInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>@Description 支出信息Mapper </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 19:43 </p>
 */
@Mapper
public interface PayInfoMapper extends BaseMapper<PayInfoEntity> {
    /**
     * <p>@Description 导出支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 22:32 </p>
     * @param userId 用户ID
     * @return 支出信息
     */
    @RowLimitInterceptor
    List<PayDTO> exportPayInfoData(@Param("userId") String userId);
    /**
     * <p>@Description 统计月支出费用 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 09:44 </p>
     * @param userId 用户ID
     * @return 月支出费用
     */
    @RowLimitInterceptor
    List<MonthDTO> countMonthPayInfo(@Param("userId") String userId);
    /**
     * <p>@Description 查询店铺保证金 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 22:32 </p>
     * @param userId 用户ID
     * @return 店铺保证金
     */
    BigDecimal findShopGuarantee(@Param("userId")String userId);
    /**
     * <p>@Description 查询支出信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:58 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return 支出信息列表
     */
    Page<PayDTO> findPayList(Page<PayDTO> page, @Param("obj") PayDTO dto);
}
