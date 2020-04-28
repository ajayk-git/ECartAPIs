package com.springbootcamp.springsecurity.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileNoValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
public @interface IsValidMobileNo {
    String message() default "Please enter a valid Mobile number( Of 10 digits followed by 91 0r 0.) Pattern : =91XXXXXXXXXX or 0XXXXXXXXXX";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
