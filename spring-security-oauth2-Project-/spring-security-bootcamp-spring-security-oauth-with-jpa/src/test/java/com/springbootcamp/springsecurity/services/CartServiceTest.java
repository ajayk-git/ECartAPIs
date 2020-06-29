package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.dtos.CartDetailsDto;
import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.CartProductVariation;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.repositories.CartProductVariationRepository;
import com.springbootcamp.springsecurity.repositories.CartRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(value = MockitoJUnitRunner.class)
public class CartServiceTest {

    @Mock
    CartRepository cartRepository;
    @Mock
    CartProductVariationRepository cartProductVariationRepository;
    @Mock
    CustomerRepository customerRepository;
    @InjectMocks
    CartService cartService;


    @Test
    @WithMockUser
    public void getCartDetailsByCustomerSuccessFullTest() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");

        Map<String, String> metaDataTestProduct = new HashMap<>();
        metaDataTestProduct.put("TestKey", "testValue");
        ProductVariation productVariation = new ProductVariation();
        productVariation.setId(1L);
        productVariation.setPrice(123F);
        productVariation.setQuantityAvailable(12);
        productVariation.setIsActive(true);
        productVariation.setMetaData(metaDataTestProduct);


        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);

        List<CartProductVariation> cartProductVariationList = new ArrayList<>();
        CartProductVariation cartProductVariation = new CartProductVariation();
        cartProductVariation.setCart(cart);
        cartProductVariation.setProductVariation(productVariation);
        cartProductVariation.setIsWishListItem(true);
        cartProductVariation.setQuantity(1);
        cartProductVariationList.add(cartProductVariation);

        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(cartRepository.findByCustomer(Mockito.any())).thenReturn(cart);
        Mockito.when(cartProductVariationRepository.findByCart(cart)).thenReturn(cartProductVariationList);

        ResponseEntity<CartDetailsDto> cartDetailsDtoResponseEntity = cartService.getCartDetailsByCustomer(new Principal() {
            @Override
            public String getName() {
                return customer.getEmail();
            }
        });

        assertEquals(HttpStatus.OK, cartDetailsDtoResponseEntity.getStatusCode());
    }

    @Test
    public void addProductInCartByCustomer() {
    }

    @Test
    @WithMockUser
    public void removeProductFromCartByCustomerSuccessFullTest() {

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("customer@gmail.com");

        Map<String, String> metaDataTestProduct = new HashMap<>();
        metaDataTestProduct.put("TestKey", "testValue");
        ProductVariation productVariation = new ProductVariation();
        productVariation.setId(1L);
        productVariation.setPrice(123F);
        productVariation.setQuantityAvailable(12);
        productVariation.setIsActive(true);
        productVariation.setMetaData(metaDataTestProduct);


        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);

        CartProductVariation cartProductVariation = new CartProductVariation();
        cartProductVariation.setCart(cart);
        cartProductVariation.setProductVariation(productVariation);
        cartProductVariation.setIsWishListItem(true);
        cartProductVariation.setQuantity(1);
        cartProductVariation.setId(1L);

        Mockito.when(customerRepository.findByEmail(Mockito.anyString())).thenReturn(customer);
        Mockito.when(cartRepository.findByCustomer(Mockito.any())).thenReturn(cart);
        Mockito.when(cartProductVariationRepository.findByProductVariantAndCart(1L,1L)).thenReturn(cartProductVariation);
        Mockito.doNothing().when(cartProductVariationRepository).deleteByCartIdAndProductVariationId(Mockito.anyLong(), Mockito.anyLong());

        ResponseEntity responseEntity = cartService.removeProductFromCartByCustomer(new Principal() {
            @Override
            public String getName() {
                return customer.getEmail();
            }
        }, 1L);

        assertEquals("Product removed from cart.", responseEntity.getBody().toString());

    }

    @Test
    public void addProductToWishList() {
    }

    @Test
    public void removeProductFromWishList() {
    }
}