package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.product.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Long> {
    List<Category> findAll();
    Category findByName(String categoryName);
}
