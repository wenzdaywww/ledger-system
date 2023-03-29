package com.www.ledger.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.www.common.config.exception.BusinessException;
import com.www.ledger.data.dao.IPayInfoDAO;
import com.www.ledger.data.dto.PayDTO;
import com.www.ledger.data.entity.PayInfoEntity;
import com.www.ledger.data.mapper.PayInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * <p>@Description 支出信息DAO实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/13 20:45 </p>
 */
@Repository
public class PayInfoDAOImpl extends ServiceImpl<PayInfoMapper, PayInfoEntity> implements IPayInfoDAO {
    @Autowired
    private PayInfoMapper payInfoMapper;

    /**
     * <p>@Description 查询店铺保证金 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 22:32 </p>
     * @param userId 用户ID
     * @return 店铺保证金
     */
    @Override
    public BigDecimal findShopGuarantee(String userId) {
        return payInfoMapper.findShopGuarantee(userId);
    }
    /**
     * <p>@Description 查询支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 20:11 </p>
     * @param userId 用户ID
     * @param payId  支出ID
     * @return 为空则抛出业务异常
     */
    @Override
    public PayInfoEntity findPayInfoEntity(String userId, Long payId) {
        QueryWrapper<PayInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PayInfoEntity::getUserId,userId)
                .eq(PayInfoEntity::getPayId,payId);
        PayInfoEntity entity = payInfoMapper.selectOne(wrapper);
        if(entity == null){
            throw new BusinessException("查询不到用户的支出信息");
        }
        return entity;
    }
    /**
     * <p>@Description 删除支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 20:09 </p>
     * @param userId 用户ID
     * @param payId  支出ID
     * @return true删除成功，false删除失败
     */
    @Override
    public boolean deletePayInfo(String userId, Long payId) {
        if(StringUtils.isBlank(userId) || payId == null){
            return false;
        }
        QueryWrapper<PayInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PayInfoEntity::getUserId,userId)
                .eq(PayInfoEntity::getPayId,payId);
        return payInfoMapper.delete(wrapper) != 0;
    }
    /**
     * <p>@Description 查询支出信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:58 </p>
     * @param page 分页信息
     * @param dto  查询条件
     * @return 支出信息列表
     */
    @Override
    public Page<PayDTO> findPayList(Page<PayDTO> page, PayDTO dto) {
        return payInfoMapper.findPayList(page,dto);
    }
}
