package com.revpay.controller;

import com.revpay.config.JwtUtil;
import com.revpay.dto.RegisterRequest;
import com.revpay.entity.Role;
import com.revpay.entity.User;
import com.revpay.entity.Wallet;
import com.revpay.repository.RoleRepository;
import com.revpay.repository.UserRepository;
import com.revpay.repository.WalletRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          WalletRepository walletRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String username = request.get("username");
        
     // Allow email key also
        if (username == null) {
            username = request.get("email");
        }
        
        String password = request.get("password");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String token = jwtUtil.generateToken(username);

        return Map.of("token", token);
    }

    // ---------- REGISTER ----------
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest request) {

        // 1. Check if user already exists
        if (request.getEmail() != null && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        if (request.getPhone() != null && userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("Phone already registered");
        }

        // 2. Create User
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);

        // 3. Assign Role
        String roleName = request.getRole();
        if (roleName == null || roleName.isBlank()) {
            roleName = "ROLE_PERSONAL";
        }

        final String finalRoleName = roleName;

        Role role = roleRepository.findByName(finalRoleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + finalRoleName));
        user.setRoles(Set.of(role));

        // 4. Save User
        User savedUser = userRepository.save(user);

        // 5. Create Wallet
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(java.math.BigDecimal.ZERO);
        walletRepository.save(wallet);

        return Map.of("message", "User registered successfully");
    }
}