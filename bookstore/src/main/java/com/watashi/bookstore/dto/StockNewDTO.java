package com.watashi.bookstore.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Component
public class StockNewDTO extends EntityDTO{
    @NotNull(message = "O produto é obrigatório")
    private ProductIdDTO product;

    @Min(value = 1, message = "A quantidade mínima é 1")
    @NotNull(message = "A quantidade é obrigatória")
    private Integer amount;
}
