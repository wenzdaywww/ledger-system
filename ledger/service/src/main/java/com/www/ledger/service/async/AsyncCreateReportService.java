package com.www.ledger.service.async;

import com.www.common.config.code.CodeDict;
import com.www.common.config.exception.BusinessException;
import com.www.common.config.mvc.MyMvcProperties;
import com.www.common.config.mvc.upload.IFileService;
import com.www.common.data.constant.CharConstant;
import com.www.common.utils.DateUtils;
import com.www.common.utils.ExcelUtils;
import com.www.common.utils.FileUtils;
import com.www.ledger.data.dao.IDaySalesDAO;
import com.www.ledger.data.dao.IDocInfoDAO;
import com.www.ledger.data.dao.IMonthSalesDAO;
import com.www.ledger.data.dao.IOrderInfoDAO;
import com.www.ledger.data.dao.IPayInfoDAO;
import com.www.ledger.data.dao.IShopSalesDAO;
import com.www.ledger.data.dao.IYearSalesDAO;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.ExportDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.dto.PayDTO;
import com.www.ledger.data.dto.ShopDTO;
import com.www.ledger.data.dto.YearDTO;
import com.www.ledger.data.entity.DocInfoEntity;
import com.www.ledger.data.enums.CodeTypeEnum;
import com.www.ledger.data.enums.ExportEnum;
import com.www.ledger.data.properties.LedgerProperties;
import com.www.ledger.service.book.IBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>@Description 异步创建导出的报表业务处理service </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/4/2 20:46 </p>
 */
@Slf4j
@Service
public class AsyncCreateReportService {
    @Autowired
    private LedgerProperties ledgerProperties;
    @Autowired
    private MyMvcProperties myMvcProperties;
    @Autowired
    private IBookService bookService;
    @Autowired
    private IShopSalesDAO shopSalesDAO;
    @Autowired
    private IYearSalesDAO yearSalesDAO;
    @Autowired
    private IMonthSalesDAO monthSalesDAO;
    @Autowired
    private IDaySalesDAO daySalesDAO;
    @Autowired
    private IOrderInfoDAO orderInfoDAO;
    @Autowired
    private IPayInfoDAO payInfoDAO;
    @Autowired
    private IFileService fileService;
    @Autowired
    private IDocInfoDAO docInfoDAO;

