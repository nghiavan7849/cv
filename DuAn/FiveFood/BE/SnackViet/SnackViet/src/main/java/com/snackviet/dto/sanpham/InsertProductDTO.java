package com.snackviet.dto.sanpham;

import java.util.Date;
import java.util.List;

import com.snackviet.model.HinhAnhSP;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InsertProductDTO {
    String tenSanPham;
    double gia;
    String moTa;
    String hinhAnh;
    List<String> listHinhAnhSP;
    double trongLuong;
    boolean trangThai;
    Date ngayThem = new Date();
    String loaiSP;
}
