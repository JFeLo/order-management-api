package com.example.orderapi.service;

import org.springframework.stereotype.Service;

@Service
public class MockPaymentGateway {

    public boolean processPayment(String cardToken, double amount) {
        //If not null, simulate succesfull
        return cardToken != null && !cardToken.isBlank();
    }
}