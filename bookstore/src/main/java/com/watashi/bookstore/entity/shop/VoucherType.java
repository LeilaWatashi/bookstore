package com.watashi.bookstore.entity.shop;

import lombok.Getter;

@Getter
public enum VoucherType {
    DISCOUNT("Desconto"),
    TRADE("Troca");

    private final String displayName;

    VoucherType(String displayName) {
        this.displayName = displayName;
    }
}
