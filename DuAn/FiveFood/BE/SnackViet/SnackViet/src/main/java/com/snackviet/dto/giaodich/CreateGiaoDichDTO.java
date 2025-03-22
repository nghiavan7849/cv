package com.snackviet.dto.giaodich;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class CreateGiaoDichDTO {
	private String codeGiaoDich;
	private String soTaiKhoan;
	private String hoVaTen;
	private String ngayGiaoDich;
	private boolean trangThai;
	private double soTien;	
	private Integer maTaiKhoan;	
	private Integer maHoaDon;
}
