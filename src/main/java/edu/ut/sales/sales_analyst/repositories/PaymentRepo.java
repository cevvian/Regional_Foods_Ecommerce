package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, String> {
    Page<Payment> findByOrder_OrderId(String orderId, Pageable pageable);

    Optional<Payment> findPaymentByTransactionId(String transactionId);

    @Query("""
    SELECT p 
    FROM Payment p
    WHERE p.order.orderId = :orderId
      AND p.status = "PAID"
    """)
    Payment findPaymentByOrderIdAndPaid(@Param("orderId") String orderId);

}
