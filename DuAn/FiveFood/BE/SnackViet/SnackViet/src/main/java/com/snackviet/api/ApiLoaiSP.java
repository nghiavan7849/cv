    package com.snackviet.api;

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

import com.snackviet.model.LoaiSP;
import com.snackviet.repository.LoaiSPRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin("*")
@RestController
public class ApiLoaiSP {
    @Autowired
    LoaiSPRepository loaiSPRepository;

    @GetMapping("/api/loai-san-pham/list")
    public Map<String, Object> getAll() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("loaiSP", loaiSPRepository.findAll(Sort.by(Direction.ASC, "maLoai"))));
        response.put("message", "Lấy dữ liệu loại sản phẩm thành công");
        return response;
    }

    @GetMapping("/api/loai-san-pham/page")
    public Map<String, Object> getListPage(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(Direction.ASC, "maLoai"));
        Page<LoaiSP> pageList = loaiSPRepository.findAll(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("totalPage", pageList.getTotalPages());
        response.put("data", Map.of("loaiSP", pageList.getContent()));
        response.put("message", "Lấy dữ liệu loại sản phẩm thành công");

        return response;
    }
}
