package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.product.ProductReview;
import org.springframework.data.repository.CrudRepository;

public interface ProductReviewRepository extends CrudRepository<ProductReview,Long> {
}
