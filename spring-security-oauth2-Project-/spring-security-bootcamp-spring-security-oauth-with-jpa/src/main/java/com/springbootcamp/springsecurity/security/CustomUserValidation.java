package com.springbootcamp.springsecurity.security;

import com.springbootcamp.springsecurity.entities.users.User;
import com.springbootcamp.springsecurity.repositories.UserRepository;
import com.springbootcamp.springsecurity.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomUserValidation extends DaoAuthenticationProvider {


        @Autowired
        PasswordEncoder passwordEncoder;
        @Autowired
        UserRepository userRepository;
        @Autowired
        EmailService emailService;

        @Override
        protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken
                authentication) throws AuthenticationException {

            if (authentication.getCredentials() == null){
                throw new BadCredentialsException("Username and password are wrong");
            }
            String currentPassword = authentication.getCredentials().toString();
            if (!passwordEncoder.matches(currentPassword,userDetails.getPassword())){
                User user = (User) userDetails;
                Integer temp = user.getFalseAttemptCount();
                user.setFalseAttemptCount(++temp);
                if (temp == 3){
                    user.setIsAccountNonLocked(false);
                    emailService.sendAccountLockedMail(user.getEmail());
                }
                userRepository.save(user);


                throw new BadCredentialsException("Wrong Password");
            }
        }

        @Override
        protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                             UserDetails user) {
            User user1 = (User) user;
            user1.setFalseAttemptCount(0);
            userRepository.save(user1);
            return super.createSuccessAuthentication(principal,authentication,user);
        }
    }

