package com.springbootcamp.springsecurity.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    AppUserDetailsService userDetailsService;

    public ResourceServerConfiguration() {
        super();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/customer/registration","/seller/registration",
                        "/seller/registrationConfirm", "/customer/registrationConfirm",
                        "/customer/forgotPassword","/customer/confirmReset",
                        "/seller/forgotPassword","seller/confirmReset").anonymous()

                .antMatchers("/customer/","/customer/{id}", "/seller/","/seller/{id}",
                        "/customer/activateAccount/{id}","/seller/activateAccount/{id}",
                        "customer/deactivateAccount/{id}","seller/deactivateAccount/{id}").hasAnyRole("ADMIN")

                .antMatchers("/customer/viewProfile","/customer/updateProfile","/customer/updateCustomerPassword",
                        "/address/delete/{id}","/address/addCustomerAddress",
                        "address/updateCustomerAddress/{id}").hasAnyRole("USER")

                .antMatchers("seller/updateSellerPassword","/seller/viewProfile",
                       "seller/updateProfile", "address/updateSellerAddress/{id}").hasAnyRole("SELLER")

                .antMatchers("/logout/").hasAnyRole("SELLER","ADMIN","USER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}