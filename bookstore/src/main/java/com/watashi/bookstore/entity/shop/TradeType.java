package com.watashi.bookstore.entity.shop;

import lombok.Getter;

@Getter
public enum TradeType {
    EXCHANGE("Troca"),
    DEVOLUTION("Devolução");

    private final String displayName;

    TradeType(String displayName) {
        this.displayName = displayName;
    }
}
