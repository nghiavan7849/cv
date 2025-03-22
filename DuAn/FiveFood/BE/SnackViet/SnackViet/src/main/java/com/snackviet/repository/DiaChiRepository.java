package com.snackviet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.DiaChi;
import com.snackviet.model.TaiKhoan;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer>{
	public List<DiaChi> findByTaiKhoanDC(TaiKhoan taiKhoanDC, Sort sort);
	public List<DiaChi> findByTaiKhoanDC(TaiKhoan taiKhoanDC);
	public List<DiaChi> findByTaiKhoanDCAndTrangThai(TaiKhoan taiKhoanDC, boolean trangThai);
	public Page<DiaChi> findByTaiKhoanDC(TaiKhoan taiKhoanDC, Pageable pageable);
	public DiaChi findByTaiKhoanDCAndTrangThai(TaiKhoan taiKhoanDC,Boolean trangThai, Sort sort);
    DiaChi findByTaiKhoanDCAndTrangThai(TaiKhoan taikhoanDC,Boolean trangThai);
}
