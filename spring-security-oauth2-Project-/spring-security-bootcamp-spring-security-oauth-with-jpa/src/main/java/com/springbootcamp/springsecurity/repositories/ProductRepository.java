package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product,Long> {


    Optional<Product> findByName(String productNameCo);

    @Query(value = "select * from Product where CATEGORY_ID =:categoryId",nativeQuery = true)
    List<Product> findByCategory(@Param("categoryId") Long categoryId, Pageable pageable);


    @Query(value ="select * from Product where IsActive=0",nativeQuery = true)
    List<Product> findIsNotActiveProduct();
}
