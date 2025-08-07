package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "ADDRESSES")
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "addressId")
    private String addressId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "addressLine")
    private String addressLine;

    @Column(name = "province")
    private String province;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_default")
    private Boolean isDefault;

    @OneToMany(mappedBy = "address")
    private List<Order> orders;
}
