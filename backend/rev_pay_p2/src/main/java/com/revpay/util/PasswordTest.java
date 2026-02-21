package com.revpay.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "password123";   // choose your password
        String encoded = encoder.encode(rawPassword);
        System.out.println(encoded);
    }
}