package com.snackviet.api.Admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.TaiKhoanRepository;

@CrossOrigin("*")
@RestController
public class ApiAdminIndex {

    @Autowired
    ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/api/admin/data")
    public Map<String,Object> getData(){
        Map<String,Object> response = new HashMap<>();

        //số lượng sản phẩm bán ra
		int totalProduct = chiTietHoaDonRepository.getTotalProduct();
        //doanh thu đạt được
		double totalRevenue = hoaDonRepository.getTotalRevenue();
        System.out.println("Đây là doanh thu: "+totalRevenue);
        //lượng khách đăng ký
		int totalRegister = taiKhoanRepository.getTotalRegister();
        //tổng số hóa đơn
		int totalOrder = hoaDonRepository.getTotalOrder();

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

        // Lấy danh sách các loại sản phẩm và số lượng bán
        List<Object[]> purchaseCounts = chiTietHoaDonRepository.getPurchaseCountsByProductType();

        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (Object[] purchaseCount : purchaseCounts) {
            labels.add((String) purchaseCount[0]);
            data.add(((Number) purchaseCount[1]).longValue());
        }

        Map<String,Object> pieChart = new HashMap<>();
        pieChart.put("labels", labels);
        pieChart.put("dulieu", data);

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


        //TRẢ VỀ RESPONSE
        response.put("status", "success");
        response.put("data", Map.of(
            "totalProduct", totalProduct,
            "totalRevenue",totalRevenue,
            "totalRegister",totalRegister,
            "totalOrder",totalOrder,
            "monthlyRevenues", monthlyRevenues,
            "pieChart",pieChart,
            "monthlyProductSoldRevenues",monthlyProductSoldRevenues

        ));
        response.put("message", "Lấy dữ liệu Dashboard thành công");
        return response;
    }

}
