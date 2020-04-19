package com.springbootcamp.springsecurity;

import com.springbootcamp.springsecurity.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Dummy {

    @Autowired
    UserRepository userRepository;

    long getCount(){
        return userRepository.count();
    }
}
