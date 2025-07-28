package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    User findByUserId(String id);

    @Query("""
        SELECT u FROM User u WHERE (:isActive IS NULL OR u.isActive = :isActive)
    """)
    Page<User> findAllByIsActiveNullable(@Param("isActive") Boolean isActive, Pageable pageable);
}
