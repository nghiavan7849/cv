create table DanhGia 
(
    maDanhGia int PRIMARY KEY AUTO_INCREMENT,
    ngayDanhGia date,
    soSao int,
    binhLuan varchar(255),
    trangThai bit,
    maSanPham int,
    maTaiKhoan int,
    maHoaDon int,

    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon)    
)