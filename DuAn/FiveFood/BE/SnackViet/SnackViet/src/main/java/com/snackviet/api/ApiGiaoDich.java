package com.snackviet.api;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.dto.giaodich.CreateGiaoDichDTO;
import com.snackviet.model.GiaoDich;
import com.snackviet.model.HoaDon;
import com.snackviet.model.LoaiSP;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.GiaoDichRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.TaiKhoanRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
public class ApiGiaoDich {
    @Autowired
    GiaoDichRepository giaoDichRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/api/giao-dich/list")
    public Map<String, Object> getList() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("giaoDich", giaoDichRepository.findAll(Sort.by(Direction.DESC, "maGiaoDich"))));
        response.put("message", "Lấy dữ liệu giao dịch thành công");
        return response;
    }

    @GetMapping("/api/giao-dich/page")
    public Map<String, Object> getListPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Direction.DESC, "maGiaoDich"));
        Page<GiaoDich> pageGD = giaoDichRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", pageGD.getTotalPages());
        response.put("data", Map.of("giaoDich", pageGD.getContent()));
        response.put("message", "Lấy dữ liệu giao dịch thành công");
        return response;
    }

    @PostMapping("/api/giao-dich/create")
    public Map<String, Object> create(@RequestBody CreateGiaoDichDTO createGiaoDichDTO) {
        HoaDon hd = hoaDonRepository.findById(createGiaoDichDTO.getMaHoaDon()).get();
        TaiKhoan tk = taiKhoanRepository.findById(createGiaoDichDTO.getMaTaiKhoan()).get();
        GiaoDich gd = new GiaoDich();
        gd.setCodeGiaoDich(createGiaoDichDTO.getCodeGiaoDich());
        gd.setHoVaTen(createGiaoDichDTO.getHoVaTen());
        gd.setHoaDonGD(hd);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            gd.setNgayGiaoDich(simpleDateFormat.parse(createGiaoDichDTO.getNgayGiaoDich()));
        } catch (Exception e) {
            // TODO: handle exception
        }
        gd.setSoTaiKhoan(createGiaoDichDTO.getSoTaiKhoan());
        gd.setSoTien(createGiaoDichDTO.getSoTien());
        gd.setTaiKhoanGD(tk);
        gd.setTrangThai(createGiaoDichDTO.isTrangThai());
        giaoDichRepository.saveAndFlush(gd);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("giaoDich", gd));
        response.put("message", "Thêm mới giao dịch thành công");
        return response;
    }

}
