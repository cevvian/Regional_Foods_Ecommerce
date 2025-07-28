package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category, String> {
    Category findByCategoryName(String name);
    Category findByCategoryId(String id);
}
