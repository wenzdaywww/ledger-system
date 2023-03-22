package com.www.ledger.service.order;

import com.www.common.data.response.Response;
import com.www.ledger.data.dto.OrderDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>@Description 订单信息service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
public interface IOrderService {
    /**
     * <p>@Description 导入订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/21 22:12 </p>
     * @param file
     * @param orderDTO
     * @return
     */
    Response<String> importOrder(MultipartFile file,OrderDTO orderDTO);
    /**
     * <p>@Description 保存订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:41 </p>
     * @param orderDTO 订单信息
     * @return Response<java.lang.String>
     */
    Response<String> saveOrderInfo(OrderDTO orderDTO);
    /**
     * <p>@Description 删除订单 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 09:59 </p>
     * @param userId 用户ID
     * @param oiId 订单ID
     * @return
     */
    Response<String> deleteOrderInfo(String userId,Long oiId);
    /**
     * <p>@Description 查询订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:43 </p>
     * @param orderDTO 订单查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return Response<java.util.List < com.www.ledger.data.dto.OrderDTO>>
     */
    Response<List<OrderDTO>> findOrdeList(OrderDTO orderDTO, int pageNum, long pageSize);
}
