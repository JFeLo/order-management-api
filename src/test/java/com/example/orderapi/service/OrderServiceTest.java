package com.example.orderapi.service;

import com.example.orderapi.model.OrderEntity;
import com.example.orderapi.model.Product;
import com.example.orderapi.repository.OrderRepository;
import com.example.orderapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private MockPaymentGateway paymentGateway;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmptyOrder() {
        OrderEntity saved = new OrderEntity();
        saved.setSeatLetter("A");
        saved.setSeatNumber("12");
        saved.setStatus("OPEN");

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(saved);

        OrderEntity result = orderService.createEmptyOrder("A", "12");

        assertEquals("A", result.getSeatLetter());
        assertEquals("12", result.getSeatNumber());
        assertEquals("OPEN", result.getStatus());
    }

    @Test
    void testCreateEmptyOrderWithInvalidSeat() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                orderService.createEmptyOrder("", null)); // Asiento vacío o nulo

        assertTrue(ex.getMessage().contains("Invalid seat information"));
    }

    @Test
    void testCancelOrder() {
        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setStatus("OPEN");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        OrderEntity result = orderService.cancelOrder(1L);
        assertEquals("CANCELED", result.getStatus());
    }

    @Test
    void testUpdateOrderWithInsufficientStock() {
        Product product = new Product(1L, "Agua", 1.0, 0, "img", null);
        List<Product> products = List.of(product);
        OrderEntity order = new OrderEntity();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findAllById(List.of(1L))).thenReturn(products);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                orderService.updateOrder(1L, "user@example.com", List.of(1L)));

        assertTrue(ex.getMessage().contains("out of stock"));
    }

    @Test
    void testUpdateOrderWithProductsAndBuyerEmail() {
        Product product = new Product(1L, "Coca", 2.5, 10, "img", null);
        List<Product> products = List.of(product);

        OrderEntity existing = new OrderEntity();
        existing.setId(1L);
        existing.setStatus("OPEN");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.findAllById(List.of(1L))).thenReturn(products);
        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        OrderEntity updated = orderService.updateOrder(1L, "user@example.com", List.of(1L));

        assertEquals("user@example.com", updated.getBuyerEmail());
        assertEquals(2.5, updated.getTotalPrice());
        assertEquals(1, updated.getProducts().size());
    }

    @Test
    void testFinishOrder_Success() {
        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setTotalPrice(50.0);
        order.setStatus("OPEN");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentGateway.processPayment("tok123", 50.0)).thenReturn(true);
        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        OrderEntity result = orderService.finishOrder(1L, "PAID", "tok123", "MockGateway");

        assertEquals("FINISHED", result.getStatus());
        assertEquals("PAID", result.getPaymentStatus());
        assertEquals("tok123", result.getCardToken());
        assertEquals("MockGateway", result.getPaymentGateway());
        assertNotNull(result.getPaymentDate());
    }

    @Test
    void testFinishOrder_Failure() {
        OrderEntity order = new OrderEntity();
        order.setId(1L);
        order.setTotalPrice(50.0);
        order.setStatus("OPEN");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(paymentGateway.processPayment("failtoken", 50.0)).thenReturn(false);
        when(orderRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        OrderEntity result = orderService.finishOrder(1L, "PaymentFailed", "failtoken", "MockGateway");

        assertEquals("FAILED", result.getPaymentStatus());
        assertNotEquals("FINISHED", result.getStatus()); // no se marca como terminada
    }

    @Test
    void testUpdateOrderWithNoProducts() {
        OrderEntity order = new OrderEntity();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findAllById(List.of(1L))).thenReturn(Collections.emptyList());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                orderService.updateOrder(1L, "user@example.com", List.of(1L)));

        assertTrue(ex.getMessage().contains("No products found"));
    }

    @Test
    void testCreateEmptyOrderWithSeatOnly() {
        OrderEntity saved = new OrderEntity();
        saved.setSeatLetter("B");
        saved.setSeatNumber("20");
        saved.setStatus("OPEN");

        when(orderRepository.save(any(OrderEntity.class))).thenReturn(saved);

        OrderEntity result = orderService.createEmptyOrder("B", "20");

        assertEquals("B", result.getSeatLetter());
        assertEquals("20", result.getSeatNumber());
        assertEquals("OPEN", result.getStatus());
    }
}