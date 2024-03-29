package com.www.ledger.controller.code;

import com.www.common.config.code.dto.CodeDTO;
import com.www.common.config.code.write.CodeRedisWriteHandler;
import com.www.common.data.response.Result;
import com.www.ledger.data.vo.code.CodeOutVO;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>@Description code信息controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@Validated
@RestController
@RequestMapping("code")
public class CodeController {
    @Autowired
    private CodeRedisWriteHandler codeRedisWriteHandler;
    /**
     * <p>@Description 查询多个数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/3 16:39 </p>
     * @param list 字典类型集合
     * @return Response<List<CodeDTO>>
     */
    @PostMapping("codes")
    public Result<Map<String, List<CodeOutVO>>> findCodeDataList(@NotEmpty(message = "codeType不能为空") @RequestParam List<String> list){
        return this.findCodeDataList(list);
    }
    /**
     * <p>@Description 查询单个数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/3 16:39 </p>
     * @param type 字典类型
     * @return Response<List <CodeDTO>>
     */
    @GetMapping("code/{type}")
    public Result<Map<String,List<CodeOutVO>>> findCodeData(@PathVariable("type") String type){
        List<String> list = new ArrayList<>();
        list.add(type);
        return this.findCode(list);
    }
    /**
     * <p>@Description 查询数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/17 23:16 </p>
     * @param list
     * @return
     */
    private Result<Map<String, List<CodeOutVO>>> findCode(List<String> list){
        Result<Map<String,List<CodeOutVO>>> result = new Result<>();
        Map<String, Map<String, CodeDTO>> codeMap = codeRedisWriteHandler.getCodeData();
        Map<String,List<CodeOutVO>> typeMap = new HashMap<>();
        for (String codeType : list){
            Map<String, CodeDTO> valueMap = codeMap.get(codeType);
            if(MapUtils.isNotEmpty(valueMap)){
                List<CodeDTO> collect = valueMap.values().stream().collect(Collectors.toList());
                List<CodeOutVO> outVOList = new ArrayList<>();
                collect.forEach(dto -> {
                    CodeOutVO code = new CodeOutVO();
                    code.setValue(dto.getValue());
                    code.setName(dto.getName());
                    outVOList.add(code);
                });
                typeMap.put(codeType,outVOList);
            }
        }
        result.setData(typeMap);
        return result;
    }
}
