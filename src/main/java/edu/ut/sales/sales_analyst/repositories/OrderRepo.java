package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
    Order findByOrderId(String orderId);
    Page<Order> findByUser_UserId(String customerId, Pageable pageable);
    @Query("SELECT o FROM Order o WHERE o.status = :orderStatus AND o.isActive = true")
    Page<Order> findByOrderStatusAndActive(
            @Param("orderStatus") OrderStatus orderStatus,
            @Param("active") Boolean active,
            Pageable pageable
    );
}
