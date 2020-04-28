package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.users.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {

    User findByEmail(String email);

}
