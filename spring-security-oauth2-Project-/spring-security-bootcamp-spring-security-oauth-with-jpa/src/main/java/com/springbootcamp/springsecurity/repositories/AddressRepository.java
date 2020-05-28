package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.Address;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository <Address,Long> {

    @Query(value = "select * from Address where user_id=:customerId AND id=:addressId",nativeQuery =true)
    Address findByIdAndCustomerId(Long addressId, Long customerId);
}
