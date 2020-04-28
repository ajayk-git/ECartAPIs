package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart,Long>{


}
