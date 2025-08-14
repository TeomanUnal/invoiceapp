package com.teoman.service;

import com.teoman.dto.DtoInvoiceResponse;
import com.teoman.model.Invoice;
import com.teoman.model.UserAuth;

import java.util.List;

public interface InvoiceService {

    Invoice saveInvoice(UserAuth auth, double amount, String productName, String billNo);

    List<DtoInvoiceResponse> getApprovedInvoices();
    List<DtoInvoiceResponse> getRejectedInvoicesDto();
}
