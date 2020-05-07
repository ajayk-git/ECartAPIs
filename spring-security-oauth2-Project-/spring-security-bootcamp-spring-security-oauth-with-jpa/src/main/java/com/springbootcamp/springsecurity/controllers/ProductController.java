package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.dtos.ProductDto;
import com.springbootcamp.springsecurity.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/Product")
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ProductDto getProductByid(@PathVariable long id,Principal principal) {
        System.out.println("Product Details are :");
        return productService.getProductByid(id,principal);
    }

    @GetMapping("/")
    public List<ProductDto>getAllProducts(Principal principal){
        return productService.getAllProducts(principal);
    }



}
