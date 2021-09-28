package com.watashi.bookstore.entity.shop;

import lombok.Getter;

@Getter
public enum TradeStatus {
    AWAITING_AUTHORIZATION("Aguardando autorização"),
    DENIED("Negada"),
    AUTHORIZED("Autorizada"),
    RECEIVED_ITEMS("Itens recebidos"),
    GENERATED_VOUCHER("Cupom gerado"),
    REPLACEMENT_ON_DELIVERY("Subistituição na entrega"),
    REPLACEMENT_DELIVERED("Substituição entregue");

    private final String displayName;

    TradeStatus(String displayName) {
        this.displayName = displayName;
    }
}

