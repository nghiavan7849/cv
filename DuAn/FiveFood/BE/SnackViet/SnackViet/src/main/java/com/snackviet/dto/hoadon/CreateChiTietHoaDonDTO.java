package com.snackviet.dto.hoadon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class CreateChiTietHoaDonDTO {
    private int soLuong;
    private double gia;
    private Integer maHoaDon;
    private Integer maSanPham;
}
