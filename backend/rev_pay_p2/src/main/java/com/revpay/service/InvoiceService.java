package com.revpay.service;

import com.revpay.dto.CreateInvoiceRequest;
import com.revpay.dto.InvoiceResponse;

import java.util.List;

public interface InvoiceService {

    InvoiceResponse createInvoice(CreateInvoiceRequest request);

    List<InvoiceResponse> getMyBusinessInvoices();

    InvoiceResponse getInvoiceById(Long invoiceId);

    InvoiceResponse markInvoiceAsPaid(Long invoiceId);
    
    InvoiceResponse sendInvoice(Long invoiceId);

    InvoiceResponse cancelInvoice(Long invoiceId);
}