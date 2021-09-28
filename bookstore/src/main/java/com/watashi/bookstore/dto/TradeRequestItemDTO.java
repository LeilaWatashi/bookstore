package com.watashi.bookstore.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Component
public class TradeRequestItemDTO extends EntityDTO{
    @NotNull(message = "O item é obrigatório")
    private Long id;
    @NotNull(message = "A quantidade é obrigatória")
    private Integer amount;
    private String reason;
    private boolean include;
}
