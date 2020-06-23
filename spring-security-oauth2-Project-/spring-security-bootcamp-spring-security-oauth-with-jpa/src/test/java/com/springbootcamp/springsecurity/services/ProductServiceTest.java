package com.springbootcamp.springsecurity.services;

//import org.junit.jupiter.api.Test;

import com.springbootcamp.springsecurity.co.ProductCo;
import com.springbootcamp.springsecurity.co.ProductUpdateBySellerCo;
import com.springbootcamp.springsecurity.co.ProductVariationCo;
import com.springbootcamp.springsecurity.co.ProductVariationUpdateCo;
import com.springbootcamp.springsecurity.dtos.*;
import com.springbootcamp.springsecurity.entities.product.Category;
import com.springbootcamp.springsecurity.entities.product.Product;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.entities.users.Seller;
import com.springbootcamp.springsecurity.exceptions.ResourceAlreadyExistException;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void viewProductByAdminSuccessFullTest() {
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
    public void viewProductBySellerSuccessFullTest() {
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
    public void getProductByCustomerSuccessFullTest() {

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
    public void getProductVariantBySellerSuccessFullTest() {

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

    @Test
    @WithMockUser
    public void addNewProductVariantSuccessFullTest() {

        List<ProductVariation> productVariationList = new ArrayList<>();
        Map<String, String> metaDataTestProduct = new HashMap<>();
        Map<String, String> metaDataTestProductCO = new HashMap<>();

        metaDataTestProduct.put("TestKey", "testValue");
        metaDataTestProductCO.put("testKeyCO", "testValueCO");

        ProductVariation productVariation = new ProductVariation();
        productVariation.setId(1L);
        productVariation.setPrice(123F);
        productVariation.setQuantityAvailable(12);
        productVariation.setIsActive(true);
        productVariation.setMetaData(metaDataTestProduct);

        ProductVariation productVariation1 = new ProductVariation();
        productVariation1.setId(2L);
        productVariation1.setPrice(133F);
        productVariation1.setQuantityAvailable(10);
        productVariation1.setIsActive(true);
        productVariation1.setMetaData(metaDataTestProduct);

        productVariationList.add(productVariation);
        productVariationList.add(productVariation1);

        ProductVariationCo productVariationCo = new ProductVariationCo();
        productVariationCo.setPrice(123F);
        productVariationCo.setQuantityAvailable(1);
        productVariationCo.setMetaData(metaDataTestProductCO);

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setFirstName("sellerTestFirstName");
        seller.setEmail("sellerTest@gmail.com");

        Product product = new Product();
        product.setId(1L);
        product.setBrand("testBrandName");
        product.setName("testProductName");
        product.setDescription("testDescriptionProduct");
        product.setIsReturnable(false);
        product.setIsCancelable(false);
        product.setIsActive(true);
        product.setIsDeleted(false);
        product.setSeller(seller);
        product.setProductVariationList(productVariationList);
        productVariation.setProduct(product);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        Mockito.doNothing().when(auditLogsMongoDBService).saveNewObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
        Mockito.when(productRepository.save(product)).thenReturn(product);

        ResponseEntity responseEntity = productService.addNewProductVariant(product.getId(), productVariationCo, new Principal() {
            @Override
            public String getName() {
                return seller.getEmail();
            }
        });

        assertEquals("Product variation added successfully.", responseEntity.getBody().toString());
    }

    @Test
    @WithMockUser
    public void updateProductVariantBySellerSuccessFullTest() {
        ProductVariationUpdateCo productVariationUpdateCo = new ProductVariationUpdateCo();
        Map<String, String> metaDataTestProductVariationUpdateCO = new HashMap<>();
        metaDataTestProductVariationUpdateCO.put("testKeyCO", "testValueCO");
        productVariationUpdateCo.setIsActive(true);
        productVariationUpdateCo.setPrice(12F);
        productVariationUpdateCo.setQuantityAvailable(1);
        productVariationUpdateCo.setMetaData(metaDataTestProductVariationUpdateCO);

        List<ProductVariation> productVariationList = new ArrayList<>();

        Map<String, String> metaDataTestProductVariation = new HashMap<>();
        metaDataTestProductVariation.put("variationMetaDataKey", "variationMetaDataValue");

        ProductVariation productVariation = new ProductVariation();
        productVariation.setId(1L);
        productVariation.setPrice(123F);
        productVariation.setQuantityAvailable(12);
        productVariation.setIsActive(true);
        productVariation.setMetaData(metaDataTestProductVariation);
        productVariationList.add(productVariation);

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setFirstName("sellerTestFirstName");
        seller.setEmail("sellerTest@gmail.com");

        Product product = new Product();
        product.setId(1L);
        product.setBrand("testBrandName");
        product.setName("testProductName");
        product.setDescription("testDescriptionProduct");
        product.setIsReturnable(false);
        product.setIsCancelable(false);
        product.setIsActive(true);
        product.setIsDeleted(false);
        product.setSeller(seller);
        product.setProductVariationList(productVariationList);
        productVariation.setProduct(product);

        Mockito.when(productVariationRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(productVariation));
        Mockito.doNothing().when(auditLogsMongoDBService).updateObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());
        Mockito.when(productVariationRepository.save(productVariation)).thenReturn(productVariation);

        ResponseEntity responseEntity = productService.updateProductVariantBySeller(productVariation.getId(), new Principal() {
            @Override
            public String getName() {
                return product.getSeller().getEmail();
            }
        }, productVariationUpdateCo);

        assertEquals("Product with productVariantId : " + productVariation.getId() + " is updated successfully.", responseEntity.getBody().toString());
    }

    @Test
    @WithMockUser
    public void updateProductBySellerSuccessFullTest() {

        ProductUpdateBySellerCo productUpdateBySellerCo = new ProductUpdateBySellerCo();
        productUpdateBySellerCo.setBrandName("TestBrandNameProductCO");
        productUpdateBySellerCo.setDescription("testDescriptionProductCO");
        productUpdateBySellerCo.setProductName("testProductNameProductCO");
        productUpdateBySellerCo.setIsReturnable(false);
        productUpdateBySellerCo.setIsCancellable(false);

        Seller seller = new Seller();
        seller.setId(1L);
        seller.setFirstName("sellerTestFirstName");
        seller.setEmail("sellerTest@gmail.com");

        Product product = new Product();
        product.setId(1L);
        product.setBrand("testBrandName");
        product.setName("testProductName");
        product.setDescription("testDescriptionProduct");
        product.setIsReturnable(false);
        product.setIsCancelable(false);
        product.setIsActive(true);
        product.setIsDeleted(false);
        product.setSeller(seller);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.doNothing().when(auditLogsMongoDBService).updateObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ResponseEntity responseEntity = productService.updateProductBySeller(productUpdateBySellerCo, product.getId(), new Principal() {
            @Override
            public String getName() {
                return product.getSeller().getEmail();
            }
        });

        assertEquals("Product with productId : " + product.getId() + " is updated successfully.", responseEntity.getBody().toString());
    }

    @Test
    @WithMockUser
    public void activateProductByAdminSuccessFullTest() {
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setFirstName("sellerTestFirstName");
        seller.setEmail("sellerTest@gmail.com");

        Product product = new Product();
        product.setId(1L);
        product.setBrand("testBrandName");
        product.setName("testProductName");
        product.setDescription("testDescriptionProduct");
        product.setIsReturnable(false);
        product.setIsCancelable(false);
        product.setIsActive(false);
        product.setIsDeleted(false);
        product.setSeller(seller);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.doNothing().when(emailService).mailNotificationSellerProductActivate(Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(auditLogsMongoDBService).activateObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ResponseEntity responseEntity = productService.activateProduct(product.getId(), new Principal() {
            @Override
            public String getName() {
                return "admin@gmail.com";
            }
        });

        assertEquals("Product is activated successfully.Email is triggered to seller.", responseEntity.getBody().toString());
    }

    @Test
    @WithMockUser
    public void deactivateProductByAdminSuccessFullTest() {
        Seller seller = new Seller();
        seller.setId(1L);
        seller.setFirstName("sellerTestFirstName");
        seller.setEmail("sellerTest@gmail.com");

        Product product = new Product();
        product.setId(1L);
        product.setBrand("testBrandName");
        product.setName("testProductName");
        product.setDescription("testDescriptionProduct");
        product.setIsReturnable(false);
        product.setIsCancelable(false);
        product.setIsActive(true);
        product.setIsDeleted(false);
        product.setSeller(seller);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.doNothing().when(emailService).mailNotificationSellerProductDeactivate(Mockito.anyString(), Mockito.any());
        Mockito.doNothing().when(auditLogsMongoDBService).deactivateObject(Mockito.anyString(), Mockito.anyLong(), Mockito.anyString());

        ResponseEntity responseEntity = productService.deactivateProduct(product.getId(), new Principal() {
            @Override
            public String getName() {
                return "admin@gmail.com";
            }
        });

        assertEquals("Product is deactivated successfully.Email is triggered to seller.", responseEntity.getBody().toString());
    }

    @Test
    @WithMockUser
    public void deleteProductBySellerSuccessFullTest() {

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

        Product product = new Product();
        product.setId(1L);
        product.setBrand("testBrandName");
        product.setName("testProductName");
        product.setCategory(category);
        product.setDescription("testDescriptionProduct");
        product.setIsReturnable(false);
        product.setIsCancelable(false);
        product.setIsActive(true);
        product.setIsDeleted(false);
        product.setSeller(seller);

        Product product1 = new Product();
        product1.setId(2L);
        product1.setBrand("testBrandNameProduct1");
        product1.setName("testProductNameProduct1");
        product1.setCategory(category);
        product1.setDescription("testDescriptionProduct1");
        product1.setIsReturnable(false);
        product1.setIsCancelable(false);
        product1.setIsActive(true);
        product1.setIsDeleted(false);
        product1.setSeller(seller);

        Mockito.when(productRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.doNothing().when(emailService).mailNotificationSellerProductDeactivate(Mockito.anyString(), Mockito.any());

        ResponseEntity responseEntity = productService.deleteProductBySeller(2L, new Principal() {
            @Override
            public String getName() {
                return seller.getEmail();
            }
        });

        assertEquals("Product deleted with mentioned productId.", responseEntity.getBody().toString());
    }
}