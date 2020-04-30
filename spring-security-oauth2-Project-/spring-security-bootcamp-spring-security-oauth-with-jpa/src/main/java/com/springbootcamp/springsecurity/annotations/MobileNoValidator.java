package com.springbootcamp.springsecurity.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileNoValidator implements ConstraintValidator<IsValidMobileNo,String> {

    Pattern pattern;
    Matcher matcher;

    private  static final  String MOBILE_PATTERN= "^[789]\\d{9}$"; //Indian mobile number Pattern

    @Override
    public void initialize(IsValidMobileNo constraintAnnotation) {
       // System.out.println("GST Number is invalid,Please enter a valid Mobile  Number.");

    }

    @Override
    public boolean isValid(String mobileNo, ConstraintValidatorContext context) {
        return  validateMobileNo(mobileNo);
    }

    private  boolean validateMobileNo(String mobileNo){

        pattern=Pattern.compile(MOBILE_PATTERN);
        matcher=pattern.matcher(mobileNo);
        return matcher.matches();
    }
}
