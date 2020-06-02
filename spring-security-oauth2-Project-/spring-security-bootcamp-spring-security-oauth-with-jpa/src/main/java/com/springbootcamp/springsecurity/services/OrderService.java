package com.springbootcamp.springsecurity.services;

import com.springbootcamp.springsecurity.entities.users.Address;
import com.springbootcamp.springsecurity.entities.Addresscopy;
import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.CartProductVariation;
import com.springbootcamp.springsecurity.entities.order.Order;
import com.springbootcamp.springsecurity.entities.order.OrderProduct;
import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import com.springbootcamp.springsecurity.entities.users.Customer;
import com.springbootcamp.springsecurity.exceptions.ResourceNotFoundException;
import com.springbootcamp.springsecurity.rabbitMqConfigurations.RabbitMqConfig;
import com.springbootcamp.springsecurity.repositories.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Log4j2
@Service
public class OrderService {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    ProductVariationRepository productVariationRepository;

    @Autowired
    CartProductVariationRepository cartProductVariationRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;


    List<CartProductVariation> getWishListProducts() {
        return cartProductVariationRepository.findByIsWishListItem();
    }

    //=================================================Order Place  by Customer =========================================================
    public ResponseEntity orderPlaceByCustomer(Principal principal, Long addressId, String paymentMethod) {
        Customer customer = customerRepository.findByEmail(principal.getName());
        Address address = addressRepository.findByIdAndCustomerId(addressId, customer.getId());
        if (address == null)
            throw new ResourceNotFoundException("Address does not exist with customer.");
        Addresscopy addresscopy = new Addresscopy(address);
        Cart cart = cartRepository.findByCustomer(customer);

        List<CartProductVariation> cartProductVariationFromDataBaseList = cartProductVariationRepository.findByCartIdAndWishListProducts(customer.getCart().getId());
        if (!cartProductVariationFromDataBaseList.isEmpty()) {
            Float totalAmountPaid = 0F;
            Order order = new Order();
            order.setCustomer(customer);
            order.setAddresscopy(addresscopy);
            order.setPaymentMethod(paymentMethod);
            Order savedOrder = orderRepository.save(order);
            Iterator<CartProductVariation> cartProductVariationIterator = cartProductVariationFromDataBaseList.iterator();
            while (cartProductVariationIterator.hasNext()) {
                OrderProduct orderProduct = new OrderProduct();
                CartProductVariation currentCartProduct = cartProductVariationIterator.next();
                if ((!currentCartProduct.getProductVariation().getProduct().getIsActive()) || (!currentCartProduct.getProductVariation().getIsActive())) {
                    throw new ResourceNotFoundException("Product(Product Variation) is not active.");
                }
                if (currentCartProduct.getProductVariation().getQuantityAvailable() - currentCartProduct.getQuantity() < 0) {
                    throw new ResourceNotFoundException("Insufficient Stock for productId :" + currentCartProduct.getProductVariation().getId());
                }
                orderProduct.setProductVariation(currentCartProduct.getProductVariation());
                Integer stockLeft = currentCartProduct.getProductVariation().getQuantityAvailable() - currentCartProduct.getQuantity();
                orderProduct.setQuantity(currentCartProduct.getQuantity());
                ProductVariation productVariation = currentCartProduct.getProductVariation();
                productVariation.setQuantityAvailable(stockLeft);
                productVariationRepository.save(productVariation);
                orderProduct.setMetadata(currentCartProduct.getProductVariation().getMetaData().toString());
                orderProduct.setPrice(currentCartProduct.getProductVariation().getPrice());
                totalAmountPaid += (currentCartProduct.getProductVariation().getPrice() * currentCartProduct.getQuantity());
                orderProduct.setOrder(savedOrder);
                cartProductVariationRepository.deleteByCartProductVariation(cart.getId(), productVariation.getId());
                orderProductRepository.save(orderProduct);
            }
            if (totalAmountPaid > 0F) {
                savedOrder.setAmountPaid(totalAmountPaid);
                orderRepository.save(savedOrder);
                rabbitTemplate.convertAndSend(RabbitMqConfig.topicExchangeName, "routing_Key", savedOrder.getId());
            } else {
                log.info("Inside amount lss than 0");
                orderRepository.deleteByIdAndAmountPaid(savedOrder.getId());
            }
        } else {
            throw new ResourceNotFoundException("No product is in wishList please add to place order");
        }
        return new ResponseEntity("Order Placed successfully", null, HttpStatus.OK);
    }
}
