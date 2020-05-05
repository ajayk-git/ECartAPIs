package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {


    Optional<Product> findByName(String productNameCo);

}
