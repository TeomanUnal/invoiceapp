package com.teoman.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DtoInvoiceResponse {
    private Long id;
    private String billNo;
    private double amount;
    private boolean approved;
    private String productName;
}
