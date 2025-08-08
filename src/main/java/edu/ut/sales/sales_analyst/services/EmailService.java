package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.Payment;
import edu.ut.sales.sales_analyst.services.impl.IEmailService;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Override
    public void sendOrderConfirmationEmail(String email, Order order) {

    }

    @Override
    public void sendPaymentConfirmationEmail(String email, Payment payment) {

    }
}
