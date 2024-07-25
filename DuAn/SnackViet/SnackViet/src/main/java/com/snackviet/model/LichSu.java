package com.snackviet.model;

import java.io.Serializable;


import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class LichSu implements Serializable {
    @Id
    Integer maHoaDon;
    String hinhAnh;
    String tenSanPham;
    int soLuong;
    double tongTien;
    Integer maSanPham;
    
}

