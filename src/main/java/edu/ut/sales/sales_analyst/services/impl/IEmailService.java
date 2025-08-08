package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.Payment;

public interface IEmailService {
    void sendOrderConfirmationEmail(String email, Order order);
    void sendPaymentConfirmationEmail(String email, Payment payment);
}
