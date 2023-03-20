package com.www.ledger.controller.book;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.dto.MonthDTO;
import com.www.ledger.data.dto.OrderDTO;
import com.www.ledger.data.vo.book.BookDayResponse;
import com.www.ledger.data.vo.book.BookInfoResponse;
import com.www.ledger.data.vo.book.BookYearResponse;
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
     * <p>@Description 查询近些天销售额排名靠前店铺销售额趋势图 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:45 </p>
     * @return
     */
    @GetMapping("day")
    public Response<List<BookDayResponse>> findLastDaySales(){
        Response<List<List<OrderDTO>>> dtoResponse = bookService.findLastDaySales(JwtAuthorizationTokenFilter.getUserId());
        List<BookDayResponse> lastList = Optional.ofNullable(dtoResponse.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResponse.getData()))
                .map(list0 -> {
                    //第1层List,即前3店铺
                    List<BookDayResponse> tempList = new ArrayList<>();
                    list0.forEach(list1 -> {
                        BookDayResponse dayResponse = new BookDayResponse();
                        dayResponse.setShopNm(list1.get(0).getShopName());
                        //第2层List,即店铺的订单
                        List<BookDayResponse.DaySalesResponse> dayList = Optional.ofNullable(list1).filter(e1 -> CollectionUtils.isNotEmpty(list1))
                                .map(list3 -> {
                                    List<BookDayResponse.DaySalesResponse> dayTempList = new ArrayList<>();
                                    list3.forEach(dto -> {
                                        BookDayResponse.DaySalesResponse day = new BookDayResponse.DaySalesResponse();
                                        day.setDay(dto.getOrderDateStr()).setSales(dto.getSaleAmount());
                                        dayTempList.add(day);
                                    });
                                    return dayTempList;
                                }).orElse(null);
                        dayResponse.setDay(dayList);
                        tempList.add(dayResponse);
                    });
                    return tempList;
                }).orElse(null);
        return new Response<>(dtoResponse,lastList);
    }
    /**
     * <p>@Description 查询用户近一年的销售额 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 16:45 </p>
     * @return
     */
    @GetMapping("year")
    public Response<List<BookYearResponse>> findLastYearSales(){
        Response<List<MonthDTO>> dtoResponse = bookService.findLastYearSales(JwtAuthorizationTokenFilter.getUserId());
        List<BookYearResponse> lastList = Optional.ofNullable(dtoResponse.getData()).filter(e -> CollectionUtils.isNotEmpty(dtoResponse.getData()))
                .map(list -> {
                    List<BookYearResponse> tempList = new ArrayList<>();
                    list.forEach(dto -> {
                        BookYearResponse last = new BookYearResponse();
                        last.setMonth(dto.getMonthDateStr()).setSales(dto.getSaleAmount());
                        tempList.add(last);
                    });
                    return tempList;
                }).orElse(null);
        return new Response<>(dtoResponse,lastList);
    }
    /**
     * <p>@Description 统计用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:24 </p>
     * @return
     */
    @PostMapping("tal")
    public Response<String> updateBookData(){
        return bookService.saveAndCountBookData(JwtAuthorizationTokenFilter.getUserId());
    }
    /**
     * <p>@Description 查询用户账簿信息 </p>
     * <p>@Author www </p>
     * <p>@Date 2023/3/19 00:24 </p>
     * @return
     */
    @GetMapping("info")
    public Response<BookInfoResponse> findBookData(){
        Response<BookDTO> dtoResponse = bookService.findBookData(JwtAuthorizationTokenFilter.getUserId());
        BookInfoResponse bookResponse = Optional.ofNullable(dtoResponse.getData())
                .map(dto -> {
                    BookInfoResponse tempResponse = new BookInfoResponse();
                    tempResponse.setRetPro(dto.getRetainedProfits()).setRetProRat(dto.getRetainedProfitsRate())
                        .setGroPro(dto.getGrossProfit()).setGroProRat(dto.getGrossProfitRate())
                        .setTalOrd(dto.getTotalOrder()).setSucOrd(dto.getSucceedOrder())
                        .setSucOrdRat(dto.getSucceedOrderRate()).setFaiOrd(dto.getFailedOrder())
                        .setSalAmt(dto.getSaleAmount()).setCosAmt(dto.getCostAmount())
                        .setAdvAmt(dto.getAdvertAmount()).setSerAmt(dto.getServiceAmount())
                        .setVirAmt(dto.getVirtualAmount()).setShopNum(dto.getShopNum())
                        .setGdsNum(dto.getGoodsNum()).setTalCos(dto.getTotalCost());
                    return tempResponse;
                }).orElse(null);
        return new Response<>(dtoResponse,bookResponse);
    }
}
