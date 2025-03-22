package com.snackviet.api.Admin;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.dto.tinhthanh.TinhThanhRequest;
import com.snackviet.model.QuanHuyen;
import com.snackviet.model.TinhThanh;
import com.snackviet.model.XaPhuong;
import com.snackviet.repository.QuanHuyenRepository;
import com.snackviet.repository.TinhThanhRepository;
import com.snackviet.repository.XaPhuongRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin("*")
@RequestMapping("api/admin/tinh-thanh")
public class ApiAdminTinhThanh {
    @Autowired
    TinhThanhRepository tinhThanhRepository;
    @Autowired 
    QuanHuyenRepository quanHuyenRepository;
    @Autowired
    XaPhuongRepository xaPhuongRepository;


    @GetMapping("")
    public Map<String, Object> getListAll() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("tinhThanh", tinhThanhRepository.findAll()));
        response.put("message", "Lấy dữ liệu tỉnh thành thành công");
        return response;
    }
    
    @GetMapping("/search")
    public Map<String, Object> getOne(@RequestParam(name = "id", defaultValue = "")String id,
        @RequestParam(name = "province-id", defaultValue = "")String provinceId    
    ) {
        Map<String, Object> response = new LinkedHashMap<>();
        if(!id.isEmpty()){
            TinhThanh tinhThanh = new TinhThanh();
            try {
                tinhThanh = tinhThanhRepository.findById(Integer.valueOf(id)).get();
            } catch (NoSuchElementException e) {
                
                response.put("status", "error");
                response.put("data", null);
                response.put("message", "Tỉnh thành có id "+id+ " không tồn tại!!!");
                return response;
            }

            response.put("status", "success");
            response.put("data", Map.of("tinhThanh", tinhThanh));
            response.put("message", "Lấy dữ liệu tỉnh thành thành công");
            return response;
        }
        // if(!provinceId.isEmpty()){
        //     List<TinhThanh> tinhThanh = tinhThanhRepository.findByProvinceID(Integer.valueOf(provinceId));
        //     if(tinhThanh.isEmpty()){ 
        //         response.put("status", "error");
        //         response.put("data", null);
        //         response.put("message", "Tỉnh thành có province id "+provinceId+ " không tồn tại!!!");
        //         return response;
        //     }
        //     response.put("status", "success");
        //     response.put("data", Map.of("tinhThanh", tinhThanh));
        //     response.put("message", "Lấy dữ liệu tỉnh thành thành công");
        //     return response;
        // }

        response.put("status", "error");
        response.put("data",null);
        response.put("message", "Vui lòng truyền id hoặc provinceId để tìm kiếm!!!");
        return response;
    }
    


    @PostMapping("")
    public Map<String, Object> addTinhThanh(@RequestBody TinhThanhRequest tinhThanhRequest) {
        //TODO: process POST request
        TinhThanh tinhThanh = new TinhThanh();
        tinhThanh.setProvinceID(tinhThanhRequest.getProvinceID());
        tinhThanh.setProvinceName(tinhThanhRequest.getProvinceName());

        tinhThanhRepository.saveAndFlush(tinhThanh);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("tinhThanh", tinhThanh));
        response.put("message", "Thêm mơi tỉnh thành thành công");
        return response;
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> updateTinhThanh(@PathVariable String id, @RequestBody TinhThanhRequest tinhThanhRequest) {
        TinhThanh tinhThanh = new TinhThanh();
        try {
            tinhThanh = tinhThanhRepository.findById(Integer.valueOf(id)).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Tỉnh thành có id "+id+ " không tồn tại!!!");
            return response;
        }
        tinhThanh.setProvinceID(tinhThanhRequest.getProvinceID());
        tinhThanh.setProvinceName(tinhThanhRequest.getProvinceName());

        tinhThanhRepository.saveAndFlush(tinhThanh);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", Map.of("tinhThanh", tinhThanh));
        response.put("message", "Cập nhật tỉnh thành thành công");
        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteTinhThanh(@PathVariable String id) {
        TinhThanh tinhThanh = new TinhThanh();
        try {
            tinhThanh = tinhThanhRepository.findById(Integer.valueOf(id)).get();
        } catch (Exception e) {
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "error");
            response.put("data", null);
            response.put("message", "Tỉnh thành có id "+id+ " không tồn tại!!!");
            return response;
        }
 
        List<QuanHuyen> listQuanHuyen = quanHuyenRepository.findByTinhThanhId(tinhThanh);
        if(!listQuanHuyen.isEmpty()){
            List<QuanHuyen> result = new ArrayList<QuanHuyen>();
            for(QuanHuyen quanHuyen : listQuanHuyen){
                List<XaPhuong> listXaPhuongs = xaPhuongRepository.findByQuanHuyenId(quanHuyen);
                if(!listXaPhuongs.isEmpty()){
                    List<XaPhuong> litsXP = new ArrayList<XaPhuong>();
                    for(XaPhuong xaPhuong : listXaPhuongs){
                        litsXP.add(xaPhuong);
                    }
                    xaPhuongRepository.deleteAll(litsXP);
                }
                result.add(quanHuyen);
            }
            quanHuyenRepository.deleteAll(result);
        }

        tinhThanhRepository.delete(tinhThanh);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "success");
        response.put("data", null);
        response.put("message", "Xóa tỉnh thành thành công");
        return response;
    }
}   
