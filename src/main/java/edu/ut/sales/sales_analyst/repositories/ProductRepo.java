package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Integer> {

    Product findByProductName(String name);

    Product findByProductId(String id);
}
