package com.www.ledger.data.vo.code;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>@Description 查询数据字典数据的响应对象 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/17 23:14 </p>
 */
@Data
@Accessors(chain = true)
public class CodeResponse {
    /** code的数值 **/
    private String value;
    /** code的数值名称 **/
    private String name;
}
