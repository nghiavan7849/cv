/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;

import com.cafe.model.KhuVuc;
import com.cafe.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.impl.jam.JPackage;

/**
 *
 * @author ADMIN
 */
public class KhuVucDAO extends CafeDAO<KhuVuc, String> {

    String INSERT_SQL = "INSERT INTO KhuVuc (MaKV, TenKV, MoTa, MaDV) VALUES (?, ?, ?, ?)";
    String UPDATE_SQL = "UPDATE KhuVuc SET TenKV = ?, MoTa = ?, MaDV = ? WHERE MaKV = ?";
    String DELETE_SQL = "DELETE FROM KhuVuc WHERE MaKV = ?";
    String SELECT_ALL_SQL = "SELECT * FROM KhuVuc";
    String SELECT_BY_ID_SQL = "SELECT * FROM KhuVuc WHERE MaKV = ?";

    @Override
    public void insert(KhuVuc e) {
        jdbcHelper.update(INSERT_SQL, e.getMaKV(), e.getTenKV(), e.getMoTa(), e.getMaDV());
    }

    @Override
    public void update(KhuVuc e) {
        jdbcHelper.update(UPDATE_SQL, e.getTenKV(), e.getMoTa(), e.getMaDV(), e.getMaKV());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public KhuVuc selectById(String id) {
        List<KhuVuc> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (KhuVuc nv : list) {
            if (!nv.getMaKV().equals(id)) {
                return null;
            }
        }
        return list.get(0);
    }

    @Override
    public List<KhuVuc> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<KhuVuc> selectBySql(String sql, Object... args) {
        List<KhuVuc> list = new ArrayList<KhuVuc>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                KhuVuc entity = new KhuVuc();
                entity.setMaKV(rs.getString("MaKV"));
                entity.setTenKV(rs.getString("TenKV"));
                entity.setMoTa(rs.getString("MoTa"));
                entity.setMaDV(rs.getString("MaDV"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<KhuVuc> selectByKeyWordAndDV(String maDV, String keyword) {
        String sql = "SELECT * FROM KhuVuc WHERE MaDV LIKE ? AND TenKV LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%", "%" + keyword + "%");
    }
    public List<KhuVuc> selectByKeyWord(String keyword) {
        String sql = "SELECT * FROM KhuVuc WHERE TenKV LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%");
    }
    public List<KhuVuc> selectByMaDV(String keyword) {
        String sql = "SELECT * FROM KhuVuc WHERE MaDV = ?";
        return this.selectBySql(sql, keyword);
    }

    public List<KhuVuc> selectByTenKVandDV(String keyword, String maDV) {
        String sql = "SELECT * FROM KhuVuc WHERE TenKV LIKE ? AND MaDV LIKE ? ";
        return this.selectBySql(sql, "%" + keyword + "%","%" + maDV + "%");
    }
    public List<KhuVuc> selectByTenKV(String keyword) {
        String sql = "SELECT * FROM KhuVuc WHERE TenKV LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%");
    }
    public boolean chechTrungMa(String ma) {
        List<KhuVuc> list = this.selectAll();
        for (KhuVuc nv : list) {
            if (nv.getMaKV().equals(ma)) {
                return false;
            }
        }
        return true;
    }

}
