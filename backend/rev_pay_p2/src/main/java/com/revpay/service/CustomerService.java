package com.revpay.service;

import com.revpay.dto.CreateCustomerRequest;
import com.revpay.dto.UpdateCustomerRequest;
import com.revpay.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CreateCustomerRequest request);

    List<CustomerResponse> getMyCustomers();

    CustomerResponse getCustomerById(Long id);

    CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request);

    void deleteCustomer(Long id);
}