package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Category;
import edu.ut.sales.sales_analyst.model.entities.New;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewRepo extends JpaRepository<New, Integer> {
    boolean existsByTitle(String title);

    New findByNewId(String newId);

    Page<New> findByCategory(Category category, Pageable pageable);
}
