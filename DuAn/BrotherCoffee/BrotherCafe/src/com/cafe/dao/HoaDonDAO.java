/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;


import com.cafe.model.HoaDon;
import com.cafe.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.naming.ldap.Rdn;

/**
 *
 * @author NGHIA
 */
public class HoaDonDAO extends CafeDAO<HoaDon, Integer>{
    String INSERT_SQL = "INSERT INTO HoaDon (NgayDatBan, NgayThanhToan, ThoiGianTaoHD, ThoiGianThanhToan, TongTien, TrangThai, MaNV, MaBan, MaKH) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String UPDATE_SQL = "UPDATE HoaDon SET NgayDatBan = ?, NgayThanhToan = ?, ThoiGianTaoHD = ?, ThoiGianThanhToan = ?, TongTien = ?, TrangThai = ? WHERE MaHD = ?";
    String DELETE_SQL = "DELETE FROM HoaDon WHERE MaHD = ?";
    String SELECT_ALL_SQL = "SELECT * FROM HoaDon";
    String SELECT_BY_ID_SQL = "SELECT * FROM HoaDon WHERE MaHD = ?";
    String UPDATE_TONGTIEN_SQL = "UPDATE HoaDon SET TongTien = ? WHERE MaHD = ?";
    String UPDATE_MABAN_SQL = "UPDATE HoaDon set MaBan = ? Where MaHD = ?";
    String UPDATE_NgayDB_SQL = "UPDATE HoaDon set NgayDatBan = ? Where MaHD = ?";
    @Override
    public void insert(HoaDon e) {
        jdbcHelper.update(INSERT_SQL,e.getNgayDatBan(),e.getNgayThanhToan(),e.getThoiGianTaoHD(),e.getThoiGianThanhToan(),e.getTongTien(),
        e.isTrangThai(),e.getMaNV(),e.getMaBan(),e.getMaKH());
    }

    @Override
    public void update(HoaDon e) {
        jdbcHelper.update(UPDATE_SQL,e.getNgayDatBan(),e.getNgayThanhToan(),e.getThoiGianTaoHD(),e.getThoiGianThanhToan(),e.getTongTien(),
        e.isTrangThai(),e.getMaHD());
    }
    public void updateTongTien(HoaDon e) {
        jdbcHelper.update(UPDATE_TONGTIEN_SQL,e.getTongTien(),e.getMaHD());
    }
    public void updateMaBanCuaHD(HoaDon e) {
        jdbcHelper.update(UPDATE_MABAN_SQL,e.getMaBan(),e.getMaHD());
    }
    public void updateNgayDBCuaHD(HoaDon e) {
        jdbcHelper.update(UPDATE_NgayDB_SQL,e.getNgayDatBan(),e.getMaHD());
    }
    @Override
    public void delete(Integer id) {
        jdbcHelper.update(DELETE_SQL, id);
    }

    @Override
    public HoaDon selectById(Integer id) {
         List<HoaDon> list = this.selectBySql(SELECT_BY_ID_SQL, id);
        if (list.isEmpty()) {
            return null;
        }
        for (HoaDon ban : list) {
            if (ban.getMaHD() != id) {
                return null;
            }
        }
        return list.get(0);
    }

    @Override
    public List<HoaDon> selectAll() {
        return this.selectBySql(SELECT_ALL_SQL);
    }

    @Override
    protected List<HoaDon> selectBySql(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<HoaDon>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getInt("MaHD"));
                hd.setNgayDatBan(rs.getTimestamp("NgayDatBan"));
                hd.setNgayThanhToan(rs.getDate("NgayThanhToan"));
                hd.setThoiGianTaoHD(rs.getString("ThoiGianTaoHD"));
                hd.setThoiGianThanhToan(rs.getString("ThoiGianThanhToan"));
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setTrangThai(rs.getBoolean("TrangThai"));
                hd.setMaNV(rs.getString("MaNV"));
                hd.setMaKH(rs.getString("MaKH"));
                hd.setMaBan(rs.getString("MaBan"));
         
                list.add(hd);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private List<Object[]> getListOfArray(String sql, String[] cols, Object...args){
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = jdbcHelper.query(sql, args);
            while(rs.next()){
                Object[] vals = new Object[cols.length];
                for(int i = 0; i<cols.length; i++){
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
     public List<Object[]> getHoaDon(Date tuNgay, Date DenNgay) {
        String sql = "{CALL Proc_HoaDon(?,?)}";
        String[] cols= {"MaHD", "MaBan", "NgayDatBan", "ThoiGianTaoHD","NgayThanhToan", "ThoiGianThanhToan", "MaNV", "TongTien", "TrangThai"};
        return getListOfArray(sql, cols, tuNgay,DenNgay);
        
    }
    public List<HoaDon> selectByMaBan(String keyword) {
        String sql = "SELECT * FROM HoaDon WHERE MaBan LIKE ?";
        return this.selectBySql(sql,keyword);
    }
    public List<HoaDon> selectByMaSP(String keyword) {
        String sql = "SELECT * FROM HoaDon WHERE MaSP LIKE ?";
        return this.selectBySql(sql,keyword);
    }
     public List<HoaDon> selectByByDV(String keyword) {
        String sql = "SELECT hd.* FROM HoaDon hd INNER JOIN NhanVien nv ON hd.MaNV = nv.MaNV WHERE nv.MaDV LIKE ?";
        return this.selectBySql(sql,keyword);
    }
}
