package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Cart;
import edu.ut.sales.sales_analyst.model.entities.CartItem;
import edu.ut.sales.sales_analyst.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    @Query("""
        SELECT ci FROM CartItem ci
        WHERE ci.cart.user.userId = :userId
    """)
    List<CartItem> findAllByUserId(@Param("userId") String userId);

    long countAllByCartItemIdIn(List<String> ids);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cartItemId = :cartItemId AND ci.cart.user.userId = :userId")
    Optional<CartItem> findByIdAndCartUserId(@Param("cartItemId") String cartItemId, @Param("userId") String userId);
}
