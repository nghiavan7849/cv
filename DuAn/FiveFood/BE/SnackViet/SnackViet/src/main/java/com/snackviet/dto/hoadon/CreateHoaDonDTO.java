package com.snackviet.dto.hoadon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter @Setter
public class CreateHoaDonDTO {
    private Integer maHoaDon;
	private double phiVanChuyen;
	private double tongTien;
	private boolean phuongThucThanhToan;
    private boolean trangThai;
    private String ghiChu;
    private String lyDoHuy;
    private String diaChiNhan;
    private String dcHoTen;
    private String dcSoDienThoai;
    private Integer maTaiKhoan;
    private Integer maTrangThaiHoaDon;

}
