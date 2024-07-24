/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

/**
 *
 * @author ADMIN
 */
public class KhachHang {
    private String maKH;
    private String tenKH;
    private String SDT;
    private String email;
    private boolean gioiTinh;
    private String diaChi;

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public KhachHang(String maKH, String tenKH, String SDT, String email, boolean gioiTinh, String diaChi) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.SDT = SDT;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.diaChi = diaChi;
    }

    
    public KhachHang() {
    }
}
