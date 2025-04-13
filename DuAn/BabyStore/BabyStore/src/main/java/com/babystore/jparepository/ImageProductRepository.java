package com.babystore.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.babystore.model.ImageProduct;

public interface ImageProductRepository extends JpaRepository<ImageProduct, Integer> {

}
