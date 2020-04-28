package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.product.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Long> {
}
