package com.zeotap.ruleengine.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SpendValidator.class)
public @interface ValidSpend {

    String message() default "Spend must be less than income";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
