package com.babystore.controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.babystore.jparepository.AccountRepository;
import com.babystore.model.Account;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@Controller
public class UserController {
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ServletContext context;
	@Autowired
	HttpServletRequest req;
	List<Account> listAccount;
	String errorUsername;
	String errorPass;
	String errorEmail;
	String errorFullname;

	@RequestMapping({ "/admin/user", "/admin/user/delete" })
	public String user(Model model) {
		String uri = req.getRequestURI();
		if (uri.contains("/admin/user/delete")) {
			Integer id = Integer.parseInt(req.getParameter("id"));
			Optional<Account> entity = accountRepository.findById(id);
			accountRepository.delete(entity.get());
			return "redirect:/admin/user";
		}
		listAccount = accountRepository.findAll();
		model.addAttribute("user", new Account());
		model.addAttribute("listAccount", listAccount);
		return "admins/management/user";
	}

	@GetMapping("admin/user/update")
	public String getUpdate(Model model, @RequestParam("id") Integer id) {
		Optional<Account> entity = accountRepository.findById(id);
		listAccount = accountRepository.findAll();
		model.addAttribute("user", entity.get());
		model.addAttribute("listAccount", listAccount);
		return "admins/management/user";
	}

	@PostMapping({ "/admin/user", "/admin/user/update" })
	public String getInsert(Model model, @RequestPart("photo") MultipartFile photo, HttpServletRequest req) {
		String uri = req.getRequestURI(); // Get the full request URI
		Account entity = new Account();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String fullname = req.getParameter("fullname");
		entity.setUserName(username);
		entity.setPassword(password);
		entity.setEmail(email);
		entity.setFullName(fullname);
		entity.setRole(Boolean.parseBoolean(req.getParameter("role")));
		entity.setDelete(false);

		if (uri.contains("update")) {
			Integer id = Integer.parseInt(req.getParameter("id"));
			Optional<Account> entityOld = accountRepository.findById(id);
			entity.setId(id);
			if (photo == null || photo.isEmpty()) {
				entity.setAvata(entityOld.get().getAvata());
			} else {
				entity.setAvata(UpdateImg(photo));
			}
			if (Check(username, password, email, fullname)) {
				if (CountUserName(req.getParameter("username"), id) > 0) {
					
					model.addAttribute("user", entity);
					model.addAttribute("listAccount", listAccount);
					model.addAttribute("errorUsername", "Username đã tồn tại");
					return "admins/management/user";
				}
				if (CountEmail(req.getParameter("email"), id) > 0) {
					model.addAttribute("user", entity);
					model.addAttribute("listAccount", listAccount);
					model.addAttribute("errorEmail", "Email đã tồn tại");
					return "admins/management/user";
				}
				accountRepository.saveAndFlush(entity);
				return "redirect:/admin/user";
			} else {
				Check(username, password, email, fullname); // This seems redundant, consider removing
				listAccount = accountRepository.findAll();
				model.addAttribute("user", entity);
				model.addAttribute("listAccount", listAccount);
				return "admins/management/user";
			}
		} else {
			entity.setAvata(UpdateImg(photo));
			if (CountUserName(username, listAccount.size() + 1) > 0) {
				model.addAttribute("user", entity);
				model.addAttribute("listAccount", listAccount);
				model.addAttribute("errorUsername", "Username đã tồn tại");
				return "admins/management/user";
			}
			if (CountEmail(email, listAccount.size() + 1) > 0) {
				model.addAttribute("user", entity);
				model.addAttribute("listAccount", listAccount);
				model.addAttribute("errorEmail", "Email đã tồn tại");
				return "admins/management/user";
			}
			if (Check(username, password, email, fullname)) {
				accountRepository.saveAndFlush(entity);
				return "redirect:/admin/user";
			} else {
				Check(username, password, email, fullname); // This seems redundant, consider removing
				listAccount = accountRepository.findAll();
				model.addAttribute("user", entity);
				model.addAttribute("listAccount", listAccount);
				return "admins/management/user";
			}
		}
	}

	public Integer CountEmail(String email, Integer id) {
		int count = 0;
		listAccount = accountRepository.findAll();
		for (Account account : listAccount) {
			if (account.getEmail().equals(email) && !account.getId().equals(id)) {
				count++;
			}
		}
		return count;
	}

	public Integer CountUserName(String username, Integer id) {
		int count = 0;
		listAccount = accountRepository.findAll();
		for (Account account : listAccount) {
			if (account.getUserName().equals(username) && !account.getId().equals(id)) {
				count++;
			}
		}
		return count;
	}

	public boolean Check(String username, String password, String email, String fullname) {
		if (username.isBlank()) {
			errorUsername = "Vui lòng nhập username";
			req.setAttribute("errorUsername", errorUsername);
			return false;
		}
		if (password.isBlank()) {
			errorPass = "Vui lòng nhập password";
			req.setAttribute("errorPass", errorPass);
			return false;
		} else {
			if (password.length() < 8) {
				errorPass = "Password ít nhất là 8 kí tự";
				req.setAttribute("errorPass", errorPass);
				return false;
			}
		}
		if (email.isBlank()) {
			errorEmail = "Vui lòng nhập email";
			req.setAttribute("errorEmail", errorEmail);
			return false;
		}
		if (fullname.isBlank()) {
			errorFullname = "Vui lòng nhập fullname";
			req.setAttribute("errorFullname", errorFullname);
			return false;
		}
		return true;
	}

	public String UpdateImg(MultipartFile photo) {
		if (!photo.isEmpty()) {
			String fileName = photo.getOriginalFilename();
			String realPath = context.getRealPath("/template/clients/asset/images/" + fileName);
			Path path = Path.of(realPath);

			// kiểm tra đường dẫn có tồn tại không
			if (!Files.exists(path)) {
				try {
					Files.createDirectories(path);
				} catch (IOException e) {
					System.out.println("1");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// upload file chọn lên thư mục img
			File file = new File(context.getRealPath("/template/clients/asset/images/" + fileName));
			try {
				photo.transferTo(file);
				return fileName;
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
