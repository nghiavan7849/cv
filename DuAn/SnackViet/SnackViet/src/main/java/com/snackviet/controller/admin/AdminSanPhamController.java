package com.snackviet.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.query.sqm.UnknownPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.snackviet.groupValidation.FullValidationSanPhamGroup;
import com.snackviet.groupValidation.FullValidationTaiKhoanGroup;
import com.snackviet.model.HinhAnhDG;
import com.snackviet.model.HinhAnhSP;
import com.snackviet.model.LoaiSP;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.HinhAnhSPRepository;
import com.snackviet.repository.LoaiSPRepository;
import com.snackviet.repository.SanPhamRepository;

import jakarta.servlet.ServletContext;

@Controller
public class AdminSanPhamController {
	
	boolean editting = false;
	@Autowired
	SanPhamRepository sanPhamRepository;
	@Autowired
	LoaiSPRepository loaiSPRepository;
	@Autowired
	HinhAnhSPRepository hinhAnhSPRepository;
	@Autowired
	ServletContext app;
	
	//VALIDATION MESSAGES
	String errProductName="", errPrice="", errPass="", errQuantity="", errWeight="", errType="", errStatus="", errImg="";
	
	@RequestMapping("/adminIndex/quan-ly-san-pham")
	public String products(Model model, @RequestParam("pageNo") Optional<Integer> pageNo,
	        @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
	        @RequestParam(name = "search", defaultValue = "") String search,
	        @RequestParam(name = "sort", defaultValue = "") String sort) {
		
		
		//lấy tổng số sản phẩm
		model.addAttribute("total",sanPhamRepository.findAll());
		//lấy số sản phẩm ngưng bán
		model.addAttribute("canceled",sanPhamRepository.findByTrangThai(false));
		
		
		model.addAttribute("sanPham",new SanPham());
		
		editting = false;
		model.addAttribute("editting",editting);
		
		//PHÂN TRANG VÀ TÌM KIẾM
		int currentPage = pageNo.orElse(1);
	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
	    Page<SanPham> productPage = null;
	    

	    if (!search.isEmpty() && !sort.isEmpty()) {
	        // Lọc dữ liệu theo cả search và sort
	    	if (sort.equals("Loại")) {
	    	    productPage = sanPhamRepository.findByLoaiSPTenLoaiContaining(search, pageable);
	    	} else if (sort.equals("Tên sản phẩm")) {
	            productPage = sanPhamRepository.findByTenSanPhamContaining(search, pageable);
	        } 
	        else if (sort.equals("Trọng lượng")) {
	            productPage = sanPhamRepository.findByTrongLuong(Double.parseDouble(search), pageable);
	        } 
	        else if (sort.equals("Trạng thái hoạt động")) {
	        	productPage = sanPhamRepository.findByTrangThai(true, pageable);
	        } 
	        else if (sort.equals("Trạng thái ngưng bán")) {
	        	productPage = sanPhamRepository.findByTrangThai(false, pageable);
	        } 
	    } 
	    else if(!sort.isEmpty()) {
	    	if (sort.equals("Loại") && search.equals("")) {
	    	    productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	    	} else if (sort.equals("Tên sản phẩm") && search.equals("")) {
	    		productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	        } 
	        else if (sort.equals("Trọng lượng") && search.equals("")) {
	        	productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	        }
	        else if (sort.equals("Giá thấp tới cao")) {
	        	productPage = sanPhamRepository.findByOrderByGiaAsc(pageable);
	        }
	        else if (sort.equals("Giá cao tới thấp")) {
	        	productPage = sanPhamRepository.findByOrderByGiaDesc(pageable);
	        }
	        else if (sort.equals("Trạng thái hoạt động")) {
	        	productPage = sanPhamRepository.findByTrangThai(true, pageable);
	        } 
	        else if (sort.equals("Trạng thái ngưng bán")) {
	        	productPage = sanPhamRepository.findByTrangThai(false, pageable);
	        } 
	    }
	    else {
	        // Không lọc dữ liệu
	        productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	    }
	    
	 // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về admin/quanLySanPham
	    if (productPage == null || productPage.isEmpty()) {
	        model.addAttribute("products", Collections.emptyList());
	        model.addAttribute("totalItems", 0);
	        model.addAttribute("totalPages", 0);
	        model.addAttribute("currentPage", 0);
	        model.addAttribute("pageSize", pageSize);
	        model.addAttribute("search", search);
	        model.addAttribute("sort", sort);
	        // Load lại dữ liệu select box đã được chọn
	        model.addAttribute("displaySelected", sort);
	        return "admin/quanLySanPham";
	    }

	    model.addAttribute("products", productPage.getContent());
	    model.addAttribute("totalItems", productPage.getTotalElements());
	    model.addAttribute("totalPages", productPage.getTotalPages());
	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("search", search);
	    model.addAttribute("sort", sort);

	    //Load lại dữ liệu select box đã được chọn
	    model.addAttribute("displaySelected", sort);
	    
	    //Đổ dữ liệu cho select box
	    model.addAttribute("listLoaiSP",loaiSPRepository.findAll());
	    
	    System.out.println(sort);
		
		return "admin/quanLySanPham";
		
	}
	
