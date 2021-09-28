package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.shop.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TradeHelper {

    private final ObjectProvider<Trade> tradeProvider;
    private final TradeItemHelper tradeItemHelper;

    @Autowired
    public TradeHelper(ObjectProvider<Trade> tradeProvider, TradeItemHelper tradeItemHelper) {
        this.tradeProvider = tradeProvider;
        this.tradeItemHelper = tradeItemHelper;
    }

    public Trade adapt(TradeInProgress tradeInProgress) {
        Trade newTrade = tradeProvider.getObject();

        Sale order = tradeInProgress.getOrder();
        TradeType tradeType = tradeInProgress.getType();
        List<ItemInProgress> tradeItemsInProgress = tradeInProgress.getItems()
                .stream()
                .filter(ItemInProgress::isInclude)
                .collect(Collectors.toList());

        List<TradeItem> tradeItems = tradeItemsInProgress.stream()
                .map(itemInProgress -> tradeItemHelper.adapt(itemInProgress, newTrade))
                .collect(Collectors.toList());

        newTrade.setOrder(order);
        newTrade.setItems(tradeItems);
        newTrade.setType(tradeType);
        newTrade.setItems(tradeItems);

        return newTrade;
    }
}
