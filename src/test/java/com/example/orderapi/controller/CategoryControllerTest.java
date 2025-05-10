package com.example.orderapi.controller;

import com.example.orderapi.model.Category;
import com.example.orderapi.repository.CategoryRepository;
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

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllCategories() throws Exception {
        Category c = new Category();
        c.setName("Drinks");
        when(categoryRepository.findAll()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Drinks"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllCategoriesVerifyRepositoryCall() throws Exception {
        Category c = new Category();
        c.setName("Drinks");
        when(categoryRepository.findAll()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/categories"));

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllCategoriesEmpty() throws Exception {
        when(categoryRepository.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void testGetAllCategoriesThrowsException() throws Exception {
        when(categoryRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Database error"));
    }

}