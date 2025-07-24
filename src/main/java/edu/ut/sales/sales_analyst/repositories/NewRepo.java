package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.New;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewRepo extends JpaRepository<New, Integer> {
    boolean existsByTitle(String title);

    New findByNewId(String newId);

}
