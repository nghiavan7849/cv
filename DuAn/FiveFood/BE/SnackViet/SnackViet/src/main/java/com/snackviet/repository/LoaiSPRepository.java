package com.snackviet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.LoaiSP;

@Repository
public interface LoaiSPRepository extends JpaRepository<LoaiSP,Integer> {
	Page<LoaiSP> findByTenLoai(String tenLoai, Pageable pageable);

	LoaiSP findByTenLoai(String type);
}
