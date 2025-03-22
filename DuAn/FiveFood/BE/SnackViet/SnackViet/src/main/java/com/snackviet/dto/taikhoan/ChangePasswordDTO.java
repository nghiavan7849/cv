package com.snackviet.dto.taikhoan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ChangePasswordDTO {
    private String tenDangNhap;
    private String matKhau; 
    private String changeMatKhau;

}
