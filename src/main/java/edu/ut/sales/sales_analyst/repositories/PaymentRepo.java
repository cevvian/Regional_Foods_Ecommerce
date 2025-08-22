package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.dtos.responses.MonthlyRevenue;
import edu.ut.sales.sales_analyst.model.entities.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, String> {
    Page<Payment> findByOrder_OrderId(String orderId, Pageable pageable);

    Optional<Payment> findPaymentByTransactionId(String transactionId);

    @Query("""
    SELECT p FROM Payment p
    WHERE p.order.orderId = :orderId
      AND p.status = "PAID"
    """)
    Payment findPaymentByOrderIdAndPaid(@Param("orderId") String orderId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'PAID' AND MONTH(p.paidAt) = :month AND YEAR(p.paidAt) = :year")
    long sumRevenueByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT new edu.ut.sales.sales_analyst.model.dtos.responses.MonthlyRevenue(MONTH(o.updatedAt), YEAR(o.updatedAt), SUM(o.totalAmount)) " +
            "FROM Order o " +
            "WHERE o.status = 'COMPLETED' " +
            "GROUP BY YEAR(o.updatedAt), MONTH(o.updatedAt) " +
            "ORDER BY YEAR(o.updatedAt), MONTH(o.updatedAt)")
    List<MonthlyRevenue> getMonthlyRevenue(LocalDateTime startDate);
}
