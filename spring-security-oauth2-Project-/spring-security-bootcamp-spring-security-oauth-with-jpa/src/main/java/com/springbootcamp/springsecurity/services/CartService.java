package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.CartAddProductCo;
import com.springbootcamp.springsecurity.dtos.CartDetailsDto;
import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.CartRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
import com.springbootcamp.springsecurity.repositories.ProductVariationRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductVariationRepository variationRepository;


    ModelMapper modelMapper = new ModelMapper();


    //=================================================Get Cart details Customer =========================================================

    public ResponseEntity getCartDetailsByCustomer(Principal principal) {
        Customer customer = customerRepository.findByEmail(principal.getName());
        List<Cart> cartList = cartRepository.findByCustomer(customer);

        Type listType = new TypeToken<List<CartDetailsDto>>() {
        }.getType();

        List<CartDetailsDto> cartDetailsDtos = modelMapper.map(cartList, listType);
        return new ResponseEntity(cartDetailsDtos, null, HttpStatus.OK);
    }


    //=================================================Add product to Cart by Customer =========================================================
//
//    public ResponseEntity addProductInCartByCustomer(Principal principal, CartAddProductCo cartAddProductCo) {
//
//        if (!variationRepository.findById(cartAddProductCo.getProductVariationId()).isPresent()) {
//            throw new ResourceNotFoundException("Product variation is not available");
//        }
//
//        ProductVariation productVariation = variationRepository.findById(cartAddProductCo.getProductVariationId()).get();
//
//        if (!productVariation.getIsActive()) {
//            throw new ResourceNotFoundException("Product variation is not active.");
//        }
//
//        if (productVariation.getQuantityAvailable() - cartAddProductCo.getQuantity() < 0) {
//            throw new ResourceNotFoundException("In sufficient stock.");
//        }
//
//        Cart cart=new Cart();
//
//        cart.setIsWishListItem(false);
//        cart.setCustomer(customerRepository.findByEmail(principal.getName()));
//        cart.setQuantity(cartAddProductCo.getQuantity());
//        cart.set
//
//        cartRepository.save(cart);
//
//            return new ResponseEntity("Product added successfully in Cart.", HttpStatus.OK);
//    }
}
