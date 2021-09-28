package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.stock.Stock;
import com.watashi.bookstore.entity.stock.StockHistory;
import com.watashi.bookstore.service.IAlternativeDomainService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class StockHistoryHelper {

    private final IAlternativeDomainService<StockHistory> alternativeDomainService;
    private ObjectProvider<StockHistory> stockHistoryObjectProvider;

    public StockHistoryHelper(@Qualifier("alternativeDomainService")
                              IAlternativeDomainService<StockHistory> alternativeDomainService,
                              ObjectProvider<StockHistory> stockHistoryObjectProvider) {
        this.alternativeDomainService = alternativeDomainService;
        this.stockHistoryObjectProvider = stockHistoryObjectProvider;
    }

    public StockHistory registerStockOperationOnHistory(Stock stock) {
        StockHistory stockHistory = stockHistoryObjectProvider.getObject();
        stockHistory.fillFieldsWith(stock);

        return alternativeDomainService.save(stockHistory);
    }
}
