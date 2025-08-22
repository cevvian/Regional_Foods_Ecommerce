package edu.ut.sales.sales_analyst.model.dtos.responses;

import edu.ut.sales.sales_analyst.model.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueStatsResponse {
    private List<MonthlyRevenue> revenues;
    private List<Order> recentOrders;
}
