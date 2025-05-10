package com.example.orderapi.controller;

import com.example.orderapi.model.Category;
import com.example.orderapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    //GET: list categories
    @GetMapping
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    //Create category
    @PostMapping
    public Category create(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    //Update category
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        //Check if exists
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        //Update fields of category
        existingCategory.setName(category.getName());
        existingCategory.setParent(category.getParent());

        return categoryRepository.save(existingCategory);
    }

    // ELIMINAR categoría
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        // Verificar si la categoría existe
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        categoryRepository.delete(existingCategory);
    }
}