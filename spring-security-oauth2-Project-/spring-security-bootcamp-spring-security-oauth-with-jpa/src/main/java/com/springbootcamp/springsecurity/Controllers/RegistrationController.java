package com.springbootcamp.springsecurity.Controllers;

import com.springbootcamp.springsecurity.CO.CustomerCO;
import com.springbootcamp.springsecurity.CO.EmailCO;
import com.springbootcamp.springsecurity.CO.PasswordUpdateCO;
import com.springbootcamp.springsecurity.CO.SellerCO;
import com.springbootcamp.springsecurity.Services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    RegistrationService registrationService;


    @PostMapping("/customer")
    public ResponseEntity customerRegistration(@Valid @RequestBody CustomerCO customerCO) {
        return registrationService.registerCustomer(customerCO);
    }

    @PostMapping("/seller")
    public ResponseEntity sellerRegistration(@Valid @RequestBody SellerCO sellerCO) {
        return registrationService.registerSeller(sellerCO);
    }


    @GetMapping("/confirm-customer")
    public ResponseEntity registrationConfirmCustomer(@RequestParam("token") String token) {
        return registrationService.registrationConfirmCustomer(token);
    }

    @GetMapping("/confirm-seller")
    public ResponseEntity registrationConfirmSeller(@RequestParam("token") String token) {
        return registrationService.sellerRegistrationConfirm(token);
    }
    @PostMapping("/forgot-password")
    public  void  forgotPassword(@RequestBody EmailCO emailCO){
        registrationService.forgotPasswordSendTokenToMail(emailCO.getEmail());
    }
    @PatchMapping("/confirm-reset")
    public ResponseEntity<String> updateForgotPassword(@RequestParam("token") String token,@Valid @RequestBody PasswordUpdateCO passwordUpdateCO ){
        return registrationService.updateForgotPassword(token,passwordUpdateCO);
    }

}
