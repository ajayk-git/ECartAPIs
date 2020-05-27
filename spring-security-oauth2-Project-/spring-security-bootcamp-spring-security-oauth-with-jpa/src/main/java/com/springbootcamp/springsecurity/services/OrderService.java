package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.entities.CartProductVariation;
import com.springbootcamp.springsecurity.repositories.CartProductVariationRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CartProductVariationRepository cartProductVariationRepository;

    List<CartProductVariation> getWishListProducts(){
        return cartProductVariationRepository.findByIsWishListItem();
    }





}
