package com.springbootcamp.springsecurity.Repositories;

import com.springbootcamp.springsecurity.Entities.Product.ProductVariation;
import org.springframework.data.repository.CrudRepository;

public interface ProductVariationRepository extends CrudRepository<ProductVariation,Long> {
}
