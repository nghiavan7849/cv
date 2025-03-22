package com.snackviet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.TinhThanh;


@Repository
public interface TinhThanhRepository extends JpaRepository<TinhThanh, Integer>{
    public TinhThanh findByProvinceID(Integer provinceID);
}
