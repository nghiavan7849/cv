package com.snackviet.controller.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.eclipse.tags.shaded.org.apache.bcel.generic.NEW;
import org.hibernate.type.TrueFalseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
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

import com.snackviet.groupValidation.DangNhapGroup;
import com.snackviet.groupValidation.FullValidationTaiKhoanGroup;
import com.snackviet.model.SanPham;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.MaHoaMKService;

import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;

@Controller
public class NguoiDungController {
	
	boolean editting = false;
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	MaHoaMKService maHoaMKService;
	@Autowired
	ServletContext app;
	
	//VALIDATION MESSAGES
	String errFullname="", errUsername="", errPass="", errEmail="", errRole="", errStatus="", errPhone="", errImg="";
	
	@RequestMapping("/adminIndex/quan-ly-nguoi-dung")
	public String users(Model model, @RequestParam("pageNo") Optional<Integer> pageNo,
	        @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
	        @RequestParam(name = "search", defaultValue = "") String search,
	        @RequestParam(name = "sort", defaultValue = "") String sort) {
		
		
		//lấy tổng số người dùng
		model.addAttribute("total",taiKhoanRepository.findAll());
		//lấy số người dùng bị khóa
		model.addAttribute("inactive", taiKhoanRepository.findByTrangThai(false));

		model.addAttribute("insert",new TaiKhoan());
		
		editting = false;
		model.addAttribute("editting",editting);
		
		//PHÂN TRANG VÀ TÌM KIẾM
		int currentPage = pageNo.orElse(1);
	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
	    Page<TaiKhoan> userPage = null;
	    

	    if (!search.isEmpty() && !sort.isEmpty()) {
	        // Lọc dữ liệu theo cả search và sort
	        if (sort.equals("Tên tài khoản")) {
	            userPage = taiKhoanRepository.findByTenDangNhapContaining(search, pageable);
	        } else if (sort.equals("Số điện thoại")) {
	            userPage = taiKhoanRepository.findBySoDienThoaiContaining(search, pageable);
	        } 
	        else if (sort.equals("Email")) {
	            userPage = taiKhoanRepository.findByEmailContaining(search, pageable);
	        } 
	        else if (sort.equals("Họ và tên")) {
	            userPage = taiKhoanRepository.findByHoVaTenContaining(search, pageable);
	        } 
	    }
	    else {
	        // Không lọc dữ liệu
	        userPage = taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc(pageable);
	    }
	    
	 // Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về admin/quanLyNguoiDung
	    if (userPage == null ||userPage.isEmpty()) {
	        model.addAttribute("users", Collections.emptyList());
	        model.addAttribute("totalItems", 0);
	        model.addAttribute("totalPages", 0);
	        model.addAttribute("currentPage", 0);
	        model.addAttribute("pageSize", pageSize);
	        model.addAttribute("search", search);
	        model.addAttribute("sort", sort);
	        // Load lại dữ liệu select box đã được chọn
	        model.addAttribute("displaySelected", sort);
	        return "admin/quanLyNguoiDung";
	    }

	    model.addAttribute("users", userPage.getContent());
	    model.addAttribute("totalItems", userPage.getTotalElements());
	    model.addAttribute("totalPages", userPage.getTotalPages());
	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("pageSize", pageSize);
	    model.addAttribute("search", search);
	    model.addAttribute("sort", sort);

	    //Load lại dữ liệu select box đã được chọn
	    model.addAttribute("displaySelected", sort);
	    
	    
	    System.out.println(sort);
		
		return "admin/quanLyNguoiDung";
	}
	
