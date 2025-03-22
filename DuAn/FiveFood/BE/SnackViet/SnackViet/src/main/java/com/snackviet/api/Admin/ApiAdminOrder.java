package com.snackviet.api.Admin;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.HoaDon;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.TrangThaiHoaDonRepository;
import com.snackviet.service.MailService;

@RestController
@CrossOrigin("*")
public class ApiAdminOrder {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    TrangThaiHoaDonRepository trangThaiHoaDonRepository;
    @Autowired
    MailService mailService;

    @GetMapping("api/admin/quan-ly-hoa-don")
    public Map<String,Object> getCards(@RequestParam("pageNo") Optional<Integer> pageNo,
	        @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
	        @RequestParam(name = "search", defaultValue = "") String search,
	        @RequestParam(name = "sort", defaultValue = "") String sort,
	        @RequestParam(name = "ngayFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayFrom,
            @RequestParam(name = "ngayTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayTo){

        Map<String,Object> cards = new HashMap<>();
        //lấy số hóa đơn có trạng thái đã hủy
        cards.put("canceled",hoaDonRepository.findByTrangThaiHoaDonTenTrangThai("Đã hủy").size());
        //lấy tổng số hóa đơn
        cards.put("total", hoaDonRepository.count());

        //PHÂN TRANG VÀ TÌM KIẾM

		int currentPage = pageNo.orElse(1);
	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
	    Page<HoaDon> invoicePage = null;
	    

	    if (!search.isEmpty()) {
            if (sort.equals("Mã hóa đơn")) {
                try {
                    invoicePage = hoaDonRepository.findByMaHoaDon(Integer.parseInt(search), pageable);
                } catch (NumberFormatException e) {
                    invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
                }
            } else if (sort.equals("Tên khách hàng")) {
                invoicePage = hoaDonRepository.findByTaiKhoanHDHoVaTenContaining(search, pageable);
            } else if (sort.equals("Trạng thái")) {
                invoicePage = hoaDonRepository.findByTrangThaiHoaDonTenTrangThaiContaining(search, pageable);
            } else {
                invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
            }
        } else if (!sort.isEmpty()) {
            if (sort.equals("Ngày thanh toán") && ngayFrom != null && ngayTo != null && ngayFrom.before(ngayTo)) {
                invoicePage = hoaDonRepository.findByNgayThanhToanBetween(ngayFrom, ngayTo, pageable);
            } else {
                invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
            }
        } else {
            // Không lọc dữ liệu
            invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
        }
	    

        Map<String,Object> response = new HashMap<>();
        response.put("status","Success");
        response.put("data",Map.of(
            "cards",cards,
            "listTrangThai",trangThaiHoaDonRepository.findAll(),
            "editting",false
        ));
        response.put("message", "Lấy dữ liệu hiển thị HoaDon thành công");

        // Thêm giá trị mới vào "data"
        Map<String, Object> data = new HashMap<>((Map<String, Object>) response.get("data"));

        System.out.println("Đây là review Page: "+invoicePage);

        // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về admin/quanLyHoaDon
	    if (invoicePage == null || invoicePage.isEmpty()) {
	        data.put("invoices", Collections.emptyList());
            data.put("pagination",Map.of(
                "totalItems",0,
                "totalPages",0,
                "currentPage",0,
                "pageSize",pageSize,
                "search",search,
                "sort",sort,
                "ngayFrom",ngayFrom,
                "ngayTo",ngayTo
            ));

	        // Load lại dữ liệu select box đã được chọn
	        data.put("displaySelected", sort);
	        // return "admin/quanLyHoaDon";
	    }

	    data.put("invoices", invoicePage.getContent());
        data.put("pagination",Map.of(
                "totalItems",invoicePage.getTotalElements(),
                "totalPages",invoicePage.getTotalPages(),
                "currentPage",currentPage,
                "pageSize",pageSize,
                "search",search,
                "sort",sort
                // ,
                // "ngayFrom",ngayFrom,
                // "ngayTo",ngayTo
            ));

	    //Load lại dữ liệu select box đã được chọn
	    data.put("displaySelected", sort);

        // Cập nhật lại "data" vào trong response
        response.put("data", data);

        return response;
    }

    @GetMapping("api/admin/quan-ly-hoa-don/{maHoaDon}")
    public Map<String,Object> getOrder(@PathVariable("maHoaDon") int maHoaDon){
        Map<String,Object> response = new HashMap<>();
        Map<String,Object> data = new HashMap<>();
        //set lại trạng thái cho biến editting
        data.put("editting",true);
        response.put("status", "Success");

        List<ChiTietHoaDon> edit = hoaDonRepository.findAllChiTietHoaDonByMaHoaDon(maHoaDon);
        
        HoaDon hd = hoaDonRepository.findById(maHoaDon).get();
	    if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đã đặt hàng")) {
            data.put("listTrangThai",trangThaiHoaDonRepository.findAllByTenTrangThaiIn(new String[] {"Đã Hủy","Đã đặt hàng","Đã xác nhận"}));
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đã xác nhận")) {
	    	data.put("listTrangThai",trangThaiHoaDonRepository.findAllByTenTrangThaiIn(new String[] {"Đã xác nhận","Đang xử lý"}));
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đang xử lý")) {
	    	data.put("listTrangThai",trangThaiHoaDonRepository.findAllByTenTrangThaiIn(new String[] {"Đang xử lý","Đang vận chuyển"}));
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đang vận chuyển")) {
	    	data.put("listTrangThai",trangThaiHoaDonRepository.findAllByTenTrangThaiIn(new String[] {"Đang vận chuyển","Giao thành công"}));
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Giao thành công")) {
	    	data.put("editting",false);
            data.put("listTrangThai",null);
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đã Hủy")) {
	    	data.put("editting",false);
            data.put("listTrangThai",null);
	    }
        //đổ dữ liệu cho bảng chi tiết
        data.put("displayI", edit);
        data.put("displayHoaDon",hoaDonRepository.findById(maHoaDon).get());

        response.put("data", data);
        response.put("message", "Lấy dữ liệu hóa đơn được chọn thành công");
        return response;
    }

    @PutMapping("api/admin/quan-ly-hoa-don/update/{maHoaDon}")
    public Map<String, Object> updateOrderStatus(@PathVariable("maHoaDon") int maHoaDon,@RequestBody Map<String, String> request){
        Map<String, Object> response = new HashMap<>();
        Optional<HoaDon> hd = hoaDonRepository.findById(maHoaDon);
        if(hd.isPresent()){
            HoaDon update = hd.get();
            // Cập nhật trạng thái hóa đơn
            update.setTrangThaiHoaDon(trangThaiHoaDonRepository.findByTenTrangThai(request.get("trangThai")));

            if(update.getTrangThaiHoaDon().getTenTrangThai().equals("Đã Hủy")){
                if(!update.isPhuongThucThanhToan()){
                    mailService.sendOrderCancelledNotificationForOnlinePayment(update.getTaiKhoanHD().getEmail(), update.getMaHoaDon(),request.get("lyDo"));
                }
				else{
                    mailService.sendOrderCancelledNotification(update.getTaiKhoanHD().getEmail(), update.getMaHoaDon(),request.get("lyDo"));
                }
			}
			else if(update.getTrangThaiHoaDon().getTenTrangThai().equals("Giao thành công")){
				mailService.sendOrderDeliveredNotification(update.getTaiKhoanHD().getEmail(), update.getMaHoaDon());
                update.setTrangThai(true);
			}
            update.setLyDoHuy(request.get("lyDo"));

            hoaDonRepository.saveAndFlush(update);
            response.put("status", "Success");
            response.put("message", "Cập nhật trạng thái thành công");
        }else {
            response.put("status", "Error");
            response.put("message", "Không tìm thấy hóa đơn với mã đã cung cấp");
        }
        return response;
    }
}
