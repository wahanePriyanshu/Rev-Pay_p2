package com.revpay.controller;

import com.revpay.dto.BusinessProfileResponse;
import com.revpay.dto.BusinessRegisterRequest;
import com.revpay.service.BusinessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @PostMapping("/register")
    public ResponseEntity<BusinessProfileResponse> registerBusiness(
            @RequestBody BusinessRegisterRequest request) {
        return ResponseEntity.ok(businessService.registerBusiness(request));
    }

    @GetMapping("/me")
    public ResponseEntity<BusinessProfileResponse> getMyBusinessProfile() {
        return ResponseEntity.ok(businessService.getMyBusinessProfile());
    }
}