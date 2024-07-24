/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

/**
 *
 * @author NGHIA
 */
public class DonVi {
    private String maDV;
    private String tenDV;
    private String diaChi;
    private String email;
    private String SDT;
    private String website;

    public DonVi() {
    }

    public DonVi(String maDV, String tenDV, String diaChi, String email, String SDT, String website) {
        this.maDV = maDV;
        this.tenDV = tenDV;
        this.diaChi = diaChi;
        this.email = email;
        this.SDT = SDT;
        this.website = website;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    public String getTenDV() {
        return tenDV;
    }

    public void setTenDV(String tenDV) {
        this.tenDV = tenDV;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    
    
}
