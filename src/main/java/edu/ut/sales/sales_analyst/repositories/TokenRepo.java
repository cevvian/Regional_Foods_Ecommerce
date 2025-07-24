package edu.ut.sales.sales_analyst.repositories;

import edu.ut.sales.sales_analyst.model.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token, Integer> {
    List<Token> findByUser_userId(String userId);

    Optional<Token> findByToken(String token);

    Optional<Token> findByRefreshToken(String token);

}
