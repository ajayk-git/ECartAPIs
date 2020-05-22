package com.springbootcamp.springsecurity.repositories;

import com.springbootcamp.springsecurity.entities.users.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByEmail(String email);

    @Query(value = "select * from User where IsActive=0",nativeQuery = true)
    List<User> findByIsNotActive();
}
