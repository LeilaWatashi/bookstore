package com.watashi.bookstore.entity.user;

import lombok.Getter;

@Getter
public enum CardType {

    VISA("Visa"),
    MASTERCARD("MasterCard");


    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }
}
