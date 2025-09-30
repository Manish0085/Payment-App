package com.paypal.user.constants;


import com.paypal.user.dto.JwtResponse;
import com.paypal.user.dto.LoginRequest;
import com.paypal.user.dto.ResponseDto;
import com.paypal.user.dto.SignupRequest;
import com.paypal.user.entity.User;
import com.paypal.user.exception.UserAlreadyExistsException;
import com.paypal.user.exception.UserNotFoundException;
import com.paypal.user.repository.UserRepo;
import com.paypal.user.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;


    public AuthController(UserRepo userRepo,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request){
        Optional<User> existingUser = userRepo.findByEmail(request.getEmail());
        if (existingUser.isPresent()){
            return ResponseEntity.status(400).body(new UserAlreadyExistsException("User Already registered"));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");
        User savedUser = userRepo.save(user);
        return ResponseEntity.status(201).body(new ResponseDto(UserConstants.STATUS_201, UserConstants.MESSAGE_201));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        Optional<User> userOpt = userRepo.findByEmail(loginRequest.getEmail());
        if (userOpt.isEmpty()){
            return ResponseEntity.status(401).body(new UserNotFoundException("User not found with "+loginRequest.getEmail()+ "email."));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            return ResponseEntity.status(401).body("Invalid username and password");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        String token = jwtUtil.generateToken(claims, user.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }
}

