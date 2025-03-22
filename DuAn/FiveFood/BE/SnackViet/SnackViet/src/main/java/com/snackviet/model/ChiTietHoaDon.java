package com.snackviet.model;

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

@Data
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "ChiTietHoaDon")
public class ChiTietHoaDon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maChiTietHoaDon;
	int soLuong;
	double gia;

	@ManyToOne @JoinColumn(name = "maHoaDon")
	HoaDon hoaDonCT;
	
	@ManyToOne @JoinColumn(name = "maSanPham")
	SanPham sanPhamCT;
}
