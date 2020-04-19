package com.springbootcamp.springsecurity.Repositories;

import com.springbootcamp.springsecurity.Entities.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart,Long>{


}
