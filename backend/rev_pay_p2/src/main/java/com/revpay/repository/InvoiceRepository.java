package com.revpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.entity.Invoice;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByBusinessUserId(Long businessUserId);
    List<Invoice> findByCustomerId(Long customerId);
}