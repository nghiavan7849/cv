package com.snackviet.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "DiaChi")
public class DiaChi {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maDiaChi;
	@Nationalized
	String hoVaTen;
	String soDienThoai;
	boolean trangThai;
	String diaChi;
	String diaChiChiTiet;
	int maTinhThanh;
	int maQuanHuyen;
	int maXaPhuong;
	boolean trangThaiXoa = false;
	
	@ToString.Exclude
	@ManyToOne @JoinColumn(name = "maTaiKhoan")
	TaiKhoan taiKhoanDC;
}
