package com.snackviet.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.HoaDon;
import com.snackviet.model.TaiKhoan;

@Repository
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
            "WHERE hd.ngayThanhToan BETWEEN :startDate AND :endDate AND hd.trangThai = 1 " +
            "GROUP BY loai.tenLoai", nativeQuery = true)
	List<Object[]> findRevenueByProductType(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Query(value = "SELECT loai.tenLoai AS LoaiSP, " +
               "SUM(cthd.soLuong) AS SoLuongBan, " +
               "SUM(hd.tongTien) AS DoanhThu " +
               "FROM ChiTietHoaDon cthd " +
               "INNER JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon " +
               "INNER JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham " +
               "INNER JOIN LoaiSP loai ON sp.maLoai = loai.maLoai " +
               "WHERE hd.trangThai = 1 " + // Chỉ lấy các hóa đơn giao thành công
               "GROUP BY loai.tenLoai " +
               "ORDER BY DoanhThu DESC", nativeQuery = true)
	List<Object[]> findAllRevenueByProductType();

	Page<HoaDon> findByOrderByMaHoaDon(Pageable pageable);
	List<HoaDon> findByOrderByMaHoaDon();
	
	Page<HoaDon> findAllByOrderByMaHoaDonDesc(Pageable pageable);
	List<HoaDon> findAllByOrderByMaHoaDonDesc();
	
	@Query(value="select sum(hd.tongTien) FROM ChiTietHoaDon cthd\n" + //
				"               INNER JOIN HoaDon hd ON cthd.maHoaDon = hd.maHoaDon\n" + //
				"               INNER JOIN SanPham sp ON cthd.maSanPham = sp.maSanPham\n" + //
				"               INNER JOIN LoaiSP loai ON sp.maLoai = loai.maLoai\n" + //
				"               where hd.trangThai = 1",nativeQuery = true)
	Double getTotalRevenue();
	
	@Query("select count(hd.maHoaDon) from HoaDon hd")
	int getTotalOrder();
	
	@Query("SELECT SUM(hd.tongTien) FROM HoaDon hd WHERE hd.trangThai = true AND FUNCTION('MONTH', hd.ngayThanhToan) = :month AND FUNCTION('YEAR', hd.ngayThanhToan) = :year")
    Double getTotalRevenueByMonth(int month, int year);
	
	Page<HoaDon> findByTaiKhoanHD(TaiKhoan taiKhoanHD, Pageable pageable);

	//lấy ra mã hóa đơn hiện tại
	@Query("SELECT MAX(h.maHoaDon) FROM HoaDon h")
	Integer findMaxMaHoaDon();
}	
