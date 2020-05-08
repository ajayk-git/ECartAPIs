package com.springbootcamp.springsecurity.controllers;

import com.springbootcamp.springsecurity.co.CustomerCO;
import com.springbootcamp.springsecurity.co.EmailCO;
import com.springbootcamp.springsecurity.co.PasswordUpdateCO;
import com.springbootcamp.springsecurity.co.SellerCO;
import com.springbootcamp.springsecurity.services.RegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@Api(value = "Registration Rest Controller",description = "Operations Related to Registration.")
@RestController
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    RegistrationService registrationService;


    //=================================================Customer registration . =========================================================

    @ApiOperation(value = "Customer registration .")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/customer")
    public ResponseEntity customerRegistration(@Valid @RequestBody CustomerCO customerCO) {
        return registrationService.registerCustomer(customerCO);
    }

    //=================================================Customer registration . =========================================================

    @ApiOperation(value = "Seller registration .")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/seller")
    public ResponseEntity sellerRegistration(@Valid @RequestBody SellerCO sellerCO) {
        return registrationService.registerSeller(sellerCO);
    }

    //=================================================Customer registration confirmation . =========================================================

    @ApiOperation(value = "Registration confirmation.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @GetMapping("/confirm")
    public ResponseEntity registrationConfirm(@RequestParam("token") String token) {
        return registrationService.registrationConfirm(token);
    }

//    //=================================================Seller registration confirmation . =========================================================
//
//    @ApiOperation(value = "Seller registration confirmation .")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Success|OK"),
//            @ApiResponse(code = 400, message = "Bad Request")})
//    @GetMapping("/confirm-seller")
//    public ResponseEntity registrationConfirmSeller(@RequestParam("token") String token) {
//        return registrationService.sellerRegistrationConfirm(token);
//    }

    //=================================================Forgot password activation link mail send =======================================================

    @ApiOperation(value = "Forgot password activation link mail send.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping("/forgot-password")
    public  void  forgotPassword(@RequestBody EmailCO emailCO){
        registrationService.forgotPasswordSendTokenToMail(emailCO.getEmail());
    }

    //=================================================Update password via sent activation link email .=======================================================

    @ApiOperation(value = "Update password via sent activation link email .")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PatchMapping("/confirm-reset")
    public ResponseEntity<String> updateForgotPassword(@RequestParam("token") String token, Principal principal,
                                                       @Valid @RequestBody PasswordUpdateCO passwordUpdateCO ){
        return registrationService.updateForgotPassword(token,passwordUpdateCO,principal);
    }

    //=================================================Re-sent activation link email .=======================================================


    @ApiOperation(value = "Re-sent activation link email .")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping("/resend-activation-mail")
    public ResponseEntity<String> reSendActivationLink(@Valid @RequestBody EmailCO emailCO ){
        return registrationService.reSendActivationLink(emailCO.getEmail());
    }

}
