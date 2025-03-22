package com.snackviet.model;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DonHang implements Serializable {
	@Id
	String maHoaDon;
	// String hinhAnh;
	// String tenSanPham;
	Integer tongSanPham;
	String diaChiNhan;
	String tenTrangThai;
	double tongTien;
	String lyDoHuy;	
}
