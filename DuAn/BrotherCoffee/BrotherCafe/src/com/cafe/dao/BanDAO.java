/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;

import com.cafe.model.Ban;
import com.cafe.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
/**
 *
 * @author ASUS
 */
public class BanDAO extends CafeDAO<Ban, String>{
    String INSERT_SQL = "INSERT INTO Ban (MaBan, TenBan, TrangThai, MaKV) VALUES (?, ?, ?, ?)";
    String UPDATE_SQL = "UPDATE Ban SET TenBan = ?, TrangThai = ?, MaKV = ? WHERE MaBan = ?";
    String DELETE_SQL = "DELETE FROM Ban WHERE MaBan = ?";
    String SELECT_ALL_SQL = "SELECT * FROM Ban";
    String SELECT_BY_ID_SQL = "SELECT * FROM Ban WHERE MaBan = ?";

    @Override
    public void insert(Ban e) {
        jdbcHelper.update(INSERT_SQL, e.getMaBan(), e.getTenBan(), e.getTrangThai(), e.getKhuVuc());
    }

    @Override
    public void update(Ban e) {
        jdbcHelper.update(UPDATE_SQL, e.getTenBan(), e.getTrangThai(), e.getKhuVuc(), e.getMaBan());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public Ban selectById(String id) {
        List<Ban> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (Ban ban : list) {
            if (!ban.getMaBan().equals(id)) {
                return null;
            }
        }
        return list.get(0);
    }

    @Override
    public List<Ban> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }



    @Override
    protected List<Ban> selectBySql(String sql, Object... args) {
        List<Ban> list = new ArrayList<Ban>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                Ban b = new Ban();
                b.setMaBan(rs.getString("MaBan"));
                b.setTenBan(rs.getString("TenBan"));
                b.setKhuVuc(rs.getString("MaKV"));
                b.setTrangThai(rs.getString("TrangThai"));
                list.add(b);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Ban> selectByMaKVTraVeBan(String keyword) {
        String sql = "SELECT * FROM Ban WHERE MaKV LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%");
    }
    public List<Ban> selectByKeyWordAndDV(String maDV ,String keyword) {
        String sql = "SELECT B.* FROM Ban B INNER JOIN KhuVuc K ON B.MaKV = K.MaKV WHERE K.MaDV LIKE ? AND Tenban LIKE ? OR";
        return this.selectBySql(sql, "%" +maDV, "%" + keyword + "%");
    }
     public List<Ban> selectByKeyWord(String keyword) {
        String sql = "SELECT B.* FROM Ban B WHERE Tenban LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%");
    }
    public List<Ban> selectByTenBan(String keyword) {
        String sql = "SELECT * FROM Ban WHERE Tenban LIKE ?";
        return this.selectBySql(sql,keyword);
    }
    public List<Ban> selectByMaDV(String keyword) {
        String sql = "SELECT B.* FROM Ban B INNER JOIN KhuVuc K ON B.MaKV = K.MaKV WHERE K.MaDV = ?";
        return this.selectBySql(sql,keyword);
    }
    public boolean chechTrungMa(String ma) {
        List<Ban> list = this.selectAll();
        for (Ban ban : list) {
            if (ban.getMaBan().equals(ma)) {
                return false;
            }
        }
        return true;
    }
    public Ban selectByTenBanTraVeBan(String tenBan, String maKV) {
        String sql = "SELECT b.* FROM Ban b INNER JOIN KhuVuc kv ON b.MaKV = kv.MaKV WHERE b.Tenban LIKE ? AND kv.MaDV LIKE ?";
        List<Ban> list = this.selectBySql(sql, tenBan,maKV);
        if (list.isEmpty()) {
            return null;
        }
//        for (Ban ban : list) {
//            if (!ban.getMaBan().equals(id)) {
//                return null;
//            }
//        }
        return list.get(0);
    }
    
}
