package com.snackviet.api.Admin;

import java.util.ArrayList;
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

import com.snackviet.dto.quanhuyen.QuanHuyenRequest;
import com.snackviet.model.QuanHuyen;
import com.snackviet.model.TinhThanh;
import com.snackviet.model.XaPhuong;
import com.snackviet.repository.QuanHuyenRepository;
import com.snackviet.repository.TinhThanhRepository;
import com.snackviet.repository.XaPhuongRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("api/admin/quan-huyen")
public class ApiAdminQuanHuyen {
    @Autowired
    QuanHuyenRepository quanHuyenRepository;

    @Autowired
    TinhThanhRepository tinhThanhRepository;
    @Autowired 
    XaPhuongRepository xaPhuongRepository;

    @GetMapping("")
    public Map<String, Object> getListAll() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("quanHuyen", quanHuyenRepository.findAll()));
        response.put("message", "Lấy dữ liệu quận huyện thành công");
        return response;
    }
    
    @GetMapping("/search")
    public Map<String, Object> getOne(@RequestParam(name = "id", defaultValue = "")String id,
        @RequestParam(name = "district-id", defaultValue = "")String districtId,
        @RequestParam(name = "province-id", defaultValue = "")String provinceId     
    ) {
        Map<String, Object> response = new LinkedHashMap<>();
        if(!id.isEmpty()){
            QuanHuyen quanHuyen = new QuanHuyen();
            try {
                quanHuyen = quanHuyenRepository.findById(Integer.valueOf(id)).get();
            } catch (NoSuchElementException e) {
                response.put("status", "error");
                response.put("data", null);
                response.put("message", "Quận huyện có id "+id+ " không tồn tại!!!");
                return response;
            }

            response.put("status", "success");
            response.put("data", Map.of("quanHuyen", quanHuyen));
            response.put("message", "Lấy dữ liệu quận huyện thành công");
            return response;
        }
        // if(!districtId.isEmpty()){
        //     List<QuanHuyen> quanHuyen = quanHuyenRepository.findByDistrictID(Integer.valueOf(districtId));
        //     if(quanHuyen.isEmpty()){ 
        //         response.put("status", "error");
        //         response.put("data", null);
        //         response.put("message", "Quận huyện có province id "+districtId+ " không tồn tại!!!");
        //         return response;
        //     }
        //     response.put("status", "success");
        //     response.put("data", Map.of("quanHuyen", quanHuyen));
        //     response.put("message", "Lấy dữ liệu quận huyện thành công");
        //     return response;
        // }
        if(!provinceId.isEmpty()){
            TinhThanh tinhThanh = new TinhThanh();
            try {
                tinhThanh = tinhThanhRepository.findByProvinceID(Integer.valueOf(provinceId));
            } catch (Exception e) {
                response.put("status", "error");
                response.put("data", null);
                response.put("message", "Tỉnh thành có id "+provinceId+ " không tồn tại!!!");
                return response;
            }
            List<QuanHuyen> quanHuyen = quanHuyenRepository.findByTinhThanhId(tinhThanh);
            if(quanHuyen.isEmpty()){ 
                response.put("status", "error");
                response.put("data", null);
                response.put("message", "Quận huyện có province id "+districtId+ " không tồn tại!!!");
                return response;
            }
            response.put("status", "success");
            response.put("data", Map.of("quanHuyen", quanHuyen));
            response.put("message", "Lấy dữ liệu quận huyện thành công");
            return response;
        }

        response.put("status", "error");
        response.put("data",null);
        response.put("message", "Vui lòng truyền id hoặc districtId hoặc provinceId để tìm kiếm!!!");
        return response;
    }
    


    @PostMapping("")
    public Map<String, Object> addQuanHuyen(@RequestBody QuanHuyenRequest quanHuyenRequest) {
        //TODO: process POST request
        TinhThanh tinhThanh = new TinhThanh();
        try {
            tinhThanh = tinhThanhRepository.findById(quanHuyenRequest.getTinhThanhId()).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Tỉnh thành có id "+quanHuyenRequest.getTinhThanhId()+ " không tồn tại!!!");
            return response;
        }

        QuanHuyen quanHuyen = new QuanHuyen();
        quanHuyen.setDistrictID(quanHuyenRequest.getDistrictID());
        quanHuyen.setDistrictName(quanHuyenRequest.getDistrictName());
        quanHuyen.setTinhThanhId(tinhThanh);

        quanHuyenRepository.saveAndFlush(quanHuyen);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("quanHuyen", quanHuyen));
        response.put("message", "Thêm mơi quận huyện thành công");
        return response;
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> updateQuanHuyen(@PathVariable String id, @RequestBody QuanHuyenRequest quanHuyenRequest) {
        QuanHuyen quanHuyen = new QuanHuyen();
        try {
            quanHuyen = quanHuyenRepository.findById(Integer.valueOf(id)).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Quận huyện có id "+id+ " không tồn tại!!!");
            return response;
        }
        TinhThanh tinhThanh = new TinhThanh();
        try {
            tinhThanh = tinhThanhRepository.findById(quanHuyenRequest.getTinhThanhId()).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Tỉnh thành có id "+quanHuyenRequest.getTinhThanhId()+ " không tồn tại!!!");
            return response;
        }
        quanHuyen.setDistrictID(quanHuyenRequest.getDistrictID());
        quanHuyen.setDistrictName(quanHuyenRequest.getDistrictName());
        quanHuyen.setTinhThanhId(tinhThanh);

        quanHuyenRepository.saveAndFlush(quanHuyen);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("quanHuyen", quanHuyen));
        response.put("message", "Cập nhật quận huyện thành công");
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteQuanHuyen(@PathVariable String id) {
        QuanHuyen quanHuyen = new QuanHuyen();
        try {
            quanHuyen = quanHuyenRepository.findById(Integer.valueOf(id)).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Quận huyện có id "+id+ " không tồn tại!!!");
            return response;
        }
        List<XaPhuong> listXaPhuongs = xaPhuongRepository.findByQuanHuyenId(quanHuyen);
        if (!listXaPhuongs.isEmpty()) {
            List<XaPhuong> litsXP = new ArrayList<XaPhuong>();
            for (XaPhuong xaPhuong : listXaPhuongs) {
                litsXP.add(xaPhuong);
            }
            xaPhuongRepository.deleteAll(litsXP);
        }

        quanHuyenRepository.delete(quanHuyen);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", null);
        response.put("message", "Xóa quận huyện thành công");
        return response;
    }    
}
