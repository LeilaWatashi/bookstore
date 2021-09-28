package com.watashi.bookstore.dto;


import com.watashi.bookstore.exception.validator.AmountNotEqualToZeroConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Component
public class StockManageDTO extends EntityDTO{
    @NotNull(message = "O estoque é obrigatório")
    private Long id;

    private ProductForSimpleStockViewDTO product;

    private Integer amount;

    @AmountNotEqualToZeroConstraint
    @NotNull(message = "A quantidade da movimentação no estoque é obrigatória")
    private Integer operationAmount;
}
