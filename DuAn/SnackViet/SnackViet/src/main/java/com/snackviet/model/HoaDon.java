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
	String diaChi;
	@Nationalized
	String ghiChu;
	@ToString.Exclude
	@ManyToOne @JoinColumn(name = "maTaiKhoan")
	TaiKhoan taiKhoanHD;
	@ToString.Exclude
	@ManyToOne @JoinColumn(name = "maTrangThaiHoaDon")
	TrangThaiHoaDon trangThaiHoaDon;
	@ToString.Exclude
	@OneToMany(mappedBy = "hoaDonCT")
	List<ChiTietHoaDon> listChiTietHoaDon;
	@ToString.Exclude
	@OneToMany(mappedBy = "hoaDonGD")
	List<GiaoDich> listGiaoDich;
	
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
	
