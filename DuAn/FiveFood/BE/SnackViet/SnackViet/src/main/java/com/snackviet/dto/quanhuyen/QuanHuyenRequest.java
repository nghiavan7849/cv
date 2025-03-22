package com.snackviet.dto.quanhuyen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor@AllArgsConstructor
public class QuanHuyenRequest {
    private Integer districtID;
    private String districtName;
    private Integer tinhThanhId;
}
