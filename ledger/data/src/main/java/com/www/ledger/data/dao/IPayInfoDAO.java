package com.www.ledger.data.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.PayDTO;
import com.www.ledger.data.entity.PayInfoEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>@Description 支出信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IPayInfoDAO extends IService<PayInfoEntity> {
    /**
     * <p>@Description 统计月支出费用 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 09:44 </p>
     * @param userId 用户ID
     * @return 月支出费用
     */
    List<MonthDTO> countMonthPayInfo(String userId);
    /**
     * <p>@Description 查询店铺保证金 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 22:32 </p>
     * @param userId 用户ID
     * @return 店铺保证金
     */
    BigDecimal findShopGuarantee(String userId);
    /**
     * <p>@Description 查询支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 20:11 </p>
     * @param userId 用户ID
     * @param payId 支出ID
     * @return 为空则抛出业务异常
     */
    PayInfoEntity findPayInfoEntity(String userId, Long payId);
    /**
     * <p>@Description 删除支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 20:09 </p>
     * @param userId 用户ID
     * @param payId 支出ID
     * @return true删除成功，false删除失败
     */
    boolean deletePayInfo(String userId, Long payId);
    /**
     * <p>@Description 查询支出信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:58 </p>
     * @param page 分页信息
     * @param dto 查询条件
     * @return 支出信息列表
     */
    Page<PayDTO> findPayList(Page<PayDTO> page,PayDTO dto);
}
