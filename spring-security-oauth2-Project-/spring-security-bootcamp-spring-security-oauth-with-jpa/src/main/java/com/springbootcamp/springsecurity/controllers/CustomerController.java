package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.dtos.AddressDto;
import com.springbootcamp.springsecurity.dtos.CategoryDTO;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.services.CategoryService;
import com.springbootcamp.springsecurity.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/customer")
@RestController
public class CustomerController {


    @Autowired
    CustomerService customerService;
    @Autowired
    CategoryService categoryService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    //=============================Update Customer's password=====================================================

    @PatchMapping("/update-password")
    public ResponseEntity<String> updateCustomerPassword(@Valid @RequestBody PasswordUpdateCO passwordUpdateCO,Principal principal){
        return customerService.updateCustomerPassword(passwordUpdateCO,principal.getName());
    }


    //===========================View Profile  of Customer Account==================================================

    @GetMapping("/profile")
    public CustomerDto viewCustomerProfile(Principal principal){
        return  customerService.viewCustomerProfile(principal.getName());
    }

    //============================Update Profile  of Customer Account===============================================

    @PatchMapping("/profile")
    public ResponseEntity<String> updateCustomerProfile(Principal principal, @RequestBody CustomerProfileUpdateCo customerProfileUpdateCo){
        return customerService.updateCustomerProfile(principal.getName(),customerProfileUpdateCo);
    }



    //============================Get Address list of Customer=======================================================

    @GetMapping("/address")                   // id=customer id
    public List<AddressDto> getAllAddressCustomer(Principal principal ){
        return customerService.getAddressListCustomer(principal.getName());
    }


    //=========================Delete a address of Customer=============================================================

    @DeleteMapping("/address/{id}")               //id=address id
    public ResponseEntity<String> deleteAddressById(@PathVariable(name = "id") Long id, Principal principal) {
        return customerService.deleteAddressById(id,principal.getName());
    }


    //========================Add a new Address in address list of Customer============================================

    @PostMapping("/address")
    public ResponseEntity<String> addCustomerNewAddress(@RequestBody AddressCO addressCO, Principal principal){
        return customerService.addCustomerAddress(addressCO,principal.getName());
    }


    //============================Update a customers address==========================================================

    @PatchMapping("/address/{id}")
    public ResponseEntity<String> updateCustomerAddress(@PathVariable("id") Long id,@RequestBody AddressCO addressCO,Principal principal){
        return customerService.updateCustomerAddress(addressCO,id,principal.getName());
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getCategoriesByCustomer(@RequestParam(value = "id",required = false) Long categoryId){
        return categoryService.getCategoriesByCustomer(categoryId);
    }

}







