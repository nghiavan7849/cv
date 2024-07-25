CREATE DATABASE SnackViet
GO
Use Master
USE SnackViet
GO

CREATE TABLE [TaiKhoan] (
  [maTaiKhoan] int IDENTITY PRIMARY KEY,
  [tenDangNhap] nvarchar(30),
  [matKhau] nvarchar(255),
  [soDienThoai] nvarchar(10) UNIQUE,
  [email] nvarchar(255) UNIQUE,
  [hoVaTen] nvarchar(255),
  [gioiTinh] bit,
  [hinhAnh] nvarchar(255),
  [trangThai] bit,
  [vaiTro] bit
)
GO


CREATE TABLE [LoaiSP] (
  [maLoai] int IDENTITY PRIMARY KEY,
  [tenLoai] nvarchar(255)
)
GO

CREATE TABLE [SanPham] (
  [maSanPham] int IDENTITY PRIMARY KEY,
  [tenSanPham] nvarchar(255),
  [gia] float,
  [hinhAnh] nvarchar(255),
  [moTa] nvarchar(255),
  [soLuong] int,
  [trongLuong] float,
  [trangThai] bit,
  [ngayThem] date,
  [maLoai] int
)
GO

CREATE TABLE [HinhAnhSP] (
  [maHinhAnhSP] int IDENTITY PRIMARY KEY,
  [tenHinhAnh] nvarchar(255),
  [maSanPham] int
)
GO

CREATE TABLE [DanhGia] (
  [maDanhGia] int IDENTITY PRIMARY KEY,
  [ngayDanhGia] date,
  [soSao] int,
  [binhLuan] nvarchar(255),
  [trangThai] bit,
  [maSanPham] int,
  [maTaiKhoan] int
)
GO

CREATE TABLE [HinhAnhDG] (
  [maHinhAnhDG] int IDENTITY PRIMARY KEY,
  [tenHinhAnh] nvarchar(255),
  [maDanhGia] int
)
GO

CREATE TABLE [ChiTietGioHang] (
  [maChiTietGioHang] int IDENTITY PRIMARY KEY,
  [soLuong] int,
  [maSanPham] int,
  [maTaiKhoan] int
)
GO

CREATE TABLE [HoaDon] (
  [maHoaDon] int IDENTITY PRIMARY KEY,
  [tongTien] float,
  [ngayThanhToan] date,
  [diaChi] nvarchar(255),
  [ghiChu] nvarchar(255),
  [phiVanChuyen] float,
  [phuongThucThanhToan] bit,
  [maTaiKhoan] int,
  [maTrangThaiHoaDon] int
)
GO

CREATE TABLE [TrangThaiHoaDon] (
  [maTrangThaiHoaDon] int IDENTITY PRIMARY KEY,
  [tenTrangThai] nvarchar(255)
)
GO

CREATE TABLE [ChiTietHoaDon] (
  [maChiTietHoaDon] int IDENTITY PRIMARY KEY,
  [soLuong] int,
  [maHoaDon] int,
  [maSanPham] int
)
GO

CREATE TABLE [DiaChi] (
  [maDiaChi] int IDENTITY PRIMARY KEY,
  [soDienThoai] nvarchar(10),
  [hoVaTen] nvarchar(255),
  [trangThai] bit,
  [diaChi] nvarchar(255),
  [maTaiKhoan] int
)
GO

CREATE TABLE [GiaoDich] (
  [maGiaoDich] int IDENTITY PRIMARY KEY,
  [codeGiaoDich] nvarchar(255),
  [soTaiKhoan] varchar(255),
  [hoVaTen] nvarchar(255),
  [ngayGiaoDich] date,
  [trangThai] bit,
  [soTien] float,
  [maTaiKhoan] int,
  [maHoaDon] int
)
GO

