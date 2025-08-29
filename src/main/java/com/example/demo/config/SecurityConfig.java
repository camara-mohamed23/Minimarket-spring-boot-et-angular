package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.auth.UserRepository;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepo;
    private final JwtFilter jwt;
    private final CorsConfig cors;

    public SecurityConfig(UserRepository userRepo, JwtFilter jwt, CorsConfig cors) {
        this.userRepo = userRepo;
        this.jwt = jwt;
        this.cors = cors;
    }

    @Bean
    public UserDetailsService uds() {
        return username -> userRepo.findByEmail(username).orElseThrow();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(reg -> reg
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**", "/api/categories/**").permitAll()
                        .anyRequest().authenticated())
                .cors(c -> c.configurationSource(cors.corsConfigurationSource()))
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
