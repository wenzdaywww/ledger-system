package com.www.ledger.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.config.mvc.upload.IFileUpload;
import com.www.common.data.constant.CharConstant;
import com.www.common.data.enums.DateFormatEnum;
import com.www.common.data.response.Result;
import com.www.common.utils.DateUtils;
import com.www.common.utils.FileUtils;
import com.www.common.utils.MoneyUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private IFileUpload fileUpload;
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
     * @return 导入失败的则下载订单数据信息
     */
    @Override
    public Result<String> importOrder(MultipartFile file, OrderDTO orderDTO, String password) {
        UserShopEntity shopEntity = userShopDAO.findUserShop(orderDTO.getUserId(),orderDTO.getShopId());
        String fileName = orderDTO.getUserId() + CharConstant.MINUS_SIGN + orderDTO.getShopId() + CharConstant.MINUS_SIGN + DateUtils.format(DateUtils.getCurrentDateTime(), DateFormatEnum.YYYYMMDDHHMMSSSSS);
        //上传文件并返回文件对象
        String path = fileUpload.uploadFileBackPath(file,ledgerProperties.getImportPath(),fileName);
        //获取文件格式
        ArrayList<ArrayList<String>> importList = FileUtils.readCsvOrExcel(path,password);
        //根据店铺平台获取不同平台订单文件数据读取对象
        IOrderImportService orderImportService = OrderImportFactory.getOrderImport(shopEntity.getShopType());
        //获取待保存的订单数据
        List<OrderRowDTO> saveList = new ArrayList<>();
        List<OrderRowDTO> failList = orderImportService.handleOrderData(importList,saveList,shopEntity);
        //保存导入的订单数据
//        this.saveimportOrderData(saveList);
        if(CollectionUtils.isNotEmpty(failList)){
            //TODO 2023/3/25 01:34 导入失败则下载失败文件数据，待开发
            return new Result<>("导入失败");
        }
        return new Result<>("导入成功");
    }
    /**
     * <p>@Description 保存导入的订单数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/23 23:06 </p>
     * @param saveList 待保存的订单数据
     * @return 失败的订单数据
     */
    private void saveimportOrderData(List<OrderRowDTO> saveList){
        if(CollectionUtils.isEmpty(saveList)){
            return;
        }
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
        entityList = Optional.ofNullable(saveList).map(list -> {
            List<OrderInfoEntity> tempList = new ArrayList<>();
            list.forEach(dto -> {
                OrderInfoEntity entity = entityMap.containsKey(dto.getOrderId()) ? entityMap.get(dto.getOrderId()) : new OrderInfoEntity();
                entity.setOrderId(dto.getOrderId()).setShopId(dto.getShopId()).setUserId(dto.getUserId())
                        .setOrderDate(dto.getOrderDate()).setSupplyId(dto.getSupplyId())
                        .setGoodsId(dto.getGoodsId()).setGoodsName(dto.getGoodsName())
                        .setOrderState(dto.getOrderState()).setSaleAmount(dto.getSaleAmount())
                        .setPayoutAmount(dto.getPaymentAmount()).setCostAmount(dto.getCostAmount())
                        .setRemark(dto.getRemark());
                //订单数据计算
                this.computeOrderData(entity);
            });
            return tempList;
        }).orElse(null);
        orderInfoDAO.saveOrUpdateBatch(entityList,200);
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
                .setSaleAmount(MoneyUtils.nullToZero(orderDTO.getSaleAmount()))
                .setPaymentAmount(MoneyUtils.nullToZero(orderDTO.getPaymentAmount()))
                .setCostAmount(MoneyUtils.nullToZero(orderDTO.getCostAmount()))
                .setPayoutAmount(MoneyUtils.nullToZero(orderDTO.getPayoutAmount()))
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
        orderEntity.setTotalCost(MoneyUtils.nullToZero(orderEntity.getCostAmount()).add(MoneyUtils.nullToZero(orderEntity.getPayoutAmount())));
        //毛利润计算
        orderEntity.setGrossProfit((orderEntity.getPaymentAmount()
                .subtract(orderEntity.getCostAmount()).subtract(orderEntity.getPayoutAmount())).setScale(2, RoundingMode.HALF_UP));
        //毛利率计算
        orderEntity.setGrossProfitRate(orderEntity.getCostAmount().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : (orderEntity.getGrossProfit().divide(orderEntity.getCostAmount(),4,RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"))).setScale(2,RoundingMode.HALF_UP));
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
