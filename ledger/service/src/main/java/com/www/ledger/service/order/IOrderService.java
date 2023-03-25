package com.www.ledger.service.order;

import com.www.common.data.response.Result;
import com.www.ledger.data.dto.OrderDTO;
import org.springframework.http.ResponseEntity;
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
     * @param file 订单文件数据
     * @param orderDTO 订单导入对象
     * @param password 订单文件密码
     * @return 0=导入失败的则返回下载订单数据信息文件路径,1=导入成功提示信息
     */
    Result<String[]> importOrder(MultipartFile file, OrderDTO orderDTO, String password);
    /**
     * <p>@Description 保存订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:41 </p>
     * @param orderDTO 订单信息
     * @return Response<java.lang.String>
     */
    Result<String> saveOrderInfo(OrderDTO orderDTO);
    /**
     * <p>@Description 删除订单 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 09:59 </p>
     * @param userId 用户ID
     * @param oiId 订单ID
     * @return
     */
    Result<String> deleteOrderInfo(String userId,Long oiId);
    /**
     * <p>@Description 查询订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:43 </p>
     * @param orderDTO 订单查询条件
     * @param pageNum 分页数量
     * @param pageSize 页面
     * @return Response<java.util.List < com.www.ledger.data.dto.OrderDTO>>
     */
    Result<List<OrderDTO>> findOrdeList(OrderDTO orderDTO, int pageNum, long pageSize);
}
