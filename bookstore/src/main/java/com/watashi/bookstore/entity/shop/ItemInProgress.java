package com.watashi.bookstore.entity.shop;

import com.watashi.bookstore.entity.Entity;
import com.watashi.bookstore.entity.product.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ItemInProgress extends Entity {

    private Product product;
    private Integer orderAmount;
    private Integer amount;
    private String reason;
    private boolean include;

    private ItemInProgress(Product product, Integer orderAmount) {
        this.product = product;
        this.orderAmount = orderAmount;
        this.amount = 1;
        this.include = false;
    }

    private ItemInProgress(Product product, Integer amount, String reason, Boolean include) {
        this.product = product;
        this.amount = include ? amount : 1;
        this.reason = include ? reason : "";
        this.include = include;
    }

    public static ItemInProgress build(Product product, Integer orderAmount) {
        return new ItemInProgress(product, orderAmount);
    }

    public static ItemInProgress build(Product product, Integer amount, String reason, Boolean include) {
        return new ItemInProgress(product, amount, reason, include);
    }

    public boolean hasReason() {
        return reason != null && !reason.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemInProgress that = (ItemInProgress) o;
        return product.equals(that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}
