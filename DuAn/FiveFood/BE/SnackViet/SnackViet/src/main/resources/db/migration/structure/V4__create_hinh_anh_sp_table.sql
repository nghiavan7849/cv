create table HinhAnhSP
(
    maHinhAnhSP int PRIMARY KEY AUTO_INCREMENT,
    tenHinhAnh varchar(255),
    maSanPham int,

    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
)