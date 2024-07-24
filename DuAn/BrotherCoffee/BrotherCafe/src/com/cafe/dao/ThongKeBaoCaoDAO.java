/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.dao;

import com.cafe.model.Ban;
import com.cafe.model.ThongKeBaoCao;
import com.cafe.utils.jdbcHelper;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class ThongKeBaoCaoDAO extends CafeDAO<ThongKeBaoCao, String>{
    
    String SELECT_SQL = "SELECT  NgayThanhToan	AS NgayThanhToan,TenSP, SUM(SoLuong) AS SoLuong , SUM(Gia*SoLuong) AS TongTien\n" +
                        "FROM ChiTietHoaDon A\n" +
                        "INNER JOIN SanPham B ON B.MaSP = A.MaSP\n" +
                        "INNER JOIN HoaDon C ON C.MaHD = A.MaHD\n" +
                        "GROUP BY  NgayThanhToan,TenSP\n" +
                        "ORDER BY NgayThanhToan ASC";
    

    @Override
    public void insert(ThongKeBaoCao entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void update(ThongKeBaoCao entity) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void delete(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ThongKeBaoCao selectById(String Id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
//
    @Override
    public List<ThongKeBaoCao> selectAll() {
        return this.selectBySql(SELECT_SQL);
    }
//
    @Override
    protected List<ThongKeBaoCao> selectBySql(String sql, Object... args) {
        List<ThongKeBaoCao> list = new ArrayList<ThongKeBaoCao>();
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            while (rs.next()) {
                ThongKeBaoCao b = new ThongKeBaoCao();
                b.setMaDV(rs.getString("MaDV"));
                b.setMaHD(rs.getInt("TenDV"));
                b.setTongTien(rs.getDouble("TongDoanhThu"));
                list.add(b);
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

        public List<Object[]> getThongKe(Date tuNgay, Date DenNgay ) {
        String sql = "{CALL Proc_ThongKe_DonVi_Ngay(?,?)}";
        String[] cols= {"MaDV","TenDV", "TongDoanhThu" };
        return getListOfArray(sql, cols, tuNgay,DenNgay);
        
    }
        public List<Object[]> getThongKeDonVi() {
        String sql = "{CALL Proc_ThongKe_DonVi()}";
        String[] cols= {"MaDV","TenDV", "TongDoanhThu" };
        return getListOfArray(sql, cols);
        
    }
       
    public List<ThongKeBaoCao> selectByLoaiSP(String keyWord) {
         String sql = "SELECT NgayThanhToan AS NgayThanhToan,TenSP, SUM(SoLuong) AS SoLuong , SUM(Gia*SoLuong) AS TongTien\n" +
"		FROM ChiTietHoaDon A\n" +
"			INNER JOIN SanPham B ON B.MaSP = A.MaSP\n" +
"			INNER JOIN HoaDon C ON C.MaHD = A.MaHD\n" +
"		WHERE LoaiSP LIKE ?\n" +
"		GROUP BY  NgayThanhToan,TenSP\n" +
"		ORDER BY NgayThanhToan ASC";
        return this.selectBySql(sql,keyWord);
        
    }
}
