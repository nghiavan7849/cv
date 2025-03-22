package com.snackviet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackviet.model.PhanHoiDanhGia;

public interface PhanHoiDanhGiaRepository extends JpaRepository<PhanHoiDanhGia,Integer>{

    PhanHoiDanhGia findByMaDanhGiaMaDanhGia(int maDanhGia);

}