    /**
     * <p>@Description 异步创建报表文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 19:58 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @param docId 文档信息ID
     */
    @Async
    public void createReport(String userId, List<Integer> sheetList,Long docId){
        DocInfoEntity docEntity = docInfoDAO.getById(docId);
        if(docEntity == null){
            throw new BusinessException("文档记录不存在，创建报表失败");
        }
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:"+ledgerProperties.getExportTemplate());
        } catch (Exception e) {
            log.error("读取模板文件失败，异常：",e);
            //生成失败
            docEntity.setDocState(CodeDict.getValue(CodeTypeEnum.DocState_Failed.getType(), CodeTypeEnum.DocState_Failed.getKey()));
            docInfoDAO.updateById(docEntity);
            return;
        }
        //组装待导出的报表数据
        ExportDTO exportDTO = this.buildExportDTO(userId,sheetList);
        //导出的报表文件名称,格式：导出保存文件夹路径/用户ID-export-导出年月日时分秒
        StringBuilder exportNameSB = new StringBuilder();
        exportNameSB.append(myMvcProperties.getSavePath()).append(ledgerProperties.getExportPath())
                .append(CharConstant.LEFT_SLASH).append(docEntity.getDocName());
        //根据模板文件配置的字段映射关系插入数据
        String exportPath = ExcelUtils.writeTemplateExcel(file.getAbsolutePath(),exportNameSB.toString(),exportDTO);
        if(StringUtils.isBlank(exportPath)){
            log.error("报表模板写入数据失败");
            //生成失败
            docEntity.setDocState(CodeDict.getValue(CodeTypeEnum.DocState_Failed.getType(), CodeTypeEnum.DocState_Failed.getKey()));
            docInfoDAO.updateById(docEntity);
            return;
        }
        //删除未选择导入的sheet页
        List<String> deleteSheetList = new ArrayList<>();
        ExportEnum.getAllEnum().forEach(enums -> {
            if(!sheetList.contains(enums.getNum())){
                deleteSheetList.add(enums.getName());
            }
        });
        String[] sheetArr = deleteSheetList.toArray(new String[deleteSheetList.size()]);
        ExcelUtils.deleteSheet(exportPath, sheetArr);
        //更新文档记录信息
        docEntity.setExtension(FileUtils.getFileType(exportPath))
                .setDocState(CodeDict.getValue(CodeTypeEnum.DocState_Complete.getType(), CodeTypeEnum.DocState_Complete.getKey()));
        String url = fileService.convertToURL(exportPath);
        docEntity.setDocUrl(url).setUpdateTime(DateUtils.getCurrentDateTime());
        docInfoDAO.updateById(docEntity);
    }
    /**
     * <p>@Description 组装待导出的报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:13 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 报表数据对象
     */
    private ExportDTO buildExportDTO(String userId, List<Integer> sheetList){
        ExportDTO exportDTO = new ExportDTO();
        //查询我的账簿报表数据
        exportDTO.setBook(this.findBookData(userId,sheetList));
        //查询我的店铺报表数据
        exportDTO.setShop(this.findShopData(userId,sheetList));
        //查询店铺的年销售额报表数据
        exportDTO.setShopYear(this.findShopYearData(userId,sheetList));
        //查询店铺汇总的年销售报表数据
        exportDTO.setYear(this.findYearData(userId,sheetList));
        //查询店铺的月销售额报表数据
        exportDTO.setShopMonth(this.findShopMonthData(userId,sheetList));
        //查询店铺汇总的月销售额报表数据
        exportDTO.setMonth(this.findMonthData(userId,sheetList));
        //查询店铺的日销售额报表数据
        exportDTO.setShopDay(this.findShopDayData(userId,sheetList));
        //查询店铺汇总的日销售额报表数据
        exportDTO.setDay(this.findDayData(userId,sheetList));
        //查询订单信息报表数据
        exportDTO.setOrder(this.findOrderData(userId,sheetList));
        //查询支出信息报表数据
        exportDTO.setPay(this.findPayData(userId,sheetList));
        return exportDTO;
    }
    /**
     * <p>@Description 查询我的账簿报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 我的账簿报表数据
     */
    private BookDTO findBookData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.BOOK.getNum())){
            return null;
        }
        return bookService.findBookData(userId,false);
    }
    /**
     * <p>@Description 查询我的店铺报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 我的店铺报表数据
     */
    private List<ShopDTO> findShopData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.SHOP.getNum())){
            return null;
        }
        return shopSalesDAO.exportShopSalesData(userId);
    }
    /**
     * <p>@Description 查询店铺的年销售额报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 店铺的年销售额报表数据
     */
    private List<YearDTO> findShopYearData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.SHOP_YEAR.getNum())){
            return null;
        }
        return yearSalesDAO.exportYearSalesData(userId,true);
    }
    /**
     * <p>@Description 查询店铺汇总的年销售报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 店铺汇总的年销售额报表数据
     */
    private List<YearDTO> findYearData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.YEAR.getNum())){
            return null;
        }
        return yearSalesDAO.exportYearSalesData(userId,false);
    }
    /**
     * <p>@Description 查询店铺的月销售额报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 店铺的月销售额报表数据
     */
    private List<MonthDTO> findShopMonthData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.SHOP_MONTH.getNum())){
            return null;
        }
        return monthSalesDAO.exportMonthSalesData(userId,true);
    }
    /**
     * <p>@Description 查询店铺汇总的月销售额报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 店铺汇总的月销售额报表数据
     */
    private List<MonthDTO> findMonthData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.MONTH.getNum())){
            return null;
        }
        return monthSalesDAO.exportMonthSalesData(userId,false);
    }
    /**
     * <p>@Description 查询店铺的日销售额报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 店铺的日销售额报表数据
     */
    private List<DayDTO> findShopDayData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.SHOP_DAY.getNum())){
            return null;
        }
        return daySalesDAO.exportDaySaleData(userId,true);
    }
    /**
     * <p>@Description 查询店铺汇总的日销售额报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 店铺汇总的日销售额报表数据
     */
    private List<DayDTO> findDayData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.DAY.getNum())){
            return null;
        }
        return daySalesDAO.exportDaySaleData(userId,false);
    }
    /**
     * <p>@Description 查询订单信息报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 订单信息报表数据
     */
    private List<OrderDTO> findOrderData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.ORDER.getNum())){
            return null;
        }
        return orderInfoDAO.exportOrderData(userId);
    }
    /**
     * <p>@Description 查询支出信息报表数据 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 21:14 </p>
     * @param userId 用户ID
     * @param sheetList 要导出的数据选项
     * @return 支出信息报表数据
     */
    private List<PayDTO> findPayData(String userId, List<Integer> sheetList){
        if(CollectionUtils.isEmpty(sheetList) || !sheetList.contains(ExportEnum.PAY.getNum())){
            return null;
        }
        return payInfoDAO.exportPayInfoData(userId);
    }
}
