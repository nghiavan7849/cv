package com.snackviet.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.snackviet.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements UserDetailsService {
    @Autowired
    JWTAuthFilter jwtAuthFilter;

    @Bean
    // authentication
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new UserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(corsConfigurationSource -> corsConfigurationSource.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-resources/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/image/**", "/css/**","/js/**").permitAll()
                        // Api Tài khoản
                        .requestMatchers(HttpMethod.GET, "/api/tai-khoan/list", "/api/get-tai-khoan").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/tai-khoan/create").permitAll()
                        .requestMatchers(HttpMethod.POST, "api/tai-khoan/auth/google","/api/tai-khoan/forgot-password/send-otp", "/api/tai-khoan/forgot-password/change").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/tai-khoan/edit-profile", "/api/tai-khoan/change-password").hasAnyRole("ADMIN", "USER")
                        // Api Chi Tiet Giỏ Hàng
                        .requestMatchers(HttpMethod.GET, "/api/chi-tiet-gio-hang/list").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/chi-tiet-gio-hang/list-by-tk/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/chi-tiet-gio-hang/create").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/chi-tiet-gio-hang/update").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/chi-tiet-gio-hang/delete/**").hasAnyRole("ADMIN", "USER")
                        // Api Chi Tiết Hóa Đơn
                        .requestMatchers(HttpMethod.GET, "/api/chi-tiet-hoa-don/list").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/chi-tiet-hoa-don/list-by-mahd").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/chi-tiet-hoa-don/create").hasAnyRole("ADMIN", "USER")
                        // Api Đánh Giá
                        .requestMatchers(HttpMethod.GET, "/api/danh-gia/list","/api/danh-gia/check").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/danh-gia/check-list").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/danh-gia/get-one","/api/danh-gia/page/**",
                            "/api/danh-gia/list-page","/api/danh-gia/page","/api/hinh-anh-danh-gia/page/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/danh-gia/create","/api/hinh-anh-danh-gia/create").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/danh-gia/update","/api/hinh-anh-danh-gia/update").hasAnyRole("ADMIN", "USER")
                        // Api Địa Chỉ
                        .requestMatchers(HttpMethod.GET, "/api/dia-chi/list","/api/dia-chi/**","/api/dia-chi/list-by-matk","/api/dia-chi/page/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/dia-chi/create").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/dia-chi/update").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/dia-chi/delete/**").hasAnyRole("ADMIN", "USER")
                        // Api Giao Dịch
                        .requestMatchers(HttpMethod.GET, "/api/giao-dich/list","/api/giao-dich/page").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "api/giao-dich/create").hasAnyRole("ADMIN", "USER")
                        // Api Hóa Đơn
                        .requestMatchers(HttpMethod.GET, "/api/hoa-don/list", "/api/hoa-don/page/**", "/api/hoa-don/get-by-ma-hoa-don/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/hoa-don/create").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/hoa-don/cap-nhat-trang-thai","/api/hoa-don/update-status-payment/**").hasAnyRole("ADMIN", "USER")
                        // Api Lịch Sử
                        .requestMatchers(HttpMethod.GET, "/api/lich-su-mua-hang", "/api/lich-su-mua-hang/chi-tiet").hasAnyRole("ADMIN", "USER")
                        // Api Loại SP
                        .requestMatchers(HttpMethod.GET, "/api/loai-san-pham/list", "/api/loai-san-pham/page").permitAll()
                        // Api Sản Phảm
                        .requestMatchers(HttpMethod.GET, "/api/san-pham/list", "/api/san-pham/page","/api/san-pham/page/**",
                            "/api/san-pham/get-one","/api/san-pham/get-list-by-bo-loc","/api/san-pham/get-list-sap-xep","/api/hinh-anh-san-pham").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/san-pham/tru-so-luong").hasAnyRole("ADMIN", "USER")
                        // Api VNPay
                        .requestMatchers(HttpMethod.POST, "/api/vnpay/create-payment", "/api/vnpay/save-session-data","/api/vnpay/save-order").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET,"/api/vnpay/get-ma-hd").hasAnyRole("ADMIN","USER")
                        // Api Liên Hệ
                        .requestMatchers(HttpMethod.GET,"/api/lien-he").permitAll()
                        // Api PayOs
                        .requestMatchers(HttpMethod.POST, "/api/payos/create-payment-link").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/payos/confirm-webhook").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/payos/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/payos/**").hasAnyRole("ADMIN", "USER")
                        //Api Image Classification
                        .requestMatchers(HttpMethod.POST, "/api/predict/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/predict/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/predict/**").hasAnyRole("ADMIN", "USER")
                        //Api Chat Bot
                        .requestMatchers(HttpMethod.POST, "/api/chat-bot/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/chat-bot/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/chat-bot/**").permitAll()
                        // Api Admin
                        .requestMatchers(HttpMethod.GET,"/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/admin/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/admin/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.POST,"/api/admin/quan-ly-nguoi-dung/create").hasAnyRole("ADMIN")
                        .requestMatchers("/ws/**").permitAll()

                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService(passwordEncoder()));
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadUserByUsername'");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://fivefood.shop", "https://api.fivefood.shop" ,"http://52.195.194.82:3000","http://103.72.98.61:3000")); // Địa chỉ frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Các phương thức cho phép
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Các header cho phép
        configuration.setAllowCredentials(true); // Cho phép cookie (nếu cần)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cho tất cả các đường dẫn
        return source;
    }
}
