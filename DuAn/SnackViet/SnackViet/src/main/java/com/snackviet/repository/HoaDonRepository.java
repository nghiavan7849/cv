package com.snackviet.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.HoaDon;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer>{

    List<HoaDon> findByTaiKhoanHDTenDangNhap(String tenTaikhoan);

	Page<HoaDon> findByTaiKhoanHDHoVaTenContaining(String search, Pageable pageable);

	Page<HoaDon> findByTrangThaiHoaDonTenTrangThaiContaining(String search, Pageable pageable);

	Page<HoaDon> findByNgayThanhToanBetween(Date ngayFrom, Date ngayTo, Pageable pageable);

	Page<HoaDon> findByMaHoaDon(int int1, Pageable pageable);

	@Query("SELECT c FROM ChiTietHoaDon c WHERE c.hoaDonCT.maHoaDon = :maHoaDon")
    List<ChiTietHoaDon> findAllChiTietHoaDonByMaHoaDon(Integer maHoaDon);

	List<HoaDon> findByTrangThaiHoaDonTenTrangThai(String string);
	
	//Report cho thống kê
	@Query(value = "SELECT loai.tenLoai AS LoaiSP, SUM(cthd.soLuong) AS SoLuongBan, SUM(hd.tongTien) AS DoanhThu " +
            "FROM ChiTietHoaDon cthd " +
            "INNER JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon " +
            "INNER JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
            "INNER JOIN LoaiSP loai ON sp.maLoai = loai.maLoai " +
            "WHERE hd.ngayThanhToan BETWEEN :startDate AND :endDate " +
            "GROUP BY loai.tenLoai", nativeQuery = true)
	List<Object[]> findRevenueByProductType(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Query(value = "SELECT loai.tenLoai AS LoaiSP, SUM(cthd.soLuong) AS SoLuongBan, SUM(hd.tongTien) AS DoanhThu " +
            "FROM ChiTietHoaDon cthd " +
            "INNER JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon " +
            "INNER JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
            "INNER JOIN LoaiSP loai ON sp.maLoai = loai.maLoai " +
            "GROUP BY loai.tenLoai", nativeQuery = true)
	List<Object[]> findAllRevenueByProductType();

	Page<HoaDon> findByOrderByMaHoaDon(Pageable pageable);
	List<HoaDon> findByOrderByMaHoaDon();
	
	Page<HoaDon> findAllByOrderByMaHoaDonDesc(Pageable pageable);
	List<HoaDon> findAllByOrderByMaHoaDonDesc();
	
	@Query("select sum(hd.tongTien+hd.phiVanChuyen) from HoaDon hd")
	double getTotalRevenue();
	
	@Query("select count(hd.maHoaDon) from HoaDon hd")
	int getTotalOrder();
	
	@Query("SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE FUNCTION('MONTH', hd.ngayThanhToan) = :month AND FUNCTION('YEAR', hd.ngayThanhToan) = :year")
    Double getTotalRevenueByMonth(int month, int year);
	
}	
