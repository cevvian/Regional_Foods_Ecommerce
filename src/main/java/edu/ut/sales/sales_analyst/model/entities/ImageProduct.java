package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "IMAGEPRODUCTS")
public class ImageProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "imageId")
    private String imageId;

    @Column(name = "imageUrl")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;
}
