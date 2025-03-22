package com.snackviet.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class PasswordEncoderService {
    private PasswordEncoder passwordEncoder;

    public PasswordEncoderService(){
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // Kiểm tra mật khẩu khi đăng nhập
    public boolean matches(String mkChuaMaHoa, String mkDaMaHoa) {
        return passwordEncoder.matches(mkChuaMaHoa, mkDaMaHoa);
    }
}
