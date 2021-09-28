package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.product.Product;
import com.watashi.bookstore.entity.shop.ItemInProgress;
import com.watashi.bookstore.entity.shop.Trade;
import com.watashi.bookstore.entity.shop.TradeItem;
import com.watashi.bookstore.entity.shop.TradeItemId;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class TradeItemHelper {

    private final IDomainService<Product> productDomainService;
    private final Product mockProduct;

    private ObjectProvider<TradeItem> tradeItemProvider;
    private ObjectProvider<TradeItemId> tradeItemIdProvider;

    public TradeItemHelper(@Qualifier("domainService")
                           IDomainService<Product> productDomainService,
                           Product mockProduct,
                           ObjectProvider<TradeItem> tradeItemProvider,
                           ObjectProvider<TradeItemId> tradeItemIdProvider) {
        this.productDomainService = productDomainService;
        this.mockProduct = mockProduct;
        this.tradeItemProvider = tradeItemProvider;
        this.tradeItemIdProvider = tradeItemIdProvider;
    }

    public TradeItem adapt(ItemInProgress itemInProgress, Trade trade) {
        Long productId = itemInProgress.getProduct().getId();
        Optional<Product> productOptional = productDomainService.findById(productId, mockProduct);
        Product product = productOptional.orElseThrow(NotFoundException::new);
        Integer amount = itemInProgress.getAmount();
        String reason = itemInProgress.getReason();

        TradeItem tradeItem = tradeItemProvider.getObject();
        TradeItemId tradeItemId = tradeItemIdProvider.getObject();
        tradeItemId.setItemId(productId);
        tradeItemId.setRegisterDate(LocalDateTime.now());

        tradeItem.setId(tradeItemId);
        tradeItem.setTrade(trade);
        tradeItem.setProduct(product);
        tradeItem.setQuantity(amount);
        tradeItem.setReason(reason);

        return tradeItem;
    }
}
