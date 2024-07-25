package com.snackviet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;

public interface HinhAnhDGRepository extends JpaRepository<HinhAnhDG,Integer>{

	List<HinhAnhDG> findByDanhGia(DanhGia danhGia);
	
	List<HinhAnhDG> findByDanhGiaMaDanhGia(int maDanhGia);


}
