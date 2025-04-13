package com.babystore.jparepository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.babystore.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findTop10ByOrderByIdDesc();

    @Query("select p from Product p where p.category.id = ?1")
    List<Product> findAllByCategory(Integer id);

    @Override
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByNameContaining(@Param("keyword") String keyword, Pageable pageable);


    @Query("SELECT p FROM Product p JOIN FETCH p.productDetails pd WHERE pd.price BETWEEN 0 AND :max")
    Page<Product> findByTotalBetween(@Param("max") Integer max, Pageable pageable);

}
