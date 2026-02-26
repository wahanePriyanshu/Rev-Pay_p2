package com.revpay.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revpay.config.JwtUtil;
import com.revpay.dto.LoginRequest;
import com.revpay.dto.LoginResponse;
import com.revpay.dto.RegisterRequest;
import com.revpay.entity.NotificationPreference;
import com.revpay.entity.Role;
import com.revpay.entity.User;
import com.revpay.entity.Wallet;
import com.revpay.repository.NotificationPreferenceRepository;
import com.revpay.repository.RoleRepository;
import com.revpay.repository.UserRepository;
import com.revpay.repository.WalletRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    
    private final  NotificationPreferenceRepository notificationPreferenceRepository;
    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          WalletRepository walletRepository,
                          PasswordEncoder passwordEncoder,
                          NotificationPreferenceRepository notificationPreferenceRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.walletRepository = walletRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationPreferenceRepository=notificationPreferenceRepository;
    }

    // ---------- LOGIN ----------
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmailOrPhone(), request.getPassword())
        );

        User user = userRepository.findByEmailOrPhone(
                request.getEmailOrPhone(),
                request.getEmailOrPhone()
        ).orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setRole(user.getRoles().iterator().next().getName());

        return ResponseEntity.ok(response);
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

        // Default role
        if (roleName == null || roleName.isBlank()) {
            roleName = "PERSONAL";
        }

        // Normalize
        roleName = roleName.toUpperCase();

        // Convert "PERSONAL" -> "ROLE_PERSONAL", "BUSINESS" -> "ROLE_BUSINESS"
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        // Fetch from DB
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRoles(Set.of(role));

        // 4. Save User
        User savedUser = userRepository.save(user);
        
        //auto create preference when user register- notification 
        
        NotificationPreference pref = new NotificationPreference();
        pref.setUser(savedUser);
        pref.setTransactionAlert(true);
        pref.setRequestAlert(true);
        pref.setLowBalanceAlert(true);

        notificationPreferenceRepository.save(pref);
        
        
        

        // 5. Create Wallet
        Wallet wallet = new Wallet();
        wallet.setUser(savedUser);
        wallet.setBalance(java.math.BigDecimal.ZERO);
        walletRepository.save(wallet);

        return Map.of("message", "User registered successfully");
    }
}