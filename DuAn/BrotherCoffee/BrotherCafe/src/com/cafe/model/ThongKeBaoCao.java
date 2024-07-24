/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

import java.util.Date;

/**
 *
 * @author ASUS
 */
public class ThongKeBaoCao {

     private String maDV;
    private String maNV;
    private int maHD;
    private String tenSP;
    private int soLuongBan;
    private Double tongTien;
    private Date tuNgay;
    private Date denNgay;
    private Date NgayThanhToan;
    private String theoSanPham;

    public ThongKeBaoCao() {
    }

    public ThongKeBaoCao(String maDV, String maNV, int maHD, String tenSP, int soLuongBan, Double tongTien, Date tuNgay, Date denNgay, Date NgayThanhToan, String theoSanPham) {
        this.maDV = maDV;
        this.maNV = maNV;
        this.maHD = maHD;
        this.tenSP = tenSP;
        this.soLuongBan = soLuongBan;
        this.tongTien = tongTien;
        this.tuNgay = tuNgay;
        this.denNgay = denNgay;
        this.NgayThanhToan = NgayThanhToan;
        this.theoSanPham = theoSanPham;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getSoLuongBan() {
        return soLuongBan;
    }

    public void setSoLuongBan(int soLuongBan) {
        this.soLuongBan = soLuongBan;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public Date getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(Date tuNgay) {
        this.tuNgay = tuNgay;
    }

    public Date getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(Date denNgay) {
        this.denNgay = denNgay;
    }

    public Date getNgayThanhToan() {
        return NgayThanhToan;
    }

    public void setNgayThanhToan(Date NgayThanhToan) {
        this.NgayThanhToan = NgayThanhToan;
    }

    public String getTheoSanPham() {
        return theoSanPham;
    }

    public void setTheoSanPham(String theoSanPham) {
        this.theoSanPham = theoSanPham;
    }

}
