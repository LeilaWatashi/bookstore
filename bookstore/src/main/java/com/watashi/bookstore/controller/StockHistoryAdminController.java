package com.watashi.bookstore.controller;

import com.watashi.bookstore.entity.stock.StockHistory;
import com.watashi.bookstore.enums.ModelAttributeType;
import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.helper.ModelAndViewHelper;
import com.watashi.bookstore.service.IAlternativeDomainService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = StockHistoryAdminController.BASE_STOCK_HISTORY_ADMIN_URL)
public class StockHistoryAdminController {

    public static final String BASE_STOCK_HISTORY_ADMIN_URL = "/admin/stock/history";

    private final IAlternativeDomainService<StockHistory> stockHistoryAlternativeDomainService;

    private final StockHistory stockHistoryMock;

    public StockHistoryAdminController(@Qualifier("alternativeDomainService")
                                       IAlternativeDomainService<StockHistory> stockHistoryAlternativeDomainService,
                                       StockHistory stockHistoryMock) {
        this.stockHistoryAlternativeDomainService = stockHistoryAlternativeDomainService;
        this.stockHistoryMock = stockHistoryMock;
    }

    @GetMapping
    public ModelAndView show() {
        List<StockHistory> foundStockOperations = stockHistoryAlternativeDomainService
                .findAll(stockHistoryMock);

        return ModelAndViewHelper.configure(
                ViewType.STOCK_HISTORY_ADMIN,
                View.LIST,
                foundStockOperations,
                ModelAttributeType.STOCK_HISTORY);
    }

}
