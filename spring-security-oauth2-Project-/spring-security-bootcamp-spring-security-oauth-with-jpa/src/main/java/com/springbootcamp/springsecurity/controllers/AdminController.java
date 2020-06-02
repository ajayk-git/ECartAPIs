package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.GlobalVariables;
import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.dtos.*;
import com.springbootcamp.springsecurity.exceptions.AccountDoesNotExistException;
import com.springbootcamp.springsecurity.services.AdminService;
import com.springbootcamp.springsecurity.services.CategoryService;
import com.springbootcamp.springsecurity.services.ProductService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Api(value = "Admin Rest Controller, Operations Related to Admin")
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

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "View all customers by Admin")
    @GetMapping("/customers")
    public List<CustomerDto> getAllCustomer(Principal principal) {
        return adminService.getAllCustomers(principal);
    }

    //=============================================get a customer by admin===========================================================================

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "View a specific customer by Admin")
    @GetMapping("/customer/{id}")
    public CustomerDto getCustomer(@PathVariable Integer id, Principal principal) throws AccountDoesNotExistException {
        return adminService.getCustomerById(id, principal);
    }


    //=============================================get a seller by admin===========================================================================

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "View a specific seller by Admin")
    @GetMapping("/seller/{sellerId}")
    public SellerDto getSellerById(@PathVariable(name = "sellerId") Long sellerId, Principal principal) {
        System.out.println("Seller registered. Details are :");
        return adminService.getSellerByid(sellerId, principal);
    }

    //=============================================get all sellers by admin===========================================================================

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "View all sellers by Admin")
    @GetMapping("/sellers")
    public List<SellerDto> getAllSellers(Principal principal) {
        return adminService.getAllSellers(principal);
    }

    //==============================================Activation  of User Account============================================================

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Activate user(Customer/Seller) by Admin")
    @PatchMapping("/activate-account/{userId}")
    public ResponseEntity activateAccountById(@PathVariable(name = "userId") Long userId, Principal principal) {
        return adminService.activateAccountById(userId, principal);
    }

    //==============================================Deactivation  of User Account============================================================

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Deactivate user(Customer/Seller) by Admin")
    @PatchMapping("/deactivate-account/{userId}")
    public ResponseEntity deactivateAccountById(@PathVariable(name = "userId") Long userId, Principal principal) {
        return adminService.deactivateAccountById(userId, principal);
    }

    //==========================================Add new MetaData fields by Admin===========================================================

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Add new MetaData fields by Admin")
    @PostMapping("/metadata-fields")
    public ResponseEntity addMetaDataFields(@Valid @RequestBody MetaDataFieldCO metaDataFieldCO, Principal principal) {
        String fieldName = metaDataFieldCO.getFieldName();
        return categoryService.addMetaDataField(fieldName, principal);
    }

    //==========================================Get all MetaData fields by Admin===========================================================

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Get all MetaData fields by Admin")
    @GetMapping("/metadata-fields")
    public List<CategoryMetaDataFieldDTO> getAllMetaDataFieldList(Principal principal) {
        return categoryService.getAllMetaDataFieldList(principal);
    }

    //==========================================Add new category by Admin===========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Add new category by Admin")
    @PostMapping("/categories")
    public ResponseEntity addNewCategory(@Valid @RequestBody CategoryCO categoryCO, Principal principal) {
        return categoryService.addNewCategory(categoryCO, principal);
    }

    //==========================================Get all categories by Admin===========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Get all categories by Admin")
    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(Principal principal) {
        return categoryService.getAllCategories(principal);
    }

    //==========================================Get specific category by Admin===========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Get specific category by Admin")
    @GetMapping("/category/{categoryId}")
    public List<CategoryDTO> getCategory(@PathVariable(name = "categoryId") Long categoryId, Principal principal) {
        return categoryService.getCategory(categoryId, principal);
    }

    //==========================================Update specific category by Admin===========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Update specific category by Admin")
    @PatchMapping("/category/{categoryId}")
    public ResponseEntity updateCategory(@PathVariable(name = "categoryId") Long categoryId, Principal principal,
                                         @Valid @RequestBody CategoryUpdateCO categoryUpdateCO) {
        return categoryService.updateCategory(categoryId, categoryUpdateCO, principal);
    }

    //==========================================Add category metadata field values by Admin===========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Add category metadata field values by Admin")
    @PostMapping("/metadata-value")
    public ResponseEntity addMetaDataValues(@Valid @RequestBody MetaDataFieldValueCo metaDataFieldValueCo, Principal principal) {
        return categoryService.addMetaDataValues(metaDataFieldValueCo, principal);
    }


    //===========================================Update category metadata field values by Admin===========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Update category metadata field values by Admin")
    @PatchMapping("/metadata-value")
    public ResponseEntity updateMetaDataValues(@Valid @RequestBody MetaDataFieldValueCo metaDataFieldValueCo, Principal principal) {
        return categoryService.updateMetaDataValues(metaDataFieldValueCo, principal);
    }

    //===========================================to activate product ===========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Activate a product by Admin")
    @PutMapping("/product-activate/{productId}")
    public ResponseEntity activateProduct(@PathVariable(name = "productId") Long productId, Principal principal) {
        return productService.activateProduct(productId, principal);
    }


    //===========================================to deactivate product ===========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Deactivate a product by Admin")
    @PutMapping("/product-deactivate/{productId}")
    public ResponseEntity deactivateProduct(@PathVariable(name = "productId") Long productId, Principal principal) {
        return productService.deactivateProduct(productId, principal);
    }

//=================================================View a Product By Admin Account==================================

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "To View a Product By Admin.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @GetMapping("/product/{productId}")
    public ProductAdminDto viewProductByAdmin(@PathVariable(name = "productId") Long productId, Principal principal) {
        return productService.viewProductByAdmin(productId, principal);

    }

    //=================================================Get a all product By Admin=========================================================
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Authorization token",
                    required = true, dataType = "string", paramType = "header")})
    @Secured("ROLE_ADMIN")
    @ApiOperation(value = "Get all products by Admin.")
    @GetMapping("/product")
    public List<ProductAdminDto> getAllProductsByAdmin(@RequestParam(value = "page", defaultValue = GlobalVariables.DEFAULT_PAGE_OFFSET) Optional<Integer> page,
                                                       @RequestParam(value = "size", defaultValue = GlobalVariables.DEFAULT_PAGE_SIZE) Optional<Integer> contentSize,
                                                       @RequestParam(value = "sort", defaultValue = GlobalVariables.DEFAULT_SORT_PROPERTY) Optional<String> sortProperty,
                                                       @RequestParam(value = "direction", defaultValue = GlobalVariables.DEFAULT_SORT_DIRECTION) Optional<String> sortDirection,
                                                       Principal principal) {
        return productService.getAllProductsByAdmin(page, contentSize, sortProperty, sortDirection, principal);
    }

}
