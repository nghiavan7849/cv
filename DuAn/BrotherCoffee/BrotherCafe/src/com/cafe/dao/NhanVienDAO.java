/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;

import com.cafe.model.DonVi;
import com.cafe.model.NhanVien;
import com.cafe.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author NGHIA
 */
public class NhanVienDAO extends CafeDAO<NhanVien, String> {

    String INSERT_SQL = "INSERT INTO NhanVien (MaNV, TenNV, MatKhau, DiaChi, SDT, Email, GioiTinh, ChucVu, MaDV) VALUES (?, ?, ?, ?, ?, ?, ?, ?. ?)";
    String UPDATE_SQL = "UPDATE NhanVien SET TenNV = ?, MatKhau = ?, DiaChi =?, SDT = ?, Email = ?, GioiTinh = ?, ChucVu = ?, MaDV = ? WHERE MaNV = ?";
    String DELETE_SQL = "DELETE FROM NhanVien WHERE MaNV = ?";
    String SELECT_ALL_SQL = "SELECT * FROM NhanVien";
    String SELECT_BY_ID_SQL = "SELECT * FROM NhanVien WHERE MaNV = ?";

    @Override
    public void insert(NhanVien e) {
        jdbcHelper.update(INSERT_SQL, e.getMaNV(), e.getTenNV(), e.getMatKhau(), e.getDiaChi(), e.getSDT(), e.getEmail(), e.isGioiTinh(), e.isChucVu(), e.getMaDV());
    }

    @Override
    public void update(NhanVien e) {
        jdbcHelper.update(UPDATE_SQL, e.getTenNV(), e.getMatKhau(), e.getDiaChi(), e.getSDT(), e.getEmail(), e.isGioiTinh(), e.isChucVu(), e.getMaDV(), e.getMaNV());
    }

    @Override
    public void delete(String id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public NhanVien selectById(String id) {
        List<NhanVien> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (NhanVien nv : list) {
            if (!nv.getMaNV().equals(id)) {
                return null;
            }
        }
        return list.get(0);
    }

    @Override
    public List<NhanVien> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<NhanVien> selectBySql(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<NhanVien>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                NhanVien entity = new NhanVien();
                entity.setMaNV(rs.getString("MaNV"));
                entity.setTenNV(rs.getString("TenNV"));
                entity.setDiaChi(rs.getString("DiaChi"));
                entity.setSDT(rs.getString("SDT"));
                entity.setEmail(rs.getString("Email"));
                entity.setMatKhau(rs.getString("MatKhau"));
                entity.setGioiTinh(rs.getBoolean("GioiTinh"));
                entity.setChucVu(rs.getBoolean("ChucVu"));
                entity.setMaDV(rs.getString("MaDV"));
                list.add(entity);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<NhanVien> selectByKeyWord(String keyword) {
        String sql = "SELECT * FROM NhanVien WHERE TenNV LIKE ? OR MaNV LIKE ? OR Email LIKE ? OR SDT LIKE ? OR DiaChi LIKE ?";
        return this.selectBySql(sql, "%" + keyword + "%","%" + keyword + "%","%" + keyword + "%","%" + keyword + "%","%" + keyword + "%");
    }
     public List<NhanVien> selectByKeyWordAndMaDV(String maDV , String keyword) {
        String sql = "SELECT * FROM NhanVien WHERE MaDV LIKE ? AND (TenNV LIKE ? OR MaNV LIKE ? OR Email LIKE ? OR SDT LIKE ? OR DiaChi LIKE ?)";
        return this.selectBySql(sql, maDV , "%" + keyword + "%","%" + keyword + "%","%" + keyword + "%","%" + keyword + "%","%" + keyword + "%");
    }
    public List<NhanVien> selectAllByMaDV(String keyword) {
        String sql = "SELECT * FROM NhanVien WHERE MaDV LIKE ? ";
        return this.selectBySql(sql, keyword+"%");
    }
    public List<NhanVien> selectByMaNV(String keyword) {
        String sql = "SELECT * FROM NhanVien WHERE MaNV LIKE ? ";
        return this.selectBySql(sql, keyword+"%");
    }
   public List<NhanVien> selectByTenNV(String keyword) {
        String sql = "SELECT * FROM NhanVien WHERE TenNV LIKE ? ";
        return this.selectBySql(sql, keyword+"%");
    }
    public boolean chechTrungMa(String ma) {
        List<NhanVien> list = this.selectAll();
        for (NhanVien nv : list) {
            if (nv.getMaNV().equals(ma)) {
                return false;
            }
        }
        return true;
    }
    public boolean chechTrungEmail(String email){  
        List<NhanVien> list = this.selectAll();
        for (NhanVien nv : list) {
            if(nv.getEmail().equals(email)){ 
                return false;
            }
        }
        return true;
    }
   public void updateMK(NhanVien e){
       String UPDATE_MK_NV = "UPDATE NhanVien SET MatKhau = ? WHERE EMAIL = ?";
        jdbcHelper.update(UPDATE_MK_NV, e.getMatKhau(),e.getEmail());
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
   
    public List<Object[]> getNhanVien() {
        String sql = "{CALL Proc_NhanVien}";
        String[] cols = {"MaDV", "MaNV", "TenNV", "DoanhSo"};
        return getListOfArray(sql, cols);

    }
    
        public List<Object[]> getNhanVienNgay(Date tuNgay, Date DenNgay) {
        String sql = "{CALL Proc_NhanVien_Ngay(?,?)}";
        String[] cols = {"MaDV", "MaNV", "TenNV", "DoanhSo"};
        return getListOfArray(sql, cols,tuNgay,DenNgay);

    }

}
