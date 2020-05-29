package com.springbootcamp.springsecurity.security;


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
        final CustomUserValidation authenticationProvider = new CustomUserValidation();

        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authenticationProvider;
    }

    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder authenticationManagerBuilder) {
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
    }


    private static final String[] AUTH_WHITELIST={
             "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/index/**",
            "/all-categories",
            "/all-users",
            "/all-products",
            "/product-count"
    };



    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/register/customer","/register/seller",
                         "/register/confirm",
                        "/register/forgot-password","/register/confirm-reset",
                        "/register/resend-activation-mail").anonymous()
                .antMatchers("/admin/customer/{id}", "admin/sellers",
                        "admin/seller/{id}","admin/customers",
                        "/admin/activate-account/{id}",
                        "/admin/deactivate-account/{id}",
                        "/admin/metadata-fields","/admin/categories",
                        "/admin/category/{id}",
                        "/admin/metadata-value",
                        "admin/product-activate/{id}",
                        "admin/product-deactivate/{id}").hasAnyRole("ADMIN")

                .antMatchers("/customer/*").hasAnyRole("USER")

                .antMatchers("seller/*").hasAnyRole("SELLER")

                .antMatchers("/logout/").hasAnyRole("SELLER","ADMIN","USER")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}
