package com.revpay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revpay.entity.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
}