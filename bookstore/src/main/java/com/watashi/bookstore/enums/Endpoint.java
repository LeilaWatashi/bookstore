package com.watashi.bookstore.enums;

import lombok.Getter;

@Getter
public enum Endpoint {
    NEW("/new"),
    EDIT("/edit"),
    MANAGE("/manage"),
    PATH_VARIABLE("/{pathVariable}"),
    IN_PROGRESS("/in-progress"),
    REQUEST("/request"),
    FINISH("/finish"),

    ADMIN("/admin"),
    AUTH("/auth"),
    LOGIN("/login"),
    SIGN_UP("/sign-up"),

    CUSTOMER("/customer"),

    PRODUCTS("/products"),
    STOCKS("/stocks"),
    STOCK_HISTORIES("/history"),
    SALES("/sales"),

    CUSTOMERS("/customers"),
    ACCOUNT("/account/"),
    ADRESSES("/adresses"),
    CREDIT_CARDS("/credit-cards"),
    ORDERS("/orders"),
    ORDER("/order"),
    TRADES("/trades"),
    TRADE("/trade"),

    SHOP("/shop"),
    CART("/cart"),
    ITEMS("/items"),
    CHECKOUT("/checkout"),
    STEP("/step"),
    STEP_ONE("/one"),
    STEP_TWO("/two"),
    STEP_THREE("/three"),

    ERROR("/error");

    private final String path;

    Endpoint(String path) {
        this.path = path;
    }
}
