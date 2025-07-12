package edu.ut.sales.sales_analyst.model.entities;

import edu.ut.sales.sales_analyst.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "orderId")
    private String orderId;

    @OneToOne(cascade = CascadeType.ALL)
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Column(name = "totalAmount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "isActive")
    private boolean isActive = true;

    @Column(name = "orderDate")
    private Date orderDate = new Date();

}
