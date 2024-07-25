package com.snackviet.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "HinhAnhDG")
public class HinhAnhDG {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maHinhAnhDG;
	String tenHinhAnh;
	
	@ManyToOne @JoinColumn(name = "maDanhGia")
	DanhGia danhGia;
	
}
