package com.snackviet.dto.danhgia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Getter @Setter
public class UpdateHinhAnhDanhGiaDTO {
    private Integer maHinhAnhDG;
    private Integer maDanhGia;
    private String tenHinhAnh;
}
