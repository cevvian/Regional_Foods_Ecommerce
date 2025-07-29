package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "TOKENS")
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String tokenId;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Column(columnDefinition = "TEXT")
    private String refreshToken;

    private String tokenType;

    private LocalDateTime expirationDate;

    private LocalDateTime refreshExpirationDate;

    private boolean revoked;

    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
}
