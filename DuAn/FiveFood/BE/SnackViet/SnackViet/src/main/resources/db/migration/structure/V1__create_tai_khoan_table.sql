create table TaiKhoan
(
    maTaiKhoan int PRIMARY KEY AUTO_INCREMENT,
    tenDangNhap varchar(30),
    matKhau varchar(255),
    soDienThoai varchar(10) UNIQUE,
    email varchar(255) UNIQUE,
    hoVaTen varchar(255),
    gioiTinh bit,
    hinhAnh varchar(255),
    trangThai bit,
    vaiTro bit
)