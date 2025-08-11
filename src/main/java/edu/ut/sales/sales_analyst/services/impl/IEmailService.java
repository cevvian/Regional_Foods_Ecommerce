package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.requests.PaymentRequest;
import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.Payment;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendOrderConfirmationEmail(String email, Order order);
    void sendPaymentConfirmationEmail(String email, Payment payment);
    void sendInvoiceEmail(String email, PaymentRequest request) throws MessagingException;
}
