package com.babystore.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Avaluation;

public interface AvaluationRepository extends JpaRepository<Avaluation, Integer> {
    List<Avaluation> findTop3OrderByAmountStars(int id);
}
