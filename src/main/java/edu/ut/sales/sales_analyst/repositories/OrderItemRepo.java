package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.model.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {
//    List<OrderItem> findByOrderId(String orderId);
    void deleteByOrder(Order order);
}
