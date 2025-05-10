package com.example.orderapi.controller;

import com.example.orderapi.model.Product;
import com.example.orderapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllProducts() throws Exception {
        Product p = new Product();
        p.setName("Coca");
        when(productRepository.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Coca"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllProductsVerifyRepositoryCall() throws Exception {
        Product p = new Product();
        p.setName("Coca");
        when(productRepository.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products"));

        verify(productRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllProductsEmpty() throws Exception {
        when(productRepository.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllProductsThrowsException() throws Exception {
        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllProductsVerifyFullResponse() throws Exception {
        Product p = new Product();
        p.setName("Coca");
        p.setPrice(1.5);
        when(productRepository.findAll()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Coca"))
                .andExpect(jsonPath("$[0].price").value(1.5));
    }


}