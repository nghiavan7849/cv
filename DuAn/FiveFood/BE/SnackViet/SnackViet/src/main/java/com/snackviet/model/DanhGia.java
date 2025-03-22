package com.snackviet.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "DanhGia")
@ToString(exclude = {"listHinhAnhDG", "listPhanHoiDanhGia"})
public class DanhGia {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maDanhGia;
	@Temporal(TemporalType.DATE)
	Date ngayDanhGia;
	int soSao;
	@Nationalized
	String binhLuan;
	boolean trangThai;
	
	@JsonIgnore
	@OneToMany(mappedBy = "danhGia")
	List<HinhAnhDG> listHinhAnhDG;

	@JsonIgnore
	@OneToMany(mappedBy = "maDanhGia")
	List<PhanHoiDanhGia> listPhanHoiDanhGia;
	
	@ManyToOne @JoinColumn(name = "maSanPham")
	SanPham sanPhamDG;
	
	@ManyToOne @JoinColumn(name = "maTaiKhoan")
	TaiKhoan taiKhoanDG;

	@ManyToOne @JoinColumn(name = "maHoaDon")
	HoaDon hoaDonDG;
}
