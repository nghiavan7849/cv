create table DiaChi
(
    maDiaChi int PRIMARY KEY AUTO_INCREMENT,
    soDienThoai varchar(10),
    hoVaTen varchar(255),
    diaChi varchar(255),
    diaChiChiTiet varchar(255),
    trangThai bit,
    maTinhThanh int,
    maQuanHuyen int, 
    maXaPhuong int,
    trangThaiXoa bit,
    maTaiKhoan int,

    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan)
)