package com.snackviet.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.snackviet.model.ChiTietHoaDon;
import com.snackviet.model.DanhGia;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.HoaDon;
import com.snackviet.model.LoaiSP;
import com.snackviet.model.SanPham;
import com.snackviet.model.TrangThaiHoaDon;
import com.snackviet.repository.ChiTietHoaDonRepository;
import com.snackviet.repository.HoaDonRepository;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.repository.TrangThaiHoaDonRepository;

import jakarta.servlet.ServletContext;

@Controller
public class HoaDonController {
	
	@Autowired
	HoaDonRepository hoaDonRepository;
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	ChiTietHoaDonRepository chiTietHoaDonRepository;
	@Autowired
	TrangThaiHoaDonRepository trangThaiHoaDonRepository;
	@Autowired
	ServletContext app;
	boolean editting = false;
	String errType = "";
	
	@RequestMapping("/adminIndex/quan-ly-hoa-don")
	public String invoice(Model model, @RequestParam("pageNo") Optional<Integer> pageNo,
	        @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
	        @RequestParam(name = "search", defaultValue = "") String search,
	        @RequestParam(name = "sort", defaultValue = "") String sort,
	        @RequestParam(name = "ngayFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayFrom,
            @RequestParam(name = "ngayTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayTo) {
		
		
		editting = false;
		model.addAttribute("editting",editting);
		
		//lấy số hóa đơn có trạng thái đã hủy
		model.addAttribute("canceled",hoaDonRepository.findByTrangThaiHoaDonTenTrangThai("Đã hủy"));
		//lấy tổng số hóa đơn
		model.addAttribute("total",hoaDonRepository.findAll());
		//lấy list trạng thái hóa đơn
		model.addAttribute("listTrangThai",trangThaiHoaDonRepository.findAll());
		
		//PHÂN TRANG VÀ TÌM KIẾM
		int currentPage = pageNo.orElse(1);
	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
	    Page<HoaDon> invoicePage = null;
	    

	    if (!search.isEmpty() && !sort.isEmpty()) {
	        // Lọc dữ liệu theo cả search và sort
	    	if (sort.equals("Mã hóa đơn")) {
	            try {
	            	invoicePage = hoaDonRepository.findByMaHoaDon(Integer.parseInt(search), pageable);
				} catch (Exception e) {
					invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
				}
	        } 
	        else if (sort.equals("Tên khách hàng")) {
	            invoicePage = hoaDonRepository.findByTaiKhoanHDHoVaTenContaining(search, pageable);
	        }
	        else if (sort.equals("Trạng thái")) {
	        	invoicePage = hoaDonRepository.findByTrangThaiHoaDonTenTrangThaiContaining(search, pageable);
	        }
	    } 
	    else if(!sort.isEmpty()) {
	        if (sort.equals("Ngày thanh toán") && ngayFrom != null && ngayTo != null && ngayFrom.before(ngayTo)) {
	    		invoicePage = hoaDonRepository.findByNgayThanhToanBetween(ngayFrom, ngayTo, pageable);
	    	}
	        else if (sort.equals("Ngày thanh toán") && ngayFrom != null && ngayTo != null && ngayFrom.after(ngayTo)) {
	    		return "redirect:/adminIndex/quan-ly-hoa-don?notFound=date";
	    	}
	    }
	    else {
	        // Không lọc dữ liệu
	        invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
	    }
	    
	    
	    // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về admin/quanLyHoaDon
	    if (invoicePage == null || invoicePage.isEmpty()) {
	        model.addAttribute("invoices", Collections.emptyList());
	        model.addAttribute("totalItems", 0);
	        model.addAttribute("totalPages", 0);
	        model.addAttribute("currentPage", 0);
	        model.addAttribute("pageSize", pageSize);
	        model.addAttribute("search", search);
	        model.addAttribute("sort", sort);
	        model.addAttribute("ngayFrom", ngayFrom);
	        model.addAttribute("ngayTo", ngayTo);
	        // Load lại dữ liệu select box đã được chọn
	        model.addAttribute("displaySelected", sort);
	        return "admin/quanLyHoaDon";
	    }

	    model.addAttribute("invoices", invoicePage.getContent());
	    model.addAttribute("totalItems", invoicePage.getTotalElements());
	    model.addAttribute("totalPages", invoicePage.getTotalPages());
	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("search", search);
	    model.addAttribute("sort", sort);
	    model.addAttribute("ngayFrom", ngayFrom);
	    model.addAttribute("ngayTo", ngayTo);

	    //Load lại dữ liệu select box đã được chọn
	    model.addAttribute("displaySelected", sort);
	    
	    System.out.println(sort);
		
		return "admin/quanLyHoaDon";
		
	}
	
	@RequestMapping("/adminIndex/quan-ly-hoa-don/{maHoaDon}")
	public String editHoaDon(Model model,@PathVariable("maHoaDon") int maHoaDon,
			@RequestParam("pageNo") Optional<Integer> pageNo,
			@RequestParam(name = "search", defaultValue = "") String search,
	        @RequestParam(name = "sort", defaultValue = "") String sort,
	        @RequestParam(name = "ngayFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayFrom,
            @RequestParam(name = "ngayTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date ngayTo) {
		

		editting = true;
		model.addAttribute("editting",editting);
		
		//Bắt lỗi tìm với id nằm ngoài dữ liệu
//		Optional<List<ChiTietHoaDon>> editOptional = Optional.of(hoaDonRepository.findAllChiTietHoaDonByMaHoaDon(maHoaDon));
//		if (!editOptional.isPresent()) {
//			return "redirect:/adminIndex/quan-ly-hoa-don?notFound=true";
//		}
//		List<ChiTietHoaDon> edit = (List<ChiTietHoaDon>) editOptional.get();
		
		//lấy số hóa đơn có trạng thái đã hủy
		model.addAttribute("canceled",hoaDonRepository.findByTrangThaiHoaDonTenTrangThai("Đã hủy"));
		//lấy tổng số hóa đơn
		model.addAttribute("total",hoaDonRepository.findAll());
		//lấy list trạng thái hóa đơn
		model.addAttribute("listTrangThai",trangThaiHoaDonRepository.findAll());
		
		List<ChiTietHoaDon> edit = hoaDonRepository.findAllChiTietHoaDonByMaHoaDon(maHoaDon);
		
		//PHÂN TRANG SAU KHI EDIT
		int pageSize = 5;

		if (pageSize == 0) {
			pageSize = 5;
		}

		int currentPage = pageNo.orElse(1);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<HoaDon> invoicePage = null;

		//Hiển thị lại bộ lọc
		if (!search.isEmpty() && !sort.isEmpty()) {
	        // Lọc dữ liệu theo cả search và sort
	    	if (sort.equals("Mã hóa đơn")) {
	            try {
	            	invoicePage = hoaDonRepository.findByMaHoaDon(Integer.parseInt(search), pageable);
				} catch (Exception e) {
					invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
				}
	        } 
	        else if (sort.equals("Tên khách hàng")) {
	            invoicePage = hoaDonRepository.findByTaiKhoanHDHoVaTenContaining(search, pageable);
	        }
	        else if (sort.equals("Trạng thái")) {
	        	invoicePage = hoaDonRepository.findByTrangThaiHoaDonTenTrangThaiContaining(search, pageable);
	        }
	    } 
	    else if(!sort.isEmpty()) {
	    	if (sort.equals("Ngày thanh toán") && ngayFrom != null && ngayTo != null && ngayFrom.before(ngayTo)) {
	    		invoicePage = hoaDonRepository.findByNgayThanhToanBetween(ngayFrom, ngayTo, pageable);
	    	}
	        else if (sort.equals("Ngày thanh toán") && ngayFrom != null && ngayTo != null && ngayFrom.after(ngayTo)) {
	    		return "redirect:/adminIndex/quan-ly-hoa-don?notFound=date";
	    	}
	    }
	    else {
	        // Không lọc dữ liệu
	        invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
	    }
	    
	 // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về admin/quanLyHoaDon
	    if (invoicePage == null || invoicePage.isEmpty()) {
	        model.addAttribute("invoices", Collections.emptyList());
	        model.addAttribute("totalItems", 0);
	        model.addAttribute("totalPages", 0);
	        model.addAttribute("currentPage", 0);
	        model.addAttribute("pageSize", pageSize);
	        model.addAttribute("search", search);
	        model.addAttribute("sort", sort);
	        model.addAttribute("ngayFrom", ngayFrom);
	        model.addAttribute("ngayTo", ngayTo);
	        // Load lại dữ liệu select box đã được chọn
	        model.addAttribute("displaySelected", sort);
	        return "admin/quanLyHoaDon";
	    }
	    
	    HoaDon hd = hoaDonRepository.findById(maHoaDon).get();
	    if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đã đặt hàng")) {
	    	model.addAttribute("listTrangThai",trangThaiHoaDonRepository.findAllByTenTrangThaiIn(new String[] {"Đã Hủy","Đã đặt hàng","Đã xác nhận"}));
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đã xác nhận")) {
	    	model.addAttribute("listTrangThai",trangThaiHoaDonRepository.findAllByTenTrangThaiIn(new String[] {"Đã xác nhận","Đang xử lý "}));
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đang xử lý ")) {
	    	model.addAttribute("listTrangThai",trangThaiHoaDonRepository.findAllByTenTrangThaiIn(new String[] {"Đang xử lý ","Đang vận chuyển"}));
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đang vận chuyển")) {
	    	model.addAttribute("listTrangThai",trangThaiHoaDonRepository.findAllByTenTrangThaiIn(new String[] {"Đang vận chuyển","Giao thành công"}));
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Giao thành công")) {
	    	model.addAttribute("editting",false);
	    	model.addAttribute("listTrangThai","");
	    }
	    else if(hd.getTrangThaiHoaDon().getTenTrangThai().equals("Đã Hủy")) {
	    	model.addAttribute("editting",false);
	    	model.addAttribute("listTrangThai","");
	    }
		
		long totalItems = invoicePage.getTotalElements();
		int totalPages = invoicePage.getTotalPages();

		model.addAttribute("invoices", invoicePage.getContent());
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("search", search);
		model.addAttribute("sort", sort);
		model.addAttribute("displaySelected", sort);
	    model.addAttribute("ngayFrom", ngayFrom);
	    model.addAttribute("ngayTo", ngayTo);
		
	    //đổ dữ liệu cho bảng chi tiết
	    model.addAttribute("displayI", edit);
	    model.addAttribute("displayHoaDon",hoaDonRepository.findById(maHoaDon).get());
		
		return "admin/quanLyHoaDon";
		
	}
	
	@PostMapping("/adminIndex/quan-ly-hoa-don/update/{maHoaDon}")
	public String updateInvoice(Model model,
	                            @RequestParam(name="trangThaiHoaDon.tenTrangThai", required = false) String trangThai,
	                            @PathVariable("maHoaDon") int maHoaDon) {
	    model.addAttribute("idForUpdate", maHoaDon);

	    // Tìm kiếm TrangThaiHoaDon từ tên trạng thái
	    TrangThaiHoaDon tthd = trangThaiHoaDonRepository.findByTenTrangThai(trangThai);

	    if (validation(model, tthd)) {
	        HoaDon update = hoaDonRepository.findById(maHoaDon).orElse(null);

	        if (update == null || tthd == null) {
	            model.addAttribute("errType", "Trạng thái hóa đơn không hợp lệ!");
	            return "admin/quanLySanPham";
	        }

	        update.setTrangThaiHoaDon(tthd);
	        hoaDonRepository.saveAndFlush(update);

	        return "redirect:/adminIndex/quan-ly-hoa-don?update=success";
	    }

	    model.addAttribute("invoices", hoaDonRepository.findAllByOrderByMaHoaDonDesc());
	    editting = true;
	    model.addAttribute("editting", editting);

	    // Lấy list trạng thái hóa đơn
	    model.addAttribute("listTrangThai", trangThaiHoaDonRepository.findAll());

	    // Lấy lại danh sách sản phẩm để hiển thị
	    Pageable pageable = PageRequest.of(0, 5); // Chỉnh sửa pageNo và pageSize tùy ý
	    Page<HoaDon> invoicePage = hoaDonRepository.findAllByOrderByMaHoaDonDesc(pageable);
	    model.addAttribute("invoices", invoicePage.getContent());
	    model.addAttribute("totalItems", invoicePage.getTotalElements());
	    model.addAttribute("totalPages", invoicePage.getTotalPages());
	    model.addAttribute("currentPage", 1); // hoặc sử dụng giá trị hiện tại nếu có
	    model.addAttribute("pageSize", 5); // Chỉnh sửa pageSize tùy ý
	    model.addAttribute("search", ""); // hoặc giá trị search hiện tại
	    model.addAttribute("sort", ""); // hoặc giá trị sort hiện tại
	    model.addAttribute("displaySelected", ""); // hoặc giá trị sort hiện tại

	    HoaDon reload = hoaDonRepository.findById((int) model.getAttribute("idForUpdate")).orElse(null);
	    model.addAttribute("displayHoaDon", reload);

	    return "admin/quanLySanPham";
	}

	


	public boolean validation(Model model, TrangThaiHoaDon tthd) {
	    boolean hasError = false;

	    if (tthd == null || tthd.getTenTrangThai() == null) {
	        hasError = true;
	        errType = "Vui lòng chọn trạng thái để cập nhật đơn hàng";
	        model.addAttribute("errType", errType);
	    }

	    return !hasError;
	}
	
}
