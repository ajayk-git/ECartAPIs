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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        Float totalAmountPaid = 0f;

        Order order = new Order();
        order.setCustomer(customer);
        order.setAddresscopy(addresscopy);
        order.setPaymentMethod(paymentMethod);
        Order savedOrder = orderRepository.save(order);

        List<OrderProduct> orderProductList = new ArrayList<>();

        List<CartProductVariation> cartProductVariationFromDataBaseList = cartProductVariationRepository.findByCartIdAndWislIstProducts(customer.getCart().getId());

        if (!cartProductVariationFromDataBaseList.isEmpty()) {


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
                totalAmountPaid = totalAmountPaid + (currentCartProduct.getProductVariation().getPrice() * currentCartProduct.getQuantity());
                orderProduct.setOrder(savedOrder);
                orderProductRepository.save(orderProduct);
            }

            if (totalAmountPaid > 0) {
                savedOrder.setAmountPaid(totalAmountPaid);
                orderRepository.save(savedOrder);
                rabbitTemplate.convertAndSend(RabbitMqConfig.topicExchangeName,"routing_Key","OrderPlaced by customer");

            }

            return new ResponseEntity("Order Placed successfully", null, HttpStatus.OK);

        }

        else
            throw new ResourceNotFoundException("No product is in wishList please add to place order");

    }
}
