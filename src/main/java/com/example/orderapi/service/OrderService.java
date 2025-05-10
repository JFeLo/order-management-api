package com.example.orderapi.service;

import com.example.orderapi.model.OrderEntity;
import com.example.orderapi.model.Product;
import com.example.orderapi.repository.OrderRepository;
import com.example.orderapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MockPaymentGateway paymentGateway;

    public OrderEntity createEmptyOrder(String seatLetter, String seatNumber) {
        if (seatLetter == null || seatLetter.isEmpty() || seatNumber == null || seatNumber.isEmpty()) {
            throw new RuntimeException("Invalid seat information");
        }

        OrderEntity order = new OrderEntity();
        order.setSeatLetter(seatLetter);
        order.setSeatNumber(seatNumber);
        order.setStatus("OPEN");
        return orderRepository.save(order);
    }

    @Transactional
    public OrderEntity updateOrder(Long orderId, String buyerEmail, List<Long> productIds) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (buyerEmail != null) order.setBuyerEmail(buyerEmail);

        if (productIds != null && !productIds.isEmpty()) {
            List<Product> products = productRepository.findAllById(productIds);

            if (products.isEmpty()) {
                throw new RuntimeException("No products found");
            }

            for (Product p : products) {
                if (p.getStock() <= 0) {
                    throw new RuntimeException("Product out of stock: " + p.getName());
                }
            }

            // Descontar stock (simplemente decrementar por 1)
            for (Product p : products) {
                p.setStock(p.getStock() - 1);
            }
            productRepository.saveAll(products);

            order.setProducts(products);
            double total = products.stream().mapToDouble(Product::getPrice).sum();
            order.setTotalPrice(total);
        }

        return orderRepository.save(order);
    }

    public OrderEntity cancelOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Cambio a "CANCELED" en vez de "DROPPED" según la regla de negocio
        order.setStatus("CANCELED");
        return orderRepository.save(order);
    }

    public OrderEntity finishOrder(Long orderId, String paymentStatus, String cardToken, String gateway) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        boolean success = paymentGateway.processPayment(cardToken, order.getTotalPrice());

        order.setPaymentGateway(gateway);
        order.setCardToken(cardToken);
        order.setPaymentDate(LocalDateTime.now());

        if (success) {
            order.setPaymentStatus(paymentStatus != null ? paymentStatus : "PAID");
            order.setStatus("FINISHED");
        } else {
            order.setPaymentStatus("FAILED");
        }

        return orderRepository.save(order);
    }
}