package com.example.AGRIMART.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/**").permitAll() // Allow access to the registration endpoint
                                .anyRequest().authenticated() // All other endpoints require authentication
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF protection (use with caution)

        return http.build();
    }
}
