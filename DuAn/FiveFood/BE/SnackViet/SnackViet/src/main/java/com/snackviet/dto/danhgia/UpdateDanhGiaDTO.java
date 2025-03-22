package com.snackviet.dto.danhgia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter @Setter
public class UpdateDanhGiaDTO {
    private Integer maDanhGia;
    private int soSao;
	private String binhLuan;
    private Integer maSanPham;
    private Integer maTaiKhoan;
}
