/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

/**
 *
 * @author NGHIA
 */
public class ChiTietHoaDon{
    private int maCTHD;
    private int maHD;
    private String maSP;
    private int soLuong;
    private Double tongTienSP;
    private String ghiChu;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(int maCTHD, int soLuong, Double tongTienSP) {
        this.maCTHD = maCTHD;
        this.soLuong = soLuong;
        this.tongTienSP = tongTienSP;
    }
    
    public ChiTietHoaDon(int maCTHD, int maHD, String maSP, int soLuong, Double tongTienSP, String ghiChu) {
        this.maCTHD = maCTHD;
        this.maHD = maHD;
        this.maSP = maSP;
        this.soLuong = soLuong;
        this.tongTienSP = tongTienSP;
        this.ghiChu = ghiChu;
    }

    public int getMaCTHD() {
        return maCTHD;
    }

    public void setMaCTHD(int maCTHD) {
        this.maCTHD = maCTHD;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Double getTongTienSP() {
        return tongTienSP;
    }

    public void setTongTienSP(Double tongTienSP) {
        this.tongTienSP = tongTienSP;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
    
}
