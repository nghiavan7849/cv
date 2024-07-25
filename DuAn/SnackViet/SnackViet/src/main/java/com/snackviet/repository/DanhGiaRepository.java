package com.snackviet.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
	public Page<DanhGia> findBySoSaoAndSanPhamDG(int soSao, SanPham sanPhamDG, Pageable pageable);

	public Page<DanhGia> findBySanPhamDG(SanPham sanPhamDG, Pageable pageable);

	public Page<DanhGia> findBySanPhamDGTenSanPhamContaining(String search, Pageable pageable);

	public Page<DanhGia> findBySoSao(int search, Pageable pageable);

	public Page<DanhGia> findByTrangThai(boolean b, Pageable pageable);

	public Page<DanhGia> findByBinhLuanContaining(String search, Pageable pageable);

	public Page<DanhGia> findByNgayDanhGiaBetween(Date ngayFrom, Date ngayTo, Pageable pageable);

	public List<DanhGia> findBySanPhamDG(SanPham sanPham);

	public Optional<DanhGia> findBySanPhamDGAndTaiKhoanDG(SanPham sanPhamDG, TaiKhoan taiKhoanDG);
	 Optional<DanhGia> findFirstBySanPhamDGAndTaiKhoanDG(SanPham sanPham, TaiKhoan taiKhoan);

	List<DanhGia> findBySoSao(int i);

}