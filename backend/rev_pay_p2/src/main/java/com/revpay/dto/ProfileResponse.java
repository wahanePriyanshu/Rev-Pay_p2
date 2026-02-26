package com.revpay.dto;

import java.util.List;

public class ProfileResponse {

    private String fullName;
    private String email;
    private String phone;
    private String role;

    public ProfileResponse(String fullName, String email, String phone, List<String> list) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getRole() { return role; }
}