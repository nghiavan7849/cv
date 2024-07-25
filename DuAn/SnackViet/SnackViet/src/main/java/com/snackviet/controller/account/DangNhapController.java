package com.snackviet.controller.account;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.snackviet.groupValidation.DangNhapGroup;
import com.snackviet.groupValidation.FullValidationTaiKhoanGroup;
import com.snackviet.model.TaiKhoan;
import com.snackviet.repository.TaiKhoanRepository;
import com.snackviet.service.CookieService;
import com.snackviet.service.MaHoaMKService;
import com.snackviet.service.SessionService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class DangNhapController {
	@Autowired
	TaiKhoanRepository taiKhoanRepository;
	@Autowired
	MaHoaMKService maHoaMKService;
	@Autowired
	CookieService cookieService;
	@Autowired
	SessionService sessionService;

	@RequestMapping("dang-nhap")
	public String dangNhap(Model model) {
		String tenDangNhap = null;
		String matKhau = null;
		if(cookieService.getCookie("tenDangNhap") != null) {
			tenDangNhap = cookieService.getCookie("tenDangNhap").getValue();
			matKhau = cookieService.getCookie("matKhau").getValue();
		}
		TaiKhoan taiKhoan = new TaiKhoan();
		if (tenDangNhap != null && matKhau != null) {
			taiKhoan.setTenDangNhap(tenDangNhap);
			taiKhoan.setMatKhau(matKhau);
			model.addAttribute("remember", true);
		}
		model.addAttribute("TaiKhoan", taiKhoan);
		return "account/dangnhap";
	}

	@PostMapping("dang-nhap/submit")
	public String submitDangNhap(Model model,
			@Validated(DangNhapGroup.class) @ModelAttribute("TaiKhoan") TaiKhoan taiKhoan, BindingResult result,
			@RequestParam(name = "remember", defaultValue = "false") boolean remember) {
		boolean checkValid = false;
		if (result.hasErrors()) {
			if (result.hasFieldErrors("matKhau")) {
				checkValid = true;
			} else {
				checkValid = false;
			}
			model.addAttribute("checkValid", checkValid);
			return "account/dangnhap";
		}

		TaiKhoan tk = taiKhoanRepository.findByTenDangNhap(taiKhoan.getTenDangNhap());
		if (tk == null) {
			model.addAttribute("TaiKhoan", taiKhoan);
			result.addError(new FieldError("TaiKhoan", "tenDangNhap", "Tài khoản hoặc mật khẩu không đúng!!!"));
			checkValid = false;
			model.addAttribute("checkValid", checkValid);
			if(remember) {
				model.addAttribute("remember", true);
			}
			System.out.println("hi");
			return "account/dangnhap"; 
		} else if(!tk.isTrangThai()){
			model.addAttribute("TaiKhoan", taiKhoan);
			result.addError(new FieldError("TaiKhoan", "tenDangNhap", "Tài khoản của bạn đã bị khóa!!!"));
			checkValid = false;
			model.addAttribute("checkValid", checkValid);
			if(remember) {
				model.addAttribute("remember", true);
			}
			System.out.println("hi");
			return "account/dangnhap";
		} else {
			String matKhauDaMaHoa = maHoaMKService.maHoaMatKhauMD5(taiKhoan.getMatKhau());
			if(matKhauDaMaHoa.equals(tk.getMatKhau()) || taiKhoan.getMatKhau().equals(tk.getMatKhau())) {
				if (remember) {
					if(taiKhoan.getMatKhau().equals(tk.getMatKhau())) {
						cookieService.setCookie("matKhau", tk.getMatKhau(), 3);
					} else {
						cookieService.setCookie("matKhau", matKhauDaMaHoa, 3);
					}
					cookieService.setCookie("tenDangNhap", tk.getTenDangNhap(), 3);
				} else {
					cookieService.setCookie("tenDangNhap", null, 0);
					cookieService.setCookie("matKhau", null, 0);
				}
				sessionService.setAttribute("sessionTaiKhoan", tk);
				return "redirect:/home-index";
			} else {
				model.addAttribute("TaiKhoan", taiKhoan);
				result.addError(new FieldError("TaiKhoan", "tenDangNhap", "Tài khoản hoặc mật khẩu không đúng!!!"));
				checkValid = false;
				model.addAttribute("checkValid", checkValid);
				if(remember) {
					model.addAttribute("remember", true);
				}
			
				return "account/dangnhap";
			}
		}
	}

}
