package com.snackviet.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.dto.chitietgiohang.CreateChiTietGioHangDTO;
import com.snackviet.model.ChiTietGioHang;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.ChiTietGioHangRepository;
import com.snackviet.repository.SanPhamRepository;
import com.snackviet.repository.TaiKhoanRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin("*")
public class ApiChiTietGioHang {
    @Autowired
    ChiTietGioHangRepository chiTietGioHangRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    SanPhamRepository sanPhamRepository;

    @GetMapping("/api/chi-tiet-gio-hang/list-all")
    public Map<String, Object> getAll() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data",
                Map.of("sanPham", chiTietGioHangRepository.findAll(Sort.by(Direction.DESC, "maChiTietGioHang"))));
        response.put("message", "Lấy dữ liệu chi tiết giỏ hàng thành công");
        return response;
    }

    @GetMapping("/api/chi-tiet-gio-hang/list-by-tk")
    public Map<String, Object> getListByTK(
            @RequestParam(name = "maTaiKhoan") int maTaiKhoan) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByMaTaiKhoan(maTaiKhoan);
        List<ChiTietGioHang> list = chiTietGioHangRepository.findByTaiKhoanGH(taiKhoan, Sort.by(Direction.DESC, "maChiTietGioHang"));

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("chiTietGioHang", list));
        response.put("message", "Lấy dữ liệu chi tiết giỏ hàng thành công");

        return response;
    }

    @PostMapping("/api/chi-tiet-gio-hang/create")
    public Map<String, Object> create(@RequestBody CreateChiTietGioHangDTO createChiTietGioHangDTO) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(createChiTietGioHangDTO.getMaTaiKhoan()).get();
        SanPham sanPham = sanPhamRepository.findById(createChiTietGioHangDTO.getMaSanPham()).get();
        ChiTietGioHang ctgh = chiTietGioHangRepository.findBySanPhamGHAndTaiKhoanGH(sanPham, taiKhoan);
        if (ctgh == null) {
            ctgh = new ChiTietGioHang();
            ctgh.setSoLuong(createChiTietGioHangDTO.getSoLuong());
            ctgh.setTaiKhoanGH(taiKhoan);
            ctgh.setSanPhamGH(sanPham);
        } else {
            ctgh.setSoLuong(ctgh.getSoLuong() + createChiTietGioHangDTO.getSoLuong());
        }
        chiTietGioHangRepository.saveAndFlush(ctgh);
        return response("success", ctgh, "Thêm mới chi tiết giỏ hàng thành công");
    }

    @PutMapping("/api/chi-tiet-gio-hang/update")
    public Map<String, Object> update(@RequestParam("maChiTietGioHang") Integer maCTGH, @RequestParam("soLuong") int soLuong) {
        ChiTietGioHang ctgh = chiTietGioHangRepository.findById(maCTGH).get();
        ctgh.setSoLuong(soLuong);
        chiTietGioHangRepository.saveAndFlush(ctgh);
        return response("success", ctgh, "Cập nhật chi tiết giỏ hàng thành công");
    }

    


    @DeleteMapping("/api/chi-tiet-gio-hang/delete/{id}")
    public Map<String, Object> delete(@PathVariable("id") Integer id) {
        ChiTietGioHang chiTietGioHang = chiTietGioHangRepository.findById(id).get();
        chiTietGioHangRepository.delete(chiTietGioHang);
        return response("success", new ChiTietGioHang(), "Xóa chi tiết giỏ hàng thành công");
    }

    public Map<String, Object> response(String status, ChiTietGioHang chiTietGioHang, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", chiTietGioHang == null ? null : Map.of("chiTietGioHang", chiTietGioHang));
        response.put("message", message);
        return response;
    }
}
