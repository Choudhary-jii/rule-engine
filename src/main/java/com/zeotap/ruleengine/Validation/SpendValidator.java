package com.zeotap.ruleengine.Validation;

import com.zeotap.ruleengine.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class SpendValidator implements ConstraintValidator<ValidSpend, User> {

    @Override
    public void initialize(ValidSpend constraintAnnotation) {
        // Initialization code if needed
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {
        if (user.getIncome() != null && user.getSpend() != null) {
            return user.getSpend() < user.getIncome();
        }
        return true;


    }
}