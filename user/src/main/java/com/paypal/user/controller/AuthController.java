package com.paypal.user.controller;

import com.paypal.user.dto.JwtResponse;
import com.paypal.user.dto.LoginRequest;
import com.paypal.user.dto.SignupRequest;
import com.paypal.user.entity.User;
import com.paypal.user.repository.UserRepo;
import com.paypal.user.service.IUserService;
import com.paypal.user.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "User API", description = "Operations related to users")
public class AuthController {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final IUserService userService;

    public AuthController(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, IUserService userService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/signup")
    @Operation(summary = "Sign up a new user", description = "Creates a new user and wallet")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        // 1. Check if user already exists
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body("⚠️ User already exists");
        }

        // 2. Map request -> User entity
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole("ROLE_USER"); // ✅ Better for Spring Security
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Use service to create user + wallet
        User savedUser = userService.createUser(user);

        // 4. Return safe response
        return ResponseEntity.ok("✅ User registered successfully with ID: " + savedUser.getId());
    }






    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> userOpt = userRepo.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("❌ User not found");
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("❌ Invalid credentials");
        }

        // Generate token with claims
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(new JwtResponse(token));
    }

}