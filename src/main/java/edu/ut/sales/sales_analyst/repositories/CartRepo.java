package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Cart;
import edu.ut.sales.sales_analyst.model.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, String> {
    Cart findByUser_UserId(String userId);

    @EntityGraph(attributePaths = {
            "items",
            "items.product",
    })
    Optional<Cart> findByUser(User user);

    boolean existsByUser(User user);
}
