package com.codeaddiction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

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

    /**
     * InMemoryUserDetailsManager is the implementation of UserDetailsService interface
     * UserDetailsMaage is child interface of UserDetailsService which contains user management related methods
     * While UserDetailsService have loadUserByUsername() Single abstract method to loan used from persistence db.
     * There are some other implementation are JdbcUserDetailManage ...etc.
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}EazyBytes@12345").authorities("read").build();
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$12$88.f6upbBvy0okEa7OfHFuorV29qeK.sVbB9VQ6J6dWM1bW6Qef8m")
                .authorities("admin").build();
        return new InMemoryUserDetailsManager(user, admin);

    }

    /**
     * PasswordEncoderFactories contains a hashmap to provide password encoder, default is bcrypt
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * From Spring Security 6.3 version
     * CompromisedPasswordChecker class added in Security 6.3 to check password is strong or not.
     * If I set password as 12345 or salman, it won't be allowed to set it
     * @return
     */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
