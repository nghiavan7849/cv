create table ChiTietHoaDon
(
    maChiTietHoaDon int PRIMARY KEY AUTO_INCREMENT,
    soLuong int,
    gia float,
    maSanPham int,
    maHoaDon int,

    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon)
)