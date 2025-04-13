package com.babystore.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Color;

public interface ColorRepository extends JpaRepository<Color, Integer> {

}
