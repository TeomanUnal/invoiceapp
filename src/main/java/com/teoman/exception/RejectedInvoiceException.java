package com.teoman.exception;

import lombok.Getter;

@Getter
public class RejectedInvoiceException extends RuntimeException {
    private final Long invoiceId;
    private final String billNo;

    public RejectedInvoiceException(Long invoiceId, String billNo, String message) {
        super(message);
        this.invoiceId = invoiceId;
        this.billNo = billNo;
    }
}
