package com.teoman.service;

import com.teoman.dto.DtoInvoiceRequest;
import com.teoman.dto.DtoInvoiceResponse;
import com.teoman.model.Invoice;
import com.teoman.model.UserAuth;

import java.util.List;

public interface InvoiceService {



    DtoInvoiceResponse createInvoice(DtoInvoiceRequest request, UserAuth auth);

    List<DtoInvoiceResponse> getApprovedInvoices();
    List<DtoInvoiceResponse> getRejectedInvoicesDto();
}
