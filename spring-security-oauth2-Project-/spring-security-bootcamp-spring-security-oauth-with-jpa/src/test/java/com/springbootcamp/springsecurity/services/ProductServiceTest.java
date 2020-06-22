package com.springbootcamp.springsecurity.services;

//import org.junit.jupiter.api.Test;

import com.springbootcamp.springsecurity.co.ProductCo;
import com.springbootcamp.springsecurity.dtos.*;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.repositories.CategoryRepository;
import com.springbootcamp.springsecurity.repositories.ProductRepository;
import com.springbootcamp.springsecurity.repositories.ProductVariationRepository;
import com.springbootcamp.springsecurity.repositories.SellerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@RunWith(value = MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;
    @Mock
    SellerRepository sellerRepository;
    @Mock
    ProductVariationRepository productVariationRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    EmailService emailService;
    @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;


    @InjectMocks
    ProductService productService;

    @WithMockUser
    @Test
    public void viewProductByAdmin() {
        Category category = new Category();
        category.setId(1L);

        Product product = new Product();
        product.setCategory(category);
        product.setId(1L);
        product.setName("6S");
        product.setBrand("IPhone");


        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ProductAdminDto productAdminDto = productService.viewProductByAdmin(1L, new Principal() {
            @Override
            public String getName() {
                return "Ajay";
            }
        });
        assertEquals("6S", productAdminDto.getName());

    }

    @WithMockUser
    @Test
    public void viewProductBySeller() {
        Category category = new Category();
        category.setId(1L);
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setEmail("seller@gmail.com");
        Product product = new Product();
        product.setCategory(category);
        product.setId(1L);
        product.setName("6S");
        product.setBrand("IPhone");
        product.setSeller(seller);
        product.setIsDeleted(false);
        product.setIsActive(true);
        product.setIsCancelable(false);
        product.setIsReturnable(false);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ProductSellerDto productSellerDto = productService.viewProductBySeller(2L, new Principal() {
            @Override
            public String getName() {
                return "seller@gmail.com";
            }
        });
        assertEquals("6S", productSellerDto.getName());

    }


    @WithMockUser
    @Test
    public void getProductByCustomer() {

        Customer customer = new Customer();
        customer.setEmail("customer@gmail.com");
        Product product = new Product();
        product.setId(2L);
        product.setName("6S");
        product.setBrand("IPhone");
        product.setIsDeleted(false);
        product.setIsActive(true);
        product.setIsCancelable(false);
        product.setIsReturnable(false);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ProductCustomerDto productCustomerDto = productService.getProductByCustomer(2L, new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });
        assertEquals("6S", productCustomerDto.getName());

    }

    @WithMockUser
    @Test
    public void getProductVariantBySeller() {

        ProductVariation productVariation = new ProductVariation();
        Product product = new Product();
        product.setId(1L);
        product.setBrand("Nokia");
        productVariation.setIsActive(true);
        productVariation.setId(2L);
        productVariation.setProduct(product);
        productVariation.setPrice(234F);
        productVariation.setQuantityAvailable(10);

        Mockito.when(productVariationRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(productVariation));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ProductVariantDto productVariantDto = productService.getProductVariantBySeller(2l, new Principal() {
            @Override
            public String getName() {
                return "Seller@gmail.com";
            }
        });
        assertEquals(2L, productVariantDto.getProductVariantId());
    }

    @Test
    @WithMockUser
    public void addNewProductBySellerSuccessFullTest() {

        ProductCo productCo = new ProductCo();
        productCo.setBrandName("TestBrandNameProductCO");
        productCo.setCancellable(false);
        productCo.setDescription("testDescriptionProductCO");
        productCo.setReturnable(false);
        productCo.setProductName("testProductNameProductCO");
        productCo.setCategoryId(1L);

        Product product = new Product();
        Category category = new Category();
        Category subCategory = new Category();
        subCategory.setId(2L);
        subCategory.setParentCategory(category);
        List<Category> categoryList = new ArrayList<>();
        category.addCategory(subCategory);
        category.setId(1L);
        category.setName("testCategoryNameProduct");
        category.setSubCategory(categoryList);


        Seller seller = new Seller();
        seller.setId(1L);
        seller.setFirstName("sellerTestFirstName");
        seller.setEmail("sellerTest@gmail.com");

        product.setId(1L);
        product.setBrand("testBrandName");
        product.setName("testProductName");
        product.setCategory(category);
        product.setDescription("testDescriptionProduct");
        product.setIsReturnable(false);
        product.setIsCancelable(false);
        product.setIsActive(false);
        product.setIsDeleted(false);
        product.setSeller(seller);


        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(java.util.Optional.of(category));
        Mockito.when(sellerRepository.findByEmail(Mockito.anyString())).thenReturn(seller);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.doNothing().when(emailService).mailNotificationAdminNewProductAdd();
        Mockito.doNothing().when(auditLogsMongoDBService).saveNewObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ResponseEntity responseEntity = productService.addNewProduct(productCo, new Principal() {
            @Override
            public String getName() {
                return seller.getEmail();
            }
        });

        assertEquals("Product added successfully.Product will  be activated  soon by admin,", responseEntity.getBody().toString());
    }
}