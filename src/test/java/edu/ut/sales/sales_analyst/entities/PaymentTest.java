package edu.ut.sales.sales_analyst.entities;

import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.Payment;
import edu.ut.sales.sales_analyst.model.enums.PaymentMethod;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testNoArgsConstructor() {
        Payment payment = new Payment();
        assertNotNull(payment);
    }

    @Test
    void testAllArgsConstructor() {
        String paymentId = "PMT001";
        String desciption = "Description";
        PaymentMethod method = PaymentMethod.CASH;
        int amount = 250000;
        PaymentStatus status = PaymentStatus.PAID;
        String transactionId = "TX123456";
        LocalDateTime paidAt = LocalDateTime.of(2025, 8, 1, 10, 0);
        LocalDateTime createdAt = LocalDateTime.of(2025, 8, 1, 9, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2025, 8, 1, 9, 30);
        Order order = new Order();

        Payment payment = new Payment(paymentId, method, amount, status, desciption, transactionId, paidAt, createdAt, updatedAt, order);

        assertEquals(paymentId, payment.getPaymentId());
        assertEquals(method, payment.getMethod());
        assertEquals(amount, payment.getAmount());
        assertEquals(status, payment.getStatus());
        assertEquals(transactionId, payment.getTransactionId());
        assertEquals(paidAt, payment.getPaidAt());
        assertEquals(createdAt, payment.getCreatedAt());
        assertEquals(updatedAt, payment.getUpdatedAt());
        assertEquals(order, payment.getOrder());
    }

    @Test
    void testGetterSetter() {
        Payment payment = new Payment();

        payment.setPaymentId("PMT002");
        payment.setMethod(PaymentMethod.VNPAY);
        payment.setAmount(50000);
        payment.setStatus(PaymentStatus.PAID);
        payment.setTransactionId("TX987654");
        LocalDateTime now = LocalDateTime.now();
        payment.setPaidAt(now);
        payment.setCreatedAt(now);
        payment.setUpdatedAt(now);

        Order order = new Order();
        payment.setOrder(order);

        assertEquals("PMT002", payment.getPaymentId());
        assertEquals(PaymentMethod.VNPAY, payment.getMethod());
        assertEquals(50000, payment.getAmount());
        assertEquals(PaymentStatus.PAID, payment.getStatus());
        assertEquals("TX987654", payment.getTransactionId());
        assertEquals(now, payment.getPaidAt());
        assertEquals(now, payment.getCreatedAt());
        assertEquals(now, payment.getUpdatedAt());
        assertEquals(order, payment.getOrder());
    }

    @Test
    void testPreUpdateSetsUpdatedAt() {
        Payment payment = new Payment();
        payment.setUpdatedAt(null);
        payment.preUpdate();
        assertNotNull(payment.getUpdatedAt());
    }
}