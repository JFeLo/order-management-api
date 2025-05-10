package com.example.orderapi.controller;

import com.example.orderapi.model.OrderEntity;
import com.example.orderapi.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //Create empty order
    @PostMapping
    public OrderEntity createOrder(@RequestBody Map<String, String> seatInfo) {
        return orderService.createEmptyOrder(
                seatInfo.get("seatLetter"),
                seatInfo.get("seatNumber")
        );
    }

    //Update order with mail and products
    @PutMapping("/{id}")
    public OrderEntity updateOrder(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updateData) {

        String email = (String) updateData.get("buyerEmail");
        List<Integer> rawIds = (List<Integer>) updateData.get("productIds");
        List<Long> productIds = rawIds != null ? rawIds.stream().map(Integer::longValue).toList() : null;

        return orderService.updateOrder(id, email, productIds);
    }

    //Cancel order
    @PostMapping("/{id}/cancel")
    public OrderEntity cancelOrder(@PathVariable Long id) {
        return orderService.cancelOrder(id);
    }

    //Finish order (payment)
    @PostMapping("/{id}/finish")
    public OrderEntity finishOrder(
            @PathVariable Long id,
            @RequestBody Map<String, String> paymentInfo) {

        return orderService.finishOrder(
                id,
                paymentInfo.get("paymentStatus"),
                paymentInfo.get("cardToken"),
                paymentInfo.get("paymentGateway")
        );
    }
}