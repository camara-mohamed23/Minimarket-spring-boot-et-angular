package com.example.demo.config;

import com.example.demo.auth.Role;
import com.example.demo.auth.User;
import com.example.demo.auth.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    public DataInitializer(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo; this.encoder = encoder;
    }

    @Bean
    CommandLineRunner seedAdmin() {
        return args -> {
            if (!userRepo.existsByEmail("admin@mini.local")) {
                User admin = new User();
                admin.setFullName("Admin");
                admin.setEmail("admin@mini.local");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Role.ROLE_ADMIN);
                userRepo.save(admin);
            }
        };
    }
}
