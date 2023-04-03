package com.www.ledger.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.config.mvc.upload.IFileService;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.constant.FileTypeConstant;
import com.www.common.data.dto.excel.CellDTO;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.CsvUtils;
import com.www.common.utils.DateUtils;
import com.www.common.utils.ExcelUtils;
import com.www.common.utils.FileUtils;
import com.www.common.utils.BigDecimalUtils;
import com.www.common.utils.UidGeneratorUtils;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dao.IUserShopDAO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.dto.OrderRowDTO;
import com.www.ledger.data.entity.OrderInfoEntity;
import com.www.ledger.data.entity.UserShopEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.properties.LedgerProperties;
import com.www.ledger.service.order.IOrderCheckService;
import com.www.ledger.service.order.IOrderImportService;
import com.www.ledger.service.order.IOrderService;
import com.www.ledger.service.order.OrderImportFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>@Description 订单信息service实现类 </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/12 23:06 </p>
 */
@Slf4j
@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private IFileService fileService;
    @Autowired
    private IOrderInfoDAO orderInfoDAO;
    @Autowired
    private IUserShopDAO userShopDAO;
    @Autowired
    private IOrderCheckService orderCheckService;
    @Autowired
    private LedgerProperties ledgerProperties;

    /**
     * <p>@Description 导入订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/21 22:12 </p>
     * @param file 订单文件数据
     * @param orderDTO 订单导入对象
     * @param password 订单文件密码
     * @return 0=导入失败的则返回下载订单数据信息文件路径,1=导入成功提示信息
     */
    @Override
    public Result<String[]> importOrder(MultipartFile file, OrderDTO orderDTO, String password) {
        UserShopEntity shopEntity = userShopDAO.findUserShop(orderDTO.getUserId(),orderDTO.getShopId());
        String fileName = orderDTO.getUserId() + CharConstant.MINUS_SIGN + orderDTO.getShopId() + CharConstant.MINUS_SIGN + DateUtils.format(DateUtils.getCurrentDateTime(), DateFormatEnum.YYYYMMDDHHMMSSSSS);
        //上传文件并返回文件对象
        String[] path = fileService.uploadFile(file,ledgerProperties.getImportPath(),fileName);
        //获取文件数据
        ArrayList<ArrayList<String>> importList = FileUtils.readCsvOrExcel(path[1],password);
        if(CollectionUtils.isEmpty(importList)){
            throw new BusinessException("订单文件数据为空");
        }
        //根据店铺平台获取不同平台订单文件数据读取对象
        IOrderImportService orderImportService = OrderImportFactory.getOrderImport(shopEntity.getShopType());
        //获取待保存的订单数据
        List<OrderRowDTO> saveList = new ArrayList<>();
        List<OrderRowDTO> failList = orderImportService.handleOrderData(importList,saveList,shopEntity);
        //保存导入的订单数据
        String sucMsg = this.saveImportOrderData(saveList);
        StringBuilder resultSb = new StringBuilder();
        String[] msgArr = new String[2];
        resultSb.append("导入的订单文件共有").append(CollectionUtils.size(importList)-1).append("笔订单。").append(sucMsg);
        if(CollectionUtils.isNotEmpty(failList)){
            //输出导入失败的数据和导入成功的数据到excel中，便于识别
            String downloadUrl = this.buildDownloadExcel(path[1],password,saveList,failList);
            msgArr[0] = downloadUrl;
            resultSb.append("读取失败的订单共").append(failList.size()).append("笔，具体原因见下载文件的黄色列【导入结果信息】");
            msgArr[1] = resultSb.toString();
            return new Result<>(msgArr);
        }
        //全部导入成功则删除上传的文件
        FileUtils.deleteFile(path[1]);
        msgArr[1] = resultSb.toString();
        return new Result<>(msgArr);
    }
    /**
     * <p>@Description 根据导入失败的数据和导入成功的数据将错误信息输出到excel后返回下载路径 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/25 23:57 </p>
     * @param filePath 导入的文件路径
     * @param password 导入的文件的密码
     * @param saveList 导入成功的数据
     * @param failList 导入失败的数据
     * @return 包含错误信息的excel文件下载路径
     */
    private String buildDownloadExcel(String filePath,String password,List<OrderRowDTO> saveList,List<OrderRowDTO> failList){
        String fileType = FileUtils.getFileType(filePath);
        //组装数据
        Map<Integer,Map<Integer, CellDTO>> dataMap = new HashMap<>();
        saveList.forEach(dto -> {
            Map<Integer, CellDTO> cellMap = new HashMap<>();
            CellDTO cellDTO = new CellDTO();
            cellDTO.setCellIndex(dto.getMaxColumn());
            //message已有数据，则说明保存成功，但还有部分问题，需提示
            if(StringUtils.isNotBlank(dto.getMessage())){
                cellDTO.setCellValue(dto.getMessage()).setFillBackgroundColor(IndexedColors.YELLOW);
            }else {
                cellDTO.setCellValue(dto.getOiId()  == null ? "新增成功" : "更新成功").setFillBackgroundColor(IndexedColors.GREEN);
            }
            cellMap.put(dto.getMaxColumn(),cellDTO);
            dataMap.put(dto.getRowNum(),cellMap);
        });
        failList.forEach(dto -> {
            Map<Integer, CellDTO> cellMap = new HashMap<>();
            CellDTO cellDTO = new CellDTO();
            cellDTO.setCellIndex(dto.getMaxColumn()).setCellValue(dto.getMessage()).setFillBackgroundColor(IndexedColors.RED);
            cellMap.put(dto.getMaxColumn(),cellDTO);
            dataMap.put(dto.getRowNum(),cellMap);
        });
        //设置首行标题列
        Map<Integer, CellDTO> headCellMap = new HashMap<>();
        CellDTO headCellDTO = new CellDTO();
        headCellDTO.setCellIndex(failList.get(0).getMaxColumn()).setCellValue("导入结果信息")
                .setFillBackgroundColor(IndexedColors.YELLOW);
        headCellMap.put(failList.get(0).getMaxColumn(),headCellDTO);
        dataMap.put(0,headCellMap);
        String excelPath = null;
        //csv文件
        if(StringUtils.equals(fileType, FileTypeConstant.CSV)){
            excelPath = CsvUtils.csvToExcel(filePath,dataMap);
            FileUtils.deleteFile(filePath);
        }else {//excel文件
            Map<Integer,Map<Integer,Map<Integer,CellDTO>>> excelMap = new HashMap<>();
            excelMap.put(0,dataMap);
            ExcelUtils.writeUpdateExcel(filePath,password,excelMap);
            excelPath = filePath;
        }
        //将文件的绝对路径转为url访问路径
        excelPath = fileService.convertToURL(excelPath);
        return excelPath;
    }
    /**
     * <p>@Description 保存导入的订单数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/23 23:06 </p>
     * @param saveList 待保存的订单数据
     * @return 保存成功信息
     */
    private String saveImportOrderData(List<OrderRowDTO> saveList){
        if(CollectionUtils.isEmpty(saveList)){
            return CharConstant.EMPTY;
        }
        int insertNum = 0;//新增数量
        int updateNum = 0;//更新数量
        int steExpNum = 0;//订单状态异常数量
        List<String> ordIdList = saveList.stream().map(dto -> dto.getOrderId()).collect(Collectors.toList());
        //查询订单是否存在
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(OrderInfoEntity::getOrderId,ordIdList)
                .eq(OrderInfoEntity::getShopId,saveList.get(0).getShopId());
        List<OrderInfoEntity> entityList = orderInfoDAO.list(wrapper);
        if(CollectionUtils.isEmpty(entityList)){
            entityList = new ArrayList<>();
        }
        Map<String,OrderInfoEntity> entityMap = entityList.stream().collect(Collectors.toMap(k -> k.getOrderId(),entity -> entity));
        for (OrderRowDTO dto : saveList){
            OrderInfoEntity entity = null ;
            if(entityMap.containsKey(dto.getOrderId())){
                entity = entityMap.get(dto.getOrderId());
                updateNum++;
                dto.setOiId(entity.getOiId());//设置主键，用于下载文件中标注
            }else {
                entity = new OrderInfoEntity();
                insertNum++;
            }
            //订单状态为【待确定】则是订单状态异常数据
            steExpNum = StringUtils.equals(dto.getOrderState(),CodeDict.getValue(CodeTypeEnum.OrderState_Unconfirme.getType(), CodeTypeEnum.OrderState_Unconfirme.getKey()))
                    ? steExpNum + 1 : steExpNum;
            entity.setOrderId(dto.getOrderId()).setShopId(dto.getShopId()).setUserId(dto.getUserId())
                    .setOrderDate(dto.getOrderDate()).setSupplyId(dto.getSupplyId())
                    .setGoodsId(dto.getGoodsId()).setGoodsName(dto.getGoodsName())
                    .setOrderState(dto.getOrderState()).setSaleAmount(dto.getSaleAmount())
                    .setPaymentAmount(dto.getPaymentAmount()).setCostAmount(dto.getCostAmount())
                    .setRemark(dto.getRemark());
            //新插入的订单才赋值【其他支出】，更新的则保持原值
            if(entity.getOiId() == null){
                entity.setPayoutAmount(BigDecimal.ZERO);
            }
            //订单数据计算
            this.computeOrderData(entity);
            entityList.add(entity);
        }
        orderInfoDAO.saveOrUpdateBatch(entityList,200);
        StringBuilder sb = new StringBuilder();
        sb.append("成功导入").append(saveList.size()).append("笔订单：新增")
                .append(insertNum).append("笔订单，更新").append(updateNum).append("笔订单。");
        if(steExpNum > 0){
            sb.append("其中有").append(steExpNum).append("笔订单状态需要待确认。");
        }
        return sb.toString();
    }
    /**
     * <p>@Description 保存订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:41 </p>
     * @param orderDTO 订单信息
     * @return Response<java.lang.String>
     */
    @Override
    public Result<String> saveOrderInfo(OrderDTO orderDTO) {
        //保存订单信息前校验
        OrderInfoEntity orderEntity = orderCheckService.checkBeforeSaveOrderInfo(orderDTO);
        if(orderEntity == null){
            throw new BusinessException("保存失败");
        }
        orderEntity.setOrderId(orderDTO.getOrderId())
                .setShopId(orderDTO.getShopId())
                .setUserId(orderDTO.getUserId())
                .setOrderDate(orderDTO.getOrderDate())
                .setSupplyId(orderDTO.getSupplyId())
                .setGoodsId(orderDTO.getGoodsId())
                .setGoodsName(orderDTO.getGoodsName())
                //订单状态为空，则设置为【待确认】
                .setOrderState(StringUtils.isNotBlank(orderDTO.getOrderState()) ? orderDTO.getOrderState()
                        : CodeDict.getValue(CodeTypeEnum.OrderState_Unconfirme.getType(),CodeTypeEnum.OrderState_Unconfirme.getKey()))
                .setSaleAmount(BigDecimalUtils.nullToZero(orderDTO.getSaleAmount()))
                .setPaymentAmount(BigDecimalUtils.nullToZero(orderDTO.getPaymentAmount()))
                .setCostAmount(BigDecimalUtils.nullToZero(orderDTO.getCostAmount()))
                .setPayoutAmount(BigDecimalUtils.nullToZero(orderDTO.getPayoutAmount()))
                .setRemark(orderDTO.getRemark())
                .setUpdateTime(DateUtils.getCurrentDateTime())
                .setCreateTime(orderEntity.getOiId() == null ? DateUtils.getCurrentDateTime() : orderEntity.getCreateTime());
        //订单ID为空则自动生成
        orderEntity.setOrderId(StringUtils.isNotBlank(orderEntity.getOrderId()) ? orderEntity.getOrderId()
                : Long.toString(UidGeneratorUtils.getSnowFlakeID()));
        //订单数据计算
        this.computeOrderData(orderEntity);
        if(orderInfoDAO.saveOrUpdate(orderEntity)){
            return new Result<>("保存成功");
        }
        return new Result<>("保存失败");
    }
    /**
     * <p>@Description 订单数据计算 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/24 23:03 </p>
     * @param orderEntity 订单数据
     * @return
     */
    private void computeOrderData(OrderInfoEntity orderEntity){
        //总成本计算
        orderEntity.setTotalCost(BigDecimalUtils.nullToZero(orderEntity.getCostAmount()).add(BigDecimalUtils.nullToZero(orderEntity.getPayoutAmount())));
        //订单状态=已发货，待签收、交易成功才计算
        if(StringUtils.equalsAny(orderEntity.getOrderState(),
                CodeDict.getValue(CodeTypeEnum.OrderState_Success.getType(), CodeTypeEnum.OrderState_Success.getKey()),
                CodeDict.getValue(CodeTypeEnum.OrderState_Sended.getType(), CodeTypeEnum.OrderState_Sended.getKey()))){
            //毛利润计算
            orderEntity.setGrossProfit((orderEntity.getPaymentAmount()
                    .subtract(orderEntity.getCostAmount()).subtract(orderEntity.getPayoutAmount())).setScale(2, RoundingMode.HALF_UP));
            //毛利率计算
            orderEntity.setGrossProfitRate(orderEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                    : (orderEntity.getGrossProfit().divide(orderEntity.getCostAmount(),4,RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
        }else {
            orderEntity.setGrossProfit(BigDecimal.ZERO).setGrossProfitRate(BigDecimal.ZERO);
        }
        orderEntity.setUpdateTime(DateUtils.getCurrentDateTime());
    }
    /**
     * <p>@Description 删除订单 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/18 09:59 </p>
     * @param userId 用户ID
     * @param oiId   订单ID
     * @return
     */
    @Override
    public Result<String> deleteOrderInfo(String userId, Long oiId) {
        if(orderInfoDAO.deleteOrderInfo(userId,oiId)){
            return new Result<>("删除成功");
        }
        return new Result<>("删除失败");
    }

    /**
     * <p>@Description 查询订单信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/16 22:43 </p>
     * @param orderDTO 订单查询条件
     * @param pageNum  分页数量
     * @param pageSize 页面
     * @return Response<java.util.List < com.www.ledger.data.dto.OrderDTO>>
     */
    @Override
    public Result<List<OrderDTO>> findOrdeList(OrderDTO orderDTO, int pageNum, long pageSize) {
        pageNum = pageNum <= 0 ? 1 : pageNum;
        pageSize = pageSize <= 0 ? 5 : pageSize;
        Page<OrderDTO> page = new Page<>(pageNum,pageSize);
        page = orderInfoDAO.findOrdeList(page,orderDTO);
        List<OrderDTO> shopList =  page.getRecords();
        if(CollectionUtils.isNotEmpty(shopList)){
            shopList.forEach(d -> {
                d.setOrderStateName(CodeDict.getCodeValueName(CodeTypeEnum.OrderState_Success.getType(), d.getOrderState()));
            });
        }
        Result<List<OrderDTO>> result = new Result<>(shopList);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setTotalNum(page.getTotal());
        return result;
    }
}