	@RequestMapping("/adminIndex/quan-ly-san-pham/{maSanPham}")
	public String editSanPham(Model model,@PathVariable("maSanPham") int maSanPham,
			@RequestParam("pageNo") Optional<Integer> pageNo,
			@RequestParam(name = "search", defaultValue = "") String search,
	        @RequestParam(name = "sort", defaultValue = "") String sort) {
		
		
		//lấy tổng số sản phẩm
		model.addAttribute("total",sanPhamRepository.findAll());
		//lấy số sản phẩm ngưng bán
		model.addAttribute("canceled",sanPhamRepository.findByTrangThai(false));
		
		//Bắt lỗi tìm với id nằm ngoài dữ liệu
		Optional<SanPham> editOptional = sanPhamRepository.findById(maSanPham);
	    if (!editOptional.isPresent()) {
	        return "redirect:/adminIndex/quan-ly-san-pham?notFound=true";
	    }
	    SanPham edit = editOptional.get();
	    
	    //Hiển thị list hình ảnh sản phẩm
	    List<HinhAnhSP> listHinhSP = hinhAnhSPRepository.findBySanPhamHAMaSanPham(maSanPham);
		model.addAttribute("listHinhSP", listHinhSP);
		
		
		editting = true;
		model.addAttribute("editting",editting);
		
		//PHÂN TRANG SAU KHI EDIT
		int pageSize = 5;

		if (pageSize == 0) {
			pageSize = 5;
		}

		int currentPage = pageNo.orElse(1);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<SanPham> productPage = null;

		//Hiển thị lại bộ lọc
		if (!search.isEmpty() && !sort.isEmpty()) {
	        // Lọc dữ liệu theo cả search và sort
	    	if (sort.equals("Loại")) {
	    	    productPage = sanPhamRepository.findByLoaiSPTenLoaiContaining(search, pageable);
	    	} else if (sort.equals("Tên sản phẩm")) {
	            productPage = sanPhamRepository.findByTenSanPhamContaining(search, pageable);
	        } 
	        else if (sort.equals("Trọng lượng")) {
	            productPage = sanPhamRepository.findByTrongLuong(Double.parseDouble(search), pageable);
	        } 
	        else if (sort.equals("Trạng thái hoạt động")) {
	        	productPage = sanPhamRepository.findByTrangThai(true, pageable);
	        } 
	        else if (sort.equals("Trạng thái ngưng bán")) {
	        	productPage = sanPhamRepository.findByTrangThai(false, pageable);
	        } 
	    } 
	    else if(!sort.isEmpty()) {
	    	if (sort.equals("Loại") && search.equals("")) {
	    	    productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	    	} else if (sort.equals("Tên sản phẩm") && search.equals("")) {
	    		productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	        } 
	        else if (sort.equals("Trọng lượng") && search.equals("")) {
	        	productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	        }
	        else if (sort.equals("Giá thấp tới cao")) {
	        	productPage = sanPhamRepository.findByOrderByGiaAsc(pageable);
	        }
	        else if (sort.equals("Giá cao tới thấp")) {
	        	productPage = sanPhamRepository.findByOrderByGiaDesc(pageable);
	        }
	        else if (sort.equals("Trạng thái hoạt động")) {
	        	productPage = sanPhamRepository.findByTrangThai(true, pageable);
	        } 
	        else if (sort.equals("Trạng thái ngưng bán")) {
	        	productPage = sanPhamRepository.findByTrangThai(false, pageable);
	        } 
	    }
	    else {
	        // Không lọc dữ liệu
	        productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
	    }
		
		// Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về admin/quanLySanPham
	    if (productPage == null || productPage.isEmpty()) {
	        model.addAttribute("products", Collections.emptyList());
	        model.addAttribute("totalItems", 0);
	        model.addAttribute("totalPages", 0);
	        model.addAttribute("currentPage", 0);
	        model.addAttribute("pageSize", pageSize);
	        model.addAttribute("search", search);
	        model.addAttribute("sort", sort);
	        // Load lại dữ liệu select box đã được chọn
	        model.addAttribute("displaySelected", sort);
	        return "admin/quanLySanPham";
	    }
		
		long totalItems = productPage.getTotalElements();
		int totalPages = productPage.getTotalPages();

		model.addAttribute("products", productPage.getContent());
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("search", search);
		model.addAttribute("sort", sort);
		model.addAttribute("displaySelected", sort);
		
		
		model.addAttribute("listLoaiSP",loaiSPRepository.findAll());
		model.addAttribute("displayP", edit);
		
		model.addAttribute("idForUpdate",edit.getMaSanPham());
		
		return "admin/quanLySanPham";
		
	}
	
	@PostMapping("/adminIndex/quan-ly-san-pham/insert")
	public String addProduct(Model model,
			@RequestParam("hinhAnh") String hinhAnhList,
			@RequestParam("tenSanPham") String productName,
			@RequestParam("gia") double price,
			@RequestParam("soLuong") int quantity,
			@RequestParam("trongLuong") double weight,
			@RequestParam(name="loaiSP.tenLoai",required = false) String type,
			@RequestParam("trangThai") String status,
			@RequestParam("moTa") String moTa) {
		
		// Tìm kiếm LoaiSP từ tên loại
		LoaiSP loaiSP = loaiSPRepository.findByTenLoai(type);
	
		String[] hinhAnhArray = hinhAnhList.split(",");
		
		if(validation(model, productName, price, quantity, weight, loaiSP, status, hinhAnhList,false)) {
			
			SanPham insert = new SanPham();
			
			insert.setTenSanPham(productName);
			insert.setGia(price);
			insert.setSoLuong(quantity);
			insert.setTrongLuong(weight);

			if (loaiSP == null) {
	            model.addAttribute("errType", "Loại sản phẩm không hợp lệ!");
	            return "admin/quanLySanPham";
	        }
			insert.setLoaiSP(loaiSP);
			
			insert.setNgayThem(new Date());
			
			insert.setTrangThai("Còn bán".equals(status));
			
			insert.setHinhAnh(hinhAnhArray[0]);

			insert.setMoTa(moTa);
			sanPhamRepository.saveAndFlush(insert);
			
			for(String hinh : hinhAnhArray) {
			    // Tạo một đối tượng hinhAnhSP mới cho mỗi ảnh
			    HinhAnhSP hinhAnhSP = new HinhAnhSP();
			    hinhAnhSP.setSanPhamHA(insert); // Đặt lại giá trị SanPhamHA
			    hinhAnhSP.setTenHinhAnh(hinh);  // Đặt tên cho ảnh

			    // Lưu đối tượng hinhAnhSP vào cơ sở dữ liệu
			    hinhAnhSPRepository.saveAndFlush(hinhAnhSP);
			}
			
			
			return "redirect:/adminIndex/quan-ly-san-pham?insert=success";
		}

		
		model.addAttribute("products", sanPhamRepository.findAll());
		editting = false;
		model.addAttribute("editting",editting);
		
		//lấy lại dữ liệu người dùng đã nhập và hiển thị
		SanPham reload = new SanPham();
		reload.setTenSanPham(productName);
		reload.setGia(price);
		reload.setSoLuong(quantity);
		reload.setTrongLuong(weight);
		if (loaiSP == null) {
            model.addAttribute("errType", "Loại sản phẩm không hợp lệ!");
//            return "admin/quanLySanPham";
        }
		reload.setLoaiSP(loaiSP);
		
		reload.setNgayThem(new Date());
		
		reload.setTrangThai("Còn bán".equals(status));
		
		
		for(String hinh : hinhAnhArray) {
		    // Tạo một đối tượng hinhAnhSP mới cho mỗi ảnh
		    HinhAnhSP hinhAnhSP = new HinhAnhSP();
		    hinhAnhSP.setSanPhamHA(reload); // Đặt lại giá trị SanPhamHA
		    hinhAnhSP.setTenHinhAnh(hinh);  // Đặt tên cho ảnh

		}
		reload.setMoTa(moTa);
		
		model.addAttribute("displayP",reload);
		
		

		// Lấy lại danh sách sản phẩm để hiển thị
        Pageable pageable = PageRequest.of(0, 5); // Chỉnh sửa pageNo và pageSize tùy ý
        Page<SanPham> productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", 1); // hoặc sử dụng giá trị hiện tại nếu có
        model.addAttribute("pageSize", 5); // Chỉnh sửa pageSize tùy ý
        model.addAttribute("search", ""); // hoặc giá trị search hiện tại
        model.addAttribute("sort", ""); // hoặc giá trị sort hiện tại
        model.addAttribute("displaySelected", ""); // hoặc giá trị sort hiện tại
        
        model.addAttribute("listLoaiSP",loaiSPRepository.findAll());
		
		return "admin/quanLySanPham";
		
	}
	
	
	@PostMapping("/adminIndex/quan-ly-san-pham/update/{maSanPham}")
	@Transactional
	public String updateProduct(Model model,
			@RequestParam("idForUpdate") int idForUpdate,
			@RequestParam("hinhAnh") String hinhAnhList,
			@PathVariable("maSanPham") int maSanPham,
			@RequestParam("tenSanPham") String productName,
			@RequestParam("gia") double price,
			@RequestParam("soLuong") int quantity,
			@RequestParam("trongLuong") double weight,
			@RequestParam(name="loaiSP.tenLoai",required = false) String type,
			@RequestParam("trangThai") String status,
			@RequestParam("moTa") String moTa) {
		
		model.addAttribute("idForUpdate",maSanPham);
		
		String[] hinhAnhArray = hinhAnhList.split(",");
		SanPham update = sanPhamRepository.findById(maSanPham).get();
		
		// Kiểm tra nếu người dùng không chọn hình ảnh mới thì sử dụng hình ảnh hiện tại
	    if (hinhAnhList.equals("")) {
	        List<HinhAnhSP> currentImages = update.getListHinhAnhSP();
	        hinhAnhArray = currentImages.stream().map(HinhAnhSP::getTenHinhAnh).toArray(String[]::new);
	    } else {
	        hinhAnhArray = hinhAnhList.split(",");
	    }
		
		// Tìm kiếm LoaiSP từ tên loại
		LoaiSP loaiSP = loaiSPRepository.findByTenLoai(type);
		
		if(validation(model, productName, price, quantity, weight, loaiSP, status, hinhAnhList,true)) {
			
			

			update.setTenSanPham(productName);
			update.setGia(price);
			update.setSoLuong(quantity);
			update.setTrongLuong(weight);
			
	        if (loaiSP == null) {
	            model.addAttribute("errType", "Loại sản phẩm không hợp lệ!");
	            return "admin/quanLySanPham";
	        }
			
	        update.setLoaiSP(loaiSP);
			
			update.setTrangThai("Còn bán".equals(status));		
			update.setMoTa(moTa);
			update.setHinhAnh(hinhAnhArray[0]);
			sanPhamRepository.saveAndFlush(update);
			
			
			//bắt đầu xóa hình cũ
			List<HinhAnhSP> hinhAnhSP = update.getListHinhAnhSP();
		    
			for(int i = 0;i<hinhAnhSP.size();i++) {
				if(hinhAnhSP!=null) {
			    	hinhAnhSPRepository.deleteByTenHinhAnh(hinhAnhSP.get(i).getTenHinhAnh());
			    }
			}
			
			
			
		    
			//thêm hình mới
			for(String hinh : hinhAnhArray) {
			    // Tạo một đối tượng hinhAnhSP mới cho mỗi ảnh
			    
			    HinhAnhSP updateHinh = new HinhAnhSP();			    
			    updateHinh.setSanPhamHA(update); // Đặt lại giá trị SanPhamHA
			    updateHinh.setTenHinhAnh(hinh);  // Đặt tên cho ảnh

			    // Lưu đối tượng hinhAnhSP vào cơ sở dữ liệu
			    hinhAnhSPRepository.saveAndFlush(updateHinh);
			}
			
			
			return "redirect:/adminIndex/quan-ly-san-pham?update=success";
		}
		
		System.out.println(type);
		
		model.addAttribute("products", sanPhamRepository.findAllByOrderByMaSanPhamDesc());
		editting = true;
		model.addAttribute("editting",editting);
		model.addAttribute("listLoaiSP",loaiSPRepository.findAll());
		
		
		//lấy lại dữ liệu người dùng đã nhập và hiển thị
		SanPham reload = new SanPham();
		reload.setMaSanPham((int) model.getAttribute("idForUpdate"));
		reload.setTenSanPham(productName);
		reload.setGia(price);
		reload.setSoLuong(quantity);
		reload.setTrongLuong(weight);
		if (loaiSP == null) {
		    model.addAttribute("errType", "Loại sản phẩm không hợp lệ!");
//		    return "admin/quanLySanPham";
		}
		reload.setLoaiSP(loaiSP);
		
		reload.setNgayThem(new Date());
		
		reload.setTrangThai("Còn bán".equals(status));
		
		
		List<HinhAnhSP> list = hinhAnhSPRepository.findBySanPhamHAMaSanPham(maSanPham);
	    model.addAttribute("listHinhSP", list);
		reload.setMoTa(moTa);
				
		model.addAttribute("displayP",reload);
		
		// Lấy lại danh sách sản phẩm để hiển thị
        Pageable pageable = PageRequest.of(0, 5); // Chỉnh sửa pageNo và pageSize tùy ý
        Page<SanPham> productPage = sanPhamRepository.findAllByOrderByMaSanPhamDesc(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalItems", productPage.getTotalElements());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", 1); // hoặc sử dụng giá trị hiện tại nếu có
        model.addAttribute("pageSize", 5); // Chỉnh sửa pageSize tùy ý
        model.addAttribute("search", ""); // hoặc giá trị search hiện tại
        model.addAttribute("sort", ""); // hoặc giá trị sort hiện tại
        model.addAttribute("displaySelected", ""); // hoặc giá trị sort hiện tại
        
		
		return "admin/quanLySanPham";
		
	}
	
