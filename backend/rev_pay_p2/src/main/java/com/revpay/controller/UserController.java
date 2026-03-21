package com.revpay.controller;

import com.revpay.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Map<String, Object>> getAllUsers(Authentication authentication) {
        String currentUsername = authentication.getName();

        return userRepository.findAll().stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("name", user.getFullName());
            map.put("email", user.getEmail());
            // Provide a default color to match frontend interface
            map.put("avatarColor", "#4e73df");
            return map;
        }).filter(user ->
            user.get("email") != null &&
            !currentUsername.equals(user.get("email"))
        ).collect(Collectors.toList());
    }
}
