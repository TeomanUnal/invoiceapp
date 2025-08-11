package com.teoman.service;

import com.teoman.model.Invoice;
import com.teoman.model.UserAuth;

import java.util.List;

public interface InvoiceService {

    Invoice saveInvoice(UserAuth auth, double amount, String productName, String billNo);

    List<Invoice> getApprovedInvoices();
    List<Invoice> getRejectedInvoices();
}
