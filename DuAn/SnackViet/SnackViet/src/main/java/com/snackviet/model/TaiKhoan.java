package com.snackviet.model;

import java.util.List;

import org.hibernate.annotations.Nationalized;

import com.snackviet.groupValidation.FullValidationTaiKhoanGroup;
import com.snackviet.groupValidation.DangNhapGroup;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
@Table(name = "TaiKhoan")
public class TaiKhoan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maTaiKhoan;
	@NotEmpty(message = "Vui lòng nhập tên đăng nhập", groups = {DangNhapGroup.class,FullValidationTaiKhoanGroup.class})
	String tenDangNhap;
	@NotEmpty(message = "Vui lòng nhập mật khẩu" , groups = {DangNhapGroup.class,FullValidationTaiKhoanGroup.class})
	@Size(min = 8,message = "Mật khẩu phải có tối đa 8 ký tự", groups = {DangNhapGroup.class,FullValidationTaiKhoanGroup.class})
	String matKhau;
	@NotEmpty(message = "Vui lòng nhập email", groups = {FullValidationTaiKhoanGroup.class})
	@Email(message = "Vui lòng nhập đúng định dạng email", groups = {FullValidationTaiKhoanGroup.class})
	String email;
	@Nationalized
	@NotEmpty(message = "Vui lòng nhập họ và tên", groups = {FullValidationTaiKhoanGroup.class})
	String hoVaTen;
	String soDienThoai;
	boolean gioiTinh = true;
	String hinhAnh;
	boolean trangThai = true;
	boolean vaiTro = false;
	@ToString.Exclude
	@OneToMany(mappedBy = "taiKhoanDG")
	List<DanhGia> listDanhGia;
	@ToString.Exclude
	@OneToMany(mappedBy = "taiKhoanDC")
	List<DiaChi> listDiaChi;
	@ToString.Exclude
	@OneToMany(mappedBy = "taiKhoanHD")
	List<HoaDon> listHoaDon;
	@ToString.Exclude
	@OneToMany(mappedBy = "taiKhoanGH")
	List<ChiTietGioHang> lisChiTietGioHang;
	@ToString.Exclude
	@OneToMany(mappedBy = "taiKhoanGD")
	List<GiaoDich> lisGiaoDich;
	
}