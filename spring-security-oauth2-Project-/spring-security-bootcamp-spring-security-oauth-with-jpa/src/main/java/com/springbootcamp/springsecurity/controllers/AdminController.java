package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.GlobalVariables;
import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.dtos.*;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.services.AdminService;
import com.springbootcamp.springsecurity.services.CategoryService;
import com.springbootcamp.springsecurity.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Api(value = "Admin Rest Controller",description = "Operations Related to Admin")
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

    @ApiOperation(value = "View all customers by Admin")
    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomer(Principal principal){
        return adminService.getAllCustomers(principal);
    }

    //=============================================get a customer by id===========================================================================

    @ApiOperation(value = "View a specific customer by Admin")
    @GetMapping("/customer/{id}")
    public CustomerDto getCustomer(@PathVariable Integer id,Principal principal) throws AccountDoesNotExistException {
        return adminService.getCustomerById(id,principal);
    }


    //=============================================get a seller by id===========================================================================


    @ApiOperation(value = "View a specific seller by Admin")
    @GetMapping("/seller/{id}")
    public SellerDto getSellerByid(@PathVariable Long id,Principal principal) {
        System.out.println("Seller registered. Details are :");
        return adminService.getSellerByid(id,principal);
    }

    //=============================================get all seller===========================================================================

    @ApiOperation(value = "View all sellers by Admin")
    @GetMapping("/sellers")
    public List<SellerDto> getAllSellers(Principal principal)
    {
        return  adminService.getAllSellers(principal);
    }

    //==============================================Activation  of User Account============================================================

    @ApiOperation(value = "Activate user(Customer/Seller) by Admin")
    @PatchMapping("/activate-account/{id}")
    public ResponseEntity activateAccountById(@PathVariable("id") Long id,Principal principal)   {
        return  adminService.activateAccountById(id,principal);
    }

    //==============================================Deactivation  of User Account============================================================

    @ApiOperation(value = "Deactivate user(Customer/Seller) by Admin")
    @PatchMapping("/deactivate-account/{id}")
    public ResponseEntity deactivateAccountById(@PathVariable("id")Long id,Principal principal) {
        return  adminService.deactivateAccountById(id,principal);
    }

    //==========================================Add new MetaData fields by Admin===========================================================

    @ApiOperation(value = "Add new MetaData fields by Admin")
    @PostMapping("/metadata-fields")
    public ResponseEntity addMetaDataFields(@Valid @RequestBody MetaDataFieldCO metaDataFieldCO,Principal principal){
        String fieldName=metaDataFieldCO.getFieldName();
        return  categoryService.addMetaDataField(fieldName,principal);
    }

    //==========================================Get all MetaData fields by Admin===========================================================

    @ApiOperation(value = "Get all MetaData fields by Admin")
    @GetMapping("/metadata-fields")
    public List<CategoryMetaDataFieldDTO>getAllMetaDataFieldList(Principal principal){
        return categoryService.getAllMetaDataFieldList(principal);
    }

    //==========================================Add new category by Admin===========================================================

    @ApiOperation(value = "Add new category by Admin")
    @PostMapping("/categories")
    public ResponseEntity addNewCategory(@Valid @RequestBody CategoryCO categoryCO,Principal principal){
        return categoryService.addNewCategory(categoryCO,principal);
    }

    //==========================================Get all categories by Admin===========================================================

    @ApiOperation(value = "Get all categories by Admin")
    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(Principal principal){
        return categoryService.getAllCategories(principal);
    }

    //==========================================Get specific category by Admin===========================================================

    @ApiOperation(value = "Get specific category by Admin")
    @GetMapping("/category/{id}")
    public List<CategoryDTO> getCategory(@PathVariable(name = "id") Long id,Principal principal){
        return categoryService.getCategory(id,principal);
    }

    //==========================================Update specific category by Admin===========================================================

    @ApiOperation(value = "Update specific category by Admin")
    @PatchMapping("/category/{id}")
    public ResponseEntity updateCategory(@PathVariable(name = "id") Long id,Principal principal,
                                         @Valid @RequestBody CategoryUpdateCO categoryUpdateCO){
        return categoryService.updateCategory(id,categoryUpdateCO,principal);
    }

    //==========================================Add category metadata field values by Admin===========================================================

    @ApiOperation(value = "Add category metadata field values by Admin")
    @PostMapping("/metadata-value")
    public  ResponseEntity addMetaDataValues(@Valid @RequestBody MetaDataFieldValueCo metaDataFieldValueCo,Principal principal){
        return categoryService.addMetaDataValues(metaDataFieldValueCo,principal);
    }


    //===========================================Update category metadata field values by Admin===========================================================

    @ApiOperation(value = "Update category metadata field values by Admin")
    @PatchMapping("/metadata-value")
    public ResponseEntity updateMetaDataValues(@Valid @RequestBody MetaDataFieldValueCo metaDataFieldValueCo,Principal principal){
        return categoryService.updateMetaDataValues(metaDataFieldValueCo,principal);
    }

    //===========================================to activate product ===========================================================

    @ApiOperation(value = "Activate a product by Admin")
    @PutMapping("/product-activate/{id}")
    public ResponseEntity activateProduct(@PathVariable(name = "id") Long productId,Principal principal){
        return productService.activateProduct(productId,principal);
    }


    //===========================================to deactivate product ===========================================================

    @ApiOperation(value = "Deactivate a product by Admin")
    @PutMapping("/product-deactivate/{id}")
    public ResponseEntity deactivateProduct(@PathVariable(name = "id") Long productId,Principal principal){
        return productService.deactivateProduct(productId,principal);
    }

//=================================================View a Product By Admin Account==================================

    @ApiOperation(value = "To View a Product By Admin.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/product/{productId}")
    public ProductAdminDto viewProductByAdmin(@PathVariable(name = "productId") Long productId, Principal principal) {
        return productService.viewProductByAdmin(productId, principal);

    }

    //=================================================Get a all product By Admin=========================================================
    @ApiOperation(value = "Get all products by Admin.")
    @GetMapping("/product")
    public ResponseEntity getAllProductsByAdmin(@RequestParam(value = "page",defaultValue = GlobalVariables.DEFAULT_PAGE_OFFSET) Optional<Integer> page,
                                                   @RequestParam(value = "size",defaultValue = GlobalVariables.DEFAULT_PAGE_SIZE) Optional<Integer> contentSize,
                                                   @RequestParam(value = "sort",defaultValue = GlobalVariables.DEFAULT_SORT_PROPERTY)Optional<String> sortProperty,
                                                   @RequestParam(value = "direction",defaultValue = GlobalVariables.DEFAULT_SORT_DIRECTION)Optional<String> sortDirection,
                                                   Principal principal){
        return productService.getAllProductsByAdmin(page,contentSize,sortProperty,sortDirection,principal);
    }

    }
