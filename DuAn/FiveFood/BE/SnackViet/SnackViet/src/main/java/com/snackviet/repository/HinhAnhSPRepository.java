package com.snackviet.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snackviet.model.HinhAnhSP;
import com.snackviet.model.SanPham;

@Repository
public interface HinhAnhSPRepository extends JpaRepository<HinhAnhSP, Integer>{
	public List<HinhAnhSP> findBySanPhamHA(SanPham sanPhamHA);
	public List<HinhAnhSP> findByTenHinhAnh(HinhAnhSP hinhAnhSP);
	List<HinhAnhSP> findBySanPhamHAMaSanPham(int maSanPham);
	

	public void deleteByTenHinhAnh(String tenHinhAnh);
}
