/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cafe.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author NGHIA
 */
public class jdbcHelper {

    static String host;
    static String database;
    static String taikhoan;
    static String matkhau;

    private static void docthongTin() {
        String fileName = "C:\\Users\\NGHIA\\Documents\\HOC KY 4\\Du an 1 - PRO1041\\QuanLyCaFe\\BrotherCafe\\src\\com\\cafe\\connect\\thongtin.txt";
        List<String> list = list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fileReader);
            String dong;
            // Đọc từng dòng trong tệp tin
            while ((dong = br.readLine()) != null) {
                list.add(dong);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                host = list.get(i);
            } else if (i == 1) {
                database = list.get(i);
            } else if (i == 2) {
                taikhoan = list.get(i);
            } else if (i == 3) {
                matkhau = list.get(i);
            }
        }
    }
    private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String connectionUrl;

    //nạp driver
    static {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PreparedStatement getStmt(String sql, Object... args) throws SQLException {
        docthongTin();
        connectionUrl = "jdbc:sqlserver://" + host + ":1433;"
                + "databaseName=" + database + ";"
                + "user="+taikhoan+";password="+matkhau+";"
                + "encrypt=true;trustServerCertificate=true;";
        Connection conn = DriverManager.getConnection(connectionUrl);
        PreparedStatement stmt = null;
        if (sql.trim().startsWith("{")) {
            stmt = conn.prepareCall(sql);//PROC
        } else {
            stmt = conn.prepareStatement(sql);//SQL
        }
        for (int i = 0; i < args.length; i++) {
            stmt.setObject(i + 1, args[i]);
        }
        return stmt;
    }

    public static ResultSet query(String sql, Object... args) {
        try {
            PreparedStatement stmt = getStmt(sql, args);
            return stmt.executeQuery();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object value(String sql, Object... args) {
        try {
            ResultSet rs = jdbcHelper.query(sql, args);
            if (rs.next()) {
                return rs.getObject(0);
            }
            rs.getStatement().getConnection().close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static int update(String sql, Object... args) {
        try {
            PreparedStatement stmt = jdbcHelper.getStmt(sql, args);
            try {
                return stmt.executeUpdate();
            } finally {
                stmt.getConnection().close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
