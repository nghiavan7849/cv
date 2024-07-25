package com.snackviet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snackviet.model.TrangThaiHoaDon;

public interface TrangThaiHoaDonRepository extends JpaRepository<TrangThaiHoaDon,Integer>{

	TrangThaiHoaDon findByTenTrangThai(String trangThai);
	List<TrangThaiHoaDon> findAllByTenTrangThaiIn(String[] strings);
}
