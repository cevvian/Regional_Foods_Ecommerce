package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewStatsDTO {
    private long totalReviews;      // Tổng số review
    private double averageRating;   // Điểm trung bình
    private long fiveStarCount;     // Số đánh giá 5 sao
    private long fourStarCount;     // Số đánh giá 4 sao
    private long threeStarCount;    // Số đánh giá 3 sao
    private long twoStarCount;      // Số đánh giá 2 sao
    private long oneStarCount;      // Số đánh giá 1 sao
    private long uniqueCustomers;   // Số khách hàng duy nhất
}