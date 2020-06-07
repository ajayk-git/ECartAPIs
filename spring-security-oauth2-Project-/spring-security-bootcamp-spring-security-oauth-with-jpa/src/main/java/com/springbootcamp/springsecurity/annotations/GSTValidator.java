package com.springbootcamp.springsecurity.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GSTValidator implements ConstraintValidator<IsValidGST,String> {

    Pattern pattern;
    Matcher matcher;

    private  static  final String GST_PATTERN="\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}[A-Z\\d]{1}[Z]{1}[A-Z\\d]{1}";
    @Override
    public void initialize(IsValidGST constraintAnnotation) {

    }

    @Override
    public boolean isValid(String GST, ConstraintValidatorContext context) {
        return GSTValidator(GST);
    }

    private boolean GSTValidator(String gst){

        pattern=Pattern.compile(GST_PATTERN);
        matcher=pattern.matcher(gst);
        return matcher.matches();
    }
}
