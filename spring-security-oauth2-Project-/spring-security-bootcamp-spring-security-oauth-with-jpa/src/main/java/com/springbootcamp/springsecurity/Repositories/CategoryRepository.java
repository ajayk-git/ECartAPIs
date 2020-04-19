package com.springbootcamp.springsecurity.Repositories;

import com.springbootcamp.springsecurity.Entities.Product.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Long> {


    Category findByName(String mobile_phones);
}
