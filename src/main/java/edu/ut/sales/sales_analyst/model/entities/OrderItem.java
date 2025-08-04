package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "ORDERITEMS")
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "orderItemId")
    private String orderItemId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unitPrice")
    private BigDecimal unitPrice;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @OneToOne
    @JoinColumn(name = "productId")
    private Product product;
}
