package com.springbootcamp.springsecurity.Annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<IsValidEmail,String> {

    private Pattern pattern;
    private Matcher matcher;

    private static  final String EMAIL_PATTERN="^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
                                                //email pattern: ajay.mca17.du@ducs.co.in
    @Override
    public void initialize(IsValidEmail constraintAnnotation) {

     //   System.out.println("Invalid Email-id ,Please enter valid Email-id");
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return validateEmail(email);
    }

    private boolean validateEmail(String email){
        pattern=Pattern.compile(EMAIL_PATTERN);
        matcher=pattern.matcher(email);
        return  matcher.matches();
    }
}
