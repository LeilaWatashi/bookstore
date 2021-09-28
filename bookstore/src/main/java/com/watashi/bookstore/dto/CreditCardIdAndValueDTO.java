package com.watashi.bookstore.dto;

import com.watashi.bookstore.exception.validator.MinimumPaymentValueConstraint;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Component
public class CreditCardIdAndValueDTO extends EntityDTO{
    @NotNull
    private Long creditCardId;
    @NotNull
    @MinimumPaymentValueConstraint
    private Double value;

}
