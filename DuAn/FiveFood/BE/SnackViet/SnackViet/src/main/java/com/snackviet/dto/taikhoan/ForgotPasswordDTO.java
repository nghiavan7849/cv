package com.snackviet.dto.taikhoan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class ForgotPasswordDTO {
    private String email;
    private String matKhau;
    private Integer maXacNhan;
}
