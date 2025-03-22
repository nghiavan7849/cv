create table QuanHuyen
(
    maQuanHuyen int PRIMARY KEY AUTO_INCREMENT,
    quanHuyenID int,
    quanHuyenTen varchar(255),
    maTinhThanh int,

    FOREIGN KEY (maTinhThanh) REFERENCES TinhThanh(maTinhThanh)
)