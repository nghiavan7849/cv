package com.snackviet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.QuanHuyen;
import com.snackviet.model.TinhThanh;

import java.util.List;


@Repository
public interface QuanHuyenRepository extends JpaRepository<QuanHuyen, Integer>{
    public QuanHuyen findByDistrictID(Integer districtID);
    public List<QuanHuyen> findByTinhThanhId(TinhThanh tinhThanhId);
}
