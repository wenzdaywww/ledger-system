package com.www.ledger.data.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>@Description 订单导入列的标题数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/24 23:47 </p>
 */
@Data
@Accessors(chain = true)
public class HeadDataDTO {
    /**  订单导入列的标题名称 **/
    private String name;
    /**  订单导入列的标题坐在列坐标 **/
    private int index;
}
