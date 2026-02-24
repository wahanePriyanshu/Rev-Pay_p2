package com.revpay.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.revpay.dto.CreateInvoiceRequest;
import com.revpay.dto.InvoiceItemRequest;
import com.revpay.dto.InvoiceItemResponse;
import com.revpay.dto.InvoiceResponse;
import com.revpay.entity.Customer;
import com.revpay.entity.Invoice;
import com.revpay.entity.InvoiceItem;
import com.revpay.entity.User;
import com.revpay.entity.enums.InvoiceStatus;
import com.revpay.repository.CustomerRepository;
import com.revpay.repository.InvoiceItemRepository;
import com.revpay.repository.InvoiceRepository;
import com.revpay.repository.UserRepository;
import com.revpay.service.InvoiceService;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceItemRepository invoiceItemRepository,
                              UserRepository userRepository,
                              CustomerRepository customerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.userRepository = userRepository;
        this.customerRepository =customerRepository;
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {

        User businessUser = getCurrentUser();
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        Invoice invoice = new Invoice();
        invoice.setBusinessUser(businessUser);
        invoice.setCustomer(customer);
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setDueDate(request.getDueDate());

        // 1) Calculate total FIRST
        BigDecimal total = BigDecimal.ZERO;

        List<InvoiceItem> savedItems = new ArrayList<>();
        
        for (InvoiceItemRequest itemReq : request.getItems()) {

            BigDecimal lineTotal = itemReq.getUnitPrice()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            if (itemReq.getTax() != null && itemReq.getTax().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal taxAmount = lineTotal
                        .multiply(itemReq.getTax())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                lineTotal = lineTotal.add(taxAmount);
            }

            total = total.add(lineTotal);
        }

        // 2) Set total BEFORE saving (important!)
        invoice.setTotalAmount(total);

        // 3) Now save invoice (no NULL total_amount)
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 4) Save items
        for (InvoiceItemRequest itemReq : request.getItems()) {
            InvoiceItem item = new InvoiceItem();
            item.setInvoice(savedInvoice);
            item.setDescription(itemReq.getDescription());
            item.setQuantity(itemReq.getQuantity());
            item.setUnitPrice(itemReq.getUnitPrice());
            item.setTax(itemReq.getTax() != null ? itemReq.getTax() : BigDecimal.ZERO);

            invoiceItemRepository.save(item);
        }

        savedInvoice.setItems(savedItems);
        
        return mapToResponse(savedInvoice);
    }

    @Override
    public List<InvoiceResponse> getMyBusinessInvoices() {
        User businessUser = getCurrentUser();
        return invoiceRepository.findByBusinessUserId(businessUser.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceResponse getInvoiceById(Long invoiceId) {
        User businessUser = getCurrentUser();
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (!invoice.getBusinessUser().getId().equals(businessUser.getId())) {
            throw new RuntimeException("Not authorized to view this invoice");
        }

        return mapToResponse(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponse markInvoiceAsPaid(Long invoiceId) {
        User businessUser = getCurrentUser();
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        if (!invoice.getBusinessUser().getId().equals(businessUser.getId())) {
            throw new RuntimeException("Not authorized");
        }

        invoice.setStatus(InvoiceStatus.PAID);
        return mapToResponse(invoiceRepository.save(invoice));
    }

    // ---------------- Helpers ----------------

    private User getCurrentUser() {
        String emailOrPhone = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailOrPhone(emailOrPhone, emailOrPhone)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        InvoiceResponse res = new InvoiceResponse();
        res.setId(invoice.getId());
        res.setBusinessUserId(invoice.getBusinessUser().getId());
        res.setCustomerId(invoice.getCustomer().getId());
        res.setTotalAmount(invoice.getTotalAmount());
        res.setStatus(invoice.getStatus().name());
        res.setDueDate(invoice.getDueDate());
        res.setCreatedAt(invoice.getCreatedAt());

        List<InvoiceItemResponse> items = invoice.getItems().stream().map(item -> {
            InvoiceItemResponse ir = new InvoiceItemResponse();
            ir.setId(item.getId());
            ir.setDescription(item.getDescription());
            ir.setQuantity(item.getQuantity());
            ir.setUnitPrice(item.getUnitPrice());
            ir.setTax(item.getTax());

            BigDecimal lineTotal = item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));
            ir.setLineTotal(lineTotal);

            return ir;
        }).collect(Collectors.toList());

        res.setItems(items);
        return res;
    }
}