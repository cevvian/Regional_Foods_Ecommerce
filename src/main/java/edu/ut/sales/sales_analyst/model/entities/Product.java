package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "rating")
    private Double rating;

    @Column(name = "createAt")
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updateAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "regionId")
    private Region region;

    @OneToMany(mappedBy = "product")
    private List<ImageProduct> images;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews;

    @Column(name = "isDeleted")
    private boolean isDeleted = Boolean.FALSE;

    @PreUpdate
    public void preUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
