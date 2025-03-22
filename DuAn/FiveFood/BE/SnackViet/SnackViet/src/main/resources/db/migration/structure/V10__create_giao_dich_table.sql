create table GiaoDich
(
    maGiaoDich int PRIMARY KEY AUTO_INCREMENT,
    codeGiaoDich varchar(255),
    soTaiKhoan varchar(255),
    hoVaTen varchar(255),
    ngayGiaoDich date,
    trangThai bit,
    soTien float,
    maTaiKhoan int,
    maHoaDon int,

    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon)
)