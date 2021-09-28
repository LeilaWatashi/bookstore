package com.watashi.bookstore.exception.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountNotEqualToZeroValidator
        implements ConstraintValidator<AmountNotEqualToZeroConstraint, Integer> {
    public void initialize(AmountNotEqualToZeroConstraint constraint) {
    }

    public boolean isValid(Integer amount, ConstraintValidatorContext context) {
        return amount != null && amount != 0;
    }
}
