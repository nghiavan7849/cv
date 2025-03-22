package com.snackviet.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.snackviet.model.TaiKhoan;
import com.snackviet.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor {
	// @Autowired
	// SessionService sessionService;
	
	// @Override
	// public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	// 		throws Exception {
	// 	TaiKhoan taiKhoan = sessionService.getAttribute("sessionTaiKhoan");
	// 	if(taiKhoan == null) {
	// 		response.sendRedirect("/home-index");
	// 		return false;
	// 	} else if(!taiKhoan.isVaiTro()) {
	// 		response.sendRedirect("/home-index");
	// 		return false;
	// 	}
	// 	return true;
	// }
}
