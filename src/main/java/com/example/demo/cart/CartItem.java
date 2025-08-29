package com.example.demo.cart;

import com.example.demo.product.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class CartItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Cart cart;

    @ManyToOne(optional = false)
    private Product product;

    @Column(nullable=false)
    private Integer quantity;

    @Column(nullable=false)
    private BigDecimal unitPrice;

    public CartItem() {}
    public CartItem(Cart cart, Product product, Integer quantity, BigDecimal unitPrice) {
        this.cart = cart; this.product = product; this.quantity = quantity; this.unitPrice = unitPrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
