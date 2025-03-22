package com.snackviet.dto.taikhoan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class InsertUserDTO {
    private String hoVaTen;
    private String tenDangNhap;
    private String matKhau;
    private String email;
    private boolean vaiTro;
    private boolean trangThai;
    private String soDienThoai;
    private boolean gioiTinh;
    private String hinhAnh;
}
