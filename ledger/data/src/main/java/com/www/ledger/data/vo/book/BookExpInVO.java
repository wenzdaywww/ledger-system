package com.www.ledger.data.vo.book;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>@Description 生成报表文件的请求数据 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:13 </p>
 */
@Data
@Accessors(chain = true)//开启链式编程
public class BookExpInVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 要导出的数据选项 **/
    @NotNull(message = "要导出的数据选项不能为空")
    private List<Integer> sheets;
}
