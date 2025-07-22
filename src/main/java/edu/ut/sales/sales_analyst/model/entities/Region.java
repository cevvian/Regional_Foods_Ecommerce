package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "REGIONS")
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "regionId")
    private String regionId;

    @Column(unique = true, nullable = false, name = "regionName")
    private String regionName;

    @OneToMany(mappedBy = "region")
    private List<Product> products;
}
