package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.dtos.CategoryDTO;
import com.springbootcamp.springsecurity.dtos.CategoryMetaDataFieldDTO;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.dtos.SellerDto;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.services.AdminService;
import com.springbootcamp.springsecurity.services.CategoryService;
import com.springbootcamp.springsecurity.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;


    //=============================================get all customers ===========================================================================

    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomer(Principal principal){
        return adminService.getAllCustomers(principal);
    }


    //=============================================get a customer by id===========================================================================
    @GetMapping("/customer/{id}")
    public CustomerDto getCustomer(@PathVariable Integer id,Principal principal) throws AccountDoesNotExistException {
        return adminService.getCustomerById(id,principal);
    }


    //=============================================get a seller by id===========================================================================


    @GetMapping("/seller/{id}")
    public SellerDto getSellerByid(@PathVariable Long id,Principal principal) {
        System.out.println("Seller registered. Details are :");
        return adminService.getSellerByid(id,principal);
    }

    //=============================================get all seller===========================================================================

    @GetMapping("/sellers")
    public List<SellerDto> getAllSellers(Principal principal)
    {
        return  adminService.getAllSellers(principal);
    }



    //==============================================Activation  of User Account============================================================

    @PatchMapping("/activate-account/{id}")
    public ResponseEntity activateAccountById(@PathVariable("id") Long id,Principal principal)   {
        return  adminService.activateAccountById(id,principal);
    }


    //==============================================Deactivation  of User Account============================================================

    @PatchMapping("/deactivate-account/{id}")
    public ResponseEntity deactivateAccountById(@PathVariable("id")Long id,Principal principal) {
        return  adminService.deactivateAccountById(id,principal);
    }

    @PostMapping("/metadata-fields")
    public ResponseEntity addMetaDataFields(@RequestBody MetaDataFieldCO metaDataFieldCO,Principal principal){
        String fieldName=metaDataFieldCO.getFieldName();
        return  categoryService.addMetaDataField(fieldName,principal);
    }

    @GetMapping("/metadata-fields")
    public List<CategoryMetaDataFieldDTO>getAllMetaDataFieldList(Principal principal){
        return categoryService.getAllMetaDataFieldList(principal);
    }


    @PostMapping("/categories")
    public ResponseEntity addNewCategory(@Valid @RequestBody CategoryCO categoryCO,Principal principal){
        return categoryService.addNewCategory(categoryCO,principal);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(Principal principal){
        return categoryService.getAllCategories(principal);
    }


    @GetMapping("/category/{id}")
    public List<CategoryDTO> getCategory(@PathVariable(name = "id") Long id,Principal principal){
        return categoryService.getCategory(id,principal);
    }

    @PatchMapping("/category/{id}")
    public ResponseEntity updateCategory(@PathVariable(name = "id") Long id,Principal principal,
                                         @Valid @RequestBody CategoryUpdateCO categoryUpdateCO){
        return categoryService.updateCategory(id,categoryUpdateCO,principal);
    }

    @PostMapping("/metadata-value")
    public  ResponseEntity addMetaDataValues(@Valid @RequestBody MetaDataFieldValueCo metaDataFieldValueCo,Principal principal){
        return categoryService.addMetaDataValues(metaDataFieldValueCo,principal);
    }

    @PatchMapping("/metadata-value")
    public ResponseEntity updateMetaDataValues(@Valid @RequestBody MetaDataFieldValueCo metaDataFieldValueCo,Principal principal){
        return categoryService.updateMetaDataValues(metaDataFieldValueCo,principal);
    }

    //===========================================to activate product ===========================================================
    @PutMapping("/product-activate/{id}")
    public ResponseEntity activateProduct(@PathVariable(name = "id") Long productId,Principal principal){
        return productService.activateProduct(productId,principal);
    }


    //===========================================to deactivate product ===========================================================

    @PutMapping("/product-deactivate/{id}")
    public ResponseEntity deactivateProduct(@PathVariable(name = "id") Long productId,Principal principal){
        return productService.deactivateProduct(productId,principal);
    }




}
