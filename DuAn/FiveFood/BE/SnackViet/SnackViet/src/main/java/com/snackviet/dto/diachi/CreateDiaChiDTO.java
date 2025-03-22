package com.snackviet.dto.diachi;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
public class CreateDiaChiDTO {
    private String hoVaTen;
    private String soDienThoai;
    private boolean trangThai;
    private String diaChi;
    private String diaChiChiTiet;
    private int maTinhThanh;
	private int maQuanHuyen;
	private int maXaPhuong;
    private Integer maTaiKhoan;
}
