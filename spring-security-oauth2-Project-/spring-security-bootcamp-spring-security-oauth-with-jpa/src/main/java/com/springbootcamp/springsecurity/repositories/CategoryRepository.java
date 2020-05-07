package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Long> {
    List<Category> findAll();

    Optional<Category> findByName(String categoryName);

    @Query(value = "select c.categoryId,c.NAME,c.parent_Id from Category c where c.parent_Id IS NULL",nativeQuery = true)
    List<Category> findByRootCategory();

    Optional<Category> findById(Long categoryId);


}

