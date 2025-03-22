package com.snackviet.model;

import java.util.List;

import org.hibernate.annotations.Nationalized;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "LoaiSP")
public class LoaiSP {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer maLoai;
	@NotEmpty(message = "Vui lòng nhập tên loại!!!")
	@Nationalized
	String tenLoai;
	boolean trangThai;
	@Transient // Chỉ sử dụng để tính toán, không lưu vào DB
    private int totalProducts;
	
	@OneToMany(mappedBy = "loaiSP")
	@JsonIgnore
	List<SanPham> listLoaiSP;
	
	@Override
    public String toString() {
        return "LoaiSP{" +
                "maLoai=" + maLoai +
                ", tenLoai='" + tenLoai + '\'' +
				", trangThai='" + trangThai + '\'' +
                '}';
    }
	
}
