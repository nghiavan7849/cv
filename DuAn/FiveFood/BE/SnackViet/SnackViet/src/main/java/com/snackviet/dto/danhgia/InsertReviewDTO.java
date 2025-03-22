package com.snackviet.dto.danhgia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsertReviewDTO {
    String noiDungPhanHoi;
    int maDanhGia;
    int maTaiKhoan;
    int maPhanHoiDanhGia;
}
