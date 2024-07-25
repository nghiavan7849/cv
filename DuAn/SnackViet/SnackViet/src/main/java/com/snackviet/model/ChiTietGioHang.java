package com.snackviet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
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
@Table(name = "ChiTietGioHang")
public class ChiTietGioHang {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maChiTietGioHang;
	int soLuong;
	@ToString.Exclude
	@ManyToOne @JoinColumn(name = "maSanPham")
	SanPham sanPhamGH;
	@ToString.Exclude
	@ManyToOne @JoinColumn(name = "maTaiKhoan")
	TaiKhoan taiKhoanGH;
}
