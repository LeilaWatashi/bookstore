package com.watashi.bookstore.entity.shop;

import lombok.Getter;
import lombok.ToString;

@ToString

@Getter
public enum SalesStatusType {
    PROCESSING("Em Processamento"),
    IN_TRANSIT("Em Trânsito"),
    DELIVERED("Entregue");

    private String displayName;

    SalesStatusType(String displayName) {
        this.displayName = displayName;
    }

}
