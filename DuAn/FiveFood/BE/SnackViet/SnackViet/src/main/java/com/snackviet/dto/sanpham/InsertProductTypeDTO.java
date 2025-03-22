package com.snackviet.dto.sanpham;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InsertProductTypeDTO {
    String tenLoai;
    boolean trangThai;
}
