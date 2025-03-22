create table SanPham
(
    maSanPham int PRIMARY KEY AUTO_INCREMENT,
    tenSanPham varchar(255),
    gia float,
    hinhAnh varchar(255),
    moTa varchar(255),
    trongLuong float,
    trangThai bit,
    ngayThem date,
    maLoai int,

    FOREIGN KEY (maLoai) REFERENCES LoaiSP(maLoai)
)