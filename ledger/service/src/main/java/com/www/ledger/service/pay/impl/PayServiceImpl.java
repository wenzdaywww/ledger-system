package com.www.ledger.service.pay.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dao.IPayInfoDAO;
import com.www.ledger.data.dao.IUserShopDAO;
import com.www.ledger.data.dto.PayDTO;
import com.www.ledger.data.entity.PayInfoEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.service.pay.IPayService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>@Description 支出管理Service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/28 22:56 </p>
 */
@Service
public class PayServiceImpl implements IPayService {
    @Autowired
    private IPayInfoDAO payInfoDAO;
    @Autowired
    private IUserShopDAO userShopDAO;

    /**
     * <p>@Description 保存支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:27 </p>
     * @param payDTO 支出信息
     * @return 保存结果信息
     */
    @Override
    public Result<String> savePayInfo(PayDTO payDTO) {
        Date payDate = DateUtils.parse(payDTO.getPayDateStr(), DateFormatEnum.YYYYMMDD1);
        if(payDate == null){
            throw new BusinessException("支出日期格式错误");
        }
        if(CodeDict.isIllegalValue(CodeTypeEnum.PayType_advert.getType(), payDTO.getPayType())){
            throw new BusinessException("支出类型不合法");
        }
        PayInfoEntity entity = null;
        if(payDTO.getPayId() != null){
            entity = payInfoDAO.findPayInfoEntity(payDTO.getUserId(), payDTO.getPayId());
        }else {
            entity = new PayInfoEntity();
            entity.setCreateTime(DateUtils.getCurrentDateTime());
        }
        UserShopEntity shopEntity = userShopDAO.findUserShop(payDTO.getUserId(), payDTO.getShopId());
        entity.setPayDate(payDate).setShopId(payDTO.getShopId()).setUserId(payDTO.getUserId())
                .setPayType(payDTO.getPayType()).setPayAmount(payDTO.getPayAmount())
                .setRemark(payDTO.getRemark()).setUpdateTime(DateUtils.getCurrentDateTime());
        boolean isOk = payInfoDAO.saveOrUpdate(entity);
        return isOk ? new Result<>("保存成功") : new Result<>("保存失败");
    }
    /**
     * <p>@Description 删除支出信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:24 </p>
     * @param userId 用户ID
     * @param payId  支出ID
     * @return 删除结果信息
     */
    @Override
    public Result<String> deletePayInfo(String userId, Long payId) {
        boolean isOk = payInfoDAO.deletePayInfo(userId,payId);
        return isOk ? new Result<>("删除成功") : new Result<>("删除失败");
    }
    /**
     * <p>@Description 查询支出信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/29 19:23 </p>
     * @param payDTO   查询条件
     * @param pageNum  分页数量
     * @param pageSize 页面
     * @return 支出信息列表
     */
    @Override
    public Result<List<PayDTO>> findPayList(PayDTO payDTO, int pageNum, long pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 5 : pageSize;
        Page<PayDTO> page = new Page<>(pageNum,pageSize);
        page = payInfoDAO.findPayList(page,payDTO);
        List<PayDTO> shopList =  page.getRecords();
        if(CollectionUtils.isNotEmpty(shopList)){
            shopList.forEach(d -> {
                d.setPayTypeName(CodeDict.getCodeValueName(CodeTypeEnum.PayType_advert.getType(), d.getPayType()));
            });
        }
        return new Result<>(pageNum,pageSize,page.getTotal(),shopList);
    }
}
