package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.ImageNew;
import edu.ut.sales.sales_analyst.model.entities.New;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageNewRepo extends JpaRepository<ImageNew, Integer> {
    void deleteByNews(New news);
}
