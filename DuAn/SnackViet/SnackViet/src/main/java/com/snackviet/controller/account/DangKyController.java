package com.snackviet.controller.account;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.eclipse.tags.shaded.org.apache.bcel.generic.NEW;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.snackviet.service.SessionService;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class DangKyController {
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	SessionService sessionService;
	@Autowired
	CookieService cookieService;
	@Autowired
	MaHoaMKService maHoaMKService;

	String messageHoVaTen = "";
	String messageTenDangNhap = "";
	String messageSoDienThoai = "";
	String messageEmail = "";
	String messageMatKhau = "";
	String messageXacNhanMatKhau = "";
	String messageSuccess = "";
	private boolean checkValid = false;

	@RequestMapping("dang-ky")
	public String dangKy(Model model) {
		TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");

		model.addAttribute("taiKhoan", new TaiKhoan());
		model.addAttribute("messageSuccess", messageSuccess);
		messageSuccess = "";
		return "account/dangky";

	}

	@PostMapping("dang-ky/submit")
	public String addDangKy(Model model, @RequestParam("hoVaTen") String hoVaTen,
			@RequestParam("tenDangNhap") String tenDangNhap, @RequestParam("soDienThoai") String soDienThoai,
			@RequestParam("email") String email, @RequestParam("matKhau") String matKhau,
			@RequestParam("xnmatKhau") String xnmatKhau)

	{

		TaiKhoan taiKhoan = new TaiKhoan();

		checkValidDangKyTK(hoVaTen, tenDangNhap, soDienThoai, email, matKhau, xnmatKhau);
		if (checkValid) {
			model.addAttribute("taiKhoan", taiKhoan);
			model.addAttribute("messageHoVaTen", messageHoVaTen);
			model.addAttribute("messageTenDangNhap", messageTenDangNhap);
			model.addAttribute("messageSoDienThoai", messageSoDienThoai);
			model.addAttribute("messageEmail", messageEmail);
			model.addAttribute("messageMatKhau", messageMatKhau);
			model.addAttribute("messageXacNhanMatKhau", messageXacNhanMatKhau);

			model.addAttribute("hoVaTen", hoVaTen);
			model.addAttribute("tenDangNhap", tenDangNhap);
			model.addAttribute("soDienThoai", soDienThoai);
			model.addAttribute("email", email);
			model.addAttribute("matKhau", matKhau);
			model.addAttribute("xnmatKhau", xnmatKhau);

			return "account/dangky";
		} else {
			
			taiKhoan.setHoVaTen(hoVaTen);
			taiKhoan.setTenDangNhap(tenDangNhap);
			taiKhoan.setSoDienThoai(soDienThoai);
			taiKhoan.setEmail(email);
			taiKhoan.setMatKhau(maHoaMKService.maHoaMatKhauMD5(matKhau));
			model.addAttribute("taiKhoan", taiKhoan);
			taiKhoanRepository.saveAndFlush(taiKhoan);
			messageSuccess = "Đăng ký tài khoản thành công";
			return "redirect:/dang-ky";
		}
	}

	public void checkValidDangKyTK(String hoVaTen, String tenDangNhap, String soDienThoai, String email, String matKhau,
			String xnmatKhau) {
		if (hoVaTen.equals("")) {
			checkValid = true;
			messageHoVaTen = "Vui lòng nhập họ và tên!!!";
		} else {
			messageHoVaTen = "";
		}
		if (tenDangNhap.equals("")) {
			checkValid = true;
			messageTenDangNhap = "Vui lòng nhập tên đăng nhập";
		} else {
			messageTenDangNhap = "";
		}

		String patternSDT = "^(0[3-9])\\d{8}$+";
		if (soDienThoai.equals("")) {
			checkValid = true;
			messageSoDienThoai = "Vui lòng nhập số điện thoại!!!";
		} else {
			try {
				long sdt = Long.parseLong(soDienThoai);
				if (sdt < 0) {
					checkValid = true;
					messageSoDienThoai = "Số điện thoại phải là số dương!!!";
				} else if (soDienThoai.length() != 10) {
					checkValid = true;
					messageSoDienThoai = "Số điện thoại phải là 10 số!!!";
				} else if (!soDienThoai.matches(patternSDT)) {
					checkValid = true;
					messageSoDienThoai = "Số điện thoại không đúng định dạng!!!";
				} else {
					messageSoDienThoai = "";

				}
			} catch (NumberFormatException e) {
				// TODO: handle exception
				checkValid = true;
				messageSoDienThoai = "Số điện thoại phải là số!!!";
			}
		}
		String patternEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+(\\.[A-Za-z]{2,}){1,2}$";
		if (email.equals("")) {
			checkValid = true;
			messageEmail = "Vui lòng nhập email!!!";
		} else if (!email.matches(patternEmail)) {
			checkValid = true;
			messageEmail = "Email không đúng định dạng";
		} else {
			messageEmail = "";
			checkValid = false;
		}
		if (matKhau.equals("")) {
			checkValid = true;
			messageMatKhau = "Vui lòng nhập mật khẩu !!!";
		} else if (matKhau.length() < 8) {
			checkValid = true;
			messageMatKhau = "Mật khậu phải có tối thiếu 8 ký tự!!!";
		} else {
			messageMatKhau = "";
			checkValid = false;
		}

		 if  (xnmatKhau.equals("")) {
			checkValid = true;
			messageXacNhanMatKhau = "Vui lòng nhập xác nhận mật khẩu!!!";
		} else if (xnmatKhau.length() < 8) {
			checkValid = true;
			messageXacNhanMatKhau = "Mật khậu phải có tối thiếu 8 ký tự!!!";
		}
		else if (!matKhau.equals(xnmatKhau)) {
			checkValid = true;
			messageXacNhanMatKhau = "Xác nhận mật khẩu không đúng!!!";
		} else {
			messageXacNhanMatKhau = "";

		}
	}

}
