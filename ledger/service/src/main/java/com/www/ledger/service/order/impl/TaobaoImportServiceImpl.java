package com.www.ledger.service.order.impl;

import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.ledger.data.dto.HeadDataDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.dto.OrderRowDTO;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.properties.LedgerProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>@Description 淘宝订单导入数据处理类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/23 22:12 </p>
 */
@Service
public class TaobaoImportServiceImpl extends OrderImportService {
    @Autowired
    private LedgerProperties ledgerProperties;
    /**  淘宝订单状态与系统订单状态对应关系 **/
    private static HashMap<String,String> stateMap = null;

    /**
     * <p>@Description 根据文件读取的订单数据处理待保存订单数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/23 22:17 </p>
     * @param dataList 文件读取的订单数据
     * @param saveList 待保存的订单数据
     * @param shopEntity 店铺信息
     * @return 失败的订单数据
     */
    @Override
    public List<OrderRowDTO> handleOrderData(ArrayList<ArrayList<String>> dataList, List<OrderRowDTO> saveList, UserShopEntity shopEntity) {
        if(CollectionUtils.isEmpty(dataList) || dataList.size() < 2){
            throw new BusinessException("订单文件没有数据，导入失败");
        }
        //去除读取的文件首行标题中的\uFEFF
        this.handleImportData(dataList,ledgerProperties.getRowLimit());
        List<OrderRowDTO> failList = new ArrayList<>();//失败的订单数据
        //保存待导入订单列坐标数据，
        Map<String, HeadDataDTO> headMap = new HashMap<>();
        for (int rowNum = 0; rowNum < dataList.size(); rowNum++){
            //第一行标题数据
            if(rowNum == 0){
                ledgerProperties.getTaobaoHeadMap().forEach((k,v) -> {
                    //第一行标题数据存在指定的列标题名称，则获取对应坐标
                    if(dataList.get(0).contains(v)){
                        HeadDataDTO headDataDTO = new HeadDataDTO();
                        headDataDTO.setName(v).setIndex(dataList.get(0).indexOf(v));
                        headMap.put(k,headDataDTO);
                    }
                });
            }else {//订单数据
                if(headMap.size() != ledgerProperties.getTaobaoHeadMap().size()){
                    StringBuilder errSB = new StringBuilder();
                    errSB.append("首行列标题不存在以下名称：").append(ledgerProperties.getTaobaoHeadMap().values().toString()).append(" 。导入失败");
                    throw new BusinessException(errSB.toString());
                }
                OrderRowDTO orderDTO = this.buildOrderDTO(headMap,rowNum,dataList.get(0).size(),dataList.get(rowNum),shopEntity);
                //订单数据校验
                super.checkOrderDTO(headMap,saveList,failList,orderDTO);
            }
        }
        return failList;
    }
    /**
     * <p>@Description 根据拼多多订单状态获取系统订单状态码值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/24 20:22 </p>
     * @param stateName 拼多多订单状态
     * @return 系统订单状态码值
     */
    @Override
    protected String getStateCode(String stateName){
        if(stateMap == null){
            stateMap = new HashMap<>();
            stateMap.put("", CodeDict.getValue(CodeTypeEnum.OrderState_Unconfirme.getType(), CodeTypeEnum.OrderState_Unconfirme.getKey()));//待确认
            stateMap.put(null,CodeDict.getValue(CodeTypeEnum.OrderState_Unconfirme.getType(), CodeTypeEnum.OrderState_Unconfirme.getKey()));//待确认
            stateMap.put("交易成功",CodeDict.getValue(CodeTypeEnum.OrderState_Success.getType(), CodeTypeEnum.OrderState_Success.getKey()));//交易成功
            stateMap.put("已发货，待签收",CodeDict.getValue(CodeTypeEnum.OrderState_Sended.getType(), CodeTypeEnum.OrderState_Sended.getKey()));//已发货，待签收
            stateMap.put("交易关闭",CodeDict.getValue(CodeTypeEnum.OrderState_Unpaid.getType(), CodeTypeEnum.OrderState_Unpaid.getKey()));//未支付，交易关闭
            stateMap.put("已取消",CodeDict.getValue(CodeTypeEnum.OrderState_Unpaid.getType(), CodeTypeEnum.OrderState_Unpaid.getKey()));//未支付，交易关闭
            stateMap.put("卖家已发货，等待买家确认",CodeDict.getValue(CodeTypeEnum.OrderState_Sended.getType(), CodeTypeEnum.OrderState_Sended.getKey()));//已发货，待签收
        }
        String code = stateMap.get(stateName.trim());
        //code为空，则赋值为【待确认】
        return StringUtils.isNotBlank(code) ? code : stateMap.get(null);
    }
}
