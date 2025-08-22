package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.model.dtos.responses.MonthlyRevenue;
import edu.ut.sales.sales_analyst.model.dtos.responses.OverviewStatsResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.RevenueStatsResponse;
import edu.ut.sales.sales_analyst.model.entities.Order;
import edu.ut.sales.sales_analyst.repositories.OrderRepo;
import edu.ut.sales.sales_analyst.repositories.PaymentRepo;
import edu.ut.sales.sales_analyst.repositories.ProductRepo;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
import edu.ut.sales.sales_analyst.services.impl.IStatsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService implements IStatsService {
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;

    public StatsService(UserRepo userRepo, ProductRepo productRepo, OrderRepo orderRepo, PaymentRepo paymentRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.paymentRepo = paymentRepo;
    }

    @Override
    public OverviewStatsResponse getOverview() {
        LocalDate now = LocalDate.now();
        int thisMonth = now.getMonthValue();
        int thisYear = now.getYear();
        int lastMonth = thisMonth == 1 ? 12 : thisMonth - 1;
        int lastYear = thisMonth == 1 ? thisYear - 1 : thisYear;

        long usersNow = userRepo.countUsersByMonth(thisMonth, thisYear);
        long usersPrev = userRepo.countUsersByMonth(lastMonth, lastYear);

        long productsNow = productRepo.countProductsByMonth(thisMonth, thisYear);
        long productsPrev = productRepo.countProductsByMonth(lastMonth, lastYear);

        long revenueNow = paymentRepo.sumRevenueByMonth(thisMonth, thisYear);
        long revenuePrev = paymentRepo.sumRevenueByMonth(lastMonth, lastYear);

        long ordersNow = orderRepo.countPendingOrdersByMonth(thisMonth, thisYear);
        long ordersPrev = orderRepo.countPendingOrdersByMonth(lastMonth, lastYear);

        return OverviewStatsResponse.builder()
                .totalUsers(buildCard(usersNow, usersPrev, "%"))
                .totalProducts(buildCard(productsNow, productsPrev, "%"))
                .totalRevenue(buildCard(revenueNow, revenuePrev, "₫"))
                .pendingOrders(buildCard(ordersNow, ordersPrev, "đơn"))
                .build();
    }

    @Override
    public RevenueStatsResponse getRevenueStats() {
        LocalDateTime startDate = LocalDateTime.now()
                .minusMonths(11)
                .withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        List<MonthlyRevenue> rawRevenues =
                paymentRepo.getMonthlyRevenue(startDate);

        Map<String, BigDecimal> revenueMap = rawRevenues.stream()
                .collect(Collectors.toMap(
                        r -> r.getYear() + "-" + r.getMonth(),
                        MonthlyRevenue::getTotal
                ));

        // Build đủ 12 tháng liên tục
        List<MonthlyRevenue> revenues = new ArrayList<>();
        YearMonth current = YearMonth.now().minusMonths(11);
        for (int i = 0; i < 12; i++) {
            String key = current.getYear() + "-" + current.getMonthValue();
            BigDecimal total = revenueMap.getOrDefault(key, BigDecimal.ZERO);
            revenues.add(
                    MonthlyRevenue.builder()
                            .year(current.getYear())
                            .month(current.getMonthValue())
                            .total(total)
                            .build()
            );
            current = current.plusMonths(1);
        }

        // Lấy 5 đơn gần nhất
        List<Order> recentOrders = orderRepo.findRecentPaidOrders(PageRequest.of(0, 5));

        return RevenueStatsResponse.builder()
                .revenues(revenues)
                .recentOrders(recentOrders)
                .build();
    }

    private OverviewStatsResponse.StatCard buildCard(long current, long prev, String unit) {
        float change;

        if ("%".equals(unit)) {
            change = prev == 0 ? 100 : ((float) (current - prev) / prev) * 100;
        } else {
            change = current - prev;
        }

        return OverviewStatsResponse.StatCard.builder()
                .value(current)
                .change(change)
                .unit(unit)
                .build();
    }
}
