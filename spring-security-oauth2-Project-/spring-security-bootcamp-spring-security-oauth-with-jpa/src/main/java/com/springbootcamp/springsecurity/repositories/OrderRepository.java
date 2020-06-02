package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.order.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface OrderRepository extends CrudRepository<Order,Long> {


    @Transactional
    @Modifying
    @Query(value = "delete from ORDERS where id=:orderId AND amountPaid=0",nativeQuery = true)
    void deleteByIdAndAmountPaid(@Param(value = "orderId") Long orderId);
}
