package com.example.demo.payment;

import com.example.demo.order.Order;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository repo;
    private final Random random = new Random();

    public PaymentService(PaymentRepository repo) { this.repo = repo; }

    public Payment simulate(Order order) {
        Payment p = new Payment();
        p.setOrder(order);
        p.setAmount(order.getTotalAmount());
        boolean success = random.nextDouble() > 0.1; // 90% succès
        p.setStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);
        p.setTransactionId("SIM-" + UUID.randomUUID());
        return repo.save(p);
    }
}
