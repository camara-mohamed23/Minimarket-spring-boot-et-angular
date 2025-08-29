package com.example.demo.order;

import com.example.demo.auth.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository repo;
    private final OrderService service;

    public OrderController(OrderRepository repo, OrderService service) {
        this.repo = repo; this.service = service;
    }

    @GetMapping public List<Order> mine(@AuthenticationPrincipal User u) { return repo.findByUser(u); }

    @PostMapping("/checkout")
    public Order checkout(@AuthenticationPrincipal User u) { return service.checkout(u); }
}
