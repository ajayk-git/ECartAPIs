package com.springbootcamp.springsecurity.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Constraint(validatedBy = ConfirmPasswordValidator.class)

public @interface ConfirmPasswordMatcher {
    String message() default "Password and Confirm Password Does not Match.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
