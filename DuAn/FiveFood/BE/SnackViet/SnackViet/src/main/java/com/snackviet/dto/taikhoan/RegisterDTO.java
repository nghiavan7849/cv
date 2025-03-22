package com.snackviet.dto.taikhoan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class RegisterDTO {
    private String tenDangNhap;
    private String email;
    private String hoVaTen;
    private String matKhau; 
    private String soDienThoai;
}
