create table HinhAnhDG
(
    maHinhAnhDG int PRIMARY KEY AUTO_INCREMENT,
    tenHinhAnh varchar(255),
    maDanhGia int,

    FOREIGN KEY (maDanhGia) REFERENCES DanhGia(maDanhGia)
)