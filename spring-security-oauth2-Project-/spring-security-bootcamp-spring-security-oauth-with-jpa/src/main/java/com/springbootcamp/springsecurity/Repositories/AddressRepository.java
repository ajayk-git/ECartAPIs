package com.springbootcamp.springsecurity.Repositories;

import com.springbootcamp.springsecurity.Entities.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository <Address,Long> {
}
