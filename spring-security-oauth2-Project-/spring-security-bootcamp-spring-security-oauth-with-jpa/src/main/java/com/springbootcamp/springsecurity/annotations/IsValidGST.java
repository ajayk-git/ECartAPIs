package com.springbootcamp.springsecurity.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GSTValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
public @interface IsValidGST {
    String message() default "GST Number is invalid,Please enter a valid GST Number.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
