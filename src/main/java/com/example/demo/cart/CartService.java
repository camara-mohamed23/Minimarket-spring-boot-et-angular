package com.example.demo.cart;

import com.example.demo.auth.User;
import com.example.demo.product.Product;
import com.example.demo.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    public CartService(CartRepository cartRepo, ProductRepository productRepo) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
    }

    public Cart getOrCreate(User user) {
        return cartRepo.findByUser(user).orElseGet(() -> cartRepo.save(new Cart(user)));
    }

    public Cart addItem(User user, Long productId, int qty) {
        Cart c = getOrCreate(user);
        Product p = productRepo.findById(productId).orElseThrow();
        CartItem item = c.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst().orElse(null);
        if (item == null) {
            item = new CartItem(c, p, qty, p.getPrice());
            c.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + qty);
        }
        return c;
    }

    public Cart updateQty(User user, Long productId, int qty) {
        Cart c = getOrCreate(user);
        c.getItems().stream().filter(i -> i.getProduct().getId().equals(productId))
                .findFirst().ifPresent(i -> i.setQuantity(qty));
        return c;
    }

    public Cart removeItem(User user, Long productId) {
        Cart c = getOrCreate(user);
        c.getItems().removeIf(i -> i.getProduct().getId().equals(productId));
        return c;
    }

    public BigDecimal total(Cart c) {
        return c.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clear(Cart c) { c.getItems().clear(); }
}
