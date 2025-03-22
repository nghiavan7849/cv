create table HoaDon
(
    maHoaDon int PRIMARY KEY AUTO_INCREMENT,
    phiVanChuyen float,
    tongTien float,
    ngayThanhToan date,
    phuongThucThanhToan bit,
    trangThai bit,
    ghiChu varchar(255),
    lyDoHuy varchar(255),
    diaChiNhan varchar(255),
    dcHoTen varchar(255),
    dcSoDienThoai varchar(255),
    maTaiKhoan int,
    maTrangThaiHoaDon int,

    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan),
    FOREIGN KEY (maTrangThaiHoaDon) REFERENCES TrangThaiHoaDon(maTrangThaiHoaDon)
)