package com.snackviet.api.Admin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.DanhGiaRepository;
import com.snackviet.repository.HoaDonRepository;

@RestController
@CrossOrigin("*")
public class ApiAdminStatistics {

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    DanhGiaRepository danhGiaRepository;

    Map<String, String> err = new HashMap<>();

    @PostMapping("api/admin/thong-ke")
    public ResponseEntity<Map<String, Object>> getData(
                            @RequestParam(name = "select",required = false) String select,
                            @RequestBody Map<String, Object> request){
        Map<String,Object> response = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        List<Object[]> revenueData = null;
        long totalRevenue = 0;

        // Định dạng ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date ngayFrom = null;
        Date ngayTo = null;

        //gán cho select giá trị từ request
        select = (String) request.get("select");

        try {
            if (request.get("ngayFrom") != null) {
                String ngayFromStr = request.get("ngayFrom").toString();
                ngayFrom = dateFormat.parse(ngayFromStr);
            }

            if (request.get("ngayTo") != null) {
                String ngayToStr = request.get("ngayTo").toString();
                ngayTo = dateFormat.parse(ngayToStr);
            }
        } catch (Exception e) {
            // Xử lý khi định dạng ngày sai
            response.put("status", "Failed");
            response.put("message", "Định dạng ngày không hợp lệ!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        
        if(ngayFrom!=null && ngayTo!=null && ngayFrom.before(ngayTo)) {
    		if ("Lọc theo doanh thu".equals(select)) {
                revenueData = hoaDonRepository.findRevenueByProductType(ngayFrom, ngayTo);
                data.put("revenueData", revenueData);
                data.put("filterType", "doanhThu");
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
    		err.put("err", "Bộ lọc ngày không hợp lệ!");
            data.put("err",err);
            response.put("status", "Failed");
            response.put("data", err);
            response.put("message", "Lấy dữ liệu thống kê thất bại!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    	}
    	else {
    		if ("Lọc theo doanh thu".equals(select)) {
                revenueData = hoaDonRepository.findAllRevenueByProductType();
                data.put("revenueData", revenueData);
                data.put("filterType", "doanhThu");
            } 
    	}

        if (revenueData != null) {
            for (Object[] row : revenueData) {
                totalRevenue += ((Number) row[2]).longValue();
            }
        }
    	

        // Chuyển đổi Date sang String với định dạng yyyy-MM-dd để đưa vào response
        String ngayFromFormatted = ngayFrom != null ? dateFormat.format(ngayFrom) : null;
        String ngayToFormatted = ngayTo != null ? dateFormat.format(ngayTo) : null;

        data.put("ngayFrom", ngayFromFormatted);  // Đặt chuỗi định dạng vào response
        data.put("ngayTo", ngayToFormatted);      // Đặt chuỗi định dạng vào response
        data.put("totalRevenue", totalRevenue);
        data.put("displaySelected", select);

        response.put("status", "Success");
        response.put("data", data);
        response.put("message", "Lấy dữ liệu thống kê thành công");

        System.out.println("Đây là data: "+response);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
