package com.watashi.bookstore.entity.shop;

import lombok.Getter;

@Getter
public enum FreightType {
    SLOWER(4.5),
    NORMAL(3.5),
    FAST(2.5),
    SUPER_FAST(1.5),
    FREE(1.0);

    private final Double coefficient;
    private final Double weight;

    FreightType(Double coefficient) {
        this.coefficient = coefficient;
        this.weight = .04;
    }
}
