package com.snackviet.controller.account;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.CookieService;
import com.snackviet.service.MaHoaMKService;
import com.snackviet.service.MailService;;


@Controller
public class QuenMatKhauController {
	@Autowired 
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	MailService mailService;
	@Autowired
	CookieService cookieService;
	@Autowired
	MaHoaMKService maHoaMKService;
	
	
	boolean checkSendOtp = false;
	String messageEmail = "";
	String messageSuccess = "";
	String messageCode = "";
	String messageNewPass = "";
	String messageConfirmNewPass = "";
	
	@RequestMapping("quen-mat-khau")
	public String quenMatKhau(Model model) {
		checkSendOtp = false;
		model.addAttribute("checkSendOtp", checkSendOtp);
		return "account/quenmatkhau";
	}
	
	
	@PostMapping("quen-mat-khau/send-otp")
	public String senOtp(Model model, @RequestParam("email")String email) {
		boolean checkValid = false;
		String patternEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+(\\.[A-Za-z]{2,}){1,2}$";
		if(email.isEmpty()) {
			messageEmail = "Vui lòng nhập email!!!";
			checkValid = true;
		} else if(!email.matches(patternEmail)) {
			messageEmail = "Email không đúng định dạng!!!";
			checkValid = true;
		} else {
			messageEmail = "";
			checkValid = false;
		}
		
		if(!checkValid) {
			TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email);
			if(taiKhoan  == null) {
				messageEmail = "Email không tồn tại!!!";
			} else {
				int code = codeOtp();
				if(mailService.sendOtp(email,"Forgot Password" , "Your verification code OTP is "+ code)) {
					cookieService.setCookie("OTP", maHoaMKService.maHoaMatKhauMD5(String.valueOf(code)), 1);
					cookieService.setCookie("email", email, 1);
					messageSuccess = "Gửi mã thành công";
					checkSendOtp = true;
				} else {
					messageEmail = "Gửi mã thất bại";
				}
			}
		}
		model.addAttribute("messageSuccess", messageSuccess);
		model.addAttribute("messageEmail", messageEmail);
		model.addAttribute("valueEmail", email);
		model.addAttribute("checkSendOtp", checkSendOtp);
		return "account/quenmatkhau";
	}
	

	@PostMapping("quen-mat-khau/next")
	public String nextForgotPassword(Model model,@RequestParam("OTP")String OTP) {
		boolean checkValid = false;
		String code = "";
		String email = cookieService.getCookie("email").getValue();
		if(OTP.isEmpty()) {
			messageCode = "Vui lòng nhập mã!!!"; 
			checkValid = true;
		} else {
			try {
				int otp = Integer.parseInt(OTP);
				if(otp < 0) {
					messageCode = "Mã phải là số dương!!!"; 
					checkValid = true;
				} else if(OTP.length() != 6) {
					messageCode = "Mã phải là 6 số!!!"; 
					checkValid = true;
				} else  {
					messageCode = ""; 
					checkValid = false;
				}
				
			} catch (NumberFormatException e) {
				// TODO: handle exception
				messageCode = "Mã phải là số!!!"; 
				checkValid = true;
			}
			
		}
		if(!checkValid) {
			code =cookieService.getCookie("OTP").getValue();
			if(maHoaMKService.maHoaMatKhauMD5(OTP).equals(code)) {
				return "redirect:/quen-mat-khau-xac-nhan";
			} else {
				messageCode = "Mã không chính xác!!!";
				checkSendOtp = true;
			}	
		} else {
			checkSendOtp = true;
		}
		model.addAttribute("valueEmail", email);
		model.addAttribute("valueOTP",OTP);
		model.addAttribute("messageCode",messageCode);
		model.addAttribute("checkSendOtp", checkSendOtp);
		return "account/quenmatkhau";
		
	}

	@PostMapping("quen-mat-khau/back")
	public String backForgotPassword() {
		return "redirect:/dang-nhap";
	}
	
	@RequestMapping("quen-mat-khau-xac-nhan")
	public String quenMatKhauXacNhan() {
		return "account/quenmatkhauxn";
	}
	@PostMapping("quen-mat-khau-xac-nhan")
	public String post(Model model,@RequestParam("newPassword")String newPassword,@RequestParam("confirmNewPassword")String confirmNewPassword) {
		boolean checkValid = false;
		String email = cookieService.getCookie("email").getValue();
		if(newPassword.isEmpty()) {
			messageNewPass = "Vui lòng nhập mật khẩu mới!!!";
			checkValid = true;
		} else if (newPassword.length() < 8){ 
			messageNewPass = "Mật khậu phải có tối thiếu 8 ký tự!!!";
			checkValid = true;
		} else {
			messageNewPass = "";
			checkValid = false;
		}
		if(confirmNewPassword.isEmpty()) {
			messageConfirmNewPass = "Vui lòng nhập xác nhận mật khẩu mới!!!";
			checkValid = true;
		} else if(confirmNewPassword.length() < 8) {
			messageConfirmNewPass = "Mật khậu phải có tối thiếu 8 ký tự!!!";
			checkValid = true;
		} else if(!newPassword.equals(confirmNewPassword)) {
			messageConfirmNewPass = "Xác nhận mật khẩu không đúng!!!";
			checkValid = true;
		} else {
			messageConfirmNewPass = "";
			checkValid = false;
		}
		model.addAttribute("messageNewPass", messageNewPass);	
		model.addAttribute("messageConfirmNewPass", messageConfirmNewPass);	
		model.addAttribute("valueNewPass", newPassword);
		model.addAttribute("valueConfirmNewPass", confirmNewPassword);
		if(!checkValid) {
			TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email);
			taiKhoan.setMatKhau(maHoaMKService.maHoaMatKhauMD5(newPassword));
			taiKhoanRepository.saveAndFlush(taiKhoan);
			cookieService.setCookie("OTP", null, 0);
			cookieService.setCookie("email", null, 0);
			model.addAttribute("messageSuccess", "Đổi mật khẩu thành công");
		}
		return "account/quenmatkhauxn";
	}
	
	@GetMapping("quen-mat-khau-xac-nhan/back")
	public String back() {
		cookieService.setCookie("OTP", null, 0);
		cookieService.setCookie("email", null, 0);
		return "redirect:/dang-nhap";
	}
	
	
	public int codeOtp() {
		Random random = new Random();
		return 100000 + random.nextInt(900000);
	}
}