	@RequestMapping("/adminIndex/quan-ly-nguoi-dung/{maTaiKhoan}")
	public String users(Model model,@PathVariable("maTaiKhoan") int maTaiKhoan,
			@RequestParam("pageNo") Optional<Integer> pageNo,
			@RequestParam(name = "search", defaultValue = "") String search,
	        @RequestParam(name = "sort", defaultValue = "") String sort) {
		
		
		//lấy tổng số người dùng
		model.addAttribute("total",taiKhoanRepository.findAll());
		//lấy số người dùng bị khóa
		model.addAttribute("inactive", taiKhoanRepository.findByTrangThai(false));
		
		//Bắt lỗi tìm với id nằm ngoài dữ liệu
		Optional<TaiKhoan> editOptional = taiKhoanRepository.findById(maTaiKhoan);
		if (!editOptional.isPresent()) {
			return "redirect:/adminIndex/quan-ly-nguoi-dung?notFound=true";
		}
		TaiKhoan edit = editOptional.get();
		
		editting = true;
		model.addAttribute("editting",editting);
		
		//PHÂN TRANG SAU KHI EDIT
//		pageNo = (Optional<Integer>) model.getAttribute("pageNo");
		int pageSize = 5;

		if (pageSize == 0) {
			pageSize = 5;
		}

		int currentPage = pageNo.orElse(1);

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
		Page<TaiKhoan> userPage = null;

		//Hiển thị lại bộ lọc
		if (!search.isEmpty() && !sort.isEmpty()) {
	        // Lọc dữ liệu theo cả search và sort
	        if (sort.equals("Tên tài khoản")) {
	            userPage = taiKhoanRepository.findByTenDangNhapContaining(search, pageable);
	        } else if (sort.equals("Số điện thoại")) {
	            userPage = taiKhoanRepository.findBySoDienThoaiContaining(search, pageable);
	        } 
	        else if (sort.equals("Email")) {
	            userPage = taiKhoanRepository.findByEmailContaining(search, pageable);
	        } 
	        else if (sort.equals("Họ và tên")) {
	            userPage = taiKhoanRepository.findByHoVaTenContaining(search, pageable);
	        } 
	    }
		else if(!sort.isEmpty() && search.isEmpty()) {
			if (sort.equals("Tên tài khoản")) {
	            userPage = taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc(pageable);
	        } else if (sort.equals("Số điện thoại")) {
	        	userPage = taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc(pageable);
	        } 
	        else if (sort.equals("Email")) {
	        	userPage = taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc(pageable);
	        } 
	        else if (sort.equals("Họ và tên")) {
	        	userPage = taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc(pageable);
	        } 
		}
	    else {
	        // Không lọc dữ liệu
	        userPage = taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc(pageable);
	    }
		
		// Lọc không tìm thấy dữ liệu thì set lại pageNo = 0 và trả về admin/quanLySanPham
	    if (userPage == null ||userPage.isEmpty()) {
	        model.addAttribute("users", Collections.emptyList());
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
		
		long totalItems = userPage.getTotalElements();
		int totalPages = userPage.getTotalPages();

		model.addAttribute("users", userPage.getContent());
		model.addAttribute("totalItems", totalItems);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("search", search);
		model.addAttribute("sort", sort);
		model.addAttribute("displaySelected", sort);
		

		model.addAttribute("insert",edit);
		model.addAttribute("displayU", edit);
		
		model.addAttribute("idForUpdate",edit.getMaTaiKhoan());
		
		
//		model.addAttribute("users",list);
		
		return "admin/quanLyNguoiDung";
		
	}
	
	
	@PostMapping("/adminIndex/quan-ly-nguoi-dung/insert")
	public String addUsers(Model model,
			@RequestPart("hinhAnh") MultipartFile hinhAnh,
			@RequestParam("hoVaTen") String fullname,
			@RequestParam("tenDangNhap") String username,
			@RequestParam("matKhau") String pass,
			@RequestParam("email") String email,
			@RequestParam("soDienThoai") String phone,
			@RequestParam("gioiTinh") String gender,
			@RequestParam("vaiTro") String role,
			@RequestParam("trangThai") String status) {
		
		//lấy tổng số người dùng
		model.addAttribute("total",taiKhoanRepository.findAll());
		//lấy số người dùng bị khóa
		model.addAttribute("inactive", taiKhoanRepository.findByTrangThai(false));
		
		
		String imageName = "";
		if (!hinhAnh.isEmpty()) {
			try {
				String filename = hinhAnh.getOriginalFilename();
				String realPath = app.getRealPath("/image/avatars/" + filename);
				Path path = Path.of(realPath);
				if (!Files.exists(path.getParent())) {
					Files.createDirectories(path);
				}
				Files.copy(hinhAnh.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				imageName = filename;
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
		}
		
		if(validation(model, fullname, username,pass, email, role, status,gender, phone,"")){
		
			TaiKhoan insert = new TaiKhoan();
			
			//validate cho các dữ liệu có mác unique
			if(taiKhoanRepository.findByTenDangNhap(username)!=null) {
				return "redirect:/adminIndex/quan-ly-nguoi-dung?insert=fail&cause=tenDangNhap";
			}
			else if(taiKhoanRepository.findByEmail(email)!=null) {
				return "redirect:/adminIndex/quan-ly-nguoi-dung?insert=fail&cause=email";
			}
			else if(taiKhoanRepository.findBySoDienThoaiContaining(phone)!=null) {
				return "redirect:/adminIndex/quan-ly-nguoi-dung?insert=fail&cause=soDienThoai";
			}
			
//			String imageName = "";
//			if (!hinhAnh.isEmpty()) {
//				try {
//					String filename = hinhAnh.getOriginalFilename();
//					String realPath = app.getRealPath("/image/avatars/" + filename);
//					Path path = Path.of(realPath);
//					if (!Files.exists(path.getParent())) {
//						Files.createDirectories(path);
//					}
//					Files.copy(hinhAnh.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//					imageName = filename;
//				} catch (IOException e) {
//					e.printStackTrace();
//					return "error";
//				}
//			}
			
			insert.setHoVaTen(fullname);
			insert.setTenDangNhap(username);
			insert.setMatKhau(maHoaMKService.maHoaMatKhauMD5(pass));
			insert.setEmail(email);
			insert.setVaiTro("Admin".equals(role));
			insert.setTrangThai("Hoạt động".equals(status));
			insert.setSoDienThoai(phone);
			insert.setGioiTinh("Nam".equals(gender));
			insert.setHinhAnh(imageName);
			
			taiKhoanRepository.saveAndFlush(insert);
			return "redirect:/adminIndex/quan-ly-nguoi-dung?insert=success";
		
		}
		
		model.addAttribute("users", taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc());
		editting = false;
		model.addAttribute("editting",editting);
		
		//lấy lại dữ liệu người dùng đã nhập và hiển thị
		TaiKhoan reload = new TaiKhoan();
		reload.setHoVaTen(fullname);
		reload.setTenDangNhap(username);
		reload.setMatKhau(maHoaMKService.maHoaMatKhauMD5(pass));
		reload.setEmail(email);
		reload.setVaiTro("Admin".equals(role));
		reload.setTrangThai("Hoạt động".equals(status));
		reload.setSoDienThoai(phone);
		reload.setGioiTinh("Nam".equals(gender));
		reload.setHinhAnh(imageName);
		model.addAttribute("displayU",reload);
		
		
		// Lấy lại danh sách người dùng để hiển thị
        Pageable pageable = PageRequest.of(0, 5); // Chỉnh sửa pageNo và pageSize tùy ý
        Page<TaiKhoan> userPage = taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc(pageable);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("currentPage", 1); // hoặc sử dụng giá trị hiện tại nếu có
        model.addAttribute("pageSize", 5); // Chỉnh sửa pageSize tùy ý
        model.addAttribute("search", ""); // hoặc giá trị search hiện tại
        model.addAttribute("sort", ""); // hoặc giá trị sort hiện tại
        model.addAttribute("displaySelected", ""); // hoặc giá trị sort hiện tại
        
		
		return "admin/quanLyNguoiDung";

		
	}
	
	@PostMapping("/adminIndex/quan-ly-nguoi-dung/update/{maTaiKhoan}")
	public String updateUsers(Model model,
			@RequestPart("hinhAnh") MultipartFile hinhAnh,
			@PathVariable("maTaiKhoan") int maTaikhoan,
			@RequestParam("idForUpdate") int idForUpdate,
			@RequestParam("hoVaTen") String fullname,
			@RequestParam(value = "tenDangNhap", required = false) String username,
			@RequestParam(value = "matKhau", required = false) String pass, //Do khi edit matKhau bị disabled nên set required thành false
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "soDienThoai", required = false) String phone,
			@RequestParam("gioiTinh") String gender,
			@RequestParam("vaiTro") String role,
			@RequestParam("trangThai") String status) {
		
		model.addAttribute("idForUpdate",maTaikhoan);
		//lấy tổng số người dùng
		model.addAttribute("total",taiKhoanRepository.findAll());
		//lấy số người dùng bị khóa
		model.addAttribute("inactive", taiKhoanRepository.findByTrangThai(false));
		
		String imageName = "";
		
		if (!hinhAnh.isEmpty()) {
			try {
				String filename = hinhAnh.getOriginalFilename();
				String realPath = app.getRealPath("/image/avatars/" + filename);
				Path path = Path.of(realPath);
				if (!Files.exists(path.getParent())) {
					Files.createDirectories(path);	
				}
				Files.copy(hinhAnh.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				imageName = filename;
			} catch (IOException e) {
				e.printStackTrace();
				return "error";
			}
		}
		
		if(validation(model, fullname, username,pass, email, role, status, phone,gender,"")){
			
			TaiKhoan update = taiKhoanRepository.findByMaTaiKhoan(maTaikhoan);
			
			
			//validate cho các dữ liệu có mác unique
			if(!taiKhoanRepository.findByTenDangNhap(update.getTenDangNhap()).getTenDangNhap().equals(username) && !update.getTenDangNhap().equals(username)) {
				return "redirect:/adminIndex/quan-ly-nguoi-dung?update=fail&cause=tenDangNhap";
			}
			else if(!taiKhoanRepository.findByEmail(update.getEmail()).getEmail().equals(email) && !update.getEmail().equals(email)) {
				return "redirect:/adminIndex/quan-ly-nguoi-dung?update=fail&cause=email";
			}
			else if(!taiKhoanRepository.findBySoDienThoaiContaining(update.getSoDienThoai()).equals(phone) && !taiKhoanRepository.findBySoDienThoaiContaining(update.getSoDienThoai()).getSoDienThoai().equals(phone)) {
				return "redirect:/adminIndex/quan-ly-nguoi-dung?update=fail&cause=soDienThoai";
			}
			
			
			
			update.setHinhAnh(imageName);
			
			
			update.setHoVaTen(fullname);
			
			if (username != null && username.isEmpty()) {
				update.setTenDangNhap(username);
		    }
			
			if (pass != null && !pass.isEmpty()) {
		        update.setMatKhau(pass);
		    }
			
			
			update.setEmail(email);
			update.setVaiTro("Admin".equals(role));
			update.setTrangThai("Hoạt động".equals(status));
			update.setSoDienThoai(phone);
			update.setGioiTinh("Nam".equals(gender));
			
			taiKhoanRepository.saveAndFlush(update);
			
			return "redirect:/adminIndex/quan-ly-nguoi-dung?update=success";
		
		}
		
		model.addAttribute("users", taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc());
		editting = true;
		model.addAttribute("editting",editting);
		
		
		//lấy lại dữ liệu người dùng đã nhập và hiển thị
		TaiKhoan reload = new TaiKhoan();
		reload.setMaTaiKhoan((Integer) model.getAttribute("idForUpdate"));
		reload.setHoVaTen(fullname);
		reload.setTenDangNhap(username);
		reload.setMatKhau(pass);
		reload.setEmail(email);
		reload.setVaiTro("Admin".equals(role));
		reload.setTrangThai("Hoạt động".equals(status));
		reload.setSoDienThoai(phone);
		reload.setGioiTinh("Nam".equals(gender));
		reload.setHinhAnh(imageName);
		model.addAttribute("displayU",reload);
		
		// Lấy lại danh sách người dùng để hiển thị
        Pageable pageable = PageRequest.of(0, 5); // Chỉnh sửa pageNo và pageSize tùy ý
        Page<TaiKhoan> userPage = taiKhoanRepository.findAllByOrderByMaTaiKhoanDesc(pageable);
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("currentPage", 1); // hoặc sử dụng giá trị hiện tại nếu có
        model.addAttribute("pageSize", 5); // Chỉnh sửa pageSize tùy ý
        model.addAttribute("search", ""); // hoặc giá trị search hiện tại
        model.addAttribute("sort", ""); // hoặc giá trị sort hiện tại
        model.addAttribute("displaySelected", ""); // hoặc giá trị sort hiện tại
        
//        TaiKhoan reload = taiKhoanRepository.findById( (int) model.getAttribute("idForUpdate")).get();
//        
//        //đổ lại dữ liệu được chọn lên form khi validate
//        model.addAttribute("displayU",reload);
		
		return "admin/quanLyNguoiDung";
	}
	
	
	@RequestMapping("/adminIndex/quan-ly-nguoi-dung/cancel")
	public String cancel(Model model) {
		
		return "redirect:/adminIndex/quan-ly-nguoi-dung";
	}
	
	
	public boolean validation(Model model,String fullname,String username,String password,String email,String role,String status,String phone,String gender, String image) {
		
		boolean hasError = false;
		String patternSDT = "^(0[3-9])\\d{8}$+";
		
		if(fullname.equals("")) {
			errFullname = "Vui lòng nhập họ và tên!";
			model.addAttribute("errFullname",errFullname);
			hasError = true;
		}
		if(username.equals("")) {
			errUsername = "Vui lòng nhập tên đăng nhập!";
			model.addAttribute("errUsername",errUsername);
			hasError = true;
		}
		if(password.equals("")) {
			errPass = "Vui lòng nhập mật khẩu!";
			model.addAttribute("errPass",errPass);
			hasError = true;
		}
		else if(password.length()<8) {
			errPass = "Mật khẩu phải từ 8 ký tự trở lên!";
			model.addAttribute("errPass",errPass);
			hasError = true;
		}
		if(email.equals("")) {
			errEmail = "Vui lòng nhập email!";
			model.addAttribute("errEmail",errEmail);
			hasError = true;
		}
		if(phone.equals("")) {
			errPhone = "Vui lòng nhập số điện thoại!";
			model.addAttribute("errPhone",errPhone);
			hasError = true;
		}
		else if(phone.length()<10) {
			errPhone = "Số điện thoại phải có 10 số!";
			model.addAttribute("errPhone",errPhone);
			hasError = true;
		}
		else if(!phone.matches(patternSDT)) {
			errPhone = "Số điện thoại không đúng định dạng!";
			model.addAttribute("errPhone",errPhone);
			hasError = true;
		}
//		if(image.equals("")) {
//			errImg = "Vui lòng chọn ảnh đại diện!";
//			model.addAttribute("errImg",errImg);
//			hasError = true;
//		}
		
		return !hasError;
	}
	
	
}
