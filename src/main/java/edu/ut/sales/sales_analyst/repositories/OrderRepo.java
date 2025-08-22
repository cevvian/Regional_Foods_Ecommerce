package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, String> {
    Order findByOrderId(String orderId);
    Page<Order> findByUser_UserId(String customerId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.isActive = :active")
    Page<Order> findByActive(Pageable pageable, @Param("active") Boolean active);

    @Query("SELECT o FROM Order o WHERE o.status = :orderStatus AND o.isActive = :active")
    Page<Order> findByOrderStatusAndActive(
            @Param("orderStatus") OrderStatus orderStatus,
            @Param("active") Boolean active,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(oi) > 0
    FROM Order o
    JOIN o.orderItems oi
    WHERE o.user.userId = :userId
      AND oi.product.productId = :productId
      AND o.status = 'COMPLETED'
    """)
    boolean existsCompletedOrderByUserIdAndProductId(@Param("userId") String userId, @Param("productId") String productId);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product " +
            "WHERE o.orderId = :id")
    Optional<Order> findByIdWithItems(@Param("id") String id);
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.orderId = :orderId")
    Order findByOrderIdWithItems(@Param("orderId") String orderId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PROCESS' AND MONTH(o.orderDate) = :month AND YEAR(o.orderDate) = :year")
    long countPendingOrdersByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT o FROM Payment p " +
            "JOIN p.order o " +
            "WHERE p.status = 'PAID' " +
            "ORDER BY p.updatedAt DESC")
    List<Order> findRecentPaidOrders(Pageable pageable);
}
