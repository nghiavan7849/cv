package com.snackviet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.SanPham;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer>{


	Page<SanPham> findByTrongLuong(double search, Pageable pageable);

	Page<SanPham> findByOrderByGiaAsc(Pageable pageable);

	Page<SanPham> findByOrderByGiaDesc(Pageable pageable);

	List<SanPham> findByTrangThai(boolean b);

    //    Page<SanPham> findByTenSanPhamContaining(String search, Pageable pageable);
    Page<SanPham> findByTrongLuong(String search, Pageable pageable);
    Page<SanPham> findByTrangThai(boolean b, Pageable pageable);
    Page<SanPham> findByLoaiSPTenLoaiContaining(String tenLoai, Pageable pageable);

    Page<SanPham> findByLoaiSPTenLoaiContainingAndLoaiSPTrangThai(String tenLoai, boolean isActive, Pageable pageable);

    Page<SanPham> findAll(Pageable pageable);
    Page<SanPham> findAllByOrderByMaSanPhamDesc(Pageable pageable);
    Page<SanPham> findByLoaiSPMaLoai(Integer maLoai, Pageable pageable);
    Page<SanPham> findByGiaBetween(Double minPrice, Double maxPrice, Pageable pageable);
    Page<SanPham> findByTenSanPhamContaining(String keyword, Pageable pageable);
    SanPham findByTenSanPhamContaining(String keyword);
    List<SanPham> findByMaSanPhamIn(List<Integer> maSanPham);
	  List<SanPham> findAllByOrderByMaSanPhamDesc();

    String findByTenSanPham(String keyword);

}
