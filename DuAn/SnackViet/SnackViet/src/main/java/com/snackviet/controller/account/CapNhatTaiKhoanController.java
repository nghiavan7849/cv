package com.snackviet.controller.account;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.SessionService;

import jakarta.servlet.ServletContext;

@Controller
@RequestMapping("cap-nhat-tai-khoan")
public class CapNhatTaiKhoanController {
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	SessionService sessionService;
	
	@Autowired
	ServletContext context;

	String messageHoVaTen = "";
	String messageSoDienThoai = "";
	String messageEmail = "";
	String messageSuccess = "";
	private boolean checkValid = false;
	
	@GetMapping
	public String capNhatTaiKhoa(Model model) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");

		model.addAttribute("taiKhoan", taiKhoan);
		model.addAttribute("messageSuccess", messageSuccess);
		messageSuccess = "";
		return "account/capnhattaikhoan";
	}

	@PostMapping("submit")
	public String submitCapNhatTaiKhoan(Model model, @RequestParam("hoVaTen") String hoVaTen,
			@RequestParam("soDienThoai") String soDienThoai, @RequestParam("gioiTinh") boolean gioiTinh,
			@RequestParam("email") String email, @RequestPart("file") MultipartFile imageFile) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		taiKhoan.setEmail(email);
		taiKhoan.setHoVaTen(hoVaTen);
		taiKhoan.setSoDienThoai(soDienThoai);
		taiKhoan.setGioiTinh(gioiTinh);
		
		String imageName = "";
		if(!imageFile.isEmpty()) {
			String fileName = imageFile.getOriginalFilename();
			String part = context.getRealPath("/image/avatars"+fileName); 
			Path path = Path.of(part);
			if(!Files.exists(path)) {
				try {
					Files.createDirectories(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Files.copy(imageFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				imageName = fileName;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			imageName = taiKhoan.getHinhAnh();
		}
		
		
		checkValidCapNhatTK(hoVaTen, soDienThoai, email);
		if(checkValid) {
			model.addAttribute("taiKhoan", taiKhoan);
			model.addAttribute("messageHoVaTen", messageHoVaTen);
			model.addAttribute("messageSoDienThoai", messageSoDienThoai);
			model.addAttribute("messageEmail", messageEmail);
			return "account/capnhattaikhoan";
		} else {
			taiKhoan.setHinhAnh(imageName);
			model.addAttribute("taiKhoan", taiKhoan);
			taiKhoanRepository.saveAndFlush(taiKhoan);
			messageSuccess = "Cập nhật tài khoản thành công";
			return "redirect:/cap-nhat-tai-khoan";
		}
		
	}
	
	public void checkValidCapNhatTK(String hoVaTen, String soDienThoai, String email) {
		if(hoVaTen.equals("")) {
			messageHoVaTen = "Vui lòng nhập họ và tên!!!";
			checkValid = true;
		}else {
			messageHoVaTen = "";
			checkValid = false;
		}
		
		String patternSDT = "^(0[3-9])\\d{8}$";
		if(soDienThoai.equals("")) {
			messageSoDienThoai = "Vui lòng nhập số điện thoại!!!";
			checkValid = true;
		} else { 
			try {
				long sdt = Long.parseLong(soDienThoai);
				if (sdt < 0) {
					messageSoDienThoai = "Số điện thoại phải là số dương!!!";
					checkValid = true;
				} else if ( soDienThoai.length() != 10) {
					messageSoDienThoai = "Số điện thoại phải là 10 số!!!";
					checkValid = true;
				} else if (!soDienThoai.matches(patternSDT)) { 
					messageSoDienThoai = "Số điện thoại không đúng định dạng!!!";
					checkValid = true;
				} else {
					messageHoVaTen = "";
				}
			} catch (NumberFormatException e) {
				// TODO: handle exception
				messageSoDienThoai = "Số điện thoại phải là số!!!";
				checkValid = true;
			}
		}
	String patternEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+(\\.[A-Za-z]{2,}){1,2}$";
		if(email.equals("")) {
			messageEmail = "Vui lòng nhập email!!!";
			checkValid = true;
		} else if(!email.matches(patternEmail)) {
			messageEmail = "Email không đúng định dạng";
			checkValid = true;
		} else {
			messageEmail = "";
			checkValid = false;
		}
	}
}