--Tài khoản
--[tenDangNhap] nvarchar(30), [matKhau] nvarchar(50), [soDienThoai] nvarchar(10) UNIQUE,
 --[email] nvarchar(255) UNIQUE, [hoVaTen] nvarchar(255),  [gioiTinh] bit,  [hinhAnh] nvarchar(255), [trangThai] bit,  [vaiTro] bit 
INSERT INTO [TaiKhoan] VALUES ('phatdv','25d55ad283aa400af464c76d713c07ad','0702819466','phatdvpc05504@fpt.edu.vn',N'Đinh Văn Phát',1,'phatdv.png',1,1)
INSERT INTO [TaiKhoan] VALUES ('nghiatv','25d55ad283aa400af464c76d713c07ad','0702508311','nghiatvpc05510@fpt.edu.vn',N'Trần Văn Nghĩa',1,'nghiatv.png',1,1)
INSERT INTO [TaiKhoan] VALUES ('trieuttp','25d55ad283aa400af464c76d713c07ad','0702256610','trieuttppc05514@fpt.edu.vn',N'Trần Tô Phước Triều',1,'trieuttp.png',1,0)
INSERT INTO [TaiKhoan] VALUES ('quangdd','25d55ad283aa400af464c76d713c07ad','0702680453','quangddpc05711@fpt.edu.vn',N'Đặng Duy Quang',1,'quangdd.png',1,0)
INSERT INTO [TaiKhoan] VALUES ('trieunm','25d55ad283aa400af464c76d713c07ad','0702114567','trieunmpc05519@fpt.edu.vn',N'Nguyễn Minh Triệu',1,'trieunm.png',1,0)
INSERT INTO [TaiKhoan] VALUES ('user','25d55ad283aa400af464c76d713c07ad','0702114937','user@fpt.edu.vn',N'User',1,'user.png',0,0)
---

SELECT * FROM [TaiKhoan]




