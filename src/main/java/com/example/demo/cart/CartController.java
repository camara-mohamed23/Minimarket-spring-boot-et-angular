package com.example.demo.cart;

import com.example.demo.auth.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService service;

    public CartController(CartService service) { this.service = service; }

    @GetMapping public Cart get(@AuthenticationPrincipal User u) { return service.getOrCreate(u); }

    @PostMapping("/add/{productId}")
    public Cart add(@AuthenticationPrincipal User u, @PathVariable Long productId, @RequestParam int qty) {
        return service.addItem(u, productId, qty);
    }

    @PutMapping("/qty/{productId}")
    public Cart qty(@AuthenticationPrincipal User u, @PathVariable Long productId, @RequestParam int qty) {
        return service.updateQty(u, productId, qty);
    }

    @DeleteMapping("/remove/{productId}")
    public Cart remove(@AuthenticationPrincipal User u, @PathVariable Long productId) {
        return service.removeItem(u, productId);
    }
}
