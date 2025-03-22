package com.snackviet.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.dto.hoadon.CreateChiTietHoaDonDTO;
import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.HoaDon;
import com.snackviet.model.SanPham;
import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.repository.TaiKhoanRepository;

@CrossOrigin("*")
@RestController
public class ApiChiTietHoaDon {
    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    SanPhamRepository sanPhamRepository;

    @GetMapping("/api/chi-tiet-hoa-don/list")
    public Map<String, Object> getAll() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data",
                Map.of("chiTietHoaDon", chiTietHoaDonRepository.findAll(Sort.by(Direction.DESC, "maChiTietHoaDon"))));
        response.put("message", "Lấy dữ liệu chi tiết hóa đơn thành công");
        return response;
    }

    @GetMapping("/api/chi-tiet-hoa-don/list-by-mahd")
    public Map<String, Object> getListByMaHD(@RequestParam("maHoaDon")Integer maHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).get();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data",
                Map.of("chiTietHoaDon", chiTietHoaDonRepository.findByHoaDonCT(hoaDon)));
        response.put("message", "Lấy dữ liệu chi tiết hóa đơn theo mã hóa đơn thành công");
        return response;
    }

    @PostMapping("/api/chi-tiet-hoa-don/create")
    public Map<String, Object> update(@RequestBody CreateChiTietHoaDonDTO createChiTietHoaDonDTO) {
        HoaDon hoaDon = hoaDonRepository.findById(createChiTietHoaDonDTO.getMaHoaDon()).get();
        SanPham sanPham = sanPhamRepository.findById(createChiTietHoaDonDTO.getMaSanPham()).get();
        ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
        chiTietHoaDon.setHoaDonCT(hoaDon);
        chiTietHoaDon.setSoLuong(createChiTietHoaDonDTO.getSoLuong());
        chiTietHoaDon.setSanPhamCT(sanPham);
        chiTietHoaDon.setGia(createChiTietHoaDonDTO.getGia());

        chiTietHoaDonRepository.saveAndFlush(chiTietHoaDon);
        return response("success", chiTietHoaDon, "Thêm chi tiết hóa đơn thành công");
    }

    public Map<String, Object> response(String status, ChiTietHoaDon chiTietHoaDon, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", chiTietHoaDon == null ? null : Map.of("chiTietHoaDon", chiTietHoaDon));
        response.put("message", message);
        return response;
    }
}
