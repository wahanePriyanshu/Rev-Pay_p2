package com.revpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.entity.Customer;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByBusinessUserId(Long businessUserId);
}