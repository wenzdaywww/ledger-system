package com.www.ledger.service.order.impl;

import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.utils.DateUtils;
import com.www.common.utils.BigDecimalUtils;
import com.www.ledger.data.dto.HeadDataDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.dto.OrderRowDTO;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.service.order.IOrderImportService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>@Description 订单导入抽象接口 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/23 22:10 </p>
 */
public abstract class OrderImportService implements IOrderImportService {
    /** 订单ID  **/
    protected static String ORDER_ID = "ORDER_ID";
    /** 订单日期  **/
    protected static String ORDER_DATE = "ORDER_DATE";
    /** 商品ID  **/
    protected static String GOODS_ID = "GOODS_ID";
    /**  商品名称 **/
    protected static String GOODS_NAME = "GOODS_NAME";
    /**  订单状态 **/
    protected static String ORDER_STATE = "ORDER_STATE";
    /**  商品总价 **/
    protected static String SALE_AMOUNT = "SALE_AMOUNT";
    /**  支付金额 **/
    protected static String PAYMENT_AMOUNT = "PAYMENT_AMOUNT";
    /**  备注 **/
    protected static String REMARK = "REMARK";
    /**
     * <p>@Description 根据平台订单状态获取系统订单状态码值 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/24 20:22 </p>
     * @param stateName 拼多多订单状态
     * @return 系统订单状态码值
     */
    protected abstract String getStateCode(String stateName);
    /**
     * <p>@Description 根据订单行数据组装订单数据对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/24 21:16 </p>
     * @param headMap 待导入订单列坐标数据
     * @param rowNum 当前订单数据在文件中行号
     * @param maxColumn 订单数据文件最大列号
     * @param rowList 订单行数据
     * @param shopEntity 店铺信息
     * @return 一行订单数据对象
     */
    protected OrderRowDTO buildOrderDTO(Map<String, HeadDataDTO> headMap,int rowNum,int maxColumn,ArrayList<String> rowList, UserShopEntity shopEntity){
        OrderRowDTO orderDTO = new OrderRowDTO();
        orderDTO.setShopId(shopEntity.getShopId()).setUserId(shopEntity.getUserId()).setRowNum(rowNum).setMaxColumn(maxColumn);
        orderDTO.setOrderId(this.getCellData(rowList,headMap,ORDER_ID));
        orderDTO.setOrderDate(this.getOrderDate(this.getCellData(rowList,headMap,ORDER_DATE)));
        orderDTO.setGoodsId(this.getCellData(rowList,headMap,GOODS_ID));
        orderDTO.setGoodsName(StringUtils.substring(this.getCellData(rowList,headMap,GOODS_NAME),0,60));
        orderDTO.setOrderState(this.getStateCode(this.getCellData(rowList,headMap,ORDER_STATE)));
        orderDTO.setSaleAmount(BigDecimalUtils.strToAmt(this.getCellData(rowList,headMap,SALE_AMOUNT)));
        orderDTO.setPaymentAmount(BigDecimalUtils.strToAmt(this.getCellData(rowList,headMap,PAYMENT_AMOUNT)));
        orderDTO.setCostAmount(BigDecimal.ZERO);
        //商家备注存的是1688订单、和其他备注信息，格式如：3243663938570794151#10.8#这是备注内容
        String remark = this.getCellData(rowList,headMap,REMARK);
        String[] arr = remark != null ? remark.split(CharConstant.JING_HAO) : null;
        if(arr != null){
            if(arr.length >= 2){
                //订单备注前2项处理
                this.setOrderRemark(arr,orderDTO);
            }
            if(arr.length == 1){
                orderDTO.setRemark(StringUtils.isNotBlank(arr[0]) ? arr[0] : null);
            }else if(arr.length == 3){
                orderDTO.setRemark(StringUtils.isNotBlank(arr[2]) ? arr[2] : null);
            }
        }
        return orderDTO;
    };
    /**
     * <p>@Description 订单备注前2项处理 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/26 09:52 </p>
     * @param arr  订单备注数值
     * @param orderDTO 一行订单数据对象
     */
    private void setOrderRemark(String[] arr,OrderRowDTO orderDTO){
        //备注以刷单开头，则状态改为刷单
        if(StringUtils.equals(arr[0],"刷单")){
            orderDTO.setOrderState(CodeDict.getValue(CodeTypeEnum.OrderState_Virtual.getType(), CodeTypeEnum.OrderState_Virtual.getKey()));
        }else {
            orderDTO.setSupplyId(StringUtils.replace(arr[0],CharConstant.SINGLE_QUOTATION_MARK,CharConstant.EMPTY));
        }
        orderDTO.setCostAmount(BigDecimalUtils.strToAmt(arr[1]));
    }
    /**
     * <p>@Description 订单数据校验 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/24 21:19 </p>
     * @param headMap 待导入订单列坐标数据
     * @param saveList 待保存的订单数据
     * @param failList 失败的订单数据
     * @param orderDTO 订单数据对象
     */
    protected void checkOrderDTO(Map<String, HeadDataDTO> headMap, List<OrderRowDTO> saveList, List<OrderRowDTO> failList, OrderRowDTO orderDTO){
        StringBuilder errSB = new StringBuilder();
        if (StringUtils.length(orderDTO.getOrderId()) == 0 || StringUtils.length(orderDTO.getOrderId()) > 40){
            errSB.append(headMap.get(ORDER_ID).getName()).append("不能为空且不能超过40个字数;");//订单ID长度不能为空
        }
        if(orderDTO.getOrderDate() == null){
            errSB.append(headMap.get(ORDER_DATE).getName()).append("不能为空或格式错误;");//订单日期不能为空或格式错误
        }
        if(StringUtils.length(orderDTO.getSupplyId()) > 40){
            errSB.append(headMap.get(REMARK).getName()).append("的1688订单ID不能超过40个字数;");//1688订单ID不能超过40个字数
        }
        if(StringUtils.length(orderDTO.getGoodsId()) > 40){
            errSB.append(headMap.get(GOODS_ID).getName()).append("不能超过40个字数;");//商品ID不能超过40个字数
        }
        if(StringUtils.length(orderDTO.getGoodsName()) > 60){
            errSB.append(headMap.get(GOODS_NAME).getName()).append("不能超过60个字数;");//商品名称不能超过40个字数
        }
        if(orderDTO.getSaleAmount() == null){
            errSB.append(headMap.get(SALE_AMOUNT).getName()).append("不能为空或数值错误;");//商品总价不能为空或数值错误
        }else if(!(orderDTO.getSaleAmount().compareTo(BigDecimal.ZERO) != -1 && orderDTO.getSaleAmount().compareTo(new BigDecimal("99999999.99")) != 1)){
            errSB.append(headMap.get(SALE_AMOUNT).getName()).append("有效值应大于0且不大于99999999.99;");//商品总价有效值应为0~99999999.99
        }
        if(orderDTO.getPaymentAmount() == null){
            errSB.append(headMap.get(PAYMENT_AMOUNT).getName()).append("不能为空或数值错误;");//支付金额不能为空或数值错误;
        }else if(!(orderDTO.getPaymentAmount().compareTo(BigDecimal.ZERO) != -1 && orderDTO.getPaymentAmount().compareTo(new BigDecimal("99999999.99")) != 1)){
            errSB.append(headMap.get(PAYMENT_AMOUNT).getName()).append("有效值应大于0且不大于99999999.99;");//支付金额有效值应为0~99999999.99
        }
        if(errSB.length() > 0){
            orderDTO.setMessage(errSB.toString());
            failList.add(orderDTO);
        }else {
            if(StringUtils.equals(orderDTO.getOrderState(),CodeDict.getValue(CodeTypeEnum.OrderState_Unconfirme.getType(),CodeTypeEnum.OrderState_Unconfirme.getKey()))){
                orderDTO.setMessage("平台订单状态转换后为【待确定】，请确认实际订单状态");
            }
            saveList.add(orderDTO);
        }
    };
    /**
     * <p>@Description 去除读取的文件首行标题中的\uFEFF </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/24 22:09 </p>
     * @param dataList 文件读取的订单数据
     * @param rowLimt 文件行数限制，为负数则布校验文件行数
     * @return
     */
    protected void handleImportData(ArrayList<ArrayList<String>> dataList,int rowLimt){
        if(CollectionUtils.isEmpty(dataList)){
            return;
        }
        if(rowLimt > 0 && dataList.size() > rowLimt){
            throw new BusinessException("导入文件的行数不能超过"+ rowLimt + "行");
        }
        //去除读取的文件首行标题中的\uFEFF
        ArrayList<String> firstRow = Optional.ofNullable(dataList.get(0)).filter(e -> CollectionUtils.isNotEmpty(dataList.get(0)))
                .map(list -> {
                    ArrayList<String> tempRow = new ArrayList<>();
                    list.forEach(item -> {
                        //去除文件隐藏的字符\uFEFF
                        String newText = StringUtils.replace(item,"\uFEFF", CharConstant.EMPTY);
                        newText = StringUtils.replace(newText,"\ufeff",CharConstant.EMPTY);
                        tempRow.add(StringUtils.trim(newText));
                    });
                    return tempRow;
                }).orElse(new ArrayList<>());
        dataList.set(0,firstRow);
    }
    /**
     * <p>@Description 根据日期字符串转为日期对象 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/24 22:43 </p>
     * @param dateStr 日期字符串
     * @return 日期对象
     */
    protected Date getOrderDate(String dateStr){
        Date date = DateUtils.parse(dateStr, DateFormatEnum.YYYYMMDD1);
        if(date == null){
            date = DateUtils.parse(dateStr, DateFormatEnum.YYYYMMDD2);
        }
        return date;
    }
    /**
     * <p>@Description 从行数据中获取对应单元格数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 00:49 </p>
     * @param rowList 行数据
     * @param headMap 待导入订单列坐标数据
     * @param name 数据名称
     * @return 单元格数据
     */
    protected String getCellData(ArrayList<String> rowList, Map<String, HeadDataDTO> headMap,String name){
        HeadDataDTO headDataDTO = headMap.get(name);
        if(headDataDTO == null){
            return null;
        }
        return rowList.get(headDataDTO.getIndex());
    }
}
