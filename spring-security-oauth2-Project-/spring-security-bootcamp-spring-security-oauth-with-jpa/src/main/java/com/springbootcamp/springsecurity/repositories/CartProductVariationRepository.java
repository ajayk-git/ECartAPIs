package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.CartProductVariation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductVariationRepository extends CrudRepository<CartProductVariation,Long> {
}
