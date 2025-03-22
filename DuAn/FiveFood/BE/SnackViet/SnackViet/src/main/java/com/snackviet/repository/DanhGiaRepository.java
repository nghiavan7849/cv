package com.snackviet.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.DanhGia;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {
	public Page<DanhGia> findBySoSaoAndSanPhamDG(int soSao, SanPham sanPhamDG, Pageable pageable);

	public Page<DanhGia> findBySanPhamDG(SanPham sanPhamDG, Pageable pageable);

	public Page<DanhGia> findBySanPhamDGTenSanPhamContaining(String search, Pageable pageable);

	public Page<DanhGia> findBySoSao(int search, Pageable pageable);

	public Page<DanhGia> findByTrangThai(boolean b, Pageable pageable);

	public Page<DanhGia> findByBinhLuanContaining(String search, Pageable pageable);

	public Page<DanhGia> findByNgayDanhGiaBetween(Date ngayFrom, Date ngayTo, Pageable pageable);

	public List<DanhGia> findBySanPhamDG(SanPham sanPham);

	public List<DanhGia> findBySanPhamDGAndTaiKhoanDG(SanPham sanPham, TaiKhoan taiKhoan);
	 Optional<DanhGia> findFirstBySanPhamDGAndTaiKhoanDG(SanPham sanPham, TaiKhoan taiKhoan);

	List<DanhGia> findBySoSao(int i);

	List<DanhGia> findByTaiKhoanDGAndSanPhamDG(TaiKhoan taiKhoanDG, SanPham sanPham);

}