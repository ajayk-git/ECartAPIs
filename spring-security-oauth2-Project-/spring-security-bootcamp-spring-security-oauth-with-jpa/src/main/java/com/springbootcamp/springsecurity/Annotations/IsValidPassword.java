package com.springbootcamp.springsecurity.Annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
public @interface IsValidPassword {

    String message() default " DefaultPlease enter a valid password( Of min. 8 characters) Pattern :";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};


}
