package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.Cart;
import com.springbootcamp.springsecurity.entities.CartProductVariation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductVariationRepository extends CrudRepository<CartProductVariation,Long> {

    List<CartProductVariation> findByCart(Cart cart);
}
