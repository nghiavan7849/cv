package com.babystore.controller.client;

import com.babystore.jparepository.AccountRepository;
import com.babystore.model.Account;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import java.util.regex.Pattern;

@Controller
public class RegisterController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/register")
    public String showRegisterForm(@ModelAttribute("account") Account account) {
        return "/clients/account/register";
    }

    @PostMapping("/register")
    public ModelAndView handleRegister(@Valid @ModelAttribute("account") Account account, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("/clients/account/register");

        // Check for binding errors (validation errors)
        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        // Validate full name format (letters, spaces, and certain special characters)
        String fullName = account.getFullName();
        if (!isValidFullName(fullName)) {
            bindingResult.rejectValue("fullName", "fullName.invalid", "Họ và tên chỉ được chứa chữ cái, dấu cách và ký tự đặc biệt (' . -)");
            return modelAndView;
        }

        // Validate password complexity
        String password = account.getPassword();
        if (!isValidPassword(password)) {
            modelAndView.addObject("passwordError", "Mật khẩu phải có ít nhất 5 ký tự, bao gồm chữ cái viết hoa, chữ cái viết thường, số và ký tự đặc biệt.");
            return modelAndView;
        }

        // Check if username is already taken
        Account existingUser = accountRepository.findByUserName(account.getUserName());
        if (existingUser != null) {
            modelAndView.addObject("userNameError", "Tên đăng nhập đã tồn tại");
            return modelAndView;
        }

        // Check if email is already registered
        existingUser = accountRepository.findByEmail(account.getEmail());
        if (existingUser != null) {
            modelAndView.addObject("emailError", "Địa chỉ email đã được sử dụng");
            return modelAndView;
        }

        // Save the user to the database
        account.setRole(false); // Assuming role is set to false by default
        account.setDelete(false); // Assuming isDelete is set to false by default
        accountRepository.save(account);

        // Set success message
        modelAndView.addObject("successMessage", "Đăng ký thành công. Vui lòng đăng nhập.");

        return modelAndView;
    }

    // Helper method to validate password complexity
    private boolean isValidPassword(String password) {
        // Password must be at least 5 characters, containing uppercase, lowercase, digit, and special character
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{5,}$";
        return Pattern.compile(passwordRegex).matcher(password).matches();
    }

    // Helper method to validate full name
    private boolean isValidFullName(String fullName) {
        // Full name should only contain letters, spaces, and certain special characters
        String fullNameRegex = "^[\\p{L} \\.'\\-]+$";
        return Pattern.compile(fullNameRegex).matcher(fullName).matches();
    }
}
