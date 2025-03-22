package com.snackviet.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.snackviet.model.TaiKhoan;
import java.util.List;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
	public TaiKhoan findByTenDangNhap(String tenDangNhap);

	TaiKhoan findByMaTaiKhoan(int maTaiKhoan);
	List<TaiKhoan> findByTrangThai(boolean trangThai);

	//PHÂN TRANG
	Page<TaiKhoan> findByTrangThai(boolean trangThai,Pageable pageable);
	
	Page<TaiKhoan> findByTenDangNhapContaining(String search, Pageable pageable);

	Page<TaiKhoan> findBySoDienThoaiContaining(String search, Pageable pageable);

	Page<TaiKhoan> findByEmailContaining(String search, Pageable pageable);

	Page<TaiKhoan> findByHoVaTenContaining(String search, Pageable pageable);
	
	TaiKhoan findByEmail(String email);

	TaiKhoan findBySoDienThoaiContaining(String phone);

	Page<TaiKhoan> findAllByOrderByMaTaiKhoanDesc(Pageable pageable);
	Page<TaiKhoan> findAllByVaiTroNotAndMaTaiKhoanNotOrderByMaTaiKhoanDesc(boolean role, int maTaiKhoan, Pageable pageable);


	List<TaiKhoan> findAllByOrderByMaTaiKhoanDesc();
	
	@Query("select count(tk.maTaiKhoan) from TaiKhoan tk")
	int getTotalRegister();

	TaiKhoan findByTenDangNhapAndMatKhau(String tenDangNhap, String matKhau);	
}
