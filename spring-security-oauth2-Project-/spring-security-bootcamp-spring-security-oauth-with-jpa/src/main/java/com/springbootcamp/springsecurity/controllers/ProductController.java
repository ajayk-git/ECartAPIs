package com.springbootcamp.springsecurity.controllers;


import com.springbootcamp.springsecurity.dtos.ProductDto;
import com.springbootcamp.springsecurity.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/Product")
@RestController
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/{id}")
    public ProductDto getProductByid(@PathVariable long id) {
        System.out.println("Product Details are :");
        return productService.getProductByid(id);
    }

    @GetMapping("/")
    public List<ProductDto>getAllProducts(){
        return productService.getAllProducts();
    }



}
