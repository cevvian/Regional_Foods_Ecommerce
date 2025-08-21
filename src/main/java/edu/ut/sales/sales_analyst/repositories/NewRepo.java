package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Category;
import edu.ut.sales.sales_analyst.model.entities.New;
import edu.ut.sales.sales_analyst.model.enums.NewType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewRepo extends JpaRepository<New, Integer> {
    boolean existsByTitle(String title);

    New findByNewId(String newId);

    Page<New> findByCategory(Category category, Pageable pageable);


    @Query("""
        SELECT n FROM New n
        WHERE (:type IS NULL OR n.type = :type)
          AND (:category IS NULL OR n.category = :category)
    """)
    Page<New> findByTypeAndCategoryNullable(
            @Param("type") NewType type,
            @Param("category") Category category,
            Pageable pageable
    );
}
