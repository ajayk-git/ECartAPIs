package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.users.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart,Long>{


    Cart findByCustomer(Customer customer);
}
