package edu.ut.sales.sales_analyst.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "NOTIFICATIONS")
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String notificationId;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private String title;

    private String content;

    private Boolean isRead;

    private LocalDateTime createdAt = LocalDateTime.now();
}
