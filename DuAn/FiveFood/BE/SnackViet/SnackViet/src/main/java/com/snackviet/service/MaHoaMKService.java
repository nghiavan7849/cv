package com.snackviet.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class MaHoaMKService {
    public static String maHoaMatKhauMD5(String password) {
        // try {
        // // Tạo đối tượng MessageDigest với thuật toán MD5
        // MessageDigest md = MessageDigest.getInstance("MD5");

        // // Cập nhật message digest với byte của mật khẩu
        // md.update(password.getBytes());

        // // Lấy bản băm (digest) từ message digest
        // byte[] byteData = md.digest();

        // // Chuyển đổi byte thành định dạng hex
        // StringBuilder hexString = new StringBuilder();
        // for (byte b : byteData) {
        // hexString.append(String.format("%02x", b));
        // }

        // return hexString.toString();
        // } catch (NoSuchAlgorithmException e) {
        // e.printStackTrace();
        // return null;
        // }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}
