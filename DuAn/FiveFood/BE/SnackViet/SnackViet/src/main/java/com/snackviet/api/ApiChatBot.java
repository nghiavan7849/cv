package com.snackviet.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.model.SanPham;
import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.SanPhamRepository;

@RestController
@CrossOrigin("*")
@RequestMapping("api/chat-bot")
public class ApiChatBot {

    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Autowired
    SanPhamRepository sanPhamRepository;

    @GetMapping("/GET_TOP_PRODUCT")
    public Map<String,Object> getData(){
        Map<String,Object> response = new HashMap<>();

        System.out.println("Đây: "+chiTietHoaDonRepository.findTopSellingProduct());
    
        //TRẢ VỀ RESPONSE
        response.put("status", "success");
        response.put("data", chiTietHoaDonRepository.findTopSellingProduct().get(0));
        response.put("message", "Lấy dữ liệu thành công");
        return response;
    }

    @GetMapping("/TOPSALESFORMEN")
    public Map<String,Object> getDataForMen(){
        Map<String,Object> response = new HashMap<>();

        System.out.println("Đây: "+chiTietHoaDonRepository.findTopSellingProductForMen());
    
        //TRẢ VỀ RESPONSE
        response.put("status", "success");
        response.put("data", chiTietHoaDonRepository.findTopSellingProductForMen().get(0));
        response.put("message", "Lấy dữ liệu thành công");
        return response;
    }

    @GetMapping("/CHECK_PRODUCT_AVAILABILITY")
    public Map<String,Object> getget(@RequestParam("keyword") String keyword){
        Map<String,Object> response = new HashMap<>();

        System.out.println("Đây: "+sanPhamRepository.findByTenSanPhamContaining(keyword).getMaSanPham());
    
        //TRẢ VỀ RESPONSE
        response.put("status", "success");
        response.put("data", sanPhamRepository.findByTenSanPhamContaining(keyword).getMaSanPham());
        response.put("message", "Lấy dữ liệu thành công");
        return response;
    }

    @GetMapping("/GET_PRODUCT_AVAILABILITY")
    public Map<String,Object> getProductAvailability(@RequestParam("keyword") String keyword){
        Map<String,Object> response = new HashMap<>();

        System.out.println("Đây: "+sanPhamRepository.findByTenSanPhamContaining(keyword).getMaSanPham());
    
        //TRẢ VỀ RESPONSE
        response.put("status", "success");
        response.put("data", sanPhamRepository.findByTenSanPhamContaining(keyword).getMaSanPham());
        response.put("message", "Lấy dữ liệu thành công");
        return response;
    }

    @GetMapping("/productNames")
    public Map<String,Object> getProductNames(){
        Map<String,Object> response = new HashMap<>();
        List<String> productNames = new ArrayList<>();
        for(SanPham sp : sanPhamRepository.findAll()){
            productNames.add(sp.getTenSanPham());
        }
    
        //TRẢ VỀ RESPONSE
        response.put("status", "success");
        response.put("data", productNames);
        response.put("message", "Lấy dữ liệu thành công");
        return response;
    }
}
