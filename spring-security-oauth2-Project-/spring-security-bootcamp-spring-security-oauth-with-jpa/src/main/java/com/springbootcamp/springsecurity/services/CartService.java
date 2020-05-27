package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.co.CartAddProductCo;
import com.springbootcamp.springsecurity.dtos.CartDetailsDto;
import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.CartProductVariation;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.repositories.CartProductVariationRepository;
import com.springbootcamp.springsecurity.repositories.CartRepository;
import com.springbootcamp.springsecurity.repositories.CustomerRepository;
import com.springbootcamp.springsecurity.repositories.ProductVariationRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;

@Log4j2
@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductVariationRepository variationRepository;

    @Autowired
    CartProductVariationRepository cartProductVariationRepository;


    ModelMapper modelMapper = new ModelMapper();


    //=================================================Get Cart details by Customer =========================================================

    public ResponseEntity getCartDetailsByCustomer(Principal principal) {

        Customer customer = customerRepository.findByEmail(principal.getName());
        Cart cart = cartRepository.findByCustomer(customer);

        List<CartProductVariation> cartProductVariationList = cartProductVariationRepository.findByCart(cart);

        Type listType = new TypeToken<List<CartDetailsDto>>() {
        }.getType();

        List<CartDetailsDto> cartDetailsDtos = modelMapper.map(cartProductVariationList, listType);
        return new ResponseEntity(cartDetailsDtos, null, HttpStatus.OK);
    }


    //=================================================Add product in Cart by Customer =========================================================

    public ResponseEntity addProductInCartByCustomer(Principal principal, CartAddProductCo cartAddProductCo) {

        if (!variationRepository.findById(cartAddProductCo.getProductVariationId()).isPresent()) {
            throw new ResourceNotFoundException("Product variation is not available");
        }

        ProductVariation productVariation = variationRepository.findById(cartAddProductCo.getProductVariationId()).get();

        if (!productVariation.getIsActive()) {
            throw new ResourceNotFoundException("Product variation is not active.");
        }

        if (productVariation.getQuantityAvailable() - cartAddProductCo.getQuantity() < 0) {
            throw new ResourceNotFoundException("In sufficient stock.");
        }
        Customer customer = customerRepository.findByEmail(principal.getName());
        Cart cart = cartRepository.findByCustomer(customer);

        CartProductVariation cartProductVariationFromDataBase = cartProductVariationRepository.findByProductVariantAndCart(productVariation.getId(), customer.getCart().getId());

        System.out.println(cartProductVariationFromDataBase);
        if (cartProductVariationFromDataBase != null) {

            if (productVariation.getQuantityAvailable() - (cartAddProductCo.getQuantity() + cartProductVariationFromDataBase.getQuantity()) < 0) {
                throw new ResourceNotFoundException("In sufficient stock.");
            }

            Integer totalQuantity = cartAddProductCo.getQuantity() + cartProductVariationFromDataBase.getQuantity();

            cartProductVariationFromDataBase.setQuantity(totalQuantity);
            cartProductVariationRepository.save(cartProductVariationFromDataBase);
            return new ResponseEntity("Product added successfully in Cart.", HttpStatus.OK);


        }

        CartProductVariation cartProductVariation = new CartProductVariation();
        cartProductVariation.setQuantity(cartAddProductCo.getQuantity());
        cartProductVariation.setProductVariation(productVariation);
        cartProductVariation.setIsWishListItem(cartAddProductCo.getIsWishListItem());
        cartProductVariation.setCart(cart);

        cartProductVariationRepository.save(cartProductVariation);

        return new ResponseEntity("Product added successfully in Cart.", HttpStatus.OK);
    }


    public ResponseEntity removeProductFromCartByCustomer(Principal principal, Long productVariationId) {

        Customer customer = customerRepository.findByEmail(principal.getName());
        Cart cart = cartRepository.findByCustomer(customer);

        CartProductVariation cartProductVariationFromDataBase = cartProductVariationRepository.findByProductVariantAndCart(productVariationId, customer.getCart().getId());

        if (cartProductVariationFromDataBase==null){
            throw new ResourceNotFoundException("Product variation is not available");
        }

        log.warn("deleting the object");
        cartProductVariationRepository.deleteByCartIdAndProductVariationId(customer.getCart().getId(),productVariationId);
        return new ResponseEntity("Product removed from cart.",null,HttpStatus.OK);
    }
}
