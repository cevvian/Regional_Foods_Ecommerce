package edu.ut.sales.sales_analyst.model.entities;

import edu.ut.sales.sales_analyst.model.enums.PaymentMethod;
import edu.ut.sales.sales_analyst.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "PAYMENT")
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;
    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private String transactionId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    private LocalDateTime paidAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
