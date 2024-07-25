package com.snackviet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.snackviet.model.DonHang;
import com.snackviet.model.LichSu;


public interface LichSuRepository extends JpaRepository<LichSu, Integer> {
    @Query("SELECT new LichSu(hd.maHoaDon, sp.hinhAnh, sp.tenSanPham, cthd.soLuong, cthd.soLuong * sp.gia AS tongTien, sp.maSanPham) "
    		  + "FROM ChiTietHoaDon cthd "
    		  + "JOIN cthd.hoaDonCT hd "
    		  + "JOIN cthd.sanPhamCT sp "
    		  + "JOIN hd.trangThaiHoaDon tthd "
    		  + "WHERE hd.taiKhoanHD.maTaiKhoan = :maTaiKhoan "
    		  + "AND tthd.tenTrangThai = :trangThai "
    		  + "AND hd.maHoaDon = :maHoaDon ")
   public List<LichSu> lichSuMuaHang(@Param("maTaiKhoan") Integer maTaiKhoan, @Param("trangThai") String trangThai, @Param("maHoaDon")Integer maHD);
}
