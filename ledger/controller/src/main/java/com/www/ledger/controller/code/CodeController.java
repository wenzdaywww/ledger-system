package com.www.ledger.controller.code;

import com.www.common.config.code.dto.CodeDTO;
import com.www.common.config.code.write.CodeRedisWriteHandler;
import com.www.common.data.dto.response.ResponseDTO;
import com.www.common.data.enums.ResponseEnum;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @return ResponseDTO<List<CodeDTO>>
     */
    @PostMapping("codes")
    public ResponseDTO<Map<String, List<CodeDTO>>> findCodeDataList(@RequestParam List<String> list){
        ResponseDTO<Map<String,List<CodeDTO>>>response = new ResponseDTO<>();
        if(list == null || list.size() <= 0){
            response.setResponse(ResponseEnum.FAIL,"查询数据字典数据失败，信息不全",null);
            return response;
        }
        Map<String, Map<String, CodeDTO>> codeMap = codeRedisWriteHandler.getCodeData();
        Map<String,List<CodeDTO>> typeMap = new HashMap<>();
        for (String codeType : list){
            Map<String, CodeDTO> valueMap = codeMap.get(codeType);
            if(MapUtils.isNotEmpty(valueMap)){
                List<CodeDTO> collect = valueMap.values().stream().collect(Collectors.toList());
                typeMap.put(codeType,collect);
            }
        }
        response.setResponse(typeMap);
        return response;
    }
    /**
     * <p>@Description 查询单个数据字典数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2022/2/3 16:39 </p>
     * @param codeType 字典类型
     * @return com.www.common.data.dto.response.ResponseDTO<java.util.List < com.www.common.config.code.dto.CodeDTO>>
     */
    @GetMapping("code/{type}")
    public ResponseDTO<Map<String,List<CodeDTO>>> findCodeData(@PathVariable("type") String codeType){
        ResponseDTO<Map<String,List<CodeDTO>>>response = new ResponseDTO<>();
        if(StringUtils.isBlank(codeType)){
            response.setResponse(ResponseEnum.FAIL,"查询单个数据字典数据失败，信息不全",null);
            return response;
        }
        List<String> list = new ArrayList<>();
        list.add(codeType);
        return this.findCodeDataList(list);
    }
}
