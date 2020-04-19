package com.springbootcamp.springsecurity.Annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface IsValidEmail {

        String message() default "Email address is invalid,Please enter a valid Email.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

}
