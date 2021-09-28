package com.watashi.bookstore.exception.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinimumPaymentValueValidator
        implements ConstraintValidator<MinimumPaymentValueConstraint, Double> {
    public void initialize(MinimumPaymentValueConstraint constraint) {
    }

    public boolean isValid(Double paymentValue, ConstraintValidatorContext context) {
        double MINIMUM_VALUE_FOR_EACH_CARD = 10.0;

        return !paymentValue.isNaN()
                && paymentValue > 0
                && paymentValue >= MINIMUM_VALUE_FOR_EACH_CARD;

    }
}
