package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "IMAGENEWS")
@AllArgsConstructor
@NoArgsConstructor
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