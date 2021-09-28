package com.watashi.bookstore.entity.sale;

import com.watashi.bookstore.entity.Entity;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class SaleProductCategoryResult extends Entity {
    private String category;
    private LocalDateTime date;
    private Long productAmount;
}
