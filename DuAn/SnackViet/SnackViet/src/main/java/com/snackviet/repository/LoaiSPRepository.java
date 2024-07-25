package com.snackviet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.snackviet.model.LoaiSP;
import java.util.List;


public interface LoaiSPRepository extends JpaRepository<LoaiSP,Integer> {
	Page<LoaiSP> findByTenLoai(String tenLoai, Pageable pageable);

	LoaiSP findByTenLoai(String type);
}
