package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.users.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {
     Customer findByEmail(String email);

}
