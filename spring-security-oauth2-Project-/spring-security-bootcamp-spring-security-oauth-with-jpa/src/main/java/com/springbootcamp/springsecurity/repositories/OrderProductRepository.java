package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.order.OrderProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository  extends CrudRepository<OrderProduct,Long> {

    @Query(value = "Select * from OrderProduct where ORDER_ID=:orderId",nativeQuery = true)
    List<OrderProduct> findByOrderId(Long orderId);
}
