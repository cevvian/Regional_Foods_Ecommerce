package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "NEW")
public class New {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String newId;

    @Column(nullable = false, unique = true, name = "title")
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String image;

    @Column(name = "createAt")
    private LocalDateTime createAt = LocalDateTime.now();

    @OneToMany(mappedBy = "news")
    private List<ImageNew> images;
}
