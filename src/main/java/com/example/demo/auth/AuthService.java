package com.example.demo.auth;

import com.example.demo.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder, JwtUtil jwt, AuthenticationManager authManager) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.jwt = jwt;
        this.authManager = authManager;
    }

    public String register(String fullName, String email, String rawPassword) {
        if (userRepo.existsByEmail(email)) throw new RuntimeException("Email déjà utilisé");
        User u = new User();
        u.setFullName(fullName);
        u.setEmail(email);
        u.setPassword(encoder.encode(rawPassword));
        u.setRole(Role.ROLE_CUSTOMER);
        userRepo.save(u);
        return jwt.generate(u.getEmail(), Map.of("role", u.getRole().name(), "uid", u.getId()));
    }

    public String login(String email, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User u = userRepo.findByEmail(email).orElseThrow();
        return jwt.generate(u.getEmail(), Map.of("role", u.getRole().name(), "uid", u.getId()));
    }
}
