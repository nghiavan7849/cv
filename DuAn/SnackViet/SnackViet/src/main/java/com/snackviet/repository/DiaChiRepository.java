package com.snackviet.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.snackviet.model.DiaChi;
import com.snackviet.model.TaiKhoan;

public interface DiaChiRepository extends JpaRepository<DiaChi, Integer>{
	public List<DiaChi> findByTaiKhoanDC(TaiKhoan taiKhoanDC, Sort sort);
	public List<DiaChi> findByTaiKhoanDC(TaiKhoan taiKhoanDC);
}
