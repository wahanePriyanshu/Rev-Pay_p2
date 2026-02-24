package com.revpay.dto;

public class UpdateProfileRequest {
    private String fullName;
    private String phone;

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}