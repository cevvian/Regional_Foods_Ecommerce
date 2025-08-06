package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {

    Product findByProductName(String name);

    Product findByProductId(String id);

    //lock hàng tồn kho
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.productId = :productId")
    Optional<Product> findByIdForUpdate(@Param("productId") String productId);


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


    @Query(value = """
    SELECT p.* FROM products p
    LEFT JOIN (
        SELECT product_id, AVG(rating) AS avg_rating
        FROM reviews
        GROUP BY product_id
    ) r ON p.product_id = r.product_id
    WHERE (:categoryId IS NULL OR p.category_id = :categoryId)
      AND (:regionId IS NULL OR p.region_id = :regionId)
      AND (:minPrice IS NULL OR p.price >= :minPrice)
      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
      AND (:minStock IS NULL OR p.stock_quantity >= :minStock)
      AND (:minRating IS NULL OR r.avg_rating >= :minRating)
    """,
            countQuery = """
    SELECT COUNT(*) FROM products p
    LEFT JOIN (
        SELECT product_id, AVG(rating) AS avg_rating
        FROM reviews
        GROUP BY product_id
    ) r ON p.product_id = r.product_id
    WHERE (:categoryId IS NULL OR p.category_id = :categoryId)
      AND (:regionId IS NULL OR p.region_id = :regionId)
      AND (:minPrice IS NULL OR p.price >= :minPrice)
      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
      AND (:minStock IS NULL OR p.stock_quantity >= :minStock)
      AND (:minRating IS NULL OR r.avg_rating >= :minRating)
    """,
            nativeQuery = true)
    Page<Product> filterProducts(
            @Param("categoryId") String categoryId,
            @Param("regionId") String regionId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minStock") Integer minStock,
            @Param("minRating") Double minRating,
            Pageable pageable
    );
}
