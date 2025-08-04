package com.codeaddiction.config;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //permitAll() Allowed all request without authentication
        //http.authorizeHttpRequests((requests) -> requests.anyRequest().permitAll());

        //denyAll() deny all request either authentication user or unauthenticated.
        //http.authorizeHttpRequests((requests) -> requests.anyRequest().denyAll());


        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/notices", "/contact", "/error").permitAll());
        http.formLogin(withDefaults());
       // http.httpBasic(withDefaults());
        return http.build();
    }
}
