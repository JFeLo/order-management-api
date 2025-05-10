package com.example.orderapi.controller;

import com.example.orderapi.model.OrderEntity;
import com.example.orderapi.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateOrder() throws Exception {
        OrderEntity order = new OrderEntity();
        order.setSeatLetter("A");
        order.setSeatNumber("12");

        when(orderService.createEmptyOrder("A", "12")).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "seatLetter", "A",
                                "seatNumber", "12"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatLetter").value("A"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateOrderVerifyServiceCall() throws Exception {
        OrderEntity order = new OrderEntity();
        order.setSeatLetter("A");
        order.setSeatNumber("12");

        when(orderService.createEmptyOrder("A", "12")).thenReturn(order);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "seatLetter", "A",
                        "seatNumber", "12"
                ))));

        verify(orderService, times(1)).createEmptyOrder("A", "12");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateOrderFails() throws Exception {
        when(orderService.createEmptyOrder("A", "12")).thenThrow(new RuntimeException("Order creation failed"));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "seatLetter", "A",
                                "seatNumber", "12"
                        ))))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Order creation failed"));
    }


}