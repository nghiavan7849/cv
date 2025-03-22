package com.snackviet.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "PhanHoiDanhGia")
public class PhanHoiDanhGia {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maPhanHoiDanhGia;
    private String noiDungPhanHoi;
    private boolean daPhanHoi;

    @ManyToOne @JoinColumn(name = "maDanhGia", referencedColumnName = "maDanhGia")
    private DanhGia maDanhGia;

    @ManyToOne @JoinColumn(name = "maTaiKhoan", referencedColumnName = "maTaiKhoan")
    private TaiKhoan maTaiKhoan;
}
