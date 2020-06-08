package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken,Long> {


    ConfirmationToken findByToken(String confirmationToken);



    ConfirmationToken findByUserId(Long id);
}
