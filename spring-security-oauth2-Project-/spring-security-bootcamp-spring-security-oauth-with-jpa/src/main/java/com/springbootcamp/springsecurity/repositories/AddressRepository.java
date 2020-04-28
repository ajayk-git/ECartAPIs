package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository <Address,Long> {
}
