package com.snackviet.dto.danhgia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter @Setter
public class CreateDanhGiaDTO {
    private int soSao;
	private String binhLuan;
    private Integer maSanPham;
    private Integer maTaiKhoan;
    private Integer maHoaDon;
}
