/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

import java.util.Date;

/**
 *
 * @author NGHIA
 */
public class HoaDon {

    private int maHD;
    private Date ngayDatBan;
    private Date ngayThanhToan;
    private String thoiGianTaoHD;
    private String thoiGianThanhToan;
    private Double tongTien;
    private boolean trangThai;
    private String maNV;
    private String maBan;
    private String maKH;

    public HoaDon() {
    }

    public HoaDon(int maHD, Double tongTien) {
        this.maHD = maHD;
        this.tongTien = tongTien;
    }

    public HoaDon(int maHD, String maBan) {
        this.maHD = maHD;
        this.maBan = maBan;
    }

    public HoaDon(int maHD, Date ngayDatBan) {
        this.maHD = maHD;
        this.ngayDatBan = ngayDatBan;
    }
    

    public HoaDon(int maHD, Date ngayDatBan, Date ngayThanhToan, String thoiGianTaoHD, String thoiGianThanhToan, Double tongTien, boolean trangThai, String maNV, String maBan, String maKH) {
        this.maHD = maHD;
        this.ngayDatBan = ngayDatBan;
        this.ngayThanhToan = ngayThanhToan;
        this.thoiGianTaoHD = thoiGianTaoHD;
        this.thoiGianThanhToan = thoiGianThanhToan;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
        this.maNV = maNV;
        this.maBan = maBan;
        this.maKH = maKH;
    }

   
    
    

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public Date getNgayDatBan() {
        return ngayDatBan;
    }

    public void setNgayDatBan(Date ngayDatBan) {
        this.ngayDatBan = ngayDatBan;
    }

    public Date getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(Date ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public String getThoiGianTaoHD() {
        return thoiGianTaoHD;
    }

    public void setThoiGianTaoHD(String thoiGianTaoHD) {
        this.thoiGianTaoHD = thoiGianTaoHD;
    }

    public String getThoiGianThanhToan() {
        return thoiGianThanhToan;
    }

    public void setThoiGianThanhToan(String thoiGianThanhToan) {
        this.thoiGianThanhToan = thoiGianThanhToan;
    }

   
   

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

}
