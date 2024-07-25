package com.snackviet.controller.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.TaiKhoanRepository;

@Controller
public class AdminIndexController {
	
	@Autowired
	ChiTietHoaDonRepository chiTietHoaDonRepository;
	@Autowired
	HoaDonRepository hoaDonRepository;
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	
	@RequestMapping("/adminIndex")
	public String index(Model model) {
		
		//số lượng sản phẩm bán ra
		int totalProduct = chiTietHoaDonRepository.getTotalProduct();
		model.addAttribute("slsp",totalProduct);
		
		//doanh thu đạt được
		double totalRevenue = hoaDonRepository.getTotalRevenue();
		model.addAttribute("dt",totalRevenue);
		
		//lượng khách đăng ký
		int totalRegister = taiKhoanRepository.getTotalRegister();
		model.addAttribute("dk",totalRegister);
		
		//tổng số hóa đơn
		int totalOrder = hoaDonRepository.getTotalOrder();
		model.addAttribute("hd",totalOrder);
		
		
		//biểu đồ biến động doanh thu
		Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        List<Double> monthlyRevenues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Double totalRevenueChart = hoaDonRepository.getTotalRevenueByMonth(month, currentYear);
            if (totalRevenueChart == null) {
            	totalRevenueChart = 0.0;
            }
            monthlyRevenues.add(totalRevenueChart);
        }

        model.addAttribute("monthlyRevenues", monthlyRevenues);
        
     // Lấy danh sách các loại sản phẩm và số lượng bán
        List<Object[]> purchaseCounts = chiTietHoaDonRepository.getPurchaseCountsByProductType();

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (Object[] purchaseCount : purchaseCounts) {
            labels.add((String) purchaseCount[0]);
            data.add(((Number) purchaseCount[1]).longValue());
        }
        
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String labelsJson = objectMapper.writeValueAsString(labels);
            String dataJson = objectMapper.writeValueAsString(data);
            model.addAttribute("labels", labelsJson);
            model.addAttribute("data", dataJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        model.addAttribute("nhan", labels);
        model.addAttribute("dulieu", data);
        
        //lấy ra số lượng sản phẩm bán hằng tháng
        Calendar calendar2 = Calendar.getInstance();
        int currentYear2 = calendar.get(Calendar.YEAR);

        List<Double> monthlyProductSoldRevenues = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            Double totalProductSoldRevenue = chiTietHoaDonRepository.getTotalProductSoldByMonth(month, currentYear2);
            if (totalProductSoldRevenue == null) {
            	totalProductSoldRevenue = 0.0;
            }
            monthlyProductSoldRevenues.add(totalProductSoldRevenue);
        }

        model.addAttribute("monthlyProductSoldRevenues", monthlyProductSoldRevenues);

        return "admin/index";
		
	}
}
