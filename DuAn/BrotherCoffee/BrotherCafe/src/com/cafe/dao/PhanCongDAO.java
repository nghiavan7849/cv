/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;

import com.cafe.model.NhanVien;
import com.cafe.model.PhanCong;
import com.cafe.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PhanCongDAO extends CafeDAO<PhanCong, String> {

    String INSERT_SQL = "INSERT INTO PhanCong (MaPC, NgayLam, TenCa, GioBatDau, GioKetThuc, GhiChu, MaNV) VALUES (?, ?, ?, ?, ?, ?,?)";
    String UPDATE_SQL = "UPDATE PhanCong SET NgayLam = ?, TenCa = ?, GioBatDau = ?, GioKetThuc = ?, GhiChu = ?, MaNV = ? WHERE MaPC = ?";
    String DELETE_SQL = "DELETE FROM PhanCong WHERE MaPC = ?";
    String SELECT_ALL_SQL = "SELECT * FROM PhanCong";
    String SELECT_BY_ID_SQL = "SELECT * FROM PhanCong WHERE MaPC = ?";

    @Override
    public void insert(PhanCong pc) {
        jdbcHelper.update(INSERT_SQL, pc.getMaPC(), pc.getNgayLam(), pc.getTenCa(), pc.getGioBatDau(), pc.getGioKetThuc(), pc.getGhiChu(), pc.getMaNV());
    }

    @Override
    public void update(PhanCong pc) {
        System.out.println(pc.getMaPC());
        jdbcHelper.update(UPDATE_SQL, pc.getNgayLam(), pc.getTenCa(), pc.getGioBatDau(), pc.getGioKetThuc(), pc.getGhiChu(), pc.getMaNV(), pc.getMaPC());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public PhanCong selectById(String id) {
        List<PhanCong> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (PhanCong nv : list) {
            if (!nv.getMaPC().equals(id)) {
                return null;
            }
        }
        return list.get(0);
    }

    @Override
    public List<PhanCong> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<PhanCong> selectBySql(String sql, Object... args) {
        List<PhanCong> list = new ArrayList<PhanCong>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                PhanCong pc = new PhanCong();
                pc.setMaPC(rs.getString("MaPC"));
                pc.setNgayLam(rs.getDate("NgayLam"));
                pc.setTenCa(rs.getInt("TenCa"));
                pc.setGioBatDau(rs.getString("GioBatDau"));
                pc.setGioKetThuc(rs.getString("GioKetThuc"));
                pc.setGhiChu(rs.getString("GhiChu"));
                pc.setMaNV(rs.getString("MaNV"));
                list.add(pc);
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<PhanCong> selectByKeyWord(String keyword) {
        String sql = "SELECT * FROM PhanCong WHERE MaNV LIKE ? OR TenCa LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%", "%" + keyword + "%");
    }
    
    }
