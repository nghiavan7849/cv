package com.babystore.jparepository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.babystore.model.ProductDetail;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer>{
    @Query("SELECT pd FROM ProductDetail pd WHERE pd.product.id = ?1")
    List<ProductDetail> findByIdProduct(Integer id);
}
