package com.example.demo.product;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;
    private final CategoryRepository catRepo;

    // ✅ Constructeur manuel, pas de Lombok
    public ProductService(ProductRepository repo, CategoryRepository catRepo) {
        this.repo = repo;
        this.catRepo = catRepo;
    }

    public List<Product> all() {
        return repo.findAll();
    }

    public Product get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Product create(Product p) {
        if (p.getCategory() != null && p.getCategory().getId() != null) {
            p.setCategory(catRepo.findById(p.getCategory().getId()).orElseThrow());
        }
        return repo.save(p);
    }

    public Product update(Long id, Product p) {
        Product e = get(id);
        e.setName(p.getName());
        e.setDescription(p.getDescription());
        e.setPrice(p.getPrice());
        e.setStock(p.getStock());
        e.setImageUrl(p.getImageUrl());
        if (p.getCategory() != null && p.getCategory().getId() != null) {
            e.setCategory(catRepo.findById(p.getCategory().getId()).orElseThrow());
        }
        return repo.save(e);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
