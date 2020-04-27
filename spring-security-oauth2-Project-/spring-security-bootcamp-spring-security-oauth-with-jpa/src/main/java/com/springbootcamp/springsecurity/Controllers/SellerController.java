package com.springbootcamp.springsecurity.Controllers;

import com.springbootcamp.springsecurity.CO.*;
import com.springbootcamp.springsecurity.DTOs.AddressDto;
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

    @GetMapping("/profile")
    public SellerDto viewSellerProfile(Principal principal){
        return  sellerService.viewSellerProfile(principal.getName());
    }


    @GetMapping("/address")
    public AddressDto getAddressSeller(Principal principal){
        return sellerService.getAddressSeller(principal.getName());
    }

    //==================================update seller password===========================================================
    @PatchMapping("/password")
    public ResponseEntity<String> updateCustomerPassword(@Valid @RequestBody PasswordUpdateCO passwordUpdateCO, Principal principal){
        return sellerService.updateSellerPassword(passwordUpdateCO,principal.getName());
    }

    //=================================================Update Profile  of seller Account==================================

    @PatchMapping("/profile")
    public ResponseEntity<String> updateCustomerProfile(Principal principal, @RequestBody SellerProfileUpdateCO sellerProfileUpdateCO){
        return sellerService.upadteSellerProfile(principal.getName(),sellerProfileUpdateCO);
    }


    @PatchMapping("/address/{id}")
    public ResponseEntity<String> updateSellerAddress(@PathVariable("id") Long id,@RequestBody  AddressCO addressCO,Principal principal){
        return sellerService.updateSellerAddress(addressCO,id,principal.getName());
    }

}
