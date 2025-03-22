package com.snackviet.api.Admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.model.XaPhuong;
import com.snackviet.dto.xaphuong.XaPhuongRequest;
import com.snackviet.model.QuanHuyen;
import com.snackviet.repository.QuanHuyenRepository;
import com.snackviet.repository.XaPhuongRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("api/admin/xa-phuong")
public class ApiAdminXaPhuong {
    @Autowired
    XaPhuongRepository xaPhuongRepository;
    @Autowired
    QuanHuyenRepository quanHuyenRepository;

    @GetMapping("")
    public Map<String, Object> getListAll() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("xaPhuong", xaPhuongRepository.findAll()));
        response.put("message", "Lấy dữ liệu xã phường thành công");
        return response;
    }

    @GetMapping("/search")

    public Map<String, Object> getOne(@RequestParam(name = "id", defaultValue = "") String id,
            @RequestParam(name = "ward-code", defaultValue = "") String wardCode,
            @RequestParam(name = "district-id", defaultValue = "") String districtId) {
        Map<String, Object> response = new LinkedHashMap<>();
        if (!id.isEmpty()) {
            XaPhuong xaPhuong = new XaPhuong();
            try {
                xaPhuong = xaPhuongRepository.findById(Integer.valueOf(id)).get();
            } catch (NoSuchElementException e) {
                response.put("status", "error");
                response.put("data", null);
                response.put("message", "Xã Phường có id " + id + " không tồn tại!!!");
                return response;
            }

            response.put("status", "success");
            response.put("data", Map.of("xaPhuong", xaPhuong));
            response.put("message", "Lấy dữ liệu xã phường thành công");
            return response;
        }
        if (!wardCode.isEmpty()) {
            List<XaPhuong> xaPhuong = xaPhuongRepository.findByWardCode(Integer.valueOf(wardCode));
            if (xaPhuong.isEmpty()) {
                response.put("status", "error");
                response.put("data", null);
                response.put("message", "Xã phường có wardCode là " + wardCode + " không tồn tại!!!");
                return response;
            }
            response.put("status", "success");
            response.put("data", Map.of("xaPhuong", xaPhuong));
            response.put("message", "Lấy dữ liệu xã phường thành công");
            return response;
        }
        if (!districtId.isEmpty()) {
            QuanHuyen quanHuyen = new QuanHuyen();
            try {
                quanHuyen = quanHuyenRepository.findByDistrictID(Integer.valueOf(districtId));
            } catch (Exception e) {
                response.put("status", "error");
                response.put("data", null);
                response.put("message", "Quận huyện có id " + districtId + " không tồn tại!!!");
                return response;
            }
            List<XaPhuong> xaPhuong = xaPhuongRepository.findByQuanHuyenId(quanHuyen);
            if (xaPhuong.isEmpty()) {
                response.put("status", "error");
                response.put("data", null);
                response.put("message", "Xã Phường có district id " + districtId + " không tồn tại!!!");
                return response;
            }
            response.put("status", "success");
            response.put("data", Map.of("xaPhuong", xaPhuong));
            response.put("message", "Lấy dữ liệu xã phường thành công");
            return response;
        }

        response.put("status", "error");
        response.put("data", null);
        response.put("message", "Vui lòng truyền id hoặc wardCode hoặc districtId để tìm kiếm!!!");
        return response;
    }

    @PostMapping("")
    public Map<String, Object> addXaPhuong(@RequestBody XaPhuongRequest xaPhuongRequest) {
        // TODO: process POST request
        QuanHuyen quanHuyen = new QuanHuyen();
        try {
            quanHuyen = quanHuyenRepository.findById(xaPhuongRequest.getQuanHuyenId()).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Quận huyện có id " + xaPhuongRequest.getQuanHuyenId() + " không tồn tại!!!");
            return response;
        }

        XaPhuong xaPhuong = new XaPhuong();
        xaPhuong.setWardCode(xaPhuongRequest.getWardCode());
        xaPhuong.setWardName(xaPhuongRequest.getWardName());
        xaPhuong.setQuanHuyenId(quanHuyen);

        xaPhuongRepository.saveAndFlush(xaPhuong);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("xaPhuong", xaPhuong));
        response.put("message", "Thêm mơi xã phường thành công");
        return response;
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateXaPhuong(@PathVariable String id, @RequestBody XaPhuongRequest xaPhuongRequest) {
        XaPhuong xaPhuong = new XaPhuong();
        try {
            xaPhuong = xaPhuongRepository.findById(Integer.valueOf(id)).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Xã Phường có id " + id + " không tồn tại!!!");
            return response;
        }
        QuanHuyen quanHuyen = new QuanHuyen();
        try {
            quanHuyen = quanHuyenRepository.findById(xaPhuongRequest.getQuanHuyenId()).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Quận huyện có id " + xaPhuongRequest.getQuanHuyenId() + " không tồn tại!!!");
            return response;
        }
        xaPhuong.setWardCode(xaPhuongRequest.getWardCode());
        xaPhuong.setWardName(xaPhuongRequest.getWardName());
        xaPhuong.setQuanHuyenId(quanHuyen);

        xaPhuongRepository.saveAndFlush(xaPhuong);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("xaPhuong", xaPhuong));
        response.put("message", "Cập nhật xã phường thành công");
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteXaPhuong(@PathVariable String id) {
        XaPhuong xaPhuong = new XaPhuong();
        try {
            xaPhuong = xaPhuongRepository.findById(Integer.valueOf(id)).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Xã phường có id " + id + " không tồn tại!!!");
            return response;
        }

        xaPhuongRepository.delete(xaPhuong);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", null);
        response.put("message", "Xóa xã phường thành công");
        return response;
    }
}
