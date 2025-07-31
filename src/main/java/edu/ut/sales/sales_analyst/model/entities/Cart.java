package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "CARTS")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cartId")
    private String cartId;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId", unique = true)
    private User user;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items;

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
