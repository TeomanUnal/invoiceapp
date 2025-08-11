package com.teoman.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String billNo;

    private double amount;

    @ManyToOne  //bir kullanıcı birden fazla fatura girebilir
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL) // bir fatura bir ürün içerecek
    @JoinColumn(name = "product_id")
    private Product product;

    private boolean approved;

}
