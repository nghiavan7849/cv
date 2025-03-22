package com.snackviet.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.snackviet.component.AdminInterceptor;
import com.snackviet.component.UserInterceptor;

@Configuration
public class Interceptor implements WebMvcConfigurer {
	// @Autowired
	// AdminInterceptor adminInterceptor;
	// @Autowired
	// UserInterceptor userInterceptor;
	
	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	// 	// TODO Auto-generated method stub
	// 	registry.addInterceptor(adminInterceptor).addPathPatterns("/adminIndex/**");
	// 	registry.addInterceptor(userInterceptor).addPathPatterns("/doi-mat-khau/**","/dia-chi/**","/cap-nhat-tai-khoan/**","/lich-su-mua-hang/**","/gio-hang/**");
	// }
}
