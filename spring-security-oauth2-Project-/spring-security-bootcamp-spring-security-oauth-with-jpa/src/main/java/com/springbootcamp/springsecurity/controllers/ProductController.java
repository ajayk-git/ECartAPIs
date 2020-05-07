package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.dtos.ProductDto;
import com.springbootcamp.springsecurity.services.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Api(value = "Product Rest Controller",description = "Operations Related to Products")
@RequestMapping("/Product")
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    //=================================================Get a product =========================================================
    @ApiOperation(value = "Get a product.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/{id}")
    public ProductDto getProductByid(@PathVariable long id,Principal principal) {
        System.out.println("Product Details are :");
        return productService.getProductByid(id,principal);
    }

    //=================================================Get all products =========================================================
    @ApiOperation(value = "Get all products.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/")
    public List<ProductDto>getAllProducts(Principal principal){
        return productService.getAllProducts(principal);
    }



}
