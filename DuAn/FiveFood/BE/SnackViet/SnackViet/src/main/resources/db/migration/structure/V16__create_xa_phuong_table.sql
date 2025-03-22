create table XaPhuong
(
    maXaPhuong int PRIMARY KEY AUTO_INCREMENT,
    xaPhuongID int,
    xaPhuongTen varchar(255),
    maQuanHuyen int,

    FOREIGN KEY (maQuanHuyen) REFERENCES QuanHuyen(maQuanHuyen)
)