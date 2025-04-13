package com.babystore.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
