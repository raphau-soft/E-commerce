package com.devraf.e_commerce.repository;

import com.devraf.e_commerce.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenDAO extends JpaRepository<Token, Long> {
    Optional<Token> findByUserIdAndTokenType(Long userId, String tokenType);
    Optional<Token> findByTokenAndTokenType(String token, String tokenType);
    Optional<Token> findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.token = :token")
    void deleteByToken(@Param("token") String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM Token t WHERE t.expiredAt < CURRENT_TIMESTAMP")
    void deleteExpiredTokens();
}
