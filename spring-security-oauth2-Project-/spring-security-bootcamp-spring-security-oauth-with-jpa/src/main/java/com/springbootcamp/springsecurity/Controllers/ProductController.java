package com.springbootcamp.springsecurity.Controllers;


import com.springbootcamp.springsecurity.DTOs.ProductDto;
import com.springbootcamp.springsecurity.DTOs.SellerDto;
import com.springbootcamp.springsecurity.Entities.Product.Product;
import com.springbootcamp.springsecurity.Services.ProductService;
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

    @PostMapping("add")
    public ProductDto addProduct(@RequestBody ProductDto productDto){
        return productService.addProduct(productDto);
    }


}
