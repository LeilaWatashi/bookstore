package com.watashi.bookstore.entity.user;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("Masculino"),
    FEMALE("Feminino"),
    OTHER(" outros");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }
}
