package com.snackviet.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.DanhGiaRepository;
import com.snackviet.repository.HoaDonRepository;

@Controller
public class ThongKeController {

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    DanhGiaRepository danhGiaRepository;

    @RequestMapping("/adminIndex/thong-ke")
    public String analytics(@RequestParam(name = "ngayFrom",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayFrom,
                            @RequestParam(name = "ngayTo",required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayTo,
                            @RequestParam(name = "select",required = false) String select,
                            Model model) {
        
    	List<Object[]> revenueData = null;
        long totalRevenue = 0;
    	
    	if(ngayFrom!=null && ngayTo!=null && ngayFrom.before(ngayTo)) {
    		if ("Lọc theo doanh thu".equals(select)) {
                revenueData = hoaDonRepository.findRevenueByProductType(ngayFrom, ngayTo);
                model.addAttribute("revenueData", revenueData);
                model.addAttribute("filterType", "doanhThu");
            } 
//            else if ("Lọc theo sản phẩm đã bán".equals(select)) {
//                List<Object[]> productsSold = chiTietHoaDonRepository.findProductsSoldByDateRange(start, end);
//                model.addAttribute("productsSold", productsSold);
//                model.addAttribute("filterType", "sanPham");
//            } 
//            else if ("Lọc theo số lượt đánh giá sản phẩm".equals(select)) {
//                List<Object[]> productReviews = danhGiaRepository.findProductReviewsByDateRange(start, end);
//                model.addAttribute("productReviews", productReviews);
//                model.addAttribute("filterType", "danhGia");
//            }
    	}
    	else if(ngayFrom!=null && ngayTo!=null && ngayFrom.after(ngayTo)) {
    		return "redirect:/adminIndex/thong-ke?notFound=date";
    	}
    	else {
    		if ("Lọc theo doanh thu".equals(select)) {
                revenueData = hoaDonRepository.findAllRevenueByProductType();
                model.addAttribute("revenueData", revenueData);
                model.addAttribute("filterType", "doanhThu");
            } 
    	}
    	
    	if (revenueData != null) {
            for (Object[] row : revenueData) {
                totalRevenue += ((Number) row[2]).longValue();
            }
        }
    	

        model.addAttribute("ngayFrom", ngayFrom);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("ngayTo", ngayTo);
        model.addAttribute("displaySelected", select);
    	
        return "admin/thongKe";
    }
}
