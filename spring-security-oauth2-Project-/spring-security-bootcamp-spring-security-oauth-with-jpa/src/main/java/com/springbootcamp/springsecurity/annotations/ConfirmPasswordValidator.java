package com.springbootcamp.springsecurity.annotations;

import com.springbootcamp.springsecurity.co.PasswordUpdateCO;
import com.springbootcamp.springsecurity.co.UserCO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPasswordMatcher,Object> {
    @Override
    public void initialize(ConfirmPasswordMatcher constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
       if(value instanceof PasswordUpdateCO){
           PasswordUpdateCO passwordUpdateCO= (PasswordUpdateCO) value;
          return   passwordUpdateCO.getConfirmPassword().equals(passwordUpdateCO.getConfirmPassword());
       }

        UserCO userCO= (UserCO) value;
       if(userCO.getPassword()==null||userCO.getConfirmPassword()==null)
           return false;

       return  userCO.getPassword().equals(userCO.getConfirmPassword());
    }

}
