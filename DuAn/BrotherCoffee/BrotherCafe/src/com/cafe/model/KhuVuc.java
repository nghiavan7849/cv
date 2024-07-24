/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.model;

/**
 *
 * @author ADMIN
 */
public class KhuVuc {
    private String maKV;
    private String tenKV;
    private String moTa;
    private String maDV;

    public String getMaKV() {
        return maKV;
    }

    public void setMaKV(String maKV) {
        this.maKV = maKV;
    }

    public String getTenKV() {
        return tenKV;
    }

    public void setTenKV(String tenKV) {
        this.tenKV = tenKV;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getMaDV() {
        return maDV;
    }

    public void setMaDV(String maDV) {
        this.maDV = maDV;
    }

    

    public KhuVuc() {
    }

    public KhuVuc(String maKV, String tenKV, String moTa, String maDV) {
        this.maKV = maKV;
        this.tenKV = tenKV;
        this.moTa = moTa;
        this.maDV = maDV;
    }


    
}
