package com.snackviet.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "GiaoDich")
public class GiaoDich {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maGiaoDich;
	String codeGiaoDich;
	String soTaiKhoan;
	String hoVaTen;
	@Temporal(TemporalType.DATE)
	Date ngayGiaoDich;
	boolean trangThai;
	double soTien;
	
	@ManyToOne @JoinColumn(name = "maTaiKhoan")
	TaiKhoan taiKhoanGD;
	
	@ManyToOne @JoinColumn(name = "maHoaDon")
	HoaDon hoaDonGD;
	
}
