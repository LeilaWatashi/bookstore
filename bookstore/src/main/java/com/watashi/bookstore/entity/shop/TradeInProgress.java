package com.watashi.bookstore.entity.shop;

import com.watashi.bookstore.entity.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter

@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS
)
@Component
public class TradeInProgress extends Entity {

    private TradeType type;
    private Sale order;
    private List<ItemInProgress> items;

    public void updateItem(ItemInProgress itemInProgress) {
        Long itemId = itemInProgress.getProduct().getId();
        Integer tradeAmount = itemInProgress.getAmount();
        String reason = itemInProgress.getReason();
        boolean include = itemInProgress.isInclude();

        ItemInProgress existingItem = findItemById(itemId);

        if (existingItem != null) {
            int index = items.indexOf(existingItem);
            existingItem.setAmount(tradeAmount);
            existingItem.setReason(reason);
            existingItem.setInclude(include);

            items.set(index, existingItem);
        }
    }

    public void removeItem(ItemInProgress existingItem) {
        if (existingItem != null) {
            items.remove(existingItem);
        }
    }

    private ItemInProgress findItemById(Long itemId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(itemId))
                .limit(1)
                .collect(Collectors.toList())
                .get(0);
    }

    public Boolean isAlreadyAddedItem(ItemInProgress itemInProgress) {
        return items.stream()
                .anyMatch(existingItem -> existingItem.equals(itemInProgress));
    }

    public Boolean isOk() {
        return items.stream().anyMatch(ItemInProgress::isInclude);
    }
}
