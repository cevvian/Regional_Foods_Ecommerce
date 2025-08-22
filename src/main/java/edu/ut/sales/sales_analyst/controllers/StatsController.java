package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.responses.OverviewStatsResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.RevenueStatsResponse;
import edu.ut.sales.sales_analyst.services.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/stats")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Stats", description = "Thống kê hệ thống: người dùng, sản phẩm, doanh thu, đơn hàng")
public class StatsController {

    StatsService statsService;

    @GetMapping("/overview")
    @Operation(
            summary = "Lấy thống kê tổng quan (dashboard)",
            description = "Trả về tổng số người dùng, sản phẩm, doanh thu, đơn hàng chờ trong tháng này và chênh lệch so với tháng trước"
    )
    public ResponseEntity<OverviewStatsResponse> getOverview() {
        return ResponseEntity.ok(statsService.getOverview());
    }

    @GetMapping("/revenue")
    @Operation(
            summary = "Thống kê doanh thu 12 tháng gần nhất",
            description = "Trả về dữ liệu doanh thu của 12 tháng (theo Payment status = PAID) và 5 đơn hàng được cập nhật gần nhất"
    )
    public ResponseEntity<RevenueStatsResponse> getRevenueStats() {
        return ResponseEntity.ok(statsService.getRevenueStats());
    }
}
