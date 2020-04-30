package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.product.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ProductRepository extends CrudRepository<Product,Long> {


    Optional<Product> findByName(String productNameCo);
}
