package com.snackviet.dto.xaphuong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor@AllArgsConstructor
public class XaPhuongRequest {
    private Integer wardCode;
    private String wardName;
    private Integer quanHuyenId;
}