GO
--Loại Sản Phẩm
--[tenLoai] nvarchar(255) 
INSERT INTO [LoaiSP] VALUES (N'Snack')
INSERT INTO [LoaiSP] VALUES (N'Bánh Tráng')
INSERT INTO [LoaiSP] VALUES (N'Khô')
INSERT INTO [LoaiSP] VALUES (N'Kẹo')
--
SELECT * FROM [LoaiSP]
GO
--Sản Phẩm
--[tenSanPham] nvarchar(255),[gia] float,[hinhAnh] nvarchar(255), [moTa] nvarchar(255),[soLuong] int,[trongLuong] float,[trangThai] bit, [ngayThem] date, [maLoai] int 
INSERT INTO [SanPham] VALUES (N'Snack Vị Mực Cay',12000,'snack-muc-cay1.png',N'Snack mực cay ngon mê li gói 35g',139,35,1,'2024-05-31',1)
INSERT INTO [SanPham] VALUES (N'Snack Vị Khoai Tây',12000,'snack-khoai-tay1.png',N'Snack vị khoai tây ngon mê li gói 90g',150,90,1,'2024-05-31',1)
INSERT INTO [SanPham] VALUES (N'Snack Gà Quay',12000,'snack-ga-quay1.png',N'Snack gà quay BoLa ngon mê li gói 68g',169,68,1,'2024-05-31',1)
INSERT INTO [SanPham] VALUES (N'Snack Vị Bò Lúc Lắc',12000,'snack-poca1.png',N'Snack vị bò lúc lắc Poca ngon mê li gói 65g',113,65,1,'2024-05-31',1)
INSERT INTO [SanPham] VALUES (N'Bánh Tráng Trộn',15000,'banh-trang-tron1.png',N'Bánh tráng trộn hành tỏi ngon mê li gói 100g',189,100,1,'2024-05-31',2)
INSERT INTO [SanPham] VALUES (N'Bánh tráng cuộn',15000,'banh-trang-cuon1.png',N'Bánh tráng cuộn ngon mê li hộp 100g',99,100,1,'2024-05-31',2)
INSERT INTO [SanPham] VALUES (N'Bánh tráng chà bông',18000,'banh-trang-cha-bong1.png',N'Snack Poca ngon mê li gói 65g',163,65,1,'2024-05-31',2)
INSERT INTO [SanPham] VALUES (N'Khô Gà Xé Không Cay',22000,'kho-ga-xe-khong-cay1.png',N'Khô gà xé không cay ngon mê li gói 50g',189,50,1,'2024-05-31',3)
INSERT INTO [SanPham] VALUES (N'Khô Gà Rong Biển',22000,'kho-ga-rong-bien1.png',N'Khô gà rong biển ngon mê li gói 50g',185,50,1,'2024-05-31',3)
INSERT INTO [SanPham] VALUES (N'Khô Gà Bơ Tỏi',22000,'kho-ga-bo-toi1.png',N'Khô gà bơ tỏi ngon mê li gói 50g',185,50,1,'2024-05-31',3)
INSERT INTO [SanPham] VALUES (N'Kẹo KOPIKO',15000,'keo-kopiko1.png',N'Kẹo KOPIKO ngon mê li gói 65g',109,65,1,'2024-05-31',4)
INSERT INTO [SanPham] VALUES (N'Kẹo mút Chupa Chups',17000,'keo-mut-chupa-chups1.png',N'Kẹo mút Chupa Chups ngon mê li gói 65g',125,65,1,'2024-05-31',4)
INSERT INTO [SanPham] VALUES (N'Kẹo sữa Alpenliebe',17000,'keo-sua-caramen-alpenliebe1.png',N'Kẹo sữa caramen Alpenliebe ngon mê li gói 65g',155,65,1,'2024-05-31',4)
--
SELECT * FROM [SanPham]
GO
--Hình Sản Phẩm
--[tenHinhAnh] nvarchar(255),  [maSanPham] int 
INSERT INTO [HinhAnhSP] VALUES ('snack-muc-cay1.png',1)
INSERT INTO [HinhAnhSP] VALUES ('snack-muc-cay2.png',1)
INSERT INTO [HinhAnhSP] VALUES ('snack-muc-cay3.png',1)
INSERT INTO [HinhAnhSP] VALUES ('snack-muc-cay4.png',1)
INSERT INTO [HinhAnhSP] VALUES ('snack-muc-cay5.png',1)
INSERT INTO [HinhAnhSP] VALUES ('snack-khoai-tay1.png',2)
INSERT INTO [HinhAnhSP] VALUES ('snack-khoai-tay2.png',2)
INSERT INTO [HinhAnhSP] VALUES ('snack-khoai-tay3.png',2)
INSERT INTO [HinhAnhSP] VALUES ('snack-khoai-tay4.png',2)
INSERT INTO [HinhAnhSP] VALUES ('snack-khoai-tay5.png',2)
INSERT INTO [HinhAnhSP] VALUES ('snack-ga-quay1.png',3)
INSERT INTO [HinhAnhSP] VALUES ('snack-ga-quay2.png',3)
INSERT INTO [HinhAnhSP] VALUES ('snack-ga-quay3.png',3)
INSERT INTO [HinhAnhSP] VALUES ('snack-ga-quay4.png',3)
INSERT INTO [HinhAnhSP] VALUES ('snack-ga-quay5.png',3)
INSERT INTO [HinhAnhSP] VALUES ('snack-poca1.png',4)
INSERT INTO [HinhAnhSP] VALUES ('snack-poca2.png',4)
INSERT INTO [HinhAnhSP] VALUES ('snack-poca3.png',4)
INSERT INTO [HinhAnhSP] VALUES ('snack-poca4.png',4)
INSERT INTO [HinhAnhSP] VALUES ('snack-poca5.png',4)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-tron1.png',5)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-tron2.png',5)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-tron3.png',5)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-cuon1.png',6)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-cuon2.png',6)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-cuon3.png',6)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-cha-bong1.png',7)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-cha-bong2.png',7)
INSERT INTO [HinhAnhSP] VALUES ('banh-trang-cha-bong3.png',7)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-xe-khong-cay1.png',8)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-xe-khong-cay2.png',8)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-xe-khong-cay3.png',8)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-rong-bien1.png',9)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-rong-bien2.png',9)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-rong-bien3.png',9)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-rong-bien4.png',9)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-rong-bien5.png',9)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-bo-toi1.png',10)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-bo-toi2.png',10)
INSERT INTO [HinhAnhSP] VALUES ('kho-ga-bo-toi3.png',10)
INSERT INTO [HinhAnhSP] VALUES ('keo-kopiko1.png',11)
INSERT INTO [HinhAnhSP] VALUES ('keo-kopiko2.png',11)
INSERT INTO [HinhAnhSP] VALUES ('keo-kopiko3.png',11)
INSERT INTO [HinhAnhSP] VALUES ('keo-kopiko4.png',11)
INSERT INTO [HinhAnhSP] VALUES ('keo-kopiko5.png',11)
INSERT INTO [HinhAnhSP] VALUES ('keo-mut-chupa-chups1.png',12)
INSERT INTO [HinhAnhSP] VALUES ('keo-mut-chupa-chups2.png',12)
INSERT INTO [HinhAnhSP] VALUES ('keo-mut-chupa-chups3.png',12)
INSERT INTO [HinhAnhSP] VALUES ('keo-mut-chupa-chups4.png',12)
INSERT INTO [HinhAnhSP] VALUES ('keo-mut-chupa-chups5.png',12)
INSERT INTO [HinhAnhSP] VALUES ('keo-sua-caramen-alpenliebe1.png',13)
INSERT INTO [HinhAnhSP] VALUES ('keo-sua-caramen-alpenliebe2.png',13)
INSERT INTO [HinhAnhSP] VALUES ('keo-sua-caramen-alpenliebe3.png',13)
INSERT INTO [HinhAnhSP] VALUES ('keo-sua-caramen-alpenliebe4.png',13)
INSERT INTO [HinhAnhSP] VALUES ('keo-sua-caramen-alpenliebe5.png',13)
--
SELECT * FROM [SanPham]
SELECT * FROM [HinhAnhSP] where maSanPham =1
GO

--Đánh Giá
--[ngayDanhGia] date,  [soSao] int,  [binhLuan] nvarchar(255), [trangThai] bit,  [maSanPham] int, [maTaiKhoan] int 
INSERT INTO [DanhGia] VALUES ('2024-05-31',5,N'Rất ngon rất đáng mua',1,1,3)
INSERT INTO [DanhGia] VALUES ('2024-05-31',5,N'Rất ngon rất đáng mua',1,5,5)
INSERT INTO [DanhGia] VALUES ('2024-05-30',5,N'Rất ngon',1,1,4)
INSERT INTO [DanhGia] VALUES ('2024-05-31',5,N'Rất ngon tôi sẽ giới thiệu cho bạn bè của tôi',1,1,3)
INSERT INTO [DanhGia] VALUES ('2024-05-30',5,N'Rất ngon',1,1,4)
INSERT INTO [DanhGia] VALUES ('2024-05-29',5,N'Rất ngon rất đáng mua',1,1,5)
--
SELECT * FROM [DanhGia]
GO
--Hình Ảnh Đánh Giá
--[tenHinhAnh] nvarchar(255), [maDanhGia] int 
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia1.png',1)
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia2.png',2)
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia2.png',2)
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia3.png',3)
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia4.png',4)
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia5.png',5)
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia51.png',5)
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia6.png',6)
INSERT INTO [HinhAnhDG] VALUES ('anh-danh-gia61.png',6)
--
SELECT * FROM [HinhAnhDG]

GO
--Chi Tiếc Giỏ Hàng
 --[soLuong] int, [maSanPham] int, [maTaiKhoan] int 
INSERT INTO [ChiTietGioHang] VALUES (3,1,3)
INSERT INTO [ChiTietGioHang] VALUES (4,6,4)
INSERT INTO [ChiTietGioHang] VALUES (2,3,5)
INSERT INTO [ChiTietGioHang] VALUES (5,6,4)
INSERT INTO [ChiTietGioHang] VALUES (2,8,3)
INSERT INTO [ChiTietGioHang] VALUES (4,10,5)
INSERT INTO [ChiTietGioHang] VALUES (3,4,3)
INSERT INTO [ChiTietGioHang] VALUES (7,1,5)
INSERT INTO [ChiTietGioHang] VALUES (3,2,4)
INSERT INTO [ChiTietGioHang] VALUES (5,9,5)
INSERT INTO [ChiTietGioHang] VALUES (3,11,3)
--
SELECT * FROM [ChiTietGioHang]
GO
--Hóa Đơn
INSERT INTO [HoaDon] VALUES (161000,'2024-05-31',N'246 Đường Trần Hưng Đạo, Phường Nguyễn Cư Trinh, Quận 1, Thành phố Hồ Chí Minh',null,20000,1,1,5)
INSERT INTO [HoaDon] VALUES (96000,'2024-05-28',N'123 Đường Lê Lợi, Phường Mỹ An, Quận Ngũ Hành Sơn, Thành phố Đà Nẵng',null,50000,1,2,6)
INSERT INTO [HoaDon] VALUES (75000,'2024-05-31',N'123 Đường 30/4, Phường An Hòa, Quận Ninh Kiều, Thành phố Cần Thơ',null,15000,1,4,3)
INSERT INTO [HoaDon] VALUES (112000,'2024-05-29',N'54 Đường 30/4, Phường An Hòa, Quận Ninh Kiều, Thành phố Cần Thơ',null,15000,0,3,4)
INSERT INTO [HoaDon] VALUES (84000,'2024-05-30',N'456 Đường Nguyễn Huệ, Phường 1, Thành phố Tân An, Tỉnh Long An',null,22000,1,2,1)
INSERT INTO [HoaDon] VALUES (11000,'2024-05-31',N'789 Đường Hùng Vương, Phường 3, Thành phố Bạc Liêu, Tỉnh Bạc Liêu',null,25000,0,5,3)
INSERT INTO [HoaDon] VALUES (112000,'2024-05-29',N'54 Đường 3/2, Phường Hưng Lợi, Quận Ninh Kiều, Thành phố Cần Thơ',null,15000,0,2,3)
INSERT INTO [HoaDon] VALUES (11000,'2024-05-31',N'29 Khu 3, Huyện Hồng Dân, Thị trấn Ngan Dừa, Tỉnh Bạc Liêu',null,25000,0,5,5)
INSERT INTO [HoaDon] VALUES (112000,'2024-05-29',N'32 Đường 91, Phường Thốt nốt, Quận Thốt nốt, Thành phố Cần Thơ',null,23000,0,5,6)
INSERT INTO [HoaDon] VALUES (131000, '2024-05-30', N'10 Đường Nguyễn Văn Trỗi, Phường 12, Quận Phú Nhuận, Thành phố Hồ Chí Minh', null, 28000, 1, 2, 3);
INSERT INTO [HoaDon] VALUES (92000, '2024-05-28', N'45 Đường Lý Tự Trọng, Phường Bến Nghé, Quận 1, Thành phố Hồ Chí Minh', null, 32000, 1, 3, 4);
INSERT INTO [HoaDon] VALUES (81000, '2024-05-31', N'123 Đường 2/9, Phường Hòa Cường Nam, Quận Hải Châu, Thành phố Đà Nẵng', null, 14000, 0, 4, 2);
INSERT INTO [HoaDon] VALUES (91000, '2024-05-29', N'78 Đường Nguyễn Huệ, Phường 1, Thành phố Tân An, Tỉnh Long An', null, 27000, 1, 5, 1);
INSERT INTO [HoaDon] VALUES (86000, '2024-05-30', N'90 Đường Hùng Vương, Phường 2, Thành phố Vĩnh Long, Tỉnh Vĩnh Long', null, 21000, 0, 2, 3);
INSERT INTO [HoaDon] VALUES (121000, '2024-05-31', N'456 Đường Phan Châu Trinh, Phường Hải Châu 1, Quận Hải Châu, Thành phố Đà Nẵng', null, 33000, 1, 3, 2);
INSERT INTO [HoaDon] VALUES (114000, '2024-05-29', N'678 Đường Nguyễn Trãi, Phường 8, Quận 5, Thành phố Hồ Chí Minh', null, 29000, 0, 4, 4);
INSERT INTO [HoaDon] VALUES (96000, '2024-05-30', N'789 Đường Lê Lợi, Phường 4, Quận Gò Vấp, Thành phố Hồ Chí Minh', null, 26000, 1, 2, 1);
INSERT INTO [HoaDon] VALUES (99000, '2024-05-31', N'32 Đường Trường Chinh, Phường An Khê, Quận Thanh Khê, Thành phố Đà Nẵng', null, 24000, 0, 3, 5);
INSERT INTO [HoaDon] VALUES (104000, '2024-05-30', N'54 Đường 3 Tháng 2, Phường Hưng Lợi, Quận Ninh Kiều, Thành phố Cần Thơ', null, 17000, 1, 5, 3);
INSERT INTO [HoaDon] VALUES (64000,'2024-05-30',N'678 Đường Nguyễn Huệ, Phường 1, Thành phố Tân An, Tỉnh Long An',null,22000,1,2,1)
--

SELECT * FROM [HoaDon] 
GO
--Trạng Thái Hóa Đơn
--[tenTrangThai] nvarchar(255)
INSERT INTO [TrangThaiHoaDon] VALUES (N'Đã đặt hàng')
INSERT INTO [TrangThaiHoaDon] VALUES (N'Đã xác nhận')
INSERT INTO [TrangThaiHoaDon] VALUES (N'Đang xử lý ')
INSERT INTO [TrangThaiHoaDon] VALUES (N'Đang vận chuyển')	
INSERT INTO [TrangThaiHoaDon] VALUES (N'Giao thành công')
INSERT INTO [TrangThaiHoaDon] VALUES (N'Đã Hủy')
--
SELECT * FROM [TrangThaiHoaDon]

GO  
--Chi Tiếc Hóa Đơn
--[soLuong] int, [maHoaDon] int,  [maSanPham] int
	INSERT INTO [ChiTietHoaDon] VALUES (1,1,3)
	INSERT INTO [ChiTietHoaDon] VALUES (4,2,2)
	INSERT INTO [ChiTietHoaDon] VALUES (3,3,1)
	INSERT INTO [ChiTietHoaDon] VALUES (2,1,5)
	INSERT INTO [ChiTietHoaDon] VALUES (6,1,8)
	INSERT INTO [ChiTietHoaDon] VALUES (3,1,9)
	INSERT INTO [ChiTietHoaDon] VALUES (2,1,13)
	INSERT INTO [ChiTietHoaDon] VALUES (10, 5, 7);
	INSERT INTO [ChiTietHoaDon] VALUES (6, 2, 9);
	INSERT INTO [ChiTietHoaDon] VALUES (8, 3, 4);
	INSERT INTO [ChiTietHoaDon] VALUES (12, 8, 6);
	INSERT INTO [ChiTietHoaDon] VALUES (15, 4, 2);
	INSERT INTO [ChiTietHoaDon] VALUES (11, 6, 5);
	INSERT INTO [ChiTietHoaDon] VALUES (13, 7, 10);
	INSERT INTO [ChiTietHoaDon] VALUES (7, 3, 6);
	INSERT INTO [ChiTietHoaDon] VALUES (9, 4, 8);
	INSERT INTO [ChiTietHoaDon] VALUES (14, 2, 10);
	INSERT INTO [ChiTietHoaDon] VALUES (16, 5, 12);
	INSERT INTO [ChiTietHoaDon] VALUES (18, 6, 7);
	INSERT INTO [ChiTietHoaDon] VALUES (20, 3, 9);
	INSERT INTO [ChiTietHoaDon] VALUES (22, 4, 11);
	INSERT INTO [ChiTietHoaDon] VALUES (3, 2, 1);
	INSERT INTO [ChiTietHoaDon] VALUES (5, 5, 3);
	INSERT INTO [ChiTietHoaDon] VALUES (2, 21, 3);


--
SELECT * FROM [ChiTietHoaDon]
GO

--Địa Chỉ
 --[soDienThoai] nvarchar(10), [trangThai] bit,  [diaChi] nvarchar,  [maTaiKhoan] int
INSERT INTO [DiaChi] VALUES ('0290294308','Trần Tô Phước Triều',1,N'68 Đ.Trần Hoàng Na, Hưng Lợi, Ninh Kiều, Cần Thơ',3)
INSERT INTO [DiaChi] VALUES ('0290294308','Trần Triều',0,N'152 Phòng 6 Đ.3 Tháng 2, Xuân Khánh, Ninh Kiều, Cần Thơ',3)
INSERT INTO [DiaChi] VALUES ('0772310934','Đặng Duy Quang',1,N'15B Đ.Trần Hoàng Na, Hưng Lợi, Ninh Kiều, Cần Thơ',4)
INSERT INTO [DiaChi] VALUES ('0772310934','Đặng Quang',0,N'2b Tầm Vu, Hưng Lợi, Ninh Kiều, Cần Thơ',4)
INSERT INTO [DiaChi] VALUES ('0912499034','Nguyễn Minh Triệu',1,N'409 Đ. 30 Tháng 4, Hưng Lợi, Ninh Kiều, Cần Thơ',5)
INSERT INTO [DiaChi] VALUES ('0912499034','Nguyễn Triệu',0,N'1 Đ. Nguyễn Văn Linh, Hưng Lợi, Ninh Kiều, Cần Thơ',5)
--
SELECT * FROM [DiaChi]
GO

--Giao Dịch
--[codeGiaoDich] nvarchar(255),
--[soTaiKhoan] int,
--[hoVaTen] nvarchar(255),
--[ngayGiaoDich] date,
--[trangThai] bit,
--[soTien] float,
--[maTaiKhoan] int,
--[maHoaDon] int
INSERT INTO [GiaoDich] VALUES ('')
--
SELECT * FROM [GiaoDich]
--
GO
ALTER TABLE [ChiTietHoaDon] ADD FOREIGN KEY ([maHoaDon]) REFERENCES [HoaDon] ([maHoaDon])
GO
ALTER TABLE [ChiTietHoaDon] ADD FOREIGN KEY ([maSanPham]) REFERENCES [SanPham] ([maSanPham])
GO
ALTER TABLE [HoaDon] ADD FOREIGN KEY ([maTaiKhoan]) REFERENCES [TaiKhoan] ([maTaiKhoan])
GO
ALTER TABLE [ChiTietGioHang] ADD FOREIGN KEY ([maTaiKhoan]) REFERENCES [TaiKhoan] ([maTaiKhoan])
GO
ALTER TABLE [ChiTietGioHang] ADD FOREIGN KEY ([maSanPham]) REFERENCES [SanPham] ([maSanPham])
GO
ALTER TABLE [SanPham] ADD FOREIGN KEY ([maLoai]) REFERENCES [LoaiSP] ([maLoai])
GO
ALTER TABLE [DanhGia] ADD FOREIGN KEY ([maSanPham]) REFERENCES [SanPham] ([maSanPham])
GO
ALTER TABLE [DanhGia] ADD FOREIGN KEY ([maTaiKhoan]) REFERENCES [TaiKhoan] ([maTaiKhoan])
GO
ALTER TABLE [DiaChi] ADD FOREIGN KEY ([maTaiKhoan]) REFERENCES [TaiKhoan] ([maTaiKhoan])
GO
ALTER TABLE [HoaDon] ADD FOREIGN KEY ([maTrangThaiHoaDon]) REFERENCES [TrangThaiHoaDon] ([maTrangThaiHoaDon])
GO
ALTER TABLE [HinhAnhSP] ADD FOREIGN KEY ([maSanPham]) REFERENCES [SanPham] ([maSanPham])
GO
ALTER TABLE [HinhAnhDG] ADD FOREIGN KEY ([maDanhGia]) REFERENCES [DanhGia] ([maDanhGia])
GO
ALTER TABLE [GiaoDich] ADD FOREIGN KEY ([maTaiKhoan]) REFERENCES [TaiKhoan] ([maTaiKhoan])
GO
ALTER TABLE [GiaoDich] ADD FOREIGN KEY ([maHoaDon]) REFERENCES [HoaDon] ([maHoaDon])
GO
select * from HoaDon hd
JOIN TaiKhoan tk on tk.maTaiKhoan = hd.maTaiKhoan
where tk.maTaiKhoan = 2


SELECT sp.tenSanPham, cthd.soLuong, tthd.tenTrangThai, (hd.tongTien + hd.phiVanChuyen) AS tongTienVaPhiVanChuyen
FROM HoaDon hd
Inner JOIN TaiKhoan tk ON tk.maTaiKhoan = hd.maTaiKhoan
Inner JOIN TrangThaiHoaDon tthd ON hd.maTrangThaiHoaDon = tthd.maTrangThaiHoaDon
Inner JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon
Inner JOIN SanPham sp ON sp.maSanPham = cthd.maSanPham
WHERE tk.maTaiKhoan = 2		
AND tthd.tenTrangThai = N'Đã đặt hàng'
select * from ChiTietHoaDon

SELECT hd.maHoaDon, COUNT(cthd.maSanPham) AS tongSanPham , dc.diaChi, tthd.tenTrangThai, hd.tongTien + hd.phiVanChuyen
FROM HoaDon hd
INNER JOIN TrangThaiHoaDon tthd ON hd.maTrangThaiHoaDon = tthd.maTrangThaiHoaDon
INNER JOIN TaiKhoan tk ON tk.maTaiKhoan = hd.maTaiKhoan
INNER JOIN DiaChi dc ON dc.maTaiKhoan = tk.maTaiKhoan
INNER JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon
INNER JOIN SanPham sp ON sp.maSanPham = cthd.maSanPham
WHERE tk.maTaiKhoan = 2
AND tthd.tenTrangThai = N'Đã hủy'
GROUP BY hd.maHoaDon, dc.diaChi, tthd.tenTrangThai, hd.tongTien + hd.phiVanChuyen

SELECT hd.maHoaDon, sp.hinhAnh, sp.tenSanPham, cthd.soLuong, cthd.soLuong * sp.gia AS tongTien
FROM HoaDon hd
INNER JOIN TaiKhoan tk ON tk.maTaiKhoan = hd.maTaiKhoan
INNER JOIN ChiTietHoaDon cthd ON cthd.maHoaDon = hd.maHoaDon
INNER JOIN TrangThaiHoaDon tthd ON hd.maTrangThaiHoaDon = tthd.maTrangThaiHoaDon
INNER JOIN SanPham sp ON sp.maSanPham = cthd.maSanPham
WHERE tk.maTaiKhoan = 2
AND tthd.tenTrangThai = N'Đã hủy'
AND hd.maHoaDon = 2
