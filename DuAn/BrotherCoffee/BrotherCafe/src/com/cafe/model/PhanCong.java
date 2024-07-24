/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author VICTUSS
 */
public class PhanCong {
    private String maPC;
    private Date ngayLam;
    private int tenCa;
    private String gioBatDau;
    private String gioKetThuc;
    private String ghiChu;
    private String maNV;

    public String getMaPC() {
        return maPC;
    }

    public void setMaPC(String maPC) {
        this.maPC = maPC;
    }

    public Date getNgayLam() {
        return ngayLam;
    }

    public void setNgayLam(Date ngayLam) {
        this.ngayLam = ngayLam;
    }

    public int getTenCa() {
        return tenCa;
    }

    public void setTenCa(int tenCa) {
        this.tenCa = tenCa;
    }

    public String getGioBatDau() {
        return gioBatDau;
    }

    public void setGioBatDau(String gioBatDau) {
        this.gioBatDau = gioBatDau;
    }

    public String getGioKetThuc() {
        return gioKetThuc;
    }

    public void setGioKetThuc(String gioKetThuc) {
        this.gioKetThuc = gioKetThuc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }
    
    @Override
    public String toString() {
        return "PhanCong [maPC=" + maPC + ", ngayLam=" + ngayLam + ", tenCa=" + tenCa
                + ", gioBatDau=" + new SimpleDateFormat("HH:mm").format(gioBatDau)
                + ", gioKetThuc=" + new SimpleDateFormat("HH:mm").format(gioKetThuc)
                + ", ghiChu=" + ghiChu + ", maNV=" + maNV + "]";
    }
    
}
