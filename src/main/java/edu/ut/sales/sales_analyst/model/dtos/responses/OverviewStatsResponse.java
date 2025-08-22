package edu.ut.sales.sales_analyst.model.dtos.responses;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverviewStatsResponse {
    private StatCard totalUsers;
    private StatCard totalProducts;
    private StatCard totalRevenue;
    private StatCard pendingOrders;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatCard {
        private long value;
        private float change;
        private String unit;
    }
}
