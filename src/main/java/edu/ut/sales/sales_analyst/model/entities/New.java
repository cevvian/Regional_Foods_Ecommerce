package edu.ut.sales.sales_analyst.model.entities;

import edu.ut.sales.sales_analyst.model.enums.NewType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "NEW")
@AllArgsConstructor
@NoArgsConstructor
public class New {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String newId;

    @Column(nullable = false, unique = true, name = "title", length = 1500)
    private String title;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(nullable = false)
    private String content;


    @Column(name = "createAt")
    private LocalDateTime createAt = LocalDateTime.now();

    @Column(name = "updatedAt")
    private LocalDateTime updateAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "news")
    private List<ImageNew> images;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 50)
    private NewType type;
}
