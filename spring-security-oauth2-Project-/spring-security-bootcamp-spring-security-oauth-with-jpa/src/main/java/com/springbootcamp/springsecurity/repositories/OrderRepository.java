package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.order.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long> {
}
