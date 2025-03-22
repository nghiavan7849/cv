create table PhanHoiDanhGia 
(
    maPhanHoiDanhGia int PRIMARY KEY AUTO_INCREMENT,
    noiDungPhanHoi varchar(255),
    daPhanHoi bit,
    maDanhGia int,
    maTaiKhoan int,

    FOREIGN KEY (maDanhGia) REFERENCES DanhGia(maDanhGia),
    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan) 
)