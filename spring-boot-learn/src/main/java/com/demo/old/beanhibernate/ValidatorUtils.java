package com.demo.old.beanhibernate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidatorUtils {
    private static Validator validator;

    static {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public static void validateEntity(Object object, Class<?>... groups){
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for(ConstraintViolation<Object> constraint:  constraintViolations){
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new RuntimeException(msg.toString());
        }
    }
}