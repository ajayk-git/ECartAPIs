package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.GlobalVariables;
import com.springbootcamp.springsecurity.co.*;
import com.springbootcamp.springsecurity.dtos.AddressDto;
import com.springbootcamp.springsecurity.dtos.CategoryDTO;
import com.springbootcamp.springsecurity.dtos.CustomerDto;
import com.springbootcamp.springsecurity.dtos.ProductCustomerDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.services.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Api(value = "Customer Rest Controller",description = "Operations Related to Customers")
@RequestMapping("/customer")
@RestController
public class CustomerController {


    @Autowired
    CustomerService customerService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    //=============================Update Customer's password=====================================================

    @ApiOperation(value = "Update Password by Customer.")
    @PatchMapping("/update-password")
    public ResponseEntity<String> updateCustomerPassword(@Valid @RequestBody PasswordUpdateCO passwordUpdateCO,Principal principal){
        return customerService.updateCustomerPassword(passwordUpdateCO,principal.getName(),principal);
    }


    //===========================View Profile  of Customer Account==================================================

    @ApiOperation(value = "Get profile by Customer.")
    @GetMapping("/profile")
    public CustomerDto viewCustomerProfile(Principal principal){
        return  customerService.viewCustomerProfile(principal.getName(),principal);
    }

    //============================Update Profile  of Customer Account===============================================

    @ApiOperation(value = "Update profile by Customer.")
    @PatchMapping("/profile")
    public ResponseEntity<String> updateCustomerProfile(Principal principal, @Valid @RequestBody CustomerProfileUpdateCo customerProfileUpdateCo){
        return customerService.updateCustomerProfile(principal.getName(),customerProfileUpdateCo,principal);
    }


    //============================Get Address list of Customer=======================================================

    @ApiOperation(value = "Get address list by Customer.")
    @GetMapping("/address")                   // id=customer id
    public List<AddressDto> getAllAddressCustomer(Principal principal ){
        return customerService.getAddressListCustomer(principal.getName(),principal);
    }


    //=========================Delete a address of Customer=============================================================

    @ApiOperation(value = "Delete address by Customer.")
    @DeleteMapping("/address/{id}")               //id=address id
    public ResponseEntity<String> deleteAddressById(@PathVariable(name = "id") Long id, Principal principal) {
        return customerService.deleteAddressById(id,principal.getName(),principal);
    }


    //========================Add a new Address in address list of Customer============================================

    @ApiOperation(value = "Add new address by Customer.")
    @PostMapping("/address")
    public ResponseEntity<String> addCustomerNewAddress(@RequestBody AddressCO addressCO, Principal principal){
        return customerService.addCustomerAddress(addressCO,principal.getName(),principal);
    }


    //============================Update a customers address==========================================================

    @ApiOperation(value = "Update a address by Customer.")
    @PatchMapping("/address/{id}")
    public ResponseEntity<String> updateCustomerAddress(@PathVariable("id") Long id,
                                                        @Valid @RequestBody AddressCO addressCO,Principal principal){
        return customerService.updateCustomerAddress(addressCO,id,principal.getName(),principal);
    }

    //=================================================Get category List By Customer =========================================================

    @ApiOperation(value = "Get category list by Customer.")
    @GetMapping("/categories")
    public List<CategoryDTO> getCategoriesByCustomer(@RequestParam(value = "id",required = false) Long categoryId,
                                                     Principal principal){
        return categoryService.getCategoriesByCustomer(categoryId,principal);
    }

    //=================================================Get a product By Customer =========================================================

    @ApiOperation(value = "Get a product by Customer.")
    @GetMapping("product/{productId}")
    public ProductCustomerDto getProductByCustomer(@PathVariable(name = "productId") Long productId,Principal principal){
        return productService.getProductByCustomer(productId,principal);
    }

