package com.www.ledger.controller.book;

import com.www.common.config.security.meta.JwtAuthorizationTokenFilter;
import com.www.common.data.response.Response;
import com.www.ledger.data.dto.BookDTO;
import com.www.ledger.data.vo.book.BookInfoResponse;
import com.www.ledger.service.book.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                        .setVirAmt(dto.getVirtualAmount()).setShopNum(dto.getShopNum()).setGdsNum(dto.getGoodsNum());
                    return tempResponse;
                }).orElse(null);
        return new Response<>(dtoResponse,bookResponse);
    }
}
