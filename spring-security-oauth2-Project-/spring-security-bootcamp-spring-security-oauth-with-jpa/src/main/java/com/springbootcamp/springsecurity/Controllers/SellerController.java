package com.springbootcamp.springsecurity.Controllers;

import com.springbootcamp.springsecurity.CO.*;
import com.springbootcamp.springsecurity.DTOs.SellerDto;
import com.springbootcamp.springsecurity.Entities.Users.Seller;
import com.springbootcamp.springsecurity.Exceptions.AccountAreadyExistException;
import com.springbootcamp.springsecurity.Repositories.SellerRepository;
import com.springbootcamp.springsecurity.Services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    SellerService sellerService;



    @GetMapping("/{id}")
    public SellerDto getSellerByid(@PathVariable Long id) {
        System.out.println("Seller registered. Details are :");
        return sellerService.getSellerByid(id);
    }

    //=============================================get all seller===========================================================================


    @GetMapping("/")
    public List<SellerDto> getAllSellers(){
        return  sellerService.getAllSellers();
    }

//================================================== seller registration====================================================================


    @PostMapping("/registration")
    public String sellerRegistration( @Valid @RequestBody SellerCO sellerCO) throws AccountAreadyExistException {
        Seller seller= sellerService.sellerRegistration(sellerCO);


        SellerDto sellerDto=new SellerDto(seller.getCompanyContact(),
                seller.getCompanyName(),seller.getLastName(),
                seller.getGst(),seller.getFirstName(),
                seller.getEmail(),seller.getId(),
                seller.getAddress().getAddressLine(),
                seller.getAddress().getCity(),
                seller.getAddress().getCountry(),
                seller.getAddress().getLable()
                ,seller.getAddress().getZipcode(),
                seller.getAddress().getState());
        return "successfully registered, Kindly go to registered mail inbox to get verified your account.";

    }

    //=====================================================Confirmation of seller registration================================================

    @GetMapping("/registrationConfirm")
    public  String sellerRegistrationConfirm(@RequestParam("token") String token){
        return  sellerService.sellerRegistrationConfirm(token);
    }

//============================================================forgot password=========================================================================


    @PostMapping("/forgotPassword")
    public  ResponseEntity<String>  forgotPassword(@RequestBody EmailCO emailCO) {
        return sellerService.forgotPasswordSendTokenToMail(emailCO.getEmail());
    }

    //==================================================reset your forgot password=====================================================================



    @PatchMapping("/confirmReset")
    public ResponseEntity<String> updateForgotPassword(@RequestParam("token") String token,@Valid @RequestBody PasswordUpdateCO passwordUpdateCO ){
        return sellerService.updateForgotPassword(token,passwordUpdateCO);
    }


    //=================================================Activation  of seller Account=========================================================

    @PatchMapping("/activateAccount/{id}")
    public ResponseEntity<String> activateSellerAccountById(@PathVariable("id") Long id) {
        return sellerService.activateSellerAccountById(id);
    }
//==============================================Deactivation  of seller Account============================================================

    @PatchMapping("/deactivateAccount/{id}")
    public ResponseEntity<String> deactivateSellerAccountById(@PathVariable("id")Long id) {
        return  sellerService.deactivateSellerAccountById(id);
    }

    //=================================================View Profile  of seller Account==================================

    @GetMapping("/viewProfile")
    public SellerDto viewSellerProfile(Principal principal){
        return  sellerService.viewSellerProfile(principal.getName());
    }

    //==================================update seller password===========================================================
    @PatchMapping("updateSellerPassword")
    public ResponseEntity<String> updateCustomerPassword(@Valid @RequestBody PasswordUpdateCO passwordUpdateCO, Principal principal){
        return sellerService.updateSellerPassword(passwordUpdateCO,principal.getName());
    }

    //=================================================Update Profile  of seller Account==================================

    @PatchMapping("updateProfile")
    public ResponseEntity<String> updateCustomerProfile(Principal principal, @RequestBody SellerProfileUpdateCO sellerProfileUpdateCO){
        return sellerService.upadteSellerProfile(principal.getName(),sellerProfileUpdateCO);
    }


}









//@DeleteMapping("/{id}")
//    public String deleteCustomer(@PathVariable Long id) throws AccountDoesNotExistException {
//        return sellerService.deleteCustomer(id);
//    }