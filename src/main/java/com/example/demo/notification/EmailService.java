package com.example.demo.notification;

import com.example.demo.order.Order;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final EmailLogRepository logRepo;

    public EmailService(JavaMailSender mailSender, EmailLogRepository logRepo) {
        this.mailSender = mailSender; this.logRepo = logRepo;
    }

    public void sendOrderConfirmation(Order order) {
        String to = order.getUser().getEmail();
        String subject = "Confirmation de commande #" + order.getId();

        StringBuilder body = new StringBuilder();
        body.append("Bonjour ").append(order.getUser().getFullName()).append(",\n\n");
        body.append("Merci pour votre commande. Montant total : ").append(order.getTotalAmount()).append(".\n");
        body.append("Détails :\n");
        order.getItems().forEach(i -> body.append("- ")
                .append(i.getProduct().getName()).append(" x").append(i.getQuantity())
                .append(" = ").append(i.getSubtotal()).append("\n"));
        body.append("\nCordialement,\nMinimarket");

        EmailLog log = new EmailLog();
        log.setToEmail(to); log.setSubject(subject); log.setBody(body.toString());
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to); msg.setSubject(subject); msg.setText(body.toString());
            mailSender.send(msg);
            log.setSent(true);
        } catch (Exception e) {
            log.setSent(false); log.setErrorMessage(e.getMessage());
        }
        logRepo.save(log);
    }
}
