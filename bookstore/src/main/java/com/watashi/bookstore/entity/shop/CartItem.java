package com.watashi.bookstore.entity.shop;

import com.watashi.bookstore.entity.Entity;
import com.watashi.bookstore.entity.product.Product;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CartItem extends Entity {

    private Product product;
    private Integer amount;
    private Double subtotal;

    private CartItem(Product product, Integer amount) {
        setProduct(product);
        setAmount(amount);
    }

    public static CartItem from(Product product, Integer amount) {
        return new CartItem(product, amount);
    }

    public void setProduct(Product product) {
        this.product = product;
        calculateSubtotalIfPermitted();
    }

    public void setAmount(Integer amount) {
        this.amount = amount < 1 ? 1 : amount;
        calculateSubtotalIfPermitted();
    }

    private void calculateSubtotalIfPermitted() {
        if (subtotalIsCalculable()) {
            calculateSubtotal();
        }
    }

    private Boolean subtotalIsCalculable() {
        return (product != null && amount != null) && (product.getPrice() != null && amount > 0);
    }

    private void calculateSubtotal() {
        this.subtotal = product.getPrice() * amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(product.getId(), cartItem.getProduct().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product.getId());
    }
}
