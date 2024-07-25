package com.snackviet.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.NumberFormat;

import com.snackviet.groupValidation.FullValidationSanPhamGroup;

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
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "SanPham")
public class SanPham {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maSanPham;
	@Nationalized
	@Size(min = 5, message = "Tên sản phẩm không được ngắn hơn 5 ký tự",groups = {FullValidationSanPhamGroup.class})
	String tenSanPham;
	@DecimalMin(value = "0.1", message = "Giá sản phẩm phải lớn hơn 0!", groups = {FullValidationSanPhamGroup.class})
	double gia;
	@Nationalized
	String moTa;
	String hinhAnh;
	@Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn 0!", groups = {FullValidationSanPhamGroup.class})
	int soLuong;
	@DecimalMin(value = "0.1", message = "Trọng lượng sản phẩm phải lớn hơn 0!", groups = {FullValidationSanPhamGroup.class})
	double trongLuong;
	boolean trangThai = true;
	@Temporal(value = TemporalType.DATE)
	Date ngayThem;
	
	@ManyToOne @JoinColumn(name = "maLoai")
	LoaiSP loaiSP;	
	
	@OneToMany(mappedBy = "sanPhamHA")
	List<HinhAnhSP> listHinhAnhSP;
	
	@OneToMany(mappedBy = "sanPhamDG")
	List<DanhGia> listDanhGia;
	
	@OneToMany(mappedBy = "sanPhamCT")
	List<ChiTietHoaDon> lisTietHoaDon;
	
	@OneToMany(mappedBy = "sanPhamGH")
	List<ChiTietGioHang> listChiTietGioHang;
	
}
