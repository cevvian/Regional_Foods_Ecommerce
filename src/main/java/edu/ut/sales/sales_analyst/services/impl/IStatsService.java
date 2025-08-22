package edu.ut.sales.sales_analyst.services.impl;

import edu.ut.sales.sales_analyst.model.dtos.responses.OverviewStatsResponse;
import edu.ut.sales.sales_analyst.model.dtos.responses.RevenueStatsResponse;

public interface IStatsService {

    OverviewStatsResponse getOverview();

    RevenueStatsResponse getRevenueStats();
}
