package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.order.OrderProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository  extends CrudRepository<OrderProduct,Long> {
}
