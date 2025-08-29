package com.example.demo.product;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository repo;

    public CategoryController(CategoryRepository repo) { this.repo = repo; }

    @GetMapping public List<Category> all() { return repo.findAll(); }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping public Category create(@RequestBody Category c) {
        if (repo.existsByName(c.getName())) throw new RuntimeException("Catégorie existe déjà");
        return repo.save(c);
    }
}
