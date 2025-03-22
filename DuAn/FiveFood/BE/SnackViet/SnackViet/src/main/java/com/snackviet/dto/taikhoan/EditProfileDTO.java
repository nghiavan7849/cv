package com.snackviet.dto.taikhoan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class EditProfileDTO {
    private String tenDangNhap;
    private String hoVaTen;
    private String soDienThoai;
    private String email;
    private Boolean gioiTinh;
    private String hinhAnh;
}
