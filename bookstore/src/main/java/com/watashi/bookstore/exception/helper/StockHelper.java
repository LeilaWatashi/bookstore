package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.dto.StockManageDTO;
import com.watashi.bookstore.entity.shop.TradeItem;
import com.watashi.bookstore.entity.stock.Stock;
import com.watashi.bookstore.entity.stock.StockHistory;
import com.watashi.bookstore.exception.InternalServerErrorException;
import com.watashi.bookstore.service.IAlternativeDomainService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class StockHelper {

    private final IAlternativeDomainService<Stock> stockAlternativeDomainService;
    private final IAlternativeDomainService<StockHistory> stockHistoryAlternativeDomainService;

    private ObjectProvider<Stock> stockObjectProvider;

    private final StockHistoryHelper stockHistoryHelper;

    public StockHelper(@Qualifier("alternativeDomainService")
                       IAlternativeDomainService<Stock> stockAlternativeDomainService,
                       @Qualifier("alternativeDomainService")
                       IAlternativeDomainService<StockHistory> stockHistoryAlternativeDomainService,
                       ObjectProvider<Stock> stockObjectProvider,
                       StockHistoryHelper stockHistoryHelper) {
        this.stockAlternativeDomainService = stockAlternativeDomainService;
        this.stockHistoryAlternativeDomainService = stockHistoryAlternativeDomainService;
        this.stockObjectProvider = stockObjectProvider;
        this.stockHistoryHelper = stockHistoryHelper;
    }

    public StockHistory up(StockManageDTO stockManageDTO) {
        Stock stock = prepare(stockManageDTO);
        return executeAndRegisterOperation(stock);
    }

    public StockHistory up(Long stockId, Integer operationAmount) {
        Stock stock = prepare(stockId, operationAmount);
        return executeAndRegisterOperation(stock);
    }

    public List<StockHistory> up(List<TradeItem> tradeItems) {
        List<StockHistory> stockHistoriesOperation = new ArrayList<>();

        tradeItems.forEach(tradeItem -> {
            Stock stock = prepare(tradeItem);
            StockHistory stockHistory = executeAndRegisterOperation(stock);
            stockHistoriesOperation.add(stockHistory);
        });

        return  stockHistoriesOperation;
    }

    public StockHistory down(StockManageDTO stockManageDTO) {
        Stock stock = prepare(stockManageDTO);
        return executeAndRegisterOperation(stock);
    }

    public StockHistory down(Long stockId, Integer operationAmount) {
        Stock stock = prepare(stockId, operationAmount);
        return executeAndRegisterOperation(stock);
    }

    public void down(Map<Long, Integer> stockIdAndOperationAmounts) {
        stockIdAndOperationAmounts.forEach((stockId, operationAmount) -> {
            Stock stock = prepare(stockId, (operationAmount * (-1)));
            executeAndRegisterOperation(stock);
        });
    }

    private Stock prepare(StockManageDTO stockManageDTO) {
        Stock stock = stockObjectProvider.getObject();

        Optional<Stock> stockOptional = stockAlternativeDomainService.findById(stockManageDTO.getId(), stock);
        Stock foundStock = stockOptional.orElseThrow(InternalServerErrorException::new);

        Stock adaptedStock = adapt(stockManageDTO);

        adaptedStock.setProduct(foundStock.getProduct());
        adaptedStock.recalculateAmount();

        return adaptedStock;
    }

    private Stock prepare(Long stockId, Integer operationAmount) {
        Stock stock = stockObjectProvider.getObject();

        Optional<Stock> stockOptional = stockAlternativeDomainService.findById(stockId, stock);
        Stock foundStock = stockOptional.orElseThrow(InternalServerErrorException::new);

        Integer existingAmount = foundStock.getAmount();
        foundStock.setLastOperationAmount(existingAmount);
        foundStock.setAmount(operationAmount);

        foundStock.recalculateAmount();

        return foundStock;
    }

    private Stock prepare(TradeItem tradeItem) {
        Stock stock = stockObjectProvider.getObject();

        Long stockId = tradeItem.getProduct().getStock().getId();
        Integer returningAmount = tradeItem.getQuantity();

        Optional<Stock> stockOptional = stockAlternativeDomainService.findById(stockId, stock);
        Stock foundStock = stockOptional.orElseThrow(InternalServerErrorException::new);

        Integer existingAmount = foundStock.getAmount();
        foundStock.setLastOperationAmount(existingAmount);
        foundStock.setAmount(returningAmount);

        foundStock.recalculateAmount();

        return foundStock;
    }

    private Stock adapt(StockManageDTO stockManageDTO) {
        return ModelMapperHelper
                .configureTypeMapWithDTOSource(StockManageDTO.class, Stock.class)
                .addMappings(mapper -> mapper.map(StockManageDTO::getAmount, Stock::setLastOperationAmount))
                .addMappings(mapper -> mapper.map(StockManageDTO::getOperationAmount, Stock::setAmount))
                .map(stockManageDTO);
    }

    private StockHistory executeAndRegisterOperation(Stock stock) {
        Integer lastOperationAmount = stock.getLastOperationAmount();

        Stock updatedStock = stockAlternativeDomainService.save(stock);
        updatedStock.setLastOperationAmount(lastOperationAmount);

        return stockHistoryHelper.registerStockOperationOnHistory(updatedStock);
    }

}
