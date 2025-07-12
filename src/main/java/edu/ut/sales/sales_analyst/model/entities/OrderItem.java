package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ORDERITEMS")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "orderItemId")
    private String orderItemId;

    @OneToOne(cascade = CascadeType.ALL)
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unitPrice")
    private BigDecimal unitPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    private Order order;
}
