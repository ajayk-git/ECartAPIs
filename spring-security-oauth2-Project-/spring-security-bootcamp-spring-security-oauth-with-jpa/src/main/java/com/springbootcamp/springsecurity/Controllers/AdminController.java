package com.springbootcamp.springsecurity.Controllers;


import com.springbootcamp.springsecurity.CO.MetaDataFieldCO;
import com.springbootcamp.springsecurity.DTOs.CategoryMetaDataFieldDTO;
import com.springbootcamp.springsecurity.DTOs.CustomerDto;
import com.springbootcamp.springsecurity.DTOs.SellerDto;
import com.springbootcamp.springsecurity.Entities.CategoryMetaDataField;
import com.springbootcamp.springsecurity.Exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.Services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;


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
    public ResponseEntity<String> activateAccountById(@PathVariable("id") Long id)   {
        return  adminService.activateAccountById(id);
    }



    //==============================================Deactivation  of User Account============================================================

    @PatchMapping("/deactivate-account/{id}")
    public ResponseEntity<String> deactivateAccountById(@PathVariable("id")Long id) {
        return  adminService.deactivateAccountById(id);
    }

    @PostMapping("/metadata-fields")
    public ResponseEntity<String> addMetaDataFields(@RequestBody MetaDataFieldCO metaDataFieldCO){
        String fieldName=metaDataFieldCO.getFieldName();
        return  adminService.addMetaDataField(fieldName);
    }

    @GetMapping("/metadata-fields")
    public List<CategoryMetaDataFieldDTO>getAllMetaDataFieldList(){
        return adminService.getAllMetaDataFieldList();
    }


}
