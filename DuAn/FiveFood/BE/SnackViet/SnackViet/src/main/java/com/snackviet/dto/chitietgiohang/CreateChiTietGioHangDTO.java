package com.snackviet.dto.chitietgiohang;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter @Setter
public class CreateChiTietGioHangDTO {
    private int soLuong;
    private Integer maTaiKhoan;
    private Integer maSanPham;
}
