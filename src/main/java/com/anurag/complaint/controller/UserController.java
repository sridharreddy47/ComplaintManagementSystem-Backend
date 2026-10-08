package com.anurag.complaint.controller;

import com.anurag.complaint.config.JwtUtil;
import com.anurag.complaint.model.User;
import com.anurag.complaint.repository.UserRepository;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;

    public UserController(UserRepository repository, JwtUtil jwtUtil) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {

        user.setRole("USER");

        return repository.save(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {

        User existingUser = repository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(
                existingUser.getEmail(),
                existingUser.getRole()
        );

        Map<String, String> response = new HashMap<>();

        response.put("token", token);
        response.put("role", existingUser.getRole());
        response.put("name", existingUser.getName());

        return response;
    }
}