    //=================================================Get all products By Customer =========================================================
    @ApiOperation(value = "Get all products by Customer.")
    @GetMapping("/product/category/{categoryId}")
    public List<ProductCustomerDto> getAllProductsByCustomer(@RequestParam(value = "page",defaultValue = GlobalVariables.DEFAULT_PAGE_OFFSET)Optional<Integer> page,
                                                       @RequestParam(value = "size",defaultValue = GlobalVariables.DEFAULT_PAGE_SIZE) Optional<Integer> contentSize,
                                                       @RequestParam(value = "sort",defaultValue = GlobalVariables.DEFAULT_SORT_PROPERTY)Optional<String> sortProperty,
                                                       @RequestParam(value = "direction",defaultValue = GlobalVariables.DEFAULT_SORT_DIRECTION)Optional<String> sortDirection,
                                                       @PathVariable(name = "categoryId") Long categoryId,Principal principal){
        return productService.getAllProductsByCustomer(page,contentSize,sortProperty,sortDirection,categoryId,principal);
    }

    //=================================================Get similar products By Customer =========================================================
    @Secured("ROLE_USER")
    @ApiOperation(value = "Get all products by Customer.")
    @GetMapping("/product-similar/{productId}")
    public ResponseEntity getSimilarProductsByCustomer(@RequestParam(value = "page",defaultValue = GlobalVariables.DEFAULT_PAGE_OFFSET)Optional<Integer> page,
                                                   @RequestParam(value = "size",defaultValue = GlobalVariables.DEFAULT_PAGE_SIZE) Optional<Integer> contentSize,
                                                   @RequestParam(value = "sort",defaultValue = GlobalVariables.DEFAULT_SORT_PROPERTY)Optional<String> sortProperty,
                                                   @RequestParam(value = "direction",defaultValue = GlobalVariables.DEFAULT_SORT_DIRECTION)Optional<String> sortDirection,
                                                   @PathVariable(name = "productId") Long productId,Principal principal){
        return productService.getSimilarProductsByCustomer(page,contentSize,sortProperty,sortDirection,productId,principal);
    }

    //=================================================Get Cart details Customer =========================================================
    @Secured("ROLE_USER")
    @ApiOperation(value = "Get Cart details by Customer")
    @GetMapping("/cart")
    public ResponseEntity getCartDetailsByCustomer(Principal principal){
        return cartService.getCartDetailsByCustomer(principal);
    }



    //=================================================Add product in Cart by Customer =========================================================
    @Secured("ROLE_USER")
    @ApiOperation(value = "Add product to Cart by Customer")
    @PostMapping("/cart")
    public ResponseEntity addProductInCartByCustomer(Principal principal,@Valid @RequestBody CartAddProductCo cartAddProductCo){
        return cartService.addProductInCartByCustomer(principal,cartAddProductCo);
    }

    //=================================================Remove a product from Cart by Customer =========================================================
    @Secured("ROLE_USER")
    @ApiOperation(value = "Remove product from Cart by Customer")
    @DeleteMapping("/cart/{productVariationId}")
    public ResponseEntity removeProductFromCartByCustomer(Principal principal,@PathVariable(name = "productVariationId") Long productVariationId){
        return cartService.removeProductFromCartByCustomer(principal,productVariationId);
    }

    //=================================================Add product in wishList by Customer =========================================================
    @Secured("ROLE_USER")
    @ApiOperation(value = "Add product in wishList by Customer")
    @PatchMapping("/cart/addWishList/{productVariationId}")
    public ResponseEntity addProductToWishList(Principal principal,@PathVariable(name = "productVariationId") Long productVariationId){
        return cartService.addProductToWishList(principal,productVariationId);
    }

    //=================================================Remove product from wishList by Customer =========================================================
    @Secured("ROLE_USER")
    @ApiOperation(value = "Remove product from wishList by Customer")
    @PatchMapping("/cart/removeWishList/{productVariationId}")
    public ResponseEntity removeProductFromWishList(Principal principal,@PathVariable(name = "productVariationId") Long productVariationId){
        return cartService.removeProductFromWishList(principal,productVariationId);
    }

    //=================================================Order Place  by Customer =========================================================
    @Secured("ROLE_USER")
    @ApiOperation(value = "Order place by Customer")
    @GetMapping("/cart/order/address/{addressId}")
    public ResponseEntity orderPlaceByCustomer(Principal principal,@PathVariable(name = "addressId")Long addressId,@RequestParam(value = "paymentMethod",required = true) String paymentMethod){
        return orderService.orderPlaceByCustomer(principal,addressId,paymentMethod);
    }





}







