package com.snackviet.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.model.DonHang;
import com.snackviet.model.LichSu;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.DonHangRepository;
import com.snackviet.repository.LichSuRepository;
import com.snackviet.repository.TaiKhoanRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
public class ApiLichSu {
    @Autowired
    DonHangRepository donHangRepository;
    @Autowired
    LichSuRepository lichSuRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    

    @GetMapping("/api/lich-su-mua-hang")
    public Map<String, Object> getLichSuMuaHang(@RequestParam("maTaiKhoan") String maTaiKhoan, 
    @RequestParam(name = "trangThai", defaultValue = "") String trangThai,  
    @RequestParam(name = "page", defaultValue = "1") int page,
    @RequestParam(name = "size", defaultValue = "5") int size ) {

        System.out.println("Đây là thông tin: "+trangThai);
        TaiKhoan taiKhoan = taiKhoanRepository.findById(Integer.parseInt(maTaiKhoan)).get();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DonHang> listPage = null;
        if (trangThai.equals("") || trangThai.equals("dadathang")) {
            listPage = donHangRepository.hoadonMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đã đặt hàng", pageable);
        } else  if (trangThai.equals("daxacnhan")) {
            listPage = donHangRepository.hoadonMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đã xác nhận", pageable);
        } else  if ( trangThai.equals("dangxuly")) {
            listPage = donHangRepository.hoadonMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đang xử lý", pageable);
        } else  if ( trangThai.equals("dangvanchuyen")) {
            listPage = donHangRepository.hoadonMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đang vận chuyển", pageable);
        } else  if ( trangThai.equals("giaothanhcong")) {
            listPage = donHangRepository.hoadonMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Giao thành công", pageable);
        } else  if (trangThai.equals("dahuy")) {
            listPage = donHangRepository.hoadonMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đã hủy", pageable);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", listPage.getTotalPages());
        response.put("data", Map.of("lichSu", listPage.getContent()));
        response.put("message", "Lấy dữ liệu lịch sử đơn mua thành công");
        return response;
    }
    @GetMapping("/api/lich-su-mua-hang/chi-tiet")
    public Map<String, Object> getChiTietLichSuMuaHang(@RequestParam("maTaiKhoan") Integer maTaiKhoan, 
    @RequestParam(name = "trangThai", defaultValue = "") String trangThai,
    @RequestParam(name = "maHoaDon", defaultValue = "") Integer maHoaDon,  
    @RequestParam(name = "page", defaultValue = "1") int page,
    @RequestParam(name = "size", defaultValue = "5") int size) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(maTaiKhoan).get();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<LichSu> listPage = null;
        if (trangThai.equals("") || trangThai.equals("dadathang")) {
            listPage = lichSuRepository.lichSuMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đã đặt hàng", maHoaDon, pageable);
        } else  if (trangThai.equals("daxacnhan")) {
            listPage = lichSuRepository.lichSuMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đã xác nhận",maHoaDon, pageable);
        } else  if ( trangThai.equals("dangxuly")) {
            listPage = lichSuRepository.lichSuMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đang xử lý",maHoaDon, pageable);
        } else  if ( trangThai.equals("dangvanchuyen")) {
            listPage = lichSuRepository.lichSuMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đang vận chuyển",maHoaDon, pageable);
        } else  if ( trangThai.equals("giaothanhcong")) {
            listPage = lichSuRepository.lichSuMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Giao thành công",maHoaDon, pageable);
        } else  if (trangThai.equals("dahuy")) {
            listPage = lichSuRepository.lichSuMuaHangOrderBy(taiKhoan.getMaTaiKhoan(), "Đã hủy", maHoaDon, pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        if(listPage.getContent().isEmpty()){
            response.put("data",null);
            response.put("message", "Không có dữ liệu!!!");
        } else {
            response.put("totalPage", listPage.getTotalPages());
            response.put("data", Map.of("lichSu", listPage.getContent()));
            response.put("message", "Lấy dữ liệu chi tiết lịch sử đơn mua thành công");
        }
       
        return response;
    }
}