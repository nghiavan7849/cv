package com.snackviet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.snackviet.model.LichSu;

@Repository
public interface LichSuRepository extends JpaRepository<LichSu, Integer> {
    @Query("SELECT new LichSu(hd.maHoaDon, sp.hinhAnh, sp.tenSanPham, cthd.soLuong, cthd.soLuong * sp.gia AS tongTien, sp.maSanPham) "
            + "FROM ChiTietHoaDon cthd "
            + "JOIN cthd.hoaDonCT hd "
            + "JOIN cthd.sanPhamCT sp "
            + "JOIN hd.trangThaiHoaDon tthd "
            + "WHERE hd.taiKhoanHD.maTaiKhoan = :maTaiKhoan "
            + "AND tthd.tenTrangThai = :trangThai "
            + "AND hd.maHoaDon = :maHoaDon ")
    public List<LichSu> lichSuMuaHang(@Param("maTaiKhoan") Integer maTaiKhoan, @Param("trangThai") String trangThai,
            @Param("maHoaDon") Integer maHD);

    @Query("SELECT new LichSu(hd.maHoaDon, sp.hinhAnh, sp.tenSanPham, cthd.soLuong, cthd.soLuong * sp.gia AS tongTien, sp.maSanPham) "
            + "FROM ChiTietHoaDon cthd "
            + "JOIN cthd.hoaDonCT hd "
            + "JOIN cthd.sanPhamCT sp "
            + "JOIN hd.trangThaiHoaDon tthd "
            + "WHERE hd.taiKhoanHD.maTaiKhoan = :maTaiKhoan "
            + "AND tthd.tenTrangThai = :trangThai "
            + "AND hd.maHoaDon = :maHoaDon "
            + "ORDER BY hd.maHoaDon DESC")
    public Page<LichSu> lichSuMuaHangOrderBy(@Param("maTaiKhoan") Integer maTaiKhoan,
            @Param("trangThai") String trangThai, @Param("maHoaDon") Integer maHD, Pageable pageable);
}
