/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

/**
 *
 * @author ASUS
 */
public class Ban {
    
    private String maBan;
    private String tenBan;
    private String trangThai;
    private String khuVuc;

    public Ban() {
    }

    public Ban(String maBan, String tenBan, String trangThai, String khuVuc) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.trangThai = trangThai;
        this.khuVuc = khuVuc;
    }

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
        this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getKhuVuc() {
        return khuVuc;
    }

    public void setKhuVuc(String khuVuc) {
        this.khuVuc = khuVuc;
    }

    
}
