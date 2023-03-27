package com.www.ledger.service.order.impl;

import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.utils.DateUtils;
import com.www.ledger.data.dto.HeadDataDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.dto.OrderRowDTO;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.properties.LedgerProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>@Description 拼多多订单导入数据处理类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/23 22:12 </p>
 */
@Slf4j
@Service
public class PddImportServiceImpl extends OrderImportService {
    @Autowired
    private LedgerProperties ledgerProperties;
    /**  拼多多订单状态与系统订单状态对应关系 **/
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
        super.handleImportData(dataList,ledgerProperties.getRowLimit());
        List<OrderRowDTO> failList = new ArrayList<>();//失败的订单数据
        //保存待导入订单列坐标数据，
        Map<String, HeadDataDTO> headMap = new HashMap<>();
        for (int rowNum = 0; rowNum < dataList.size(); rowNum++){
            //第一行标题数据
            if(rowNum == 0){
                ledgerProperties.getPddHeadMap().forEach((k,v) -> {
                    //第一行标题数据存在指定的列标题名称，则获取对应坐标
                    if(dataList.get(0).contains(v)){
                        HeadDataDTO headDataDTO = new HeadDataDTO();
                        headDataDTO.setName(v).setIndex(dataList.get(0).indexOf(v));
                        headMap.put(k,headDataDTO);
                    }
                });
            }else {//订单数据
                if(headMap.size() != ledgerProperties.getPddHeadMap().size()){
                    StringBuilder errSB = new StringBuilder();
                    errSB.append("首行列标题不存在以下名称：").append(ledgerProperties.getPddHeadMap().values().toString()).append(" 。导入失败");
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
     * <p>@Description 根据订单行数据组装订单数据对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/24 21:16 </p>
     * @param headMap    待导入订单列坐标数据
     * @param rowNum     当前订单数据在文件中行号
     * @param maxColumn  订单数据文件最大列号
     * @param rowList    订单行数据
     * @param shopEntity 店铺信息
     * @return 一行订单数据对象
     */
    @Override
    protected OrderRowDTO buildOrderDTO(Map<String, HeadDataDTO> headMap, int rowNum, int maxColumn, ArrayList<String> rowList, UserShopEntity shopEntity) {
        OrderRowDTO orderRowDTO = super.buildOrderDTO(headMap, rowNum, maxColumn, rowList, shopEntity);
        //拼多多未订单状态=已取消/待支付的订单没有【订单成交时间】，所以订单日期会为空，则此时截取订单号-前的年月日（yyMMdd）
        if(StringUtils.equalsAny(orderRowDTO.getOrderState(),
                CodeDict.getValue(CodeTypeEnum.OrderState_Unpaid.getType(), CodeTypeEnum.OrderState_Unpaid.getKey()),
                CodeDict.getValue(CodeTypeEnum.OrderState_Paidding.getType(), CodeTypeEnum.OrderState_Paidding.getKey()))){
            //拼多多订单号-前为年月日，格式：yyMMdd
            String dateStr = "20" +StringUtils.substring(orderRowDTO.getOrderId(),0,orderRowDTO.getOrderId().lastIndexOf(CharConstant.MINUS_SIGN));
            orderRowDTO.setOrderDate(DateUtils.parse(dateStr, DateFormatEnum.YYYYMMDD5));
        }
        return orderRowDTO;
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
            stateMap.put("",CodeDict.getValue(CodeTypeEnum.OrderState_Unconfirme.getType(), CodeTypeEnum.OrderState_Unconfirme.getKey()));//待确认
            stateMap.put(null,CodeDict.getValue(CodeTypeEnum.OrderState_Unconfirme.getType(), CodeTypeEnum.OrderState_Unconfirme.getKey()));//待确认
            stateMap.put("已发货，待签收",CodeDict.getValue(CodeTypeEnum.OrderState_Sended.getType(), CodeTypeEnum.OrderState_Sended.getKey()));//已发货，待签收
            stateMap.put("退款成功",CodeDict.getValue(CodeTypeEnum.OrderState_Nosend.getType(), CodeTypeEnum.OrderState_Nosend.getKey()));//未发货，退款成功
            stateMap.put("已签收",CodeDict.getValue(CodeTypeEnum.OrderState_Success.getType(), CodeTypeEnum.OrderState_Success.getKey()));//交易成功
            stateMap.put("未发货，退款成功",CodeDict.getValue(CodeTypeEnum.OrderState_Nosend.getType(), CodeTypeEnum.OrderState_Nosend.getKey()));//未发货，退款成功
            stateMap.put("已取消",CodeDict.getValue(CodeTypeEnum.OrderState_Unpaid.getType(), CodeTypeEnum.OrderState_Unpaid.getKey()));//未支付，交易关闭
            stateMap.put("待支付",CodeDict.getValue(CodeTypeEnum.OrderState_Paidding.getType(), CodeTypeEnum.OrderState_Paidding.getKey()));//待支付
            stateMap.put("待发货",CodeDict.getValue(CodeTypeEnum.OrderState_Sending.getType(), CodeTypeEnum.OrderState_Sending.getKey()));//待发货
        }
        String code = stateMap.get(stateName.trim());
        //code为空，则赋值为【待确认】
        return StringUtils.isNotBlank(code) ? code : stateMap.get(null);
    }
}
