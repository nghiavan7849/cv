package com.snackviet.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.CookieService;
import com.snackviet.service.MaHoaMKService;
import com.snackviet.service.SessionService;

@Controller
public class DoiMatKhauController {
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	SessionService sessionService;
	@Autowired
	CookieService cookieService;
	@Autowired
	MaHoaMKService maHoaMKService;

	String messageMatKhauCu = "";
	String messageMatKhauMoi = "";
	String messageXacNhanMatKhauMoi = "";
	String messageSuccess = "";
	

	@RequestMapping("doi-mat-khau")
	public String doiMatKhau(Model model) {
		model.addAttribute("messageSuccess", messageSuccess);
		messageSuccess = "";
		return "account/doimatkhau";
	}

	@PostMapping("doi-mat-khau/submit")
	public String postMethodName(Model model, @RequestParam("matKhauCu") String matKhauCu,
			@RequestParam("matKhauMoi") String matKhauMoi,
			@RequestParam("xacNhanMatKhauMoi") String xacNhanMatKhauMoi) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
		
		boolean checkValid = false;
		if (matKhauCu.equals("")) {
			messageMatKhauCu = "Vui lòng nhập mật khẩu cũ!!!";
			checkValid = true;
		} else if (matKhauCu.length() < 8) {
			messageMatKhauCu = "Mật khậu cũ phải có tối thiếu 8 ký tự!!!";
			checkValid = true;
		} else if (!maHoaMKService.maHoaMatKhauMD5(matKhauCu).equals(taiKhoan.getMatKhau())) {
			messageMatKhauCu = "Mật khẩu cũ không chính xác";
			checkValid = true;
		} else {
			messageMatKhauCu = "";
			checkValid = false;
		}

		if (matKhauMoi.equals("")) {
			messageMatKhauMoi = "Vui lòng nhập mật khẩu mới!!!";
			checkValid = true;
		} else if (matKhauMoi.length() < 8) {
			messageMatKhauMoi = "Mật khậu mới phải có tối thiếu 8 ký tự!!!";
			checkValid = true;
		} else if(maHoaMKService.maHoaMatKhauMD5(matKhauMoi).equals(taiKhoan.getMatKhau())){
			messageMatKhauMoi = "Mật khẩu mới không được trùng với mật khẩu cũ!!!";
			checkValid = true;
		} else {
			messageMatKhauMoi = "";
			checkValid = false;
		}

		if (xacNhanMatKhauMoi.equals("")) {
			messageXacNhanMatKhauMoi = "Vui lòng nhập xác nhận mật khẩu mới!!!";
			checkValid = true;
		} else if (xacNhanMatKhauMoi.length() < 8) {
			messageXacNhanMatKhauMoi = "Xác nhận mật khậu mới phải có tối thiếu 8 ký tự!!!";
			checkValid = true;
		} else if(!xacNhanMatKhauMoi.equals(matKhauMoi)) {
			messageXacNhanMatKhauMoi = "Xác nhận mật khẩu mới không đúng!!!!";
			checkValid = true;
		} else {
			messageXacNhanMatKhauMoi = "";
			checkValid = false;
		}
		if (checkValid) {
			model.addAttribute("messageMatKhauCu", messageMatKhauCu);
			model.addAttribute("messageMatKhauMoi", messageMatKhauMoi);
			model.addAttribute("messageXacNhanMatKhauMoi", messageXacNhanMatKhauMoi);
			
			model.addAttribute("matKhauCu", matKhauCu);
			model.addAttribute("matKhauMoi", matKhauMoi);
			model.addAttribute("xacNhanMatKhauMoi", xacNhanMatKhauMoi);
			return "account/doimatkhau";
		} else {
			taiKhoan.setMatKhau(maHoaMKService.maHoaMatKhauMD5(matKhauMoi));
			sessionService.setAttribute("sessionTaiKhoan", taiKhoan);
			if(cookieService.getCookie("tenDangNhap") != null) {
				int maxAge = cookieService.getCookie("matKhau").getMaxAge();
				cookieService.setCookie("matKhau",maHoaMKService.maHoaMatKhauMD5(matKhauMoi),maxAge);
			}
			messageSuccess = "Đổi mật khẩu thành công";
			taiKhoanRepository.saveAndFlush(taiKhoan);
			return "redirect:/doi-mat-khau";
		}
	}

}
