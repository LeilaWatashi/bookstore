package com.watashi.bookstore.entity.shop;


import com.watashi.bookstore.entity.Entity;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter

@Scope(
        scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class ShopCart extends Entity {


    private List<CartItem> items;
    private Integer itemsAmount;
    private Double total;

    public ShopCart() {
        items = new ArrayList<>();
        updateItemsAmountWithCurrentListSize();
    }

    public void addItem(CartItem cartItem) {
        boolean itemNotAdded = !existsItem(cartItem);

        if (itemNotAdded) {
            this.items.add(cartItem);
            updateProperties();
        }
    }

    public void removeItem(Long productId) {
        CartItem existingCartItem = findItem(productId);
        if (existingCartItem != null) {
            items.remove(existingCartItem);
            updateProperties();
        }
    }

    private Boolean existsItem(CartItem cartItem) {
        return this.items
                .stream()
                .anyMatch(existingCartItem -> areItemsTheSame(existingCartItem, cartItem));
    }

    private Boolean areItemsTheSame(CartItem one, CartItem two) {
        return one.getProduct().getId().equals(two.getProduct().getId());
    }

    public void updateItem(Long productId, Integer updatedAmount) {
        CartItem existingCartItem = findItem(productId);

        if (existingCartItem != null) {
            int index = items.indexOf(existingCartItem);
            existingCartItem.setAmount(updatedAmount);
            items.set(index, existingCartItem);
            calculateTotal();
        }
    }

    private CartItem findItem(Long productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .limit(1)
                .collect(Collectors.toList())
                .get(0);
    }

    public void clear() {
        this.items.clear();
        this.total = 0.0d;
    }

    private void updateItemsAmountWithCurrentListSize() {
        itemsAmount = items.size();
    }

    private void calculateTotal() {
        Double total = 0.0d;
        for (CartItem cartItem : items) {
            total += cartItem.getSubtotal();
        }
        this.total = total;
    }

    private void updateProperties() {
        updateItemsAmountWithCurrentListSize();
        calculateTotal();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
