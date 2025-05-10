package com.example.orderapi.controller;

import com.example.orderapi.model.Product;
import com.example.orderapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;

    //GET: Listar all products
    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    //Create product
    @PostMapping
    public Product create(@RequestBody Product product) {
        return productRepository.save(product);
    }
}