package com.springbootcamp.springsecurity.Repositories;

import com.springbootcamp.springsecurity.Entities.Users.Seller;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepository extends CrudRepository<Seller,Long> {
    Seller findByEmail(String email);

    Optional<Seller> findById(Long id);
}
