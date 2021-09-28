package com.watashi.bookstore.entity.sale;

import com.watashi.bookstore.entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SaleProductCategory extends Entity {

    private String category;
    private LocalDate date;
    private Long productAmount;

    public void addProductAmount(Long productAmount) {
        this.productAmount += productAmount;
    }
}
