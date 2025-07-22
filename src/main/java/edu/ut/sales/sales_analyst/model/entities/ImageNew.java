package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "IMAGENEWS")
public class ImageNew {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "imageId")
    private String imageId;

    @Column(nullable = false)
    private String typeContent;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "newId")
    private New news;
}