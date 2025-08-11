package edu.ut.sales.sales_analyst.model.dtos.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangedEvent {
    private String userId;
    private LocalDateTime changedAt;
}