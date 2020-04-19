package com.springbootcamp.springsecurity.Repositories;

import com.springbootcamp.springsecurity.Entities.Order.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order,Long> {
}
