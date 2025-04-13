package com.babystore.controller.client;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.babystore.jparepository.AccountRepository;
import com.babystore.model.Account;


@Controller
public class LoginController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/login")
    public String showLoginForm() {
        return "/clients/account/login";
    }

    @PostMapping("/login")
    public ModelAndView handleLogin(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password,
                                    @RequestParam(name = "rememberMe", required = false) String rememberMe,
                                    HttpSession session,
                                    HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();

        Account account = accountRepository.findByUserName(userName);

        if (account != null && account.getPassword().equals(password)) {
            if (account.getRole()) { // Assuming true for admin and false for user
                modelAndView.setViewName("redirect:/admin/home-page-1");
            } else {
             
                modelAndView.setViewName("redirect:/user/home-page");
            }

            // Save the account object in the session
            session.setAttribute("account", account);

            // If the user selected "Remember Me"
            if (rememberMe != null && rememberMe.equals("on")) {
                // Create and set cookies
                Cookie usernameCookie = new Cookie("userName", userName);
                Cookie passwordCookie = new Cookie("password", password);

                // Set the lifespan of the cookies (7 days)
                usernameCookie.setMaxAge(7 * 24 * 60 * 60);
                passwordCookie.setMaxAge(7 * 24 * 60 * 60);

                // Set the path for the cookies
                usernameCookie.setPath("/");
                passwordCookie.setPath("/");

                // Add cookies to the response
                response.addCookie(usernameCookie);
                response.addCookie(passwordCookie);
            }
        } else {
            modelAndView.setViewName("accounts/login");
            modelAndView.addObject("error", "Tên đăng nhập hoặc mật khẩu không chính xác");
        }

        return modelAndView;
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, HttpServletResponse response) {
        // Remove user-related session attributes
        session.removeAttribute("account");

        // Clear cookies related to authentication
        Cookie usernameCookie = new Cookie("username", null);
        Cookie passwordCookie = new Cookie("password", null);
        usernameCookie.setMaxAge(0);
        passwordCookie.setMaxAge(0);
        usernameCookie.setPath("/");
        passwordCookie.setPath("/");
        response.addCookie(usernameCookie);
        response.addCookie(passwordCookie);

        // Redirect to login page after logout
        return "redirect:/login";
    }
}
