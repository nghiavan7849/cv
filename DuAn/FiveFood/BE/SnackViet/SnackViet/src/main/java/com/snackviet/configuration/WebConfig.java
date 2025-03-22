package com.snackviet.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
public class WebConfig {

    // private static final Long MAX_AGE = 3600L;

    // @Bean
    // public CorsFilter corsFilter() {
    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // CorsConfiguration config = new CorsConfiguration();
    // config.setAllowCredentials(true);
    // // config.addAllowedOriginPattern("http://*.fivefood.shop");
    // config.addAllowedOrigin("http://api.fivefood.shop");
    // config.addAllowedOrigin("https://api.fivefood.shop");
    // config.addAllowedOrigin("http://fivefood.shop");
    // config.addAllowedOrigin("https://fivefood.shop");
    // config.addAllowedOrigin("http://localhost:3000");
    // config.addAllowedOrigin("http://13.115.33.62:8000");
    // config.addAllowedOrigin("http://13.115.33.62:3000");
    // config.setAllowedHeaders(Arrays.asList(
    // HttpHeaders.AUTHORIZATION,
    // HttpHeaders.CONTENT_TYPE,
    // HttpHeaders.ACCEPT,
    // "X-Requested-With"
    // ));

    // config.setAllowedMethods(Arrays.asList(
    // HttpMethod.GET.name(),
    // HttpMethod.POST.name(),
    // HttpMethod.PUT.name(),
    // HttpMethod.DELETE.name(),
    // HttpMethod.OPTIONS.name()
    // ));
    // config.setMaxAge(MAX_AGE);

    // source.registerCorsConfiguration("/**", config);
    // return new CorsFilter(source);
    // }
    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    //     registry.addMapping("/api/**")
    //             .allowedOriginPatterns("http://localhost:3000") // Allow your frontend's origin
    //             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    //             .allowedHeaders("*") // Allow all headers
    //             .allowCredentials(true); // if you need to allow credentials
    // }
}
