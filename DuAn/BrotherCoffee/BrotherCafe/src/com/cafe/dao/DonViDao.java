/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;

import com.cafe.model.DonVi;
import com.cafe.utils.jdbcHelper;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

/**
 *
 * @author NGHIA
 */
public class DonViDao extends CafeDAO<DonVi, String> {
    
    String INSERT_SQL = "INSERT INTO DonVi (MaDV, TenDV, DiaChi, SDT, Email, Website) VALUES (?, ?, ?, ?, ?, ?)";
    String UPDATE_SQL = "UPDATE DonVi SET TenDV = ?, DiaChi = ?, SDT = ?, Email = ?, Website = ? WHERE MaDV = ?";
    String DELETE_SQL = "DELETE FROM DonVi WHERE MaDV = ?";
    String SELECT_ALL_SQL = "SELECT * FROM DonVi";
    String SELECT_BY_ID_SQL = "SELECT * FROM DonVi WHERE MaDV = ?";
        
    
    @Override
    public void insert(DonVi dv) {
        jdbcHelper.update(INSERT_SQL, dv.getMaDV(), dv.getTenDV(), dv.getDiaChi(), dv.getSDT(), dv.getEmail(), dv.getWebsite());
    }

    @Override
    public void update(DonVi dv) {
        System.out.println(dv.getMaDV());
        jdbcHelper.update(UPDATE_SQL, dv.getTenDV(), dv.getDiaChi(), dv.getSDT(), dv.getEmail(), dv.getWebsite(), dv.getMaDV());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public DonVi selectById(String id) {
        List<DonVi> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (DonVi nv : list) {
            if (!nv.getMaDV().equals(id)) {
                return null;
            }
        }
        return list.get(0);
    }

    @Override
    public List<DonVi> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<DonVi> selectBySql(String sql, Object... args) {
        List<DonVi> list = new ArrayList<DonVi>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {                
                DonVi dv = new DonVi();
                dv.setMaDV(rs.getString("MaDV"));
                dv.setTenDV(rs.getString("TenDV"));
                dv.setDiaChi(rs.getString("DiaChi"));
                dv.setEmail(rs.getString("Email"));
                dv.setSDT(rs.getString("SDT"));
                dv.setWebsite(rs.getString("Website"));
                list.add(dv);
            }
            return list;
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
     public List<DonVi> selectByKeyWord(String keyword) {
        String sql = "SELECT * FROM DonVi WHERE TenDV LIKE ? OR DiaChi LIKE ? OR SDT LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%", "%" + keyword + "%","%" + keyword + "%" );
    }
     public List<DonVi> selectByTenDV(String keyword) {
        String sql = "SELECT * FROM DonVi WHERE tenDV LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%");
    }
}
