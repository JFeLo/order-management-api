package com.example.orderapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String buyerEmail;
    private String seatLetter;
    private String seatNumber;

    private double totalPrice;

    private String status; //OPEN, DROPPED, FINISHED

    private String paymentStatus; //PAID, FAILED, OFFLINE
    private String cardToken;
    private String paymentGateway;
    private LocalDateTime paymentDate;

    @ManyToMany
    private List<Product> products;
}