package com.example.demo.product;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) { this.service = service; }

    @GetMapping public List<Product> all() { return service.all(); }
    @GetMapping("/{id}") public Product get(@PathVariable Long id) { return service.get(id); }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping public Product create(@RequestBody Product p) { return service.create(p); }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}") public Product update(@PathVariable Long id, @RequestBody Product p) { return service.update(id, p); }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
