package com.www.ledger.controller.book;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.DocDTO;
import com.www.ledger.data.vo.book.BookDocInVO;
import com.www.ledger.data.vo.book.BookDocOutVO;
import com.www.ledger.data.vo.book.BookExpInVO;
import com.www.ledger.data.vo.book.BookInfoOutVO;
import com.www.ledger.service.book.IBookService;
import com.www.ledger.service.export.IExportService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>@Description 我的账本controller </p>
 * <p>@Version 1.0 </p>
 * <p>@Author www </p>
 * <p>@Date 2023/3/11 19:20 </p>
 */
@RestController
@RequestMapping("book")
public class BookController {
    @Autowired
    private IBookService bookService;
    @Autowired
    private IExportService exportService;

    /**
     * <p>@Description 生成报表文件 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/2 19:46 </p>
     * @param inVO 要导出的数据选项
     * @return 生成结果
     */
    @PostMapping("exp")
    public Result<String> createReport(@Validated BookExpInVO inVO){
        return exportService.createDocumentData(JwtAuthorizationTokenFilter.getUserId(),inVO.getSheets());
    }
    /**
     * <p>@Description 统计用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:24 </p>
     * @return 统计结果
     */
    @PostMapping("tal")
    public Result<String> updateBookData(){
        return bookService.saveAndCountBookData(JwtAuthorizationTokenFilter.getUserId());
    }
    /**
     * <p>@Description 查询用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:24 </p>
     * @return
     */
    @GetMapping("info")
    public Result<BookInfoOutVO> findBookData(){
        BookDTO bookDTO = bookService.findBookData(JwtAuthorizationTokenFilter.getUserId(),true);
        BookInfoOutVO bookInfoOutVO = Optional.ofNullable(bookDTO)
                .map(dto -> {
                    BookInfoOutVO tempOutVO = new BookInfoOutVO();
                    tempOutVO.setRetPro(dto.getRetainedProfits()).setRetProRat(dto.getRetainedProfitsRate())
                        .setGroPro(dto.getGrossProfit()).setGroProRat(dto.getGrossProfitRate())
                        .setTalOrd(dto.getTotalOrder()).setSucOrd(dto.getSucceedOrder())
                        .setSucOrdRat(dto.getSucceedOrderRate()).setFaiOrd(dto.getFailedOrder())
                        .setSalAmt(dto.getSaleAmount()).setCosAmt(dto.getCostAmount())
                        .setAdvAmt(dto.getAdvertAmount()).setSerAmt(dto.getServiceAmount())
                        .setVirAmt(dto.getVirtualAmount()).setTalCos(dto.getTotalCost())
                        .setGuatee(dto.getGuarantee());
                    return tempOutVO;
                }).orElse(null);
        return new Result<>(bookInfoOutVO);
    }
    /**
     * <p>@Description 查询报表文档信息列表 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/4/3 21:00 </p>
     * @param inVO 查询条件
     * @return 报表文档信息列表
     */
    @GetMapping("doc")
    public Result<List<BookDocOutVO>> findReportDoc(@Validated BookDocInVO inVO){
        Result<List<DocDTO>> dtoResult = exportService.findReportDoc(JwtAuthorizationTokenFilter.getUserId(),inVO.getPageNum(),inVO.getPageSize());
        List<BookDocOutVO> outVOList = Optional.ofNullable(dtoResult.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResult.getData()))
                .map(list -> {
                    List<BookDocOutVO> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        BookDocOutVO outVO = new BookDocOutVO();
                        outVO.setName(dto.getDocName()).setDate(dto.getCreateTimeStr())
                                .setSheet(dto.getRemark()).setState(dto.getDocStateStr())
                                .setUrl(dto.getDocUrl());
                        tempList.add(outVO);
                    });
                    return tempList;
                }).orElse(null);
        return new Result<>(dtoResult,outVOList);
    }
}
