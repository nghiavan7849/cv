package com.snackviet.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "HinhAnhSP")
public class HinhAnhSP {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maHinhAnhSP;
	String tenHinhAnh;
	
	@ManyToOne @JoinColumn(name = "maSanPham")
	SanPham sanPhamHA;
}
