package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    Product findByProductName(String name);

    Product findByProductId(String id);

    @Query(value = """
    SELECT\s
        CASE
            WHEN :month IS NOT NULL THEN\s
                CONCAT('Tuần ', FLOOR((EXTRACT(DAY FROM o.updated_at) - 1) / 7) + 1)
            ELSE\s
                CONCAT('Tháng ', EXTRACT(MONTH FROM o.updated_at))
        END AS label,
        SUM(oi.unit_price * oi.quantity) AS total_revenue
    FROM orders o
    JOIN orderitems oi ON o.order_id = oi.order_id
    JOIN products p ON oi.product_id = p.product_id
    JOIN regions r ON p.region_id = r.region_id
    WHERE o.status IN ('PAID', 'SHIPPED', 'COMPLETED')
      AND (:year IS NULL OR EXTRACT(YEAR FROM o.updated_at) = :year)
      AND (:month IS NULL OR EXTRACT(MONTH FROM o.updated_at) = :month)
      AND (:productId IS NULL OR p.product_id = :productId)
      AND (:regionId IS NULL OR r.region_id = :regionId)
    GROUP BY label
    ORDER BY label
    """, nativeQuery = true)
    List<Object[]> getRevenueByTime(
            @Param("year") Integer year,
            @Param("month") Integer month,
            @Param("productId") String productId,
            @Param("regionId") String regionId
    );
}
