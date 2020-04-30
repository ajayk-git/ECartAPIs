package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.co.CategoryCO;
import com.springbootcamp.springsecurity.co.CategoryUpdateCO;
import com.springbootcamp.springsecurity.co.MetaDataFieldCO;
import com.springbootcamp.springsecurity.co.MetaDataFieldValueCo;
import com.springbootcamp.springsecurity.dtos.CategoryDTO;
import com.springbootcamp.springsecurity.dtos.CategoryMetaDataFieldDTO;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.dtos.SellerDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.services.AdminService;
import com.springbootcamp.springsecurity.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;
    @Autowired
    CategoryService categoryService;


    //=============================================get all customers ===========================================================================

    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomer(){
        return adminService.getAllCustomers();
    }


    //=============================================get a customer by id===========================================================================
    @GetMapping("/customer/{id}")
    public CustomerDto getCustomer(@PathVariable Integer id) throws AccountDoesNotExistException {
        return adminService.getCustomerById(id);
    }


    //=============================================get a seller by id===========================================================================


    @GetMapping("/seller/{id}")
    public SellerDto getSellerByid(@PathVariable Long id) {
        System.out.println("Seller registered. Details are :");
        return adminService.getSellerByid(id);
    }

    //=============================================get all seller===========================================================================

    @GetMapping("/sellers")
    public List<SellerDto> getAllSellers()
    {
        return  adminService.getAllSellers();
    }



    //==============================================Activation  of User Account============================================================

    @PatchMapping("/activate-account/{id}")
    public ResponseEntity activateAccountById(@PathVariable("id") Long id)   {
        return  adminService.activateAccountById(id);
    }



    //==============================================Deactivation  of User Account============================================================

    @PatchMapping("/deactivate-account/{id}")
    public ResponseEntity deactivateAccountById(@PathVariable("id")Long id) {
        return  adminService.deactivateAccountById(id);
    }

    @PostMapping("/metadata-fields")
    public ResponseEntity addMetaDataFields(@RequestBody MetaDataFieldCO metaDataFieldCO){
        String fieldName=metaDataFieldCO.getFieldName();
        return  categoryService.addMetaDataField(fieldName);
    }

    @GetMapping("/metadata-fields")
    public List<CategoryMetaDataFieldDTO>getAllMetaDataFieldList(){

        return categoryService.getAllMetaDataFieldList();
    }


    @PostMapping("/categories")
    public ResponseEntity addNewCategory(@Valid @RequestBody CategoryCO categoryCO){
        return categoryService.addNewCategory(categoryCO);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(){
         return categoryService.getAllCategories();
    }


    @GetMapping("/category/{id}")
    public List<CategoryDTO> getCategory(@PathVariable(name = "id") Long id){
        return categoryService.getCategory(id);
    }

    @PatchMapping("/category/{id}")
    public ResponseEntity updateCategory(@PathVariable(name = "id") Long id, @Valid @RequestBody CategoryUpdateCO categoryUpdateCO){
        return categoryService.updateCategory(id,categoryUpdateCO);
    }

    @PostMapping("/metadata-value")
    public  ResponseEntity addMetaDataValues(@Valid @RequestBody MetaDataFieldValueCo metaDataFieldValueCo){
        return categoryService.addMetaDataValues(metaDataFieldValueCo);
    }

    @PatchMapping("/metadata-value")
    public ResponseEntity updateMetaDataValues(@Valid @RequestBody MetaDataFieldValueCo metaDataFieldValueCo){
        return categoryService.updateMetaDataValues(metaDataFieldValueCo);
    }


}
