package com.revpay.service.impl;

import com.revpay.dto.*;
import com.revpay.entity.Customer;
import com.revpay.entity.User;
import com.revpay.repository.CustomerRepository;
import com.revpay.repository.UserRepository;
import com.revpay.service.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        User businessUser = getCurrentUser();

        Customer customer = new Customer();
        customer.setBusinessUser(businessUser);
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());

        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    @Override
    public List<CustomerResponse> getMyCustomers() {
        User businessUser = getCurrentUser();

        return customerRepository.findByBusinessUserId(businessUser.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        User businessUser = getCurrentUser();

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // ownership check
        if (!customer.getBusinessUser().getId().equals(businessUser.getId())) {
            throw new RuntimeException("Not authorized to access this customer");
        }

        return mapToResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        User businessUser = getCurrentUser();

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.getBusinessUser().getId().equals(businessUser.getId())) {
            throw new RuntimeException("Not authorized to update this customer");
        }

        if (request.getName() != null) customer.setName(request.getName());
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());

        Customer saved = customerRepository.save(customer);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        User businessUser = getCurrentUser();

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customer.getBusinessUser().getId().equals(businessUser.getId())) {
            throw new RuntimeException("Not authorized to delete this customer");
        }

        customerRepository.delete(customer);
    }

    // -------- Helpers --------

    private User getCurrentUser() {
        String emailOrPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private CustomerResponse mapToResponse(Customer c) {
        CustomerResponse res = new CustomerResponse();
        res.setId(c.getId());
        res.setName(c.getName());
        res.setEmail(c.getEmail());
        res.setAddress(c.getAddress());
        return res;
    }
}