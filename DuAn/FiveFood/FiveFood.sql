create database FiveFood;

use FiveFood;

CREATE TABLE flyway_schema_history (
    installed_rank INT NOT NULL,
    version VARCHAR(50),
    description VARCHAR(200) NOT NULL,
    type VARCHAR(20) NOT NULL,
    script VARCHAR(1000) NOT NULL,
    checksum INT,
    installed_by VARCHAR(100) NOT NULL,
    installed_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    execution_time INT NOT NULL,
    success BOOLEAN NOT NULL,
    PRIMARY KEY (installed_rank)
);

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
);

create table LoaiSP
(
    maLoai int PRIMARY KEY AUTO_INCREMENT,
    tenLoai varchar(255),
    trangThai bit
);

create table SanPham
(
    maSanPham int PRIMARY KEY AUTO_INCREMENT,
    tenSanPham varchar(255),
    gia float,
    hinhAnh varchar(255),
    moTa varchar(255),
    soLuong int,
    trongLuong float,
    trangThai bit,
    ngayThem date,
    maLoai int,

    FOREIGN KEY (maLoai) REFERENCES LoaiSP(maLoai)
);

create table HinhAnhSP
(
    maHinhAnhSP int PRIMARY KEY AUTO_INCREMENT,
    tenHinhAnh varchar(255),
    maSanPham int,

    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham)
);

create table DanhGia 
(
    maDanhGia int PRIMARY KEY AUTO_INCREMENT,
    ngayDanhGia date,
    soSao int,
    binhLuan varchar(255),
    trangThai bit,
    maSanPham int,
    maTaiKhoan int,

    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan)
);

create table HinhAnhDG
(
    maHinhAnhDG int PRIMARY KEY AUTO_INCREMENT,
    tenHinhAnh varchar(255),
    maDanhGia int,

    FOREIGN KEY (maDanhGia) REFERENCES DanhGia(maDanhGia)
);

create table ChiTietGioHang
(
    maChiTietGioHang int PRIMARY KEY AUTO_INCREMENT,
    soLuong int,
    maSanPham int,
    maTaiKhoan int,

    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan)
);

create table TrangThaiHoaDon
(
    maTrangThaiHoaDon int PRIMARY KEY,
    tenTrangThai varchar(250)
);

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
);

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
    maTaiKhoan int,
    maDiaChi int, 
    maTrangThaiHoaDon int,

    FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan),
    FOREIGN KEY (maDiaChi) REFERENCES DiaChi(maDiaChi),
    FOREIGN KEY (maTrangThaiHoaDon) REFERENCES TrangThaiHoaDon(maTrangThaiHoaDon)
);

create table ChiTietHoaDon
(
    maChiTietHoaDon int PRIMARY KEY AUTO_INCREMENT,
    soLuong int,
    gia float,
    maSanPham int,
    maHoaDon int,

    FOREIGN KEY (maSanPham) REFERENCES SanPham(maSanPham),
    FOREIGN KEY (maHoaDon) REFERENCES HoaDon(maHoaDon)
);

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
);


-- Tài Khoản
INSERT INTO TaiKhoan (tenDangNhap, matKhau, soDienThoai, email, hoVaTen, gioiTinh, hinhAnh, trangThai, vaiTro) VALUES
('phatdv','$2a$10$nAEeuBeFZ27NIFaeg6SvlOKP5NtuJEipzdgYSgjewHOtUbhYIrag2','0702819466','phatdvpc05504@fpt.edu.vn', 'Đinh Văn Phát',1,'phatdv.png',1,1),
('nghiatv','$2a$10$nAEeuBeFZ27NIFaeg6SvlOKP5NtuJEipzdgYSgjewHOtUbhYIrag2','0702508311','nghiatvpc05510@fpt.edu.vn', 'Trần Văn Nghĩa',1,'nghiatv.png',1,1),
('trieuttp','$2a$10$nAEeuBeFZ27NIFaeg6SvlOKP5NtuJEipzdgYSgjewHOtUbhYIrag2','0702256610','trieuttppc05514@fpt.edu.vn', 'Trần Tô Phước Triều',1,'trieuttp.png',1,0),
('quangdd','$2a$10$nAEeuBeFZ27NIFaeg6SvlOKP5NtuJEipzdgYSgjewHOtUbhYIrag2','0702680453','quangddpc05711@fpt.edu.vn', 'Đặng Duy Quang',1,'quangdd.png',1,0),
('trieunm','$2a$10$nAEeuBeFZ27NIFaeg6SvlOKP5NtuJEipzdgYSgjewHOtUbhYIrag2','0702114567','trieunmpc05519@fpt.edu.vn', 'Nguyễn Minh Triệu',1,'trieunm.png',1,0),
('user','$2a$10$nAEeuBeFZ27NIFaeg6SvlOKP5NtuJEipzdgYSgjewHOtUbhYIrag2','0702114937','user@fpt.edu.vn', 'User',1,'user.png',0,0);

-- Loại Sản Phẩm
INSERT INTO LoaiSP (tenLoai, trangThai) VALUES
('Cơm',1),
('Trà sữa',1),
('Thịt',1),
('Mì',1),
('Gà Rán - Burger',1),
('Bánh kem',1),
('Nước chai',1),
('Cà phê',1),
('Tráng miệng',1);


-- Sản Phẩm
INSERT INTO SanPham (tenSanPham, gia, hinhAnh, moTa, soLuong, trongLuong, trangThai, ngayThem, maLoai) VALUES 
-- Cơm (maLoai = 1)
('Cơm Gà', 45000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-ga.png?alt=media&token=fad54726-6da5-44c8-ae11-08f98d1cd74f', 'Cơm gà thơm ngon', 100, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Sườn', 50000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-suon.png?alt=media&token=b92d30c0-bf3b-41d6-8b79-d649855c2a66', 'Cơm sườn đậm vị', 120, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Tấm', 48000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-tam.png?alt=media&token=b0371b80-6b08-4780-813f-183ab002793f
menu.js:67 Cơm Thịt Kho:', 'Cơm tấm Sài Gòn', 130, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Thịt Kho', 47000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-thit-kho.png?alt=media&token=ae795060-86e2-4ca1-8d05-1c7aa519fce6', 'Cơm thịt kho tàu', 140, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Cá Chiên', 55000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-ca-chien.png?alt=media&token=79e29154-768a-48fa-ac6a-f3b7b3a67871', 'Cơm cá chiên giòn', 110, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Xá Xíu', 52000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-xa-xiu.png?alt=media&token=7f30421f-a139-4095-a283-637d1bccc955', 'Cơm xá xíu', 150, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Chay', 40000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-chay.png?alt=media&token=f59f7cbc-a5c7-4032-a17b-02f500ce3c56', 'Cơm chay thanh đạm', 160, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Gà Xối Mỡ', 48000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-ga-xoi-mo.png?alt=media&token=b02ee538-c3c3-40a0-902c-1f70afd25493', 'Cơm gà xối mỡ', 170, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Bò Lúc Lắc', 58000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-bo-luc-lac.png?alt=media&token=2d00b629-c4ae-4f10-a72e-59c711695534', 'Cơm bò lúc lắc', 115, 500, 1, CAST('2024-05-31' AS Date), 1),
('Cơm Chiên Dương Châu', 50000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-chien-duong-chau.png?alt=media&token=a822e014-aca5-4266-9020-c359f88f8757.png', 'Cơm chiên Dương Châu', 130, 500, 1, CAST('2024-05-31' AS Date), 1),

-- Trà sữa (maLoai = 2)
('Trà Sữa Trân Châu', 30000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-tran-chau.png?alt=media&token=cdd623c1-0dee-44f2-9c86-01462029892e', 'Trá sữa trân châu đen', 200, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Matcha', 35000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-matcha.png?alt=media&token=6b059c22-7ae2-44c6-872f-7cc01f53d28e', 'Trá sữa vị matcha', 180, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Thái Xanh', 32000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-thai-xanh.png?alt=media&token=949ea33a-0805-4dd6-b0d5-13f15199382b', 'Trá sữa Thái xanh', 210, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Hokkaido', 38000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-hokkaido.png?alt=media&token=b7dc7e69-622c-496a-bd68-3694fab2fd78', 'Trá sữa Hokkaido', 190, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Chocolate', 34000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-chocolate.png?alt=media&token=2a668c19-1d3d-4bc6-be30-45658665ea7f', 'Trá sữa vị chocolate', 200, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Oolong', 33000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-oolong.png?alt=media&token=6a1476b4-bd1c-48ac-85bd-c981472d31fe', 'Trá sữa oolong', 220, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Dâu', 31000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-dau.png?alt=media&token=fc7d460f-4150-49d3-9adb-233320a40682', 'Trá sữa vị dâu', 230, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Trái Cây', 36000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-trai-cay.png?alt=media&token=b6239d65-e166-436b-a61d-4c969d9e8441', 'Trá sữa trái cây nhiệt đới', 190, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Socola Dừa', 37000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-socola-dua.png?alt=media&token=fc669dae-faca-4cb6-ab64-fe80f8a3c6a3', 'Trá sữa socola dừa', 210, 350, 1, CAST('2024-05-31' AS Date), 2),
('Trà Sữa Caramel', 35000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-caramel.png?alt=media&token=083f95c3-adfc-4242-84c0-d8635f36190d', 'Trá sữa vị caramel', 180, 350, 1, CAST('2024-05-31' AS Date), 2),

-- Thịt (maLoai = 3)
('Thịt Bò Nướng', 100000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-bo-nuong.png?alt=media&token=ae9edde3-a542-4ba9-a79b-913d379188b3', 'Thịt bò nướng than hoa', 50, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Heo Quay', 90000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-heo-quay.png?alt=media&token=3cfd9c1a-2b63-4050-973b-2230d570863d', 'Thịt heo quay giòn rụm', 80, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Dê Nướng', 120000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-de-nuong.png?alt=media&token=9722a873-b5bf-4866-9174-825cf005bf93s', 'Thịt dê nướng đặc biệt', 60, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Gà Nướng', 85000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-ga-nuong.png?alt=media&token=865a565d-b109-4304-a3bc-2f1539468fe2', 'Thịt gà nướng nguyên con', 75, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Heo Xào Xả Ớt', 95000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-heo-xao-xa-ot.png?alt=media&token=dda5ae44-23ce-4351-868c-2a024ab36114', 'Thịt heo xào sả ớt', 70, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Bò Lúc Lắc', 110000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-bo-luc-lac.png?alt=media&token=56781e4d-92d9-4b14-a212-406af3465965', 'Thịt bò lúc lắc', 55, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Heo Kho Tàu', 90000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-heo-kho-tau.png?alt=media&token=39d5d695-3c41-4938-b298-9f4846155a27', 'Thịt heo kho tàu truyền thống', 85, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Bò Xào Hành', 95000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-bo-xao-hanh.png?alt=media&token=5e12f1f5-449e-4481-aeda-db250ccad483', 'Thịt bò xào hành', 70, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Heo Nướng Riềng Mẻ', 98000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-heo-nuong-rieng-me.png?alt=media&token=b8699235-9645-417e-ac99-c7364d9a0d6c', 'Thịt heo nướng riềng mẻ', 75, 300, 1, CAST('2024-05-31' AS Date), 3),
('Thịt Bò Hầm Tiêu Xanh', 115000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-bo-ham-tieu-xanh.png?alt=media&token=e031a601-fc38-4f57-96f0-4e0dadcbcf7f', 'Thịt bò hầm tiêu xanh', 50, 300, 1, CAST('2024-05-31' AS Date), 3),

-- Mì (maLoai = 4)
('Mì Xào Bò', 55000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-bo.png?alt=media&token=fa896e67-12b8-4d4a-866f-5e82df8d01bd', 'Mì xào bò thơm ngon', 90, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Xào Hải Sản', 65000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-hai-san.png?alt=media&token=9e0aa4f4-9ee0-4be2-be13-eeda1a14ca54', 'Mì xào hải sản tươi', 80, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Ý Sốt Bò', 70000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-y-sot-bo.png?alt=media&token=b440f78b-65ab-4fe4-9c97-26208c079ceb', 'Mì Ý sốt bò bằm', 85, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Xào Gà', 60000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-ga.png?alt=media&token=c166e269-4610-4fad-84d2-9bbe13139d2f', 'Mì xào gà ngọt thịt', 95, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Trộn', 50000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-tron.png?alt=media&token=47181f5d-32b2-4e53-9380-79e1bd90796c', 'Mì trộn truyền thống', 100, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Ý Carbonara', 75000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-y-carbonara.png?alt=media&token=d8f073ee-aa6c-45eb-b579-2ae8db218c77', 'Mì Ý sốt kem Carbonara', 85, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Xào Thập Cẩm', 70000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-thap-cam.png?alt=media&token=c5a0e70f-3989-4513-b298-1d62c6e6d074', 'Mì xào thập cẩm', 80, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Cay Hàn Quốc', 65000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-cay-han-quoc.png?alt=media&token=22fa7a8e-fb9f-464b-9aba-e05a0a8e8a69', 'Mì cay Hàn Quốc', 90, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Tôm', 40000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-tom.png?alt=media&token=97f7847d-9683-4243-ab99-326d361d5353', 'Mì tôm hương vị truyền thống', 100, 300, 1, CAST('2024-05-31' AS Date), 4),
('Mì Xào Rau Củ', 50000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-rau-cu.png?alt=media&token=ecbc1675-59f9-40bd-bcfd-031c59a76116', 'Mì xào rau củ', 110, 300, 1, CAST('2024-05-31' AS Date), 4),

-- Gà Rán - Burger (maLoai = 5)
('Gà Rán Truyền Thống', 60000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-ran-truyen-thong.png?alt=media&token=013971f4-d771-48d9-b1e0-8eeffe854c12', 'Gà rán truyền thống giòn tan', 150, 300, 1, CAST('2024-05-31' AS Date), 5),
('Gà Rán Cay', 65000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-ran-cay.png?alt=media&token=7ef7a4a1-a0c6-40ca-82e6-9373ace81bc3', 'Gà rán cay đặc biệt', 140, 300, 1, CAST('2024-05-31' AS Date), 5),
('Burger Bò', 75000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fburger-bo.png?alt=media&token=b62ad881-9f25-4ebe-9a49-d42faf0611e0', 'Burger bò đặc biệt', 120, 300, 1, CAST('2024-05-31' AS Date), 5),
('Burger Gà', 70000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fburger-ga.png?alt=media&token=ec4fbd2f-2ceb-4b99-85ed-e4b13ce93fc7', 'Burger gà thơm ngon', 130, 300, 1, CAST('2024-05-31' AS Date), 5),
('Combo Gà Rán + Burger', 90000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcombo-ga-ran-burger.png?alt=media&token=74302e86-422f-4a87-a812-951031718498', 'Combo gà rán và burger', 90, 300, 1, CAST('2024-05-31' AS Date), 5),
('Gà Rán Không Xương', 68000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-ran-khong-xuong.png?alt=media&token=86c8d566-d853-491e-a214-5d1c5217e413', 'Gà rán không xương giòn tan', 110, 300, 1, CAST('2024-05-31' AS Date), 5),
('Gà Nướng', 70000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-nuong.png?alt=media&token=85e745b0-4869-4040-99a6-85ebd7d4ed52', 'Gà nướng thơm lừng', 115, 300, 1, CAST('2024-05-31' AS Date), 5),
('Burger Phô Mai', 80000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fburger-pho-mai.png?alt=media&token=c6f48456-be2c-4dba-8308-9f5d7678aa5b', 'Burger bò phô mai', 100, 300, 1, CAST('2024-05-31' AS Date), 5),
('Gà Rán Giòn', 62000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-ran-gion.png?alt=media&token=af62210d-8a62-453f-bf00-3d295fa4dd37', 'Gà rán giòn tan', 150, 300, 1, CAST('2024-05-31' AS Date), 5),
('Burger Cá', 70000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fburger-ca.png?alt=media&token=33f8062f-32e7-4900-9ab2-026ee09220d7', 'Burger cá giòn tan', 130, 300, 1, CAST('2024-05-31' AS Date), 5),

-- Bánh Kem (maLoai = 6)
('Bánh Kem Dâu', 150000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-dau.png?alt=media&token=153dfe14-cde3-4a80-8601-09620164e935', 'Bánh kem vị dâu', 50, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Socola', 180000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-socola.png?alt=media&token=4aa662c0-3d23-41a3-aec2-1f239a0a1ae0', 'Bánh kem vị socola', 45, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Vani', 160000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-vani.png?alt=media&token=938807cb-5f7a-40a1-ba33-bb3695a60244', 'Bánh kem vị vani', 55, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Caramel', 170000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-caramel.png?alt=media&token=5ec587cd-3cc9-488c-8ff0-2f95bd521b2b', 'Bánh kem vị caramel', 50, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Trái Cây', 180000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-trai-cay.png?alt=media&token=696c765f-35f3-4329-bd95-2092e62e37c2', 'Bánh kem trái cây tươi', 40, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Sầu Riêng', 200000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-sau-rieng.png?alt=media&token=507a516b-0e8e-4af6-a148-975ad5ff084d', 'Bánh kem vị sầu riêng', 30, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Kem Cheese', 190000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-cheese.png?alt=media&token=6b691b31-949e-4a78-bd05-354ecb457c54', 'Bánh kem phô mai', 60, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Hạt Dẻ', 170000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-hat-de.png?alt=media&token=4b1700d8-1a0c-4ca7-8466-d2a2b3eac2db', 'Bánh kem hạt dẻ', 45, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Matcha', 185000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-matcha.png?alt=media&token=d5c2d6fb-4e12-48fc-b10f-6dad061e4a92', 'Bánh kem vị matcha', 50, 1000, 1, CAST('2024-05-31' AS Date), 6),
('Bánh Kem Cà Phê', 170000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-ca-phe.png?alt=media&token=d1d814a6-0413-426a-8f47-70db59627357', 'Bánh kem vị cà phê', 55, 1000, 1, CAST('2024-05-31' AS Date), 6),

-- Nước Ngọt (maLoai = 7)
('Nước Ngọt Coca-Cola', 10000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-coca-cola.png?alt=media&token=9bd3e43f-211e-460b-abdd-2c1f509c7954', 'Nước ngọt Coca-Cola chai 500ml', 200, 500, 1, CAST('2024-05-31' AS Date), 7),
('Nước Ngọt Pepsi', 10000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-pepsi.png?alt=media&token=b64464d9-664d-4539-9685-7ef42fcfd9b7', 'Nước ngọt Pepsi chai 500ml', 180, 500, 1, CAST('2024-05-31' AS Date), 7),
('Nước Ngọt 7Up', 10000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-7up.png?alt=media&token=30baa1be-5eb7-491a-b039-689a79438bc8', 'Nước ngọt 7Up chai 500ml', 160, 500, 1, CAST('2024-05-31' AS Date), 7),
('Nước Suối Lavie', 5000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-suoi-lavie.png?alt=media&token=21c5b0a0-a61f-42b1-94f2-26a9a06f0f04', 'Nước suối Lavie chai 500ml', 250, 500, 1, CAST('2024-05-31' AS Date), 7),
('Nước Suối Aquafina', 5000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-suoi-aquafina.png?alt=media&token=cdb2df25-eacc-495d-917e-37ae72deccf5', 'Nước suối Aquafina chai 500ml', 230, 500, 1, CAST('2024-05-31' AS Date), 7),
('Nước Ngọt Sprite', 10000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-sprite.png?alt=media&token=a1b9e63f-8f3a-474b-861d-ef60225ebf5e', 'Nước ngọt Sprite chai 500ml', 170, 500, 1, CAST('2024-05-31' AS Date), 7),
('Nước Ngọt Fanta', 10000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-fanta.png?alt=media&token=39d8b55b-a2c3-4a1d-ad56-789157091ee9', 'Nước ngọt Fanta chai 500ml', 160, 500, 1, CAST('2024-05-31' AS Date), 7),
('Nước Tăng Lực Red Bull', 15000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-tang-luc-red-bull.png?alt=media&token=ef259573-75ff-495e-89d1-37d70af1281c', 'Nước tăng lực Red Bull chai 250ml', 190, 250, 1, CAST('2024-05-31' AS Date), 7),
('Nước Tăng Lực Sting', 12000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-tang-luc-sting.png?alt=media&token=28445053-533b-4511-bdb6-2350492293f0', 'Nước tăng lực Sting chai 330ml', 210, 330, 1, CAST('2024-05-31' AS Date), 7),
('Nước Suối Vĩnh Hảo', 5000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-suoi-vinh-hao.png?alt=media&token=b8ccbc45-0dd8-49d8-b8ce-0da98a675c05', 'Nước suối Vĩnh Hảo chai 500ml', 220, 500, 1, CAST('2024-05-31' AS Date), 7),

-- Cà Phê (maLoai = 8)
('Cà Phê Đen', 20000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-den.png?alt=media&token=f9d6867f-4f4d-4d97-a3b3-75041e08c967', 'Cà phê đen đậm vị truyền thống', 150, 200, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Sữa Đá', 25000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-sua-da.png?alt=media&token=d0b5c847-8445-4bdb-8fa6-ccd3574cd788', 'Cà phê sữa đá thơm ngon', 140, 200, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Cappuccino', 30000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-cappuccino.png?alt=media&token=d03097c9-0d92-4b88-8bef-552e95c702ca', 'Cà phê Cappuccino phong cách Ý', 130, 250, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Latte', 32000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-latte.png?alt=media&token=8d77340c-98e3-425e-ba0a-2c5231cf37c9', 'Cà phê Latte mềm mịn', 120, 250, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Espresso', 28000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-espresso.png?alt=media&token=319a4fa7-7e34-45ad-ab84-3a5c428dda86', 'Cà phê Espresso đậm đặc', 110, 100, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Mocha', 35000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-mocha.png?alt=media&token=aab88ffd-d5ec-45f1-9bc5-77dfac28eddc', 'Cà phê Mocha ngọt ngào', 100, 250, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Americano', 27000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-americano.png?alt=media&token=7ca53d27-4ce3-4d02-a0bc-bc01b5b48570', 'Cà phê Americano phong cách Mỹ', 120, 300, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Cold Brew', 30000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-cold-brew.png?alt=media&token=6fa04adc-6841-47e6-ba2f-8fe837ef9914', 'Cà phê Cold Brew mới lạ', 110, 300, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Dừa', 35000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-dua.png?alt=media&token=263e065b-ac4a-4464-96be-c6701e664c77', 'Cà phê dừa thơm béo', 100, 300, 1, CAST('2024-05-31' AS Date), 8),
('Cà Phê Trứng', 40000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-trung.png?alt=media&token=d5359764-131a-408b-8b68-6747e3b7654a', 'Cà phê trứng độc đáo', 90, 200, 1, CAST('2024-05-31' AS Date), 8),

-- Tráng Miệng (maLoai = 9)
('Kem Vanilla', 15000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fkem-vanilla.png?alt=media&token=7bdc7c55-6512-4003-a95a-02e8664fd7a9', 'Kem Vanilla mát lạnh', 100, 100, 1, CAST('2024-05-31' AS Date), 9),
('Kem Socola', 15000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fkem-socola.png?alt=media&token=c34d3c6f-a2b7-40fd-aca8-044e5392c72b', 'Kem Socola ngọt ngào', 100, 100, 1, CAST('2024-05-31' AS Date), 9),
('Tiramisu', 25000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftiramisu.png?alt=media&token=ad3548e5-a37e-4890-9530-aef8b8ea73d8', 'Tiramisu thơm ngon, béo ngậy', 80, 200, 1, CAST('2024-05-31' AS Date), 9),
('Bánh Flan', 12000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-flan.png?alt=media&token=a284fa92-b5af-4ac3-bb36-a023d54a8a80', 'Bánh Flan mềm mịn, ngon miệng', 150, 100, 1, CAST('2024-05-31' AS Date), 9),
('Bánh Gateaux', 20000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-gateaux.png?alt=media&token=27fbf2e8-0771-499c-9832-259c8c9cafe7', 'Bánh Gateaux ngọt ngào', 90, 200, 1, CAST('2024-05-31' AS Date), 9),
('Chè Thái', 18000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fche-thai.png?alt=media&token=4fb6cce0-89fd-4e5e-bed4-015abed97154', 'Chè Thái đa dạng hương vị', 100, 250, 1, CAST('2024-05-31' AS Date), 9),
('Bánh Mì Ngọt', 15000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-mi-ngot.png?alt=media&token=591286c8-f029-4553-be4d-03ce5e3595f3', 'Bánh mì ngọt thơm ngon', 110, 150, 1, CAST('2024-05-31' AS Date), 9),
('Pudding Trái Cây', 18000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fpudding-trai-cay.png?alt=media&token=bb35df7e-2a71-4bf3-bec5-c9bf18720e4e', 'Pudding trái cây tươi ngon', 90, 150, 1, CAST('2024-05-31' AS Date), 9),
('Kem Dừa', 16000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fkem-dua.png?alt=media&token=14a9f0ce-f266-4164-b506-a3722c5e2695', 'Kem dừa mát lạnh, thơm ngon', 100, 150, 1, CAST('2024-05-31' AS Date), 9),
('Bánh Mochi', 20000, 'https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-mochi.png?alt=media&token=313fb6fa-e5e9-4893-8304-1724fca64b92', 'Bánh Mochi mềm mại, ngọt ngào', 80, 100, 1, CAST('2024-05-31' AS Date), 9);

-- Hình Ảnh Sản Phẩm 
INSERT INTO HinhAnhSP (tenHinhAnh, maSanPham) VALUES 
-- Cơm (maLoai = 1)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-ga.png?alt=media&token=fad54726-6da5-44c8-ae11-08f98d1cd74f', 1),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-suon.png?alt=media&token=b92d30c0-bf3b-41d6-8b79-d649855c2a66', 2),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-tam.png?alt=media&token=b0371b80-6b08-4780-813f-183ab002793f', 3),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-thit-kho.png?alt=media&token=ae795060-86e2-4ca1-8d05-1c7aa519fce6', 4),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-ca-chien.png?alt=media&token=79e29154-768a-48fa-ac6a-f3b7b3a67871', 5),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-xa-xiu.png?alt=media&token=7f30421f-a139-4095-a283-637d1bccc955', 6),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-chay.png?alt=media&token=f59f7cbc-a5c7-4032-a17b-02f500ce3c56', 7),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-ga-xoi-mo.png?alt=media&token=b02ee538-c3c3-40a0-902c-1f70afd25493', 8),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-bo-luc-lac.png?alt=media&token=2d00b629-c4ae-4f10-a72e-59c711695534', 9),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcom-chien-duong-chau.png?alt=media&token=a822e014-aca5-4266-9020-c359f88f8757', 10),

-- Trà sữa (maLoai = 2)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-tran-chau.png?alt=media&token=cdd623c1-0dee-44f2-9c86-01462029892e', 11),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-matcha.png?alt=media&token=6b059c22-7ae2-44c6-872f-7cc01f53d28e', 12),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-thai-xanh.png?alt=media&token=949ea33a-0805-4dd6-b0d5-13f15199382b', 13),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-hokkaido.png?alt=media&token=b7dc7e69-622c-496a-bd68-3694fab2fd78', 14),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-chocolate.png?alt=media&token=2a668c19-1d3d-4bc6-be30-45658665ea7f', 15),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-oolong.png?alt=media&token=6a1476b4-bd1c-48ac-85bd-c981472d31fe', 16),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-dau.png?alt=media&token=fc7d460f-4150-49d3-9adb-233320a40682', 17),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-trai-cay.png?alt=media&token=b6239d65-e166-436b-a61d-4c969d9e8441', 18),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-socola-dua.png?alt=media&token=fc669dae-faca-4cb6-ab64-fe80f8a3c6a3', 19),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftra-sua-caramel.png?alt=media&token=083f95c3-adfc-4242-84c0-d8635f36190d', 20),

-- Thịt (maLoai = 3)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-bo-nuong.png?alt=media&token=ae9edde3-a542-4ba9-a79b-913d379188b3', 21),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-heo-quay.png?alt=media&token=3cfd9c1a-2b63-4050-973b-2230d570863d', 22),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-de-nuong.png?alt=media&token=9722a873-b5bf-4866-9174-825cf005bf93', 23),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-ga-nuong.png?alt=media&token=865a565d-b109-4304-a3bc-2f1539468fe2', 24),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-heo-xao-xa-ot.png?alt=media&token=dda5ae44-23ce-4351-868c-2a024ab36114', 25),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-bo-luc-lac.png?alt=media&token=56781e4d-92d9-4b14-a212-406af3465965', 26),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-heo-kho-tau.png?alt=media&token=39d5d695-3c41-4938-b298-9f4846155a27', 27),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-bo-xao-hanh.png?alt=media&token=5e12f1f5-449e-4481-aeda-db250ccad483', 28),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-heo-nuong-rieng-me.png?alt=media&token=b8699235-9645-417e-ac99-c7364d9a0d6c', 29),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fthit-bo-ham-tieu-xanh.png?alt=media&token=e031a601-fc38-4f57-96f0-4e0dadcbcf7f', 30),

-- Mì (maLoai = 4)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-bo.png?alt=media&token=fa896e67-12b8-4d4a-866f-5e82df8d01bd', 31),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-hai-san.png?alt=media&token=9e0aa4f4-9ee0-4be2-be13-eeda1a14ca54', 32),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-y-sot-bo.png?alt=media&token=b440f78b-65ab-4fe4-9c97-26208c079ceb', 33),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-ga.png?alt=media&token=c166e269-4610-4fad-84d2-9bbe13139d2f', 34),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-tron.png?alt=media&token=47181f5d-32b2-4e53-9380-79e1bd90796c', 35),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-y-carbonara.png?alt=media&token=d8f073ee-aa6c-45eb-b579-2ae8db218c77', 36),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-thap-cam.png?alt=media&token=c5a0e70f-3989-4513-b298-1d62c6e6d074', 37),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-cay-han-quoc.png?alt=media&token=22fa7a8e-fb9f-464b-9aba-e05a0a8e8a69', 38),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-tom.png?alt=media&token=97f7847d-9683-4243-ab99-326d361d5353', 39),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fmi-xao-rau-cu.png?alt=media&token=ecbc1675-59f9-40bd-bcfd-031c59a76116', 40),

-- Gà Rán - Burger (maLoai = 5)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-ran-truyen-thong.png?alt=media&token=013971f4-d771-48d9-b1e0-8eeffe854c12', 41),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-ran-cay.png?alt=media&token=7ef7a4a1-a0c6-40ca-82e6-9373ace81bc3', 42),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fburger-bo.png?alt=media&token=b62ad881-9f25-4ebe-9a49-d42faf0611e0', 43),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fburger-ga.png?alt=media&token=ec4fbd2f-2ceb-4b99-85ed-e4b13ce93fc7', 44),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fcombo-ga-ran-burger.png?alt=media&token=74302e86-422f-4a87-a812-951031718498', 45),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-ran-khong-xuong.png?alt=media&token=86c8d566-d853-491e-a214-5d1c5217e413', 46),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-nuong.png?alt=media&token=85e745b0-4869-4040-99a6-85ebd7d4ed52', 47),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fburger-pho-mai.png?alt=media&token=c6f48456-be2c-4dba-8308-9f5d7678aa5b', 48),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fga-ran-gion.png?alt=media&token=af62210d-8a62-453f-bf00-3d295fa4dd37', 49),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fburger-ca.png?alt=media&token=33f8062f-32e7-4900-9ab2-026ee09220d7', 50),

-- Bánh Kem (maLoai = 6)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-dau.png?alt=media&token=153dfe14-cde3-4a80-8601-09620164e935', 51),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-socola.png?alt=media&token=4aa662c0-3d23-41a3-aec2-1f239a0a1ae0', 52),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-vani.png?alt=media&token=938807cb-5f7a-40a1-ba33-bb3695a60244', 53),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-caramel.png?alt=media&token=5ec587cd-3cc9-488c-8ff0-2f95bd521b2b', 54),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-trai-cay.png?alt=media&token=696c765f-35f3-4329-bd95-2092e62e37c2', 55),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-sau-rieng.png?alt=media&token=507a516b-0e8e-4af6-a148-975ad5ff084d', 56),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-cheese.png?alt=media&token=6b691b31-949e-4a78-bd05-354ecb457c54', 57),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-hat-de.png?alt=media&token=4b1700d8-1a0c-4ca7-8466-d2a2b3eac2db', 58),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-matcha.png?alt=media&token=d5c2d6fb-4e12-48fc-b10f-6dad061e4a92', 59),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-kem-ca-phe.png?alt=media&token=d1d814a6-0413-426a-8f47-70db59627357', 60),

-- Nước Ngọt (maLoai = 7)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-coca-cola.png?alt=media&token=9bd3e43f-211e-460b-abdd-2c1f509c7954', 61),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-pepsi.png?alt=media&token=b64464d9-664d-4539-9685-7ef42fcfd9b7', 62),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-7up.png?alt=media&token=30baa1be-5eb7-491a-b039-689a79438bc8', 63),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-suoi-lavie.png?alt=media&token=21c5b0a0-a61f-42b1-94f2-26a9a06f0f04', 64),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-suoi-aquafina.png?alt=media&token=cdb2df25-eacc-495d-917e-37ae72deccf5', 65),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-sprite.png?alt=media&token=a1b9e63f-8f3a-474b-861d-ef60225ebf5e', 66),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-ngot-fanta.png?alt=media&token=39d8b55b-a2c3-4a1d-ad56-789157091ee9', 67),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-tang-luc-red-bull.png?alt=media&token=ef259573-75ff-495e-89d1-37d70af1281c', 68),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-tang-luc-sting.png?alt=media&token=28445053-533b-4511-bdb6-2350492293f0', 69),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fnuoc-suoi-vinh-hao.png?alt=media&token=b8ccbc45-0dd8-49d8-b8ce-0da98a675c05', 70),

-- Cà Phê (maLoai = 8)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-den.png?alt=media&token=f9d6867f-4f4d-4d97-a3b3-75041e08c967', 71),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-sua-da.png?alt=media&token=d0b5c847-8445-4bdb-8fa6-ccd3574cd788', 72),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-cappuccino.png?alt=media&token=d03097c9-0d92-4b88-8bef-552e95c702ca', 73),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-latte.png?alt=media&token=8d77340c-98e3-425e-ba0a-2c5231cf37c9', 74),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-espresso.png?alt=media&token=319a4fa7-7e34-45ad-ab84-3a5c428dda86', 75),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-mocha.png?alt=media&token=aab88ffd-d5ec-45f1-9bc5-77dfac28eddc', 76),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-americano.png?alt=media&token=7ca53d27-4ce3-4d02-a0bc-bc01b5b48570', 77),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-cold-brew.png?alt=media&token=6fa04adc-6841-47e6-ba2f-8fe837ef9914', 78),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-dua.png?alt=media&token=263e065b-ac4a-4464-96be-c6701e664c77', 79),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fca-phe-trung.png?alt=media&token=d5359764-131a-408b-8b68-6747e3b7654a', 80),

-- Tráng Miệng (maLoai = 9)
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fkem-vanilla.png?alt=media&token=7bdc7c55-6512-4003-a95a-02e8664fd7a9', 81),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fkem-socola.png?alt=media&token=c34d3c6f-a2b7-40fd-aca8-044e5392c72b', 82),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Ftiramisu.png?alt=media&token=ad3548e5-a37e-4890-9530-aef8b8ea73d8', 83),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-flan.png?alt=media&token=a284fa92-b5af-4ac3-bb36-a023d54a8a80', 84),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-gateaux.png?alt=media&token=27fbf2e8-0771-499c-9832-259c8c9cafe7', 85),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fche-thai.png?alt=media&token=4fb6cce0-89fd-4e5e-bed4-015abed97154', 86),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-mi-ngot.png?alt=media&token=591286c8-f029-4553-be4d-03ce5e3595f3', 87),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fpudding-trai-cay.png?alt=media&token=bb35df7e-2a71-4bf3-bec5-c9bf18720e4e', 88),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fkem-dua.png?alt=media&token=14a9f0ce-f266-4164-b506-a3722c5e2695', 89),
('https://firebasestorage.googleapis.com/v0/b/fivefood-datn-8a1cf.appspot.com/o/AnhSanPham%2Fbanh-mochi.png?alt=media&token=313fb6fa-e5e9-4893-8304-1724fca64b92', 90);


-- Đánh Giá 
INSERT INTO DanhGia (ngayDanhGia, soSao, binhLuan, trangThai, maSanPham, maTaiKhoan) VALUES 
(CAST('2024-05-31' AS Date), 3, 'Không ngon cho lắm', 1, 1, 3),
(CAST('2024-05-31' AS Date), 5, 'Rất ngon rất đáng mua', 1, 5, 5),
(CAST('2024-05-30' AS Date), 2, 'Tệ', 1, 1, 4),
(CAST('2024-05-31' AS Date), 5, 'Rất ngon tôi sẽ giới thiệu cho bạn bè của tôi', 1, 1, 3),
(CAST('2024-05-30' AS Date), 1, 'Dỡ', 1, 1, 4),
(CAST('2024-05-29' AS Date), 5, 'Rất ngon rất đáng mua', 1, 1, 5),
(CAST('2024-06-17' AS Date), 4, 'Tạm', 1, 1, 2),
(CAST('2024-08-01' AS Date), 5, 'HIhi rất ngon', 1, 1, 1),
(CAST('2024-08-07' AS Date), 4, 'HIhi rất ngon, nhưng đắt', 1, 1, 1);


-- Hình Ảnh Đánh Giá 
INSERT INTO HinhAnhDG (tenHinhAnh, maDanhGia) VALUES 
('anh-danh-gia1.png', 1),
('anh-danh-gia2.png', 2),
('anh-danh-gia2.png', 2),
('anh-danh-gia3.png', 3),
('anh-danh-gia4.png', 4),
('anh-danh-gia5.png', 5),
('anh-danh-gia51.png', 5),
('anh-danh-gia6.png', 6),
('anh-danh-gia61.png', 6),
('phatdv.png', 7),
('tenhinhanh12.png', 1);


-- Chi Tiết Giỏ Hàng 
INSERT INTO ChiTietGioHang (soLuong, maSanPham, maTaiKhoan) VALUES 
(3, 1, 1),
(2, 2, 2),
(4, 3, 3),
(2, 4, 4),
(1, 5, 5),
(3, 1, 3),
(4, 6, 4),
(2, 3, 5),
(5, 6, 4),
(2, 8, 3),
(4, 10, 5),
(3, 4, 3),
(7, 1, 5),
(3, 2, 4),
(5, 9, 5),
(3, 11, 3),
(1, 6, 1);


-- Trạng Thái Tài Khoản 
INSERT INTO TrangThaiHoaDon (maTrangThaiHoaDon, tenTrangThai) VALUES 
(1, 'Đã đặt hàng'),
(2, 'Đã xác nhận'),
(3, 'Đang xử lý'),
(4, 'Đang vận chuyển'),	
(5, 'Giao thành công'),
(6, 'Đã Hủy');


-- Địa Chỉ 
INSERT INTO DiaChi (soDienThoai, hoVaTen, trangThai, diaChi, diaChiChiTiet, maTinhThanh, maQuanHuyen, maXaPhuong, trangThaiXoa, maTaiKhoan ) VALUES 
('0290294308','Trần Tô Phước Triều',1, 'Hưng Lợi, Ninh Kiều, Cần Thơ', '68 Đ.Trần Hoàng Na,',220,1572,550110,0,3),
('0290294308','Trần Triều',0, 'Ninh Kiều, Cần Thơ', '152 Phòng 6 Đ.3 Tháng 2',220,1572,550113,0,3),
('0772310934','Đặng Duy Quang',1, 'Ninh Kiều, Cần Thơ', '15B Đ.Trần Hoàng Na,',220,1572,550110,0,4),
('0772310934','Đặng Quang',0, 'Hưng Lợi, Ninh Kiều, Cần Thơ', '2b Tầm Vu',220,1572,550110,0,4),
('0912499034','Nguyễn Minh Triệu',1, 'Hưng Lợi, Ninh Kiều, Cần Thơ', '409 Đ. 30 Tháng 4,',220,1572,550110,0,5),
('0912499034','Nguyễn Triệu',0, 'Hưng Lợi, Ninh Kiều, Cần Thơ', '1 Đ. Nguyễn Văn Linh',220,1572,550110,0,5);


-- Hóa Đơn 
INSERT INTO HoaDon (phiVanChuyen, tongTien, ngayThanhToan, phuongThucThanhToan, trangThai, ghiChu, lyDoHuy, maTaiKhoan, maDiaChi, maTrangThaiHoaDon) VALUES 
(50000, 27000, CAST('2024-05-31' AS Date), 1, 1, 'Giao hàng nhanh', null,3, 1, 1),
(75000, 36000, CAST('2024-06-01' AS Date), 0, 1, 'Thanh toán khi nhận hàng', null, 4, 2, 2),
(30000, 264000, CAST('2024-05-29' AS Date), 1, 0, 'Đã hủy đơn hàng', 'Sản phẩm bị lỗi', 5, 3, 6),
(45000, 40000, CAST('2024-05-28' AS Date), 1, 1, 'Giao hàng thành công', null, 3, 1, 3),
(60000, 17000, CAST('2024-06-05' AS Date), 0, 0, 'Khách hàng đổi địa chỉ giao', null, 5, 2, 6),
(55000, 96000, CAST('2024-06-07' AS Date), 1, 1, 'Giao hàng nhanh', null, 4, 3, 5),
(25000, 198000, CAST('2024-05-30' AS Date), 1, 1, 'Giao hàng thành công', null, 1, 1, 1),
(40000, 355000, CAST('2024-06-02' AS Date), 0, 1, 'Thanh toán khi nhận hàng', null, 2, 2, 2),
(35000, 48000, CAST('2024-06-08' AS Date), 1, 0, 'Đơn hàng bị hủy', 'Khách bom', 3, 3, 6),
(50000, 78000, CAST('2024-06-03' AS Date), 0, 1, 'Thanh toán qua thẻ', null, 5, 2, 4),
(60000, 22000, CAST('2024-06-10' AS Date), 1, 1, 'Giao hàng thành công', null, 4, 3, 5),
(40000, 90000, CAST('2024-06-12' AS Date), 0, 0, 'Khách hàng thay đổi phương thức thanh toán', null, 1, 2, 6);


-- Chi Tiết Hóa Đơn 
INSERT INTO ChiTietHoaDon (soLuong, gia, maSanPham, maHoaDon) VALUES 
(1, 12000, 1, 1),
(1, 15000, 11, 1),
(1, 12000, 4, 2),
(2, 12000, 4, 2),
(1, 22000, 10, 4),
(1, 18000, 7, 4),
(1, 17000, 12, 5),
(1, 22000, 10, 11),
(5, 18000, 3, 12),
(8, 12000, 10, 6),
(9, 15000, 6, 8),
(2, 12000, 6, 9),
(8, 18000, 8, 3),
(10, 15000, 5, 3),
(9, 22000, 8, 7),
(2, 12000, 5, 3),
(10, 22000, 9, 8),
(3, 22000, 9, 10),
(1, 12000, 1, 10);


-- Giao Dịch 

select * from ChiTietHoaDon;


