package com.watashi.bookstore.exception.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ ElementType.FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = MinimumPaymentValueValidator.class)
@Documented
public @interface MinimumPaymentValueConstraint {
    String message() default "{MinimumPaymentValueConstraint.rule}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
