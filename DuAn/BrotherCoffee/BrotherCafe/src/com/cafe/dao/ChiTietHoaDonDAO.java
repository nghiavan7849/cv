/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;

import com.cafe.model.ChiTietHoaDon;
import com.cafe.model.HoaDon;
import com.cafe.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NGHIA
 */
public class ChiTietHoaDonDAO extends CafeDAO<ChiTietHoaDon, Integer> {

    String INSERT_SQL = "INSERT INTO ChiTietHoaDon (MaHD,MaSP,SoLuong,TongTienSP,GhiChu) VALUES (?, ?, ?, ?, ?)";
    String UPDATE_SQL = "UPDATE ChiTietHoaDon SET MaHD = ? , MaSP = ?, SoLuong = ?,  TongTienSP = ?,  GhiChu = ? WHERE MaCTHD = ?";
    String DELETE_SQL = "DELETE FROM ChiTietHoaDon WHERE MaCTHD = ?";
    String SELECT_ALL_SQL = "SELECT * FROM ChiTietHoaDon";
    String SELECT_BY_ID_SQL = "SELECT * FROM ChiTietHoaDon WHERE MaCTHD = ?";
    String UPDATE_SL_SP_SQL = "UPDATE ChiTietHoaDon SET SoLuong = ?,  TongTienSP = ? WHERE MaCTHD = ?";

    @Override
    public void insert(ChiTietHoaDon e) {
        jdbcHelper.update(INSERT_SQL, e.getMaHD(),e.getMaSP(),e.getSoLuong(),e.getTongTienSP(),e.getGhiChu());
    }

    @Override
    public void update(ChiTietHoaDon e) {
        jdbcHelper.update(UPDATE_SQL, e.getMaHD(),e.getMaSP(),e.getSoLuong(),e.getTongTienSP(),e.getGhiChu(),e.getMaCTHD());
    
    }
    public void updateSLSP(ChiTietHoaDon e) {
            jdbcHelper.update(UPDATE_SL_SP_SQL, e.getSoLuong(),e.getTongTienSP(),e.getMaCTHD());

        }
    @Override
    public void delete(Integer id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public ChiTietHoaDon selectById(Integer id) {
           List<ChiTietHoaDon> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (ChiTietHoaDon ban : list) {
            if (ban.getMaCTHD()!= id) {
                return null;
            }
        }
        return list.get(0);
    }

    @Override
    public List<ChiTietHoaDon> selectAll() {
      return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<ChiTietHoaDon> selectBySql(String sql, Object... args) {
        List<ChiTietHoaDon> list = new ArrayList<ChiTietHoaDon>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setMaCTHD(rs.getInt("MaCTHD"));
                cthd.setMaHD(rs.getInt("MaHD"));
                cthd.setMaSP(rs.getString("MaSP"));
                cthd.setSoLuong(rs.getInt("SoLuong"));
                cthd.setTongTienSP(rs.getDouble("TongTienSP"));
                cthd.setGhiChu(rs.getString("GhiChu"));
         
                list.add(cthd);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<ChiTietHoaDon> selectByMaHD(Integer keyword) {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHD LIKE ?";
        return this.selectBySql(sql,keyword);
    }
    public List<ChiTietHoaDon> selectByMaBan(String keyword) {
        String sql = "SELECT cthd.* FROM ChiTietHoaDon cthd INNER JOIN HoaDon hd ON cthd.MaHD = hd.MaHD  WHERE MaBan LIKE ? AND hd.TrangThai = 0";
        return this.selectBySql(sql,keyword);
    }
    public List<ChiTietHoaDon> selectByMaSP(String keyword) {
        String sql = "SELECT * FROM ChiTietHoaDon WHERE MaSP LIKE ?";
        return this.selectBySql(sql,keyword);
    }
     public ChiTietHoaDon selectByTenSP(Integer id) {
         String sql = "SELECT * FROM ChiTietHoaDon WHERE MaCTHD = ?";
           List<ChiTietHoaDon> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (ChiTietHoaDon ban : list) {
            if (ban.getMaCTHD()!= id) {
                return null;
            }
        }
        return list.get(0);
    }
}
