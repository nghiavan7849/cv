/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;

import com.cafe.model.SanPham;
import com.cafe.utils.jdbcHelper;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author ASUS
 */
public class SanPhamDAO extends CafeDAO<SanPham, String>{

    String INSERT_SQL = "INSERT INTO SanPham (MaSP, TenSP, LoaiSP, Gia, HinhAnh, GioiThieu) VALUES (?, ?, ?, ?, ?, ?)";
    String UPDATE_SQL = "UPDATE SanPham SET TenSP = ?, LoaiSP = ?, Gia = ?, HinhAnh = ?, GioiThieu = ? WHERE MaSP = ?";
    String DELETE_SQL = "DELETE FROM SanPham WHERE MaSP = ?";
    String SELECT_ALL_SQL = "SELECT * FROM SanPham";
    String SELECT_BY_ID_SQL = "SELECT * FROM SanPham WHERE MaSP = ?";
    String SELECT_ALL_LOC_GIA_SQL = "SELECT * FROM SanPham WHERE Gia BETWEEN ? AND ?";

    @Override
    public void insert(SanPham e) {
        jdbcHelper.update(INSERT_SQL, e.getMaSP(), e.getTenSP(), e.getLoaiSP(), e.getGia(), e.getHinhAnh(), e.getGioiThieu());
    }

    @Override
    public void update(SanPham e) {
        jdbcHelper.update(UPDATE_SQL, e.getTenSP(), e.getLoaiSP(), e.getGia(), e.getHinhAnh(), e.getGioiThieu(), e.getMaSP());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public SanPham selectById(String id) {
        List<SanPham> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (SanPham nv : list) {
            if (!nv.getMaSP().equals(id)) {
                return null;
            }
        }
        return list.get(0);
    }

    @Override
    public List<SanPham> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    protected List<SanPham> selectBySql(String sql, Object... args) {
        List<SanPham> list = new ArrayList<SanPham>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                SanPham entity = new SanPham();
                entity.setMaSP(rs.getString("MaSP"));
                entity.setTenSP(rs.getString("TenSP"));
                entity.setLoaiSP(rs.getString("LoaiSP"));
                entity.setGia(rs.getDouble("Gia"));
                entity.setHinhAnh(rs.getString("HinhAnh"));
                entity.setGioiThieu(rs.getString("GioiThieu"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<SanPham> selectByKeyWord(String keyword) {
        String sql = "SELECT * FROM SanPham WHERE tenSP LIKE ? OR maSP LIKE ? OR loaiSP LIKE ? OR gia LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%");
    }

    public boolean chechTrungMa(String ma) {
        List<SanPham> list = this.selectAll();
        for (SanPham nv : list) {
            if (nv.getMaSP().equals(ma)) {
                return false;
            }
        }
        return true;
    }

    public List<SanPham> locGiaSP(String giaNho, String giaLon) {
        return this.selectBySql(SELECT_ALL_LOC_GIA_SQL, giaNho, giaLon);
    }

    public SanPham selectByMaSPTraVeTenSP(String keyword) {
        String sql = "SELECT * FROM SanPham WHERE MaSP = ?";
        List<SanPham> list = this.selectBySql(sql, keyword);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public SanPham selectByTenSPTraveMaSP(String keyword) {
        String sql = "SELECT * FROM SanPham WHERE TenSP = ?";
        List<SanPham> list = this.selectBySql(sql, keyword);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public List<SanPham> selectLayLoaiSP() {
        String sql = "Select distinct LoaiSP from SanPham";
        return this.selectBySqlLoaiSP(sql);
    }

    protected List<SanPham> selectBySqlLoaiSP(String sql, Object... args) {
        List<SanPham> list = new ArrayList<SanPham>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                SanPham entity = new SanPham();
                entity.setLoaiSP(rs.getString("LoaiSP"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Object[]> getDoanhThuSP() {
        String sql = "{CALL Proc_DoanhThuSP}";
        String[] cols = {"MaSP", "TenSP", "LoaiSP", "Gia", "SoLuong", "TongDoanhThu"};
        return getListOfArray(sql, cols);
    }

    public List<Object[]> getDoanhThuSPTheoLoai(String loaiSP) {
        String sql = "{CALL Proc_DoanhThuSPTheoLoai(?)}";
        String[] cols = {"MaSP", "TenSP", "LoaiSP", "Gia", "SoLuong", "TongDoanhThu"};
        return getListOfArray(sql, cols, loaiSP);
    }

    private List<Object[]> getListOfArray(String sql, String[] cols, Object... args) {
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                Object[] vals = new Object[cols.length];
                for (int i = 0; i < cols.length; i++) {
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    
    public List<Object[]> getDoanhThuSPNgay(Date tuNgay, Date DenNgay) {
        String sql = "{CALL Proc_DoanhThuSP_Ngay(?,?)}";
        String[] cols = {"MaSP", "TenSP", "LoaiSP", "Gia", "SoLuong", "TongDoanhThu"};
        return getListOfArray(sql, cols, tuNgay, DenNgay);

    }
}
