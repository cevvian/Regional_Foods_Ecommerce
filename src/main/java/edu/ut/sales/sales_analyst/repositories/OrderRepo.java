package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;;

public interface OrderRepo extends JpaRepository<Order, Integer> {
    Order findByOrderId(String orderId);
    Page<Order> findByCustomer_CustomerId(String customerId, Pageable pageable);
    Page<Order> findByOrderStatusAndActive(OrderStatus status, Boolean active, Pageable pageable);
}
