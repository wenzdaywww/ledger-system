package com.www.ledger.service.order;

import com.www.common.data.response.Response;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.entity.OrderInfoEntity;

/**
 * <p>@Description 订单信息校验service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
public interface IOrderCheckService {
    /**
     * <p>@Description 保存订单信息前处理校验 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 23:04 </p>
     * @param orderDTO 订单信息
     * @param response 返回结果
     * @return boolean 不为空校验通过，为空校验不通过
     */
    OrderInfoEntity checkBeforeSaveOrderInfo(OrderDTO orderDTO, Response<String> response);
}
