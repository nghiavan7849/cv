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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.snackviet.model.HinhAnhSP;
import com.snackviet.model.SanPham;
import com.snackviet.repository.HinhAnhSPRepository;
import com.snackviet.repository.LoaiSPRepository;
import com.snackviet.repository.SanPhamRepository;

@CrossOrigin("*")
@RestController
public class ApiSanPham {
    @Autowired
    SanPhamRepository sanPhamRepository;
    @Autowired
    HinhAnhSPRepository hinhAnhSPRepository;
    @Autowired 
    LoaiSPRepository loaiSPRepository;

    @GetMapping("/api/san-pham/list")
    public Map<String, Object> getAll() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("sanPham", sanPhamRepository.findAll()));
        response.put("message", "Lấy dữ liệu sản phẩm thành công");
        return response;
    }

    @GetMapping("/api/san-pham/page")
    public Map<String, Object> getListPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<SanPham> pageSP = sanPhamRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", pageSP.getTotalPages());
        response.put("data", Map.of("sanPham", pageSP.getContent()));
        response.put("message", "Lấy dữ liệu sản phẩm thành công");

        return response;
    }

    @GetMapping("/api/san-pham/page/{pageSize}")
    public Map<String, Object> getListPage(@PathVariable("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(pageSize - 1, 8);
        Page<SanPham> pageSP = sanPhamRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", pageSP.getTotalPages());
        response.put("data", Map.of("sanPham", pageSP.getContent()));
        response.put("message", "Lấy dữ liệu sản phẩm thành công");

        return response;
    }

    @GetMapping("/api/san-pham/get-one")
    public Map<String, Object> getOne(@RequestParam("maSanPham") Integer maSanPham) {
        SanPham sanPham = sanPhamRepository.findById(maSanPham).get();
        return response("success", sanPham, "Lấy dữ liệu 1 sản phẩm thành công");
    }

    @GetMapping("/api/san-pham/get-list-by-bo-loc")
    public Map<String, Object> getListByLoaiSP(@RequestParam(name = "maLoai" ,defaultValue = "0") Integer maLoai,
        @RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
        @RequestParam(name = "timKiem", defaultValue = "") String timKiem, @RequestParam(name = "minGia", defaultValue = "0") Double minGia, 
        @RequestParam(name = "maxGia", defaultValue = "0") Double maxGia
    ) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Direction.DESC, "maSanPham"));
        Page<SanPham> pageSP = null;
        if(!timKiem.equals("")) {
            pageSP = sanPhamRepository.findByTenSanPhamContaining(timKiem, pageable);
        } else if(maLoai != 0 && maLoai > 0){
            pageSP = sanPhamRepository.findByLoaiSPMaLoai(maLoai, pageable);
        } else if(minGia != 0.0 && maxGia != 0.0){
            pageSP = sanPhamRepository.findByGiaBetween(minGia, maxGia, pageable);
        }
         
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", pageSP.getTotalPages());
        response.put("data", Map.of("sanPham", pageSP.getContent()));
        response.put("message", "Lấy dữ liệu danh sách sản phẩm theo loại thành công");
        return response;
    }

    @GetMapping("/api/san-pham/get-list-sap-xep")
    public Map<String, Object> getListBySapXep(@RequestParam("sapXep") String sapXep,@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = null;
        Page<SanPham> pageSP = null;
        if(sapXep.equals("moinhat")){
            pageable = PageRequest.of(page - 1, pageSize,Sort.by(Direction.DESC, "maSanPham"));
            pageSP = sanPhamRepository.findAll(pageable);
        } else if(sapXep.equals("giatangdan")) {
            pageable = PageRequest.of(page - 1, pageSize,Sort.by(Direction.ASC, "gia"));
            pageSP = sanPhamRepository.findAll(pageable);
        } else if(sapXep.equals("giagiamdan")) {
            pageable = PageRequest.of(page - 1, pageSize,Sort.by(Direction.DESC, "gia"));
            pageSP = sanPhamRepository.findAll(pageable);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", pageSP.getTotalPages());
        response.put("data", Map.of("sanPham", pageSP.getContent()));
        response.put("message", "Lấy dữ liệu danh sách sản phẩm theo loại thành công");
        return response;
    }


    @GetMapping("/api/hinh-anh-san-pham")
    public Map<String, Object> getHinhAnhDanhGia(@RequestParam("maSanPham") Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id).get();
        List<HinhAnhSP> hinhAnhSPs = hinhAnhSPRepository.findBySanPhamHA(sanPham);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("hinhAnhSP", hinhAnhSPs));
        response.put("message", "Lấy dữ liệu hình ảnh của 1 sản phẩm thành công");
        return response;
    }

    // @PutMapping("/api/san-pham/tru-so-luong")
    // public Map<String, Object> truSoLuongSP(@RequestParam("maSanPham")Integer id, @RequestParam("soLuong")int soLuong){
    //     SanPham sanPham = sanPhamRepository.findById(id).get();
    //     sanPham.setSoLuong(sanPham.getSoLuong() - soLuong);
    //     sanPhamRepository.saveAndFlush(sanPham);
    //     return response("success", sanPham, "Trừ số lượng theo sản phẩm thành công");
    // }
    public Map<String, Object> response(String status, SanPham sanPham, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("data", sanPham == null ? null : Map.of("sanPham", sanPham));
        response.put("message", message);
        return response;
    }
}
