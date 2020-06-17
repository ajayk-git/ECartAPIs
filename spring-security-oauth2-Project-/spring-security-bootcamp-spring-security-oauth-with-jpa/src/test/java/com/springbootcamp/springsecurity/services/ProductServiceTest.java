package com.springbootcamp.springsecurity.services;

//import org.junit.jupiter.api.Test;
import com.springbootcamp.springsecurity.dtos.ProductAdminDto;
import com.springbootcamp.springsecurity.dtos.ProductCustomerDto;
import com.springbootcamp.springsecurity.dtos.ProductSellerDto;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.repositories.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@RunWith(value = MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

           @Mock
    AuditLogsMongoDBService auditLogsMongoDBService;

    @InjectMocks
    ProductService productService;
//
//    @Mock
//    Product product;
//
//    @Test
//    void viewProductBySeller() {
//    }
@WithMockUser
    @Test
    public void viewProductByAdmin() {
        Category category=new Category();
        category.setId(1L);

        Product product=new Product();
        product.setCategory(category);
        product.setId(1L);
        product.setName("6S");
        product.setBrand("IPhone");


        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString());

        ProductAdminDto productAdminDto=productService.viewProductByAdmin(1L, new Principal() {
            @Override
            public String getName() {
                return "Ajay";
            }
        });
        assertEquals("6S",productAdminDto.getName());

    }

    @WithMockUser
    @Test
    public void viewProductBySeller() {
        Category category=new Category();
        category.setId(1L);
        Seller seller=new Seller();
        seller.setId(1L);
        seller.setEmail("seller@gmail.com");
        Product product=new Product();
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
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString());

        ProductSellerDto productSellerDto=productService.viewProductBySeller(2L, new Principal() {
            @Override
            public String getName() {
                return "seller@gmail.com";
            }
        });
        assertEquals("6S",productSellerDto.getName());

    }


    @WithMockUser
    @Test
    public void getProductByCustomer() {

        Customer customer=new Customer();
        customer.setEmail("customer@gmail.com");
        Product product=new Product();
        product.setId(1L);
        product.setName("6S");
        product.setBrand("IPhone");
        product.setIsDeleted(false);
        product.setIsActive(true);
        product.setIsCancelable(false);
        product.setIsReturnable(false);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        doNothing().when(auditLogsMongoDBService).readObject(Mockito.anyString(),Mockito.anyLong(),Mockito.anyString());

        ProductCustomerDto productCustomerDto=productService.getProductByCustomer(2L, new Principal() {
            @Override
            public String getName() {
                return "customer@gmail.com";
            }
        });
        assertEquals("6S",productCustomerDto.getName());

    }
}