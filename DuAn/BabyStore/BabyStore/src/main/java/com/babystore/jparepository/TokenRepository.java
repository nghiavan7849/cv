package com.babystore.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