	//PHẦN XỬ LÝ UPLOAD NHIỀU ẢNH
//	@PostMapping("/image/AnhSanPham/")
//    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) {
//        try {
//            // Lưu tệp vào server hoặc thư mục
//            String fileName = file.getOriginalFilename();
//            Path uploadPath = Paths.get("/image/AnhSanPham/" + fileName);
//            
//            // Tạo thư mục nếu chưa tồn tại
//            if (!Files.exists(uploadPath.getParent())) {
//                Files.createDirectories(uploadPath.getParent());
//            }
//            
//            // Sao chép nội dung tệp vào thư mục lưu trữ
//            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
//
//            // Trả về URL của tệp đã tải lên
//            String fileUrl = "/image/AnhSanPham/" + fileName;
//            return ResponseEntity.ok().body(Collections.singletonMap("url", fileUrl));
//        } catch (IOException e) {
//            // Xử lý lỗi khi không thể tải lên tệp
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
//        }
//    }

	
	@RequestMapping("/adminIndex/quan-ly-san-pham/cancel")
	public String cancel(Model model) {
		
		return "redirect:/adminIndex/quan-ly-san-pham";
	}
	
public boolean validation(Model model,String productName,double price,int quantity,double weight,LoaiSP loaiSP,String status,String image,boolean isUpdate) {
		
		boolean hasError = false;
		String[] hinhAnhArray = image.split(",");
		
		if(productName.equals("")) {
			errProductName = "Vui lòng nhập tên sản phẩm!";
			model.addAttribute("errProductName",errProductName);
			hasError = true;
		}
		else if(productName.length()<8) {
			errProductName = "Tên sản phẩm phải nhiều hơn 8 ký tự!";
			model.addAttribute("errProductName",errProductName);
			hasError = true;
		}
		if(price<0) {
			errPrice = "Giá không được nhỏ hơn 0!";
			model.addAttribute("errPrice",errPrice);
			hasError = true;
		}
		if(quantity<0) {
			errQuantity = "Số lượng không được nhỏ hơn 0!";
			model.addAttribute("errQuantity",errQuantity);
			hasError = true;
		}
		if(weight<0) {
			errWeight = "Trọng lượng không được nhỏ hơn 0!";
			model.addAttribute("errWeight",errWeight);
			hasError = true;
		}
		if (loaiSP == null) {
			errType = "Loại sản phẩm không được bỏ trống!";
	        model.addAttribute("errType", errType);
	        hasError = true;
	    }
		if(image.equals("") && !isUpdate) {
			errImg = "Vui lòng chọn hình sản phẩm!";
	        model.addAttribute("errImg", errImg);
	        hasError = true;
		}
		
		return !hasError;
	}
	
	
}
