package com.snackviet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.snackviet.model.DonHang;

import jakarta.persistence.QueryHint;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, String> {

     @Query(value = "SELECT hd.maHoaDon, COUNT(cthd.maSanPham) AS tongSanPham, hd.diaChiNhan, tthd.tenTrangThai, hd.tongTien, hd.lydoHuy "
               + "FROM HoaDon hd "
               + "INNER JOIN TrangThaiHoaDon tthd ON hd.maTrangThaiHoaDon = tthd.maTrangThaiHoaDon "
               + "INNER JOIN TaiKhoan tk ON tk.maTaiKhoan = hd.maTaiKhoan "
               + "INNER JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon "
               + "INNER JOIN SanPham sp ON sp.maSanPham = cthd.maSanPham "
               + "WHERE tk.maTaiKhoan = :maTaiKhoan "
               + "AND tthd.tenTrangThai = :trangThai "
               + "GROUP BY hd.maHoaDon, hd.diaChiNhan, tthd.tenTrangThai, hd.tongTien, hd.lydoHuy", nativeQuery = true)
     List<DonHang> hoadonMuaHang(@Param("maTaiKhoan") Integer maTaiKhoan, @Param("trangThai") String trangThai);

     // @Query(value = "SELECT hd.maHoaDon, COUNT(cthd.maSanPham) AS tongSanPham, sp.hinhanh as hinhAnh, sp.tenSanPham as tenSanPham, hd.diaChiNhan, "
     //           + "tthd.tenTrangThai, hd.tongTien, hd.lydoHuy "
     //           + "FROM HoaDon hd "
     //           + "INNER JOIN TrangThaiHoaDon tthd ON hd.maTrangThaiHoaDon = tthd.maTrangThaiHoaDon "
     //           + "INNER JOIN TaiKhoan tk ON tk.maTaiKhoan = hd.maTaiKhoan "
     //           + "INNER JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon "
     //           + "INNER JOIN SanPham sp ON sp.maSanPham = cthd.maSanPham "
     //           + "WHERE tk.maTaiKhoan = :maTaiKhoan "
     //           + "AND tthd.tenTrangThai = :trangThai "
     //           + "GROUP BY hd.maHoaDon, sp.hinhanh,sp.tenSanPham, hd.diaChiNhan, tthd.tenTrangThai, hd.tongTien, hd.lydoHuy "
     //           + "ORDER BY hd.maHoaDon DESC", nativeQuery = true)
     @Query(value = "SELECT hd.maHoaDon, COUNT(cthd.maSanPham) AS tongSanPham, hd.diaChiNhan, "
               + "tthd.tenTrangThai, hd.tongTien, hd.lydoHuy "
               + "FROM HoaDon hd "
               + "INNER JOIN TrangThaiHoaDon tthd ON hd.maTrangThaiHoaDon = tthd.maTrangThaiHoaDon "
               + "INNER JOIN TaiKhoan tk ON tk.maTaiKhoan = hd.maTaiKhoan "
               + "INNER JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon "
               + "INNER JOIN SanPham sp ON sp.maSanPham = cthd.maSanPham "
               + "WHERE tk.maTaiKhoan = :maTaiKhoan "
               + "AND tthd.tenTrangThai = :trangThai "
               + "GROUP BY hd.maHoaDon, hd.diaChiNhan, tthd.tenTrangThai, hd.tongTien, hd.lydoHuy "
               + "ORDER BY hd.maHoaDon DESC", nativeQuery = true)
     Page<DonHang> hoadonMuaHangOrderBy(@Param("maTaiKhoan") Integer maTaiKhoan,
               @Param("trangThai") String trangThai,
               Pageable pageable);
}
