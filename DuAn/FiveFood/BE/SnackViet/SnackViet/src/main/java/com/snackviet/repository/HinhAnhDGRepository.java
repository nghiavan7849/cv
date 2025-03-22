package com.snackviet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;

@Repository
public interface HinhAnhDGRepository extends JpaRepository<HinhAnhDG,Integer>{

	List<HinhAnhDG> findByDanhGia(DanhGia danhGia);
	Page<HinhAnhDG> findByDanhGia(DanhGia danhGia,Pageable pageable);
	
	List<HinhAnhDG> findByDanhGiaMaDanhGia(int maDanhGia);


}
