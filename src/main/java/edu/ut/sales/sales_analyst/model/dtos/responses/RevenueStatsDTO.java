package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueStatsDTO {
    private String label;
    private Double totalRevenue;
}
