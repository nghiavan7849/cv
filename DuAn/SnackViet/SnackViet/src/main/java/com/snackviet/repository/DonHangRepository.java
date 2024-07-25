package com.snackviet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.snackviet.model.DonHang;

public interface DonHangRepository extends JpaRepository<DonHang, String> {

    @Query(value = "SELECT hd.maHoaDon, COUNT(cthd.maSanPham) AS tongSanPham, dc.diaChi, tthd.tenTrangThai, (hd.tongTien + hd.phiVanChuyen) AS tongTienVaPhiVanChuyen "
            + "FROM HoaDon hd "
            + "INNER JOIN TrangThaiHoaDon tthd ON hd.maTrangThaiHoaDon = tthd.maTrangThaiHoaDon "
            + "INNER JOIN TaiKhoan tk ON tk.maTaiKhoan = hd.maTaiKhoan "
            + "INNER JOIN DiaChi dc ON dc.maTaiKhoan = tk.maTaiKhoan "
            + "INNER JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon "
            + "INNER JOIN SanPham sp ON sp.maSanPham = cthd.maSanPham "
            + "WHERE tk.maTaiKhoan = :maTaiKhoan "
            + "AND tthd.tenTrangThai = :trangThai "
            + "GROUP BY hd.maHoaDon, dc.diaChi, tthd.tenTrangThai, hd.tongTien, hd.phiVanChuyen", nativeQuery = true)
    List<DonHang> hoadonMuaHang(@Param("maTaiKhoan") Integer maTaiKhoan, @Param("trangThai") String trangThai);
}
