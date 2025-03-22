package com.snackviet.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.dto.hoadon.CreateHoaDonDTO;
import com.snackviet.model.DiaChi;
import com.snackviet.model.HoaDon;
import com.snackviet.model.TaiKhoan;
import com.snackviet.model.TrangThaiHoaDon;
import com.snackviet.repository.DiaChiRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.repository.TrangThaiHoaDonRepository;

@CrossOrigin("*")
@RestController
public class ApiHoaDon {
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;
    @Autowired
    TrangThaiHoaDonRepository trangThaiHoaDonRepository;
    @Autowired
    DiaChiRepository diaChiRepository;

    @GetMapping("/api/hoa-don/get-by-ma-hoa-don/{id}")
    public Map<String, Object> getByMaHD(@PathVariable String id) {
        HoaDon hoaDon = hoaDonRepository.findById(Integer.valueOf(id)).get();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data",
                Map.of("hoaDon", hoaDon));
        response.put("message", "Lấy dữ liệu hóa đơn thành công");
        return response;
    }

    @GetMapping("/api/hoa-don/list")
    public Map<String, Object> getAll() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data",
                Map.of("hoaDon", hoaDonRepository.findAll(Sort.by(Direction.DESC, "maHoaDon"))));
        response.put("message", "Lấy dữ liệu hóa đơn thành công");
        return response;
    }

    @GetMapping("/api/hoa-don/page/{pageSize}")
    public Map<String, Object> getListPage(@PathVariable("pageSize") int pageSize,
            @RequestParam("maTaiKhoan") int maTaiKhoan) {
        Pageable pageable = PageRequest.of(pageSize - 1, 9, Sort.by(Direction.DESC, "maHoaDon"));
        TaiKhoan taiKhoan = taiKhoanRepository.findByMaTaiKhoan(maTaiKhoan);
        Page<HoaDon> page = hoaDonRepository.findByTaiKhoanHD(taiKhoan, pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", page.getTotalPages());
        response.put("data", Map.of("hoaDon", page.getContent()));
        response.put("message", "Lấy dữ liệu hóa đơn thành công");

        return response;
    }

    @PostMapping("/api/hoa-don/create")
    public Map<String, Object> create(@RequestBody CreateHoaDonDTO createHoaDonDTO) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(createHoaDonDTO.getMaTaiKhoan()).get();

        

        HoaDon hoaDon = new HoaDon();
        hoaDon.setGhiChu(createHoaDonDTO.getGhiChu());
        hoaDon.setLyDoHuy(createHoaDonDTO.getLyDoHuy());
        hoaDon.setPhiVanChuyen(createHoaDonDTO.getPhiVanChuyen());
        hoaDon.setPhuongThucThanhToan(createHoaDonDTO.isPhuongThucThanhToan());
        hoaDon.setTaiKhoanHD(taiKhoan);
        hoaDon.setTongTien(createHoaDonDTO.getTongTien());
        if(createHoaDonDTO.getMaTrangThaiHoaDon()!=0){
            TrangThaiHoaDon trangThaiHoaDon = trangThaiHoaDonRepository.findById(createHoaDonDTO.getMaTrangThaiHoaDon()).get();
            hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
        }
        hoaDon.setTrangThai(createHoaDonDTO.isTrangThai());
        hoaDon.setDiaChiNhan(createHoaDonDTO.getDiaChiNhan());
        hoaDon.setDcHoTen(createHoaDonDTO.getDcHoTen());
        hoaDon.setDcSoDienThoai(createHoaDonDTO.getDcSoDienThoai());
        
        hoaDonRepository.saveAndFlush(hoaDon);

        System.out.println("Đây là địa chỉ nhận:"+createHoaDonDTO.getDiaChiNhan());

        return response("success", hoaDon, "Thêm mới hóa đơn thành công");
    }

    @PutMapping("/api/hoa-don/cap-nhat-trang-thai")
    public Map<String, Object> updateTrangThai(@RequestParam("maHoaDon") Integer maHoaDon, @RequestParam("trangThai") String trangThai , @RequestParam("lyDoHuy") String lyDoHuy ) {
        HoaDon hoaDon = hoaDonRepository.findById(maHoaDon).get();
        TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
        boolean check = false;
        if(trangThai.equals("dadathang")){
            trangThaiHoaDon = trangThaiHoaDonRepository.findByTenTrangThai("Đã đặt hàng");
            check = true;
        } else if(trangThai.equals("giaothanhcong")){
            trangThaiHoaDon = trangThaiHoaDonRepository.findByTenTrangThai("Giao thành công");
            check = true;
        } else if(trangThai.equals("dahuy")){
            trangThaiHoaDon = trangThaiHoaDonRepository.findByTenTrangThai("Đã hủy");
            hoaDon.setLyDoHuy(lyDoHuy);
            check = true;
        }

        if(check){
            hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
            hoaDonRepository.saveAndFlush(hoaDon);
        } else {
            return response("error", null, "Lỗi truyền trạng thái hóa đơn");
        }
        return response("success",hoaDon , "Cập nhật trạng thái hóa đơn thành công");
    }

    @PutMapping("/api/hoa-don/update-status-payment/{id}")
    public Map<String, Object> updateStatusPayMen(@PathVariable("id") String id, @RequestParam(name = "status", defaultValue = "false")Boolean status){
        HoaDon hoaDon = hoaDonRepository.findById(Integer.valueOf(id)).get();
        hoaDon.setTrangThai(status);
        hoaDonRepository.saveAndFlush(hoaDon);
        return response("success",hoaDon , "Cập nhật trạng thái hóa đơn thành công");
    }


    public Map<String, Object> response(String status, HoaDon hoaDon, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", hoaDon == null ? null : Map.of("hoaDon", hoaDon));
        response.put("message", message);
        return response;
    }
}
