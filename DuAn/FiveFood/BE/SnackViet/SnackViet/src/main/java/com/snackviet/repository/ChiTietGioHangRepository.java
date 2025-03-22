package com.snackviet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.ChiTietGioHang;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;

@Repository
public interface ChiTietGioHangRepository extends JpaRepository<ChiTietGioHang, Integer>{
	public ChiTietGioHang findBySanPhamGHAndTaiKhoanGH(SanPham sanPhamGH, TaiKhoan taiKhoanGH);
	public ChiTietGioHang findByMaChiTietGioHangAndTaiKhoanGH(Integer maChiTietGioHang,TaiKhoan taiKhoanGH);
	public List<ChiTietGioHang> findByTaiKhoanGH(TaiKhoan taiKhoanGH, Sort sort);
	public List<ChiTietGioHang> findByTaiKhoanGH(TaiKhoan taiKhoanGH);
	public Page<ChiTietGioHang> findByTaiKhoanGH(TaiKhoan taiKhoanGH, Pageable pageable);
	
}
