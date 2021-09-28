package com.watashi.bookstore.entity.user;

import lombok.Getter;

@Getter
public enum UserType {
    CUSTOMER("Cliente"),
    ADMIN("Administrador");

    private final String displayName;

    UserType(String displayName) {
        this.displayName = displayName;
    }
}
