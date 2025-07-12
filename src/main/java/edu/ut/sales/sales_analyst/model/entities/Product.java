package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "PRODUCTS")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "productId")
    private String productId;

    @Column(name = "productName")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "stockQuantity")
    private int stockQuantity;

    @Column(name = "category")
    private String category;
}
