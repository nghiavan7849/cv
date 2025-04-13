package com.babystore.controller.client;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.babystore.jparepository.AccountRepository;
import com.babystore.model.Account;
import com.babystore.service.SessionService;

import jakarta.mail.internet.MimeMessage;

@Controller
@RequestMapping("forget")
public class QuenMKController {
	
	@Autowired
	JavaMailSender sender;
	@Autowired
	AccountRepository accountrepository;
	@Autowired
	SessionService sessionservice;


	@GetMapping("/change")
	public String quenmk() {
		return "clients/account/forgetpassword";

	}
	@PostMapping("/checkMail")
	public String checkMail(Model model, @RequestParam("email") String email
			) { 
		Account account = accountrepository.findByEmail(email);
		MimeMessage mimeMessage = sender.createMimeMessage();
		if (account != null && account.getEmail().equals(email)) {
			Random random = new Random();
			int otp = random.nextInt(900000) + 100000;
			String fromEmail = "lyhttpc05633@fpt.edu.vn";
			String subject = "Mã OTP để xác minh tài khoản";
			String content = "Mã xác minh tài khoản của bạn là: ";
			try {
				
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
				mimeMessageHelper.setFrom(fromEmail);
				mimeMessageHelper.setTo(email);
				mimeMessageHelper.setSubject(subject);
				mimeMessageHelper.setText(content + otp);
				sender.send(mimeMessage);
				sessionservice.setAttribute("otp", otp);
				sessionservice.setAttribute("account", account);
				model.addAttribute("message", "Gửi mã thành công! Vui lòng kiểm tra email!");
				return "clients/account/OTP";
			} catch (Exception e) {
				model.addAttribute("message", "Gửi mã OTP thất bại! Xin thử lại.");
				return "clients/account/forgetpassword";
			}
		} else {
			model.addAttribute("message", "Email không tồn tại");
			return "clients/account/forgetpassword";
		}
	}
	
	@PostMapping("/OTP") 
	public String checkOTP(@RequestParam("otp") int otp, Model model) {
		Integer savedOTP = (Integer) sessionservice.getAttribute("otp");
		if (savedOTP != null && savedOTP.equals(otp)) {
			model.addAttribute("message", "Mã OTP hợp lệ. Bạn có thể đặt lại mật khẩu.");
			return "clients/account/changepassword";
		} else {
			// Mã OTP không hợp lệ
			model.addAttribute("message", "Mã OTP không hợp lệ!");
			return "clients/account/OTP";
		}
	}
	@PostMapping("/changepassword")
	public String resetPassword(Model model, @RequestParam("newPass") String newPass,
			@RequestParam("confirmPass") String confirmPass, @RequestParam("id") Integer id) {
		if (!newPass.equals(confirmPass)) {
			model.addAttribute("message", "Mật khẩu mới và xác nhận mật khẩu không khớp.");
			return "clients/account/changepassword";
		}
		Account account = accountrepository.findById(id).orElse(null);
		if (account == null) {
			model.addAttribute("message", "Không tìm thấy tài khoản người dùng.");
			return "clients/account/changepassword";
		}
		account.setPassword(newPass);
		accountrepository.save(account);
		sessionservice.removeAttribute("otp");
		model.addAttribute("message", "Đổi mật khẩu thành công.");
		return "clients/account/changepassword";
	}
}
