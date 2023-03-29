package com.www.ledger.controller.book;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Result;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.DayDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.vo.book.BookDayOutVO;
import com.www.ledger.data.vo.book.BookInfoOutVO;
import com.www.ledger.data.vo.book.BookYearOutVO;
import com.www.ledger.service.book.IBookService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * <p>@Description 查询日期区间的店铺汇总日销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:45 </p>
     * @return 店铺汇总日销售额
     */
    @GetMapping("day")
    public Result<List<BookDayOutVO>> findLastDaySales(){
        Result<List<DayDTO>> listResult = bookService.findLastDaySales(JwtAuthorizationTokenFilter.getUserId());
        List<BookDayOutVO> lastList = Optional.ofNullable(listResult.getData()).filter(e -> CollectionUtils.isNotEmpty(listResult.getData()))
                .map(list -> {
                    //第1层List,即前3店铺
                    List<BookDayOutVO> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        BookDayOutVO outVO = new BookDayOutVO();
                        outVO.setDay(dto.getDayDateStr()).setSales(dto.getSaleAmount());
                        tempList.add(outVO);
                    });
                    return tempList;
                }).orElse(null);
        return new Result<>(listResult,lastList);
    }
    /**
     * <p>@Description 查询用户最近一年所有店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:45 </p>
     * @return
     */
    @GetMapping("year")
    public Result<List<BookYearOutVO>> findLastYearSales(){
        Result<List<MonthDTO>> listResult = bookService.findLastYearSales(JwtAuthorizationTokenFilter.getUserId());
        List<BookYearOutVO> lastList = Optional.ofNullable(listResult.getData()).filter(e -> CollectionUtils.isNotEmpty(listResult.getData()))
                .map(list -> {
                    List<BookYearOutVO> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        BookYearOutVO last = new BookYearOutVO();
                        last.setMonth(dto.getMonthDateStr()).setSales(dto.getSaleAmount());
                        tempList.add(last);
                    });
                    return tempList;
                }).orElse(null);
        return new Result<>(listResult,lastList);
    }
    /**
     * <p>@Description 统计用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:24 </p>
     * @return
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
        Result<BookDTO> dtoResult = bookService.findBookData(JwtAuthorizationTokenFilter.getUserId());
        BookInfoOutVO bookInfoOutVO = Optional.ofNullable(dtoResult.getData())
                .map(dto -> {
                    BookInfoOutVO tempOutVO = new BookInfoOutVO();
                    tempOutVO.setRetPro(dto.getRetainedProfits()).setRetProRat(dto.getRetainedProfitsRate())
                        .setGroPro(dto.getGrossProfit()).setGroProRat(dto.getGrossProfitRate())
                        .setTalOrd(dto.getTotalOrder()).setSucOrd(dto.getSucceedOrder())
                        .setSucOrdRat(dto.getSucceedOrderRate()).setFaiOrd(dto.getFailedOrder())
                        .setSalAmt(dto.getSaleAmount()).setCosAmt(dto.getCostAmount())
                        .setAdvAmt(dto.getAdvertAmount()).setSerAmt(dto.getServiceAmount())
                        .setVirAmt(dto.getVirtualAmount()).setShopNum(dto.getShopNum())
                        .setGdsNum(dto.getGoodsNum()).setTalCos(dto.getTotalCost());
                    return tempOutVO;
                }).orElse(null);
        return new Result<>(dtoResult,bookInfoOutVO);
    }
}
