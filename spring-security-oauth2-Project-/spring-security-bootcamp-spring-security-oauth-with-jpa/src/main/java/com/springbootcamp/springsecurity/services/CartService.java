package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.dtos.CartDetailsDto;
import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.repositories.CartRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
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

    ModelMapper modelMapper=new ModelMapper();


    public ResponseEntity getCartDetailsByCustomer(Principal principal){
     Customer customer=customerRepository.findByEmail(principal.getName());
     List<Cart> cartList=cartRepository.findByCustomer(customer);

        Type listType = new TypeToken<List<CartDetailsDto>>(){}.getType();

        List<CartDetailsDto> cartDetailsDtos = modelMapper.map(cartList,listType);
     return new ResponseEntity(cartDetailsDtos,null, HttpStatus.OK);
    }
}
