package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.dtos.ProductDto;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.exceptions.ProductDoesNotExistException;
import com.springbootcamp.springsecurity.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;

    public ProductDto getProductByid(long id) {

        if(!productRepository.findById(id).isPresent()){
            throw  new ProductDoesNotExistException("Product not available with given product Id");
        }
        Product product=productRepository.findById(id).get();
        ProductDto productDto=new ProductDto(product.getBrand(),product.getDescription(),
                product.getId(),product.getName(),product.getSeller().getCompanyName(),product.isCancelable(),product.isReturnable());

        return productDto;
    }

    public List<ProductDto> getAllProducts() {
        List<ProductDto> productDtoList=new ArrayList<>();
        Iterable<Product> productIterable=productRepository.findAll();

        productIterable.forEach(product -> productDtoList.add(new ProductDto(product.getBrand(),
                product.getDescription(),product.getId(),
                product.getName(),product.getSeller().getCompanyName(),
                product.isCancelable(),product.isReturnable())));

        return productDtoList;

    }


     public ProductDto addProduct(ProductDto productDto) {
 return  productDto;
    }
}
