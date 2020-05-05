package com.springbootcamp.springsecurity.controllers;

import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.dtos.*;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import com.springbootcamp.springsecurity.services.CategoryService;
import com.springbootcamp.springsecurity.services.ProductService;
import com.springbootcamp.springsecurity.services.SellerService;
import jdk.nashorn.internal.objects.annotations.Getter;
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

    //=================================================Update Address  of seller Account==================================

    @PatchMapping("/address/{id}")
    public ResponseEntity<String> updateSellerAddress(@PathVariable("id") Long id,@RequestBody  AddressCO addressCO,Principal principal){
        return sellerService.updateSellerAddress(addressCO,id,principal.getName());
    }

    //=================================================Add a new Product By seller Account==================================

    @PostMapping("/product")
    public ResponseEntity addNewProduct(@Valid @RequestBody ProductCo productCo,Principal principal){
        return productService.addNewProduct(productCo,principal);
    }

    //=================================================View Category(ies) by seller==================================

    @GetMapping("/categories")
    public List<CategorySellerDto> viewAllCategoriesBySeller(){
        return categoryService.viewAllCategoriesBySeller();
    }

    //=================================================View  Product-Variant By seller Account==================================

    @GetMapping("/product/variation/{id}")
    public ProductVariantDto viewProductVariant(@PathVariable(name = "id") Long productVariantId){
        return productService.getProductVariant(productVariantId);
    }

    //=================================================Add a new  Product-Variant By seller Account==================================

    @PostMapping("/product/variation/{productId}")
    public ResponseEntity addNewProductVariant(@PathVariable(name = "productId") Long productId,@Valid @RequestBody ProductVariationCo productVariationCo){
        return productService.addNewProductVariant(productId,productVariationCo);
    }

    //=================================================View a Product By seller Account==================================

    @GetMapping("/product/{productId}")
    public ProductSellerDto viewProductBySeller(@PathVariable(name = "productId") Long productId,Principal principal){
        return productService.viewProductBySeller(productId,principal);
    }

    //=================================================Delete a Product By seller Account==================================

    @DeleteMapping("/product/{productId}")
    public ResponseEntity deleteProductBySeller(@PathVariable(name = "productId") Long productId,Principal principal){
        return productService.deleteProductBySeller(productId,principal);
    }


}
