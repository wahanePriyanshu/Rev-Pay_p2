package com.revpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.entity.BusinessProfile;
import java.util.Optional;

public interface BusinessProfileRepository extends JpaRepository<BusinessProfile, Long> {
    Optional<BusinessProfile> findByUserId(Long userId);
    boolean existsByTaxId(String taxId);
}