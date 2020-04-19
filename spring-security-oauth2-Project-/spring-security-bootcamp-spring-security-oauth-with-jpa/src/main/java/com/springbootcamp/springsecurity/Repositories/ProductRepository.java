package com.springbootcamp.springsecurity.Repositories;

import com.springbootcamp.springsecurity.Entities.Product.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product,Long> {
}
