package com.teoman.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CreditLimitService {

    @Value("${credit.limit}")
    private double creditLimit;

    public double getCreditLimit() {
        return creditLimit;
    }
}
