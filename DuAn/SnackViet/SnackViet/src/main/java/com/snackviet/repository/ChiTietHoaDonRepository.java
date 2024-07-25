package com.snackviet.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.HoaDon;
import java.util.List;


public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer>{

//	@Query("SELECT sp.tenSanPham, SUM(cthd.soLuong), SUM(cthd.soLuong * cthd.donGia), hd.ngayThanhToan, kh.tenKhachHang " +
//	           "FROM ChiTietHoaDon cthd JOIN cthd.hoaDonCT hd JOIN cthd.sanPhamCT sp JOIN hd.khachHang kh " +
//	           "WHERE hd.ngayThanhToan BETWEEN :startDate AND :endDate " +
//	           "GROUP BY sp.tenSanPham, hd.ngayThanhToan, kh.tenKhachHang")
//	List<Object[]> findProductsSoldByDateRange(Date startDate, Date endDate);

	public List<ChiTietHoaDon> findByHoaDonCT(HoaDon hoaDonCT);
	
	@Query("SELECT SUM(c.soLuong) FROM ChiTietHoaDon c WHERE c.sanPhamCT.maSanPham = :maSanPham")
    Integer findTotalSoldBySanPhamId(@Param("maSanPham") Integer maSanPham);
	
	@Query("select sum(cthd.soLuong) from ChiTietHoaDon cthd")
	int getTotalProduct();
	
	
	@Query(value = "SELECT loai.tenLoai, COUNT(cthd.maSanPham) " +
            "FROM ChiTietHoaDon cthd " +
            "INNER JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
            "INNER JOIN LoaiSP loai ON sp.maLoai = loai.maLoai " +
            "GROUP BY loai.tenLoai", nativeQuery = true)
	List<Object[]> getPurchaseCountsByProductType();
	
	
	@Query("SELECT SUM(cthd.soLuong) FROM ChiTietHoaDon cthd WHERE FUNCTION('MONTH', cthd.hoaDonCT.ngayThanhToan) = :month AND FUNCTION('YEAR', cthd.hoaDonCT.ngayThanhToan) = :year")
    Double getTotalProductSoldByMonth(int month, int year);
}
