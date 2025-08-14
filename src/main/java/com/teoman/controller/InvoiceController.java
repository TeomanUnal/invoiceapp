package com.teoman.controller;

import com.teoman.dto.DtoInvoiceRequest;
import com.teoman.dto.DtoInvoiceResponse;
import com.teoman.model.UserAuth;
import com.teoman.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<DtoInvoiceResponse> createInvoice(
            @RequestBody @Valid DtoInvoiceRequest request,
            @AuthenticationPrincipal UserAuth auth) {

        return ResponseEntity.ok(invoiceService.createInvoice(request, auth));
    }

    @GetMapping("/approved")
    public ResponseEntity<List<DtoInvoiceResponse>> getApprovedInvoices() {
        log.info("Onaylı fatura listesi istendi.");

        return ResponseEntity.ok(invoiceService.getApprovedInvoices());
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<DtoInvoiceResponse>> getRejectedInvoices() {
        log.info("Reddedilen fatura listesi istendi.");

        return ResponseEntity.ok(invoiceService.getRejectedInvoicesDto());
    }
}
