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
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "HoaDon")
public class HoaDon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maHoaDon;
	double phiVanChuyen;
	double tongTien;
	boolean phuongThucThanhToan;
	@Temporal(TemporalType.DATE)
	Date ngayThanhToan = new Date();
	@Nationalized
	String ghiChu;
	String lyDoHuy;
	boolean trangThai;
	String diaChiNhan;
	String dcHoTen;
	String dcSoDienThoai;
	
	@ToString.Exclude
	@ManyToOne @JoinColumn(name = "maTaiKhoan")
	TaiKhoan taiKhoanHD;
	
	@ToString.Exclude
	@ManyToOne @JoinColumn(name = "maTrangThaiHoaDon")
	TrangThaiHoaDon trangThaiHoaDon;

	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "hoaDonCT")
	List<ChiTietHoaDon> listChiTietHoaDon;

	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "hoaDonGD")
	List<GiaoDich> listGiaoDich;

	@JsonIgnore
	@ToString.Exclude
	@OneToMany(mappedBy = "hoaDonDG")
	List<DanhGia> listDanhGia;

	@PrePersist
    @PreUpdate
    public void prePersistOrUpdate() {
        if (String.valueOf(phiVanChuyen).equals(null)) {
            phiVanChuyen = 0.0;
        }
        if (String.valueOf(tongTien).equals(null)) {
            tongTien = 0.0;
        }
    }
	
}
	
