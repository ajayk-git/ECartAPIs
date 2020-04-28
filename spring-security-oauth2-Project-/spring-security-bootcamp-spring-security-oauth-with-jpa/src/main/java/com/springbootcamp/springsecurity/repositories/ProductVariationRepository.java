package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.product.ProductVariation;
import org.springframework.data.repository.CrudRepository;

public interface ProductVariationRepository extends CrudRepository<ProductVariation,Long> {
}
