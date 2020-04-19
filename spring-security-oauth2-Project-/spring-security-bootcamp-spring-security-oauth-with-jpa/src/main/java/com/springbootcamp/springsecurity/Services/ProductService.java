package com.springbootcamp.springsecurity.Services;

import com.springbootcamp.springsecurity.DTOs.ProductDto;
import com.springbootcamp.springsecurity.Entities.Product.Product;
import com.springbootcamp.springsecurity.Exceptions.ProductDoesNotExistException;
import com.springbootcamp.springsecurity.Repositories.ProductRepository;
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
