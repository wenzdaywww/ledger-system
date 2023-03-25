package com.www.ledger.service.order;

import com.www.ledger.data.dto.OrderRowDTO;
import com.www.ledger.data.entity.UserShopEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description 订单导入接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/23 22:10 </p>
 */
public interface IOrderImportService {
    /**
     * <p>@Description 根据文件读取的订单数据处理待保存订单数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/23 22:17 </p>
     * @param dataList 文件读取的订单数据
     * @param saveList 待保存的订单数据
     * @param shopEntity 店铺信息
     * @return 失败的订单数据
     */
    List<OrderRowDTO> handleOrderData(ArrayList<ArrayList<String>> dataList, List<OrderRowDTO> saveList, UserShopEntity shopEntity);
}
