package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.ImageNew;
import edu.ut.sales.sales_analyst.model.entities.ImageProduct;
import edu.ut.sales.sales_analyst.model.entities.New;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageProductRepo extends JpaRepository<ImageProduct, String> {
    ImageProduct findByImageId(String id);
    Page<ImageProduct> findByProduct(Product product, Pageable pageable);
}
