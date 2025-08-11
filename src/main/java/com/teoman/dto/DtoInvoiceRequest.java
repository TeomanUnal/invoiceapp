package com.teoman.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class DtoInvoiceRequest {

    @Positive
    private double amount;

    @NotBlank
    private String productName;

    @NotBlank
    private String billNo;
}
