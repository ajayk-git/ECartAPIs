package com.springbootcamp.springsecurity.controllers;

import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.dtos.AddressDto;
import com.springbootcamp.springsecurity.dtos.CategorySellerDto;
import com.springbootcamp.springsecurity.dtos.SellerDto;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import com.springbootcamp.springsecurity.services.CategoryService;
import com.springbootcamp.springsecurity.services.ProductService;
import com.springbootcamp.springsecurity.services.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;
    @GetMapping("/profile")
    public SellerDto viewSellerProfile(Principal principal) {
        return sellerService.viewSellerProfile(principal.getName());
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


    @PostMapping("/product")
    public ResponseEntity addNewProduct(@Valid @RequestBody ProductCo productCo,Principal principal){
        return productService.addNewProduct(productCo,principal);
    }


    @GetMapping("/categories")
    public List<CategorySellerDto> viewAllCategoriesBySeller(){
        return categoryService.viewAllCategoriesBySeller();
    }




}
