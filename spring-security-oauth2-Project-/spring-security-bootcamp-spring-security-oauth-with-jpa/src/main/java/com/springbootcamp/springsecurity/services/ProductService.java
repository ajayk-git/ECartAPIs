package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.ProductCo;
import com.springbootcamp.springsecurity.dtos.ProductDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.exceptions.ProductDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import com.springbootcamp.springsecurity.repositories.ProductRepository;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    EmailService emailService;

    public ProductDto getProductByid(long id) {

        if (!productRepository.findById(id).isPresent()) {
            throw new ProductDoesNotExistException("Product not available with given product Id");
        }
        Product product = productRepository.findById(id).get();
        ProductDto productDto = new ProductDto(product.getBrand(), product.getDescription(),
                product.getId(), product.getName(), product.getSeller().getCompanyName(), product.isCancelable(), product.isReturnable());

        return productDto;
    }

    public List<ProductDto> getAllProducts() {
        List<ProductDto> productDtoList = new ArrayList<>();
        Iterable<Product> productIterable = productRepository.findAll();

        productIterable.forEach(product -> productDtoList.add(new ProductDto(product.getBrand(),
                product.getDescription(), product.getId(),
                product.getName(), product.getSeller().getCompanyName(),
                product.isCancelable(), product.isReturnable())));

        return productDtoList;

    }


    @Secured("ROLE_SELLER")
    //===============================Add a new Product by seller================================================================
    public ResponseEntity addNewProduct(ProductCo productCo, Principal principal) {

        String productNameCo = productCo.getProductName();
        String brandNameCo = productCo.getBrandName();
        Long categoryIdCo = productCo.getCategoryId();
        Seller seller = sellerRepository.findByEmail(principal.getName());


        if (productRepository.findByName(productNameCo).isPresent())
            throw new ResourceAlreadyExistException("Product already exist.A product can be sold by single seller.Kindly register different product.  ");


        Optional<Category> category = categoryRepository.findById(categoryIdCo);

        if (!category.isPresent())
            throw new ResourceNotFoundException("Mentioned Category not found.Kindly enter correct category.");
        else {
            if (!category.get().getSubCategory().isEmpty())
                throw new RuntimeException("Mentioned categoryId is not a leaf categoryId.Kindly enter a leaf categoryId ");
        }
        Product product = new Product();
        product.setBrand(brandNameCo);
        product.setName(productNameCo);
        product.setCategory(category.get());
        product.setDescription(productCo.getDescription());
        product.setReturnable(false);
        product.setCancelable(false);
        product.setActive(false);
        product.setSeller(seller);


        emailService.mailNotificationAdminNewProductAdd();
        productRepository.save(product);
        return new ResponseEntity("Product added successfully.Product will  be activated  soon by admin,", HttpStatus.CREATED);

    }
}
