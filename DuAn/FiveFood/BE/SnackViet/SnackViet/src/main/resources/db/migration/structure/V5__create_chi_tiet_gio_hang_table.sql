create table ChiTietGioHang
(
    maChiTietGioHang int PRIMARY KEY AUTO_INCREMENT,
    soLuong int,
    maSanPham int,
    maTaiKhoan int,

    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan)
)
