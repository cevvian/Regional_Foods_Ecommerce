package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepo extends JpaRepository<Category, String> {
    Category findByCategoryName(String name);
    Category findByCategoryId(String id);
    @Query("SELECT SIZE(c.products) FROM Category c WHERE c.categoryId = :categoryId")
    int countProductsByCategoryId(@Param("categoryId") String categoryId);
}
