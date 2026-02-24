package com.revpay.service.impl;

import com.revpay.dto.BusinessProfileResponse;
import com.revpay.dto.BusinessRegisterRequest;
import com.revpay.entity.BusinessProfile;
import com.revpay.entity.Role;
import com.revpay.entity.User;
import com.revpay.entity.enums.BusinessVerificationStatus;
import com.revpay.repository.BusinessProfileRepository;
import com.revpay.repository.RoleRepository;
import com.revpay.repository.UserRepository;
import com.revpay.service.BusinessService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessProfileRepository businessProfileRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public BusinessServiceImpl(BusinessProfileRepository businessProfileRepository,
                               UserRepository userRepository,
                               RoleRepository roleRepository) {
        this.businessProfileRepository = businessProfileRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public BusinessProfileResponse registerBusiness(BusinessRegisterRequest request) {

        User currentUser = getCurrentUser();

        // Check if already has business
        businessProfileRepository.findByUserId(currentUser.getId())
                .ifPresent(bp -> {
                    throw new RuntimeException("Business profile already exists for this user");
                });

        // Check if taxId already used
        if (businessProfileRepository.existsByTaxId(request.getTaxId())) {
            throw new RuntimeException("Tax ID already registered");
        }

        BusinessProfile profile = new BusinessProfile();
        profile.setUser(currentUser);
        profile.setBusinessName(request.getBusinessName());
        profile.setBusinessType(request.getBusinessType());
        profile.setTaxId(request.getTaxId());
        profile.setBusinessAddress(request.getBusinessAddress());
        profile.setVerificationStatus(BusinessVerificationStatus.PENDING);

        BusinessProfile saved = businessProfileRepository.save(profile);

        // Assign ROLE_BUSINESS to user
        Role businessRole = roleRepository.findByName("ROLE_BUSINESS")
                .orElseThrow(() -> new RuntimeException("ROLE_BUSINESS not found"));

        currentUser.getRoles().add(businessRole);
        userRepository.save(currentUser);

        return mapToResponse(saved);
    }

    @Override
    public BusinessProfileResponse getMyBusinessProfile() {
        User currentUser = getCurrentUser();

        BusinessProfile profile = businessProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Business profile not found"));

        return mapToResponse(profile);
    }

    // ----------------- Helper Methods -----------------

    private User getCurrentUser() {
        String emailOrPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private BusinessProfileResponse mapToResponse(BusinessProfile profile) {
        BusinessProfileResponse res = new BusinessProfileResponse();
        res.setId(profile.getId());
        res.setBusinessName(profile.getBusinessName());
        res.setBusinessType(profile.getBusinessType());
        res.setTaxId(profile.getTaxId());
        res.setBusinessAddress(profile.getBusinessAddress());
        res.setVerificationStatus(profile.getVerificationStatus().name());
        return res;
    }
}