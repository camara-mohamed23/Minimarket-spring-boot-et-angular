package com.example.demo.order;

import com.example.demo.auth.User;
import com.example.demo.cart.Cart;
import com.example.demo.cart.CartItem;
import com.example.demo.cart.CartService;
import com.example.demo.notification.EmailService;
import com.example.demo.payment.Payment;
import com.example.demo.payment.PaymentService;
import com.example.demo.payment.PaymentStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orderRepo;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final EmailService emailService;

    public OrderService(OrderRepository orderRepo, CartService cartService, PaymentService paymentService, EmailService emailService) {
        this.orderRepo = orderRepo; this.cartService = cartService; this.paymentService = paymentService; this.emailService = emailService;
    }

    @Transactional
    public Order checkout(User user) {
        Cart cart = cartService.getOrCreate(user);
        if (cart.getItems().isEmpty()) throw new RuntimeException("Panier vide");

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CREATED);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(ci.getProduct());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getUnitPrice());
            oi.setSubtotal(ci.getUnitPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
            order.getItems().add(oi);
            total = total.add(oi.getSubtotal());
        }
        order.setTotalAmount(total);
        order = orderRepo.save(order);

        Payment payment = paymentService.simulate(order);
        order.setPayment(payment);

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            order.setStatus(OrderStatus.PAID);
            cartService.clear(cart);
            emailService.sendOrderConfirmation(order);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }
        return orderRepo.save(order);
    }
}
