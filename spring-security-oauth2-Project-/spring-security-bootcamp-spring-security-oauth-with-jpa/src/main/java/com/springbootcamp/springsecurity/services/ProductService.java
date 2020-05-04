package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.ProductCo;
import com.springbootcamp.springsecurity.co.ProductVariationCo;
import com.springbootcamp.springsecurity.dtos.ProductDto;
import com.springbootcamp.springsecurity.dtos.ProductVariantDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.exceptions.ProductDoesNotExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import com.springbootcamp.springsecurity.repositories.ProductRepository;
import com.springbootcamp.springsecurity.repositories.ProductVariationRepository;
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
    ProductVariationRepository variationRepository;
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
        product.setDeleted(false);
        product.setSeller(seller);


        emailService.mailNotificationAdminNewProductAdd();
        productRepository.save(product);
        return new ResponseEntity("Product added successfully.Product will  be activated  soon by admin,", HttpStatus.CREATED);

    }

    //===========================================to activate product ===========================================================

    public ResponseEntity activateProduct(Long productId) {
        if(!productRepository.findById(productId).isPresent()){
            throw new ResourceNotFoundException("Product is not found with mentioned productId.Please enter existing productId.");
        }

        else {
            Product product = productRepository.findById(productId).get();
            if (product.isActive())
                throw new RuntimeException("Product with mentioned id is already activated.");

            product.setActive(true);
            String email=product.getSeller().getEmail();
            emailService.mailNotificationSellerProductActivate(email,product);
            productRepository.save(product);

        }
        return new ResponseEntity("Product is activated successfully.Email is triggered to seller.",HttpStatus.OK);
    }

    //===========================================to deactivate product ===========================================================


    public ResponseEntity deactivateProduct(Long productId) {
        if(!productRepository.findById(productId).isPresent()){
            throw new ResourceNotFoundException("Product is not found with mentioned productId.Please enter existing productId.");
        }
        else {
            Product product = productRepository.findById(productId).get();
            if (!product.isActive())
                throw new RuntimeException("Product with mentioned id is already deactivated.");
            product.setActive(false);
            String email=product.getSeller().getEmail();
            emailService.mailNotificationSellerProductDeactivate(email,product);
            productRepository.save(product);
        }
        return new ResponseEntity("Product is deactivated successfully.Email is triggered to seller.",HttpStatus.OK);
    }


    //=================================================View  Product-Variant By seller Account==================================


    public ProductVariantDto getProductVariant(Long productVariantId) {

        if(!variationRepository.findById(productVariantId).isPresent())
            throw  new ResourceNotFoundException("Product Variant is not found with mentioned productVariantId.Please enter existing productVariantId.");
        ProductVariation productVariation=variationRepository.findById(productVariantId).get();

        ProductVariantDto productVariantDto=new ProductVariantDto();
        productVariantDto.setProductVariantId(productVariation.getId());
        productVariantDto.setProductId(productVariation.getProduct().getId());
        productVariantDto.setBrand(productVariation.getProduct().getBrand());
        productVariantDto.setProductName(productVariation.getProduct().getName());
        productVariantDto.setMetaData(productVariation.getMetaData());
        productVariantDto.setPrice(productVariation.getPrice());
        productVariantDto.setQuantityAvailable(productVariation.getQuantityAvailable());
        productVariantDto.setActive(productVariation.isActive());
        return productVariantDto;

    }


    //=================================================Add  new Product-Variant By seller Account==================================

}
