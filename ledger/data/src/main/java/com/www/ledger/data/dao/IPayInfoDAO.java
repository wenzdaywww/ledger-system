package com.www.ledger.data.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.www.ledger.data.dto.PayDTO;
import com.www.ledger.data.entity.PayInfoEntity;

/**
 * <p>@Description 支出信息Service接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
public interface IPayInfoDAO extends IService<PayInfoEntity> {
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
