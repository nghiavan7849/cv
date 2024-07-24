CREATE DATABASE QuanLyCafe
GO
USE QuanLyCafe
GO

CREATE TABLE SanPham(
	MaSP NVARCHAR(5) PRIMARY KEY NOT NULL,
	TenSP NVARCHAR(50) NOT NULL,
	LoaiSP NVARCHAR(250) NOT NULL,
	Gia Float NOT NULL,
	HinhAnh NVARCHAR(50) NOT NULL,
	GioiThieu NVARCHAR(250) NULL
)
GO
CREATE TABLE DonVi(
	MaDV NVARCHAR(5) NOT NULL PRIMARY KEY,
	TenDV NVARCHAR(250) NOT NULL,
	DiaChi NVARCHAR(250) NOT NULL,
	SDT NVARCHAR(10) NOT NULL,
	Email NVARCHAR (250) NOT NULL,
	Website NVARCHAR (250) NOT NULL
)
GO

CREATE TABLE NhanVien(
	MaNV NVARCHAR(50) NOT NULL PRIMARY KEY,
	TenNV NVARCHAR(50) NOT NULL,
	MatKhau NVARCHAR(50) NOT NULL,
	DiaChi NVARCHAR(250) NULL,
	SDT NVARCHAR(10) NULL,
	Email NVARCHAR(250) NOT NULL,
	GioiTinh BIT NOT NULL,
	ChucVu BIT NOT NULL,
	MaDV NVARCHAR(5) NOT NULL
	CONSTRAINT FK_DV_NV FOREIGN KEY (MaDV) REFERENCES DonVi(MaDV)
)
GO

CREATE TABLE PhanCong (
	MaPC NVARCHAR(5) PRIMARY KEY NOT NULL,
	NgayLam DATE NOT NULL,
	TenCa INT NOT NULL,
	GioBatDau TIME NOT NULL,
	GioKetThuc TIME NOT NULL,
	GhiChu NVARCHAR(255) NOT NULL,
	MaNV NVARCHAR(50) NOT NULL,
	CONSTRAINT FK_PC_NV FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV)
)
GO

CREATE TABLE KhachHang(
	MaKH NVARCHAR(5) NOT NULL PRIMARY KEY,
	TenKH NVARCHAR(50) NOT NULL,	
	Email NVARCHAR(250) NULL,
	SDT NVARCHAR(10) NULL,
	GioiTinh BIT NOT NULL,
	DiaChi NVARCHAR(250) NULL,
)
GO

CREATE TABLE KhuVuc(
	MaKV NVARCHAR(5) NOT NULL PRIMARY KEY,
	TenKV NVARCHAR(50) NOT NULL,
	MoTa NVARCHAR(250) NULL,
	MaDV NVARCHAR(5) NOT NULL,
	CONSTRAINT FK_KV_DV FOREIGN KEY (MaDV) REFERENCES DonVi(MaDV) 
)
GO

CREATE TABLE Ban(
	MaBan NVARCHAR(5) NOT NULL PRIMARY KEY,
	TenBan NVARCHAR(50) NOT NULL ,
	TrangThai NVARCHAR(50) Default N'Trống',
	MaKV NVARCHAR(5) NOT NULL,
	CONSTRAINT FK_Ban_KV FOREIGN KEY (MaKV) REFERENCES KhuVuc(MaKV) ON DELETE CASCADE
)
GO

CREATE TABLE HoaDon(
	MaHD INT IDENTITY NOT NULL PRIMARY KEY,
	NgayDatBan DATETIME2 NULL,
	NgayThanhToan DATE NULL,
	ThoiGianTaoHD TIME NULL,
	ThoiGianThanhToan TIME NULL,
	TongTien INT NOT NULL,
	TrangThai BIT NOT NULL,
	MaNV NVARCHAR(50) NOT NULL,
	MaBan NVARCHAR(5) NOT NULL,
	MaKH NVARCHAR(5) NULL,
	CONSTRAINT FK_NV_HD FOREIGN KEY (MaNV) REFERENCES NhanVien(MaNV),
	CONSTRAINT FK_Ban_HD FOREIGN KEY (MaBan) REFERENCES Ban(MaBan),
	CONSTRAINT FK_KH_HD FOREIGN KEY (MaKH) REFERENCES KhachHang(MaKH)
)

GO 
CREATE TABLE ChiTietHoaDon(
	MaCTHD INT IDENTITY NOT NULL PRIMARY KEY,
	MaHD INT NOT NULL,
	MaSP NVARCHAR(5) NOT NULL,
	SoLuong INT NOT NULL,
	TongTienSP FLOAT NOT NULL,
	GhiChu NVARCHAR(255) NULL,
	CONSTRAINT FK_HD_CTHD FOREIGN KEY (MaHD) REFERENCES HoaDon(MaHD),
	CONSTRAINT FK_SP_CTHD FOREIGN KEY (MaSP) REFERENCES SanPham(MaSP)
)
GO

--INSERT DỮ LIỆU ĐƠN VỊ
INSERT INTO DonVi VALUES ('DV01',N'Brother 1',N'288 Thường Thạnh, Cái Răng, Cần Thơ','0336179835','qvinh8958@gmail.com','BrotherCoffee.vn')
INSERT INTO DonVi VALUES ('DV02',N'Brother 2',N'xã Phong Mỹ, Cao Lãnh, Đồng Tháp','0631788492','vnghia808@gmail.com','BrotherCoffee.vn')
INSERT INTO DonVi VALUES ('DV03',N'Brother 3',N'Đông Hải, Bạc Liêu','0336619738','vphat66@gmail.com','BrotherCoffee.vn')
SELECT b.* FROM Ban b INNER JOIN KhuVuc kv ON b.MaKV = kv.MaKV WHERE b.Tenban LIKE 'Bàn 1' AND kv.MaDV LIKE 'DV02'
SELECT B.* FROM Ban B INNER JOIN KhuVuc K ON B.MaKV = K.MaKV WHERE B.MaKV LIKE 'KV01'	
select * from DonVi where MaDV = 'DV04'
UPDATE DonVi SET TenDV = N'Brother 3', DiaChi = N'Đông Hải, Bạc Liêu', SDT = '0336619738', Email = 'vphat66@gmail.com', Website = 'BrotherCoffee.vn' WHERE MaDV = 'DV03'
/*NhanVien
SanPham
KhachHang
KhuVuc
Ban
HoaDon
ChiTietHoaDon*/
--INSERT DỮ LIỆU NHÂN VIÊN
INSERT INTO NhanVien VALUES (N'phatdv',N'Đinh Văn Phát','e10adc3949ba59abbe56e057f20f883e',N'Hậu Giang','0702819466',N'dinhvanphat9404@gmail.com',0,1,'DV01')
INSERT INTO NhanVien VALUES (N'nghiatv',N'Trần Văn Nghĩa','e10adc3949ba59abbe56e057f20f883e',N'Đồng Tháp','0798022014',N'tranvannghia@gmail.com',0,1,'DV02')
INSERT INTO NhanVien VALUES (N'vinhvtt',N'Võ Trần Thế Vinh','e10adc3949ba59abbe56e057f20f883e',N'An Giang','0798692214',N'votranthevinh@gmail.com',0,0,'DV03')
INSERT INTO NhanVien VALUES (N'trieuttp',N'Trần Tô Phước Triều','e10adc3949ba59abbe56e057f20f883e',N'Bạc Liêu','0798184930',N'trantophuoctrieu@gmail.com',0,0,'DV03')
INSERT INTO NhanVien VALUES (N'lylc',N'Lê Cẩm Ly','e10adc3949ba59abbe56e057f20f883e',N'Cần Thơ','0798336971',N'nguyenhoangkhang@gmail.com',1,0,'DV02')
INSERT INTO NhanVien VALUES (N'kietlt',N'Lê Tuấn Kiệt','e10adc3949ba59abbe56e057f20f883e',N'Bạc Liêu','0759712204',N'letuankiet@gmail.com',0,0,'DV03')

SELECT * FROM NhanVien WHERE MaNV LIKE N'nghia%'
GO
/*INSERT INTO NhanVien (MaNV, TenNV, MatKhau, QueQuan, SDT, Email, GioiTinh, ChucVu) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
UPDATE NhanVien SET TenNV = ?, MatKhau = ?, QueQuan =?, SDT = ?, Email = ?, GioiTinh = ?, ChucVu = ? WHERE MaNV = ?
DELETE FROM NhanVien WHERE MaNV = ?
SELECT * FROM NhanVien WHERE MaNV = ?
*/
GO

--INSERT DỮ LIỆU SẢN PHẨM
INSERT INTO SanPham VALUES (N'SP01',N'Cafe Capuchino',N'Cà phê',20000,'cappucino.png',N'Capuchino là cà phê gồm có cà phê và lớp bọt sữa')
INSERT INTO SanPham VALUES (N'SP02',N'Cafe Sữa',N'Cà phê',15000,'cafesua.png',N'Cà phê sữa là loại cà phê có hai thành phần chính cà phê và sữa đặc')
INSERT INTO SanPham VALUES (N'SP03',N'Cafe Đen Đá',N'Cà phê',12000,'cafe.png',N'Cà phê đen là một thức uống cà phê đơn giản, không cầu kỳ')
INSERT INTO SanPham VALUES (N'SP04',N'Cafe Sữa Nóng',N'Cà phê',15000,'Cafesuanong.png',N'Cà phê sữa nóng không có đá')
INSERT INTO SanPham VALUES (N'SP05',N'Cafe Muối',N'Cà phê',20000,'cafemuoi.png',N'Cà phê muối là thức uống kết hợp từ cà phê, sữa tươi lên men, sữa đặc và muối')
INSERT INTO SanPham VALUES (N'SP06',N'Nước Suối',N'Nước chai',8000,'nuocsuoi.png',N'Nước suối Aquafina')
INSERT INTO SanPham VALUES (N'SP07',N'CocaCola',N'Nước chai',15000,'Coca.png',N'Nước uống có gas CocaCola')
INSERT INTO SanPham VALUES (N'SP08',N'Sprite',N'Nước chai',15000,'Sprite.png',N'Nước uống có gas Sprite')
INSERT INTO SanPham VALUES (N'SP09',N'Fanta',N'Nước chai',15000,'Fanta.png',N'Nước uống có gas Fanta')
INSERT INTO SanPham VALUES (N'SP10',N'Trà Đào',N'Trà',20000,'tradao.png',N'Nước uống giải khác trái cây trà đào')
INSERT INTO SanPham VALUES (N'SP11',N'Sữa Chua Dâu',N'Sữa chua',25000,'yogurt-dau.png',N'Sữa chua kèm kem vị Dâu')
INSERT INTO SanPham VALUES (N'SP12',N'Sữa Chua Kiwi',N'Sữa chua',25000,'yogurt-kiwi.png',N'Sữa chua kèm kem vị Kiwi')
INSERT INTO SanPham VALUES (N'SP13',N'Trà Vải',N'Trà',20000,'travai.png',N'Trà có vị vải')
INSERT INTO SanPham VALUES (N'SP14',N'Trà Hạt Chia',N'Trà',15000,'trahatchia.png',N'Hạt chia là loại hạt có thể ăn được, thuộc họ bạc hà')
INSERT INTO SanPham VALUES (N'SP15',N'Trà Dâu',N'Trà',25000,'tradau.png',N'Trà dâu là một loại trà dược liệu hữu cơ được ủ bằng lá dâu tây')
INSERT INTO SanPham VALUES (N'SP16',N'Sinh Tố Bơ',N'Sinh tố',20000,'sinh-to-bo.png',N'Sinh tố bơ')
INSERT INTO SanPham VALUES (N'SP17',N'Nước Ép Thơm',N'Nước ép',20000,'nuoc-ep-dua.png',N'Nước ép từ trái thơm')
INSERT INTO SanPham VALUES (N'SP18',N'Nước Ép Dưa Hấu',N'Nước ép',20000,'nuoc-ep-dua-hau.png',N'Nước ép từ trái dưa hấu')
INSERT INTO SanPham VALUES (N'SP19',N'Nước Ép Cam',N'Nước ép',20000,'nuoc-ep-cam.png',N'Nước ép từ trái cam')
INSERT INTO SanPham VALUES (N'SP20',N'Trà Sữa',N'Trà',20000,'tra-sua.png',N'Trà sữa là 1 loại thức uống kết hợp từ 2 nguyên liệu chính là trà và sữa')

SELECT * FROM SanPham
SELECT * FROM SanPham WHERE LoaiSP like N'Cà phê'
GO
/*INSERT INTO SanPham (MaSP, TenSP, LoaiSP, Gia, HinhAnh, GioiThieu) VALUES (?, ?, ?, ?, ?, ?)
UPDATE SanPham SET TenSP = ?, LoaiSP = ?, Gia = ?, HinhAnh = ?, GioiThieu = ? WHERE MaSP = ?
DELETE FROM SanPham WHERE MaSP = ?
SELECT * FROM SanPham WHERE MaSP = ?
*/
GO
SELECT * FROM KhachHang
INSERT INTO KhachHang VALUES (N'KH01',N'Nguyễn Vũ Đầy','daynvpc01584@gmail.com','0798512905',0,N'Ninh Kiều, Cần Thơ')
INSERT INTO KhachHang VALUES (N'KH02',N'Đặng Vĩnh Kỳ','kydvpc059842@gmail.com','0790148705',0,N'Ninh Kiều, Cần Thơ')
INSERT INTO KhachHang VALUES (N'KH03',N'Nguyễn Minh Khanh','khanhnmpc05427@gmail.com','0697812511',0,N'Cái Răng, Cần Thơ')
INSERT INTO KhachHang VALUES (N'KH04',N'Trần Thị Trúc Ly','lytttpc08512@gmail.com','0920549800',1,N'Thốt Nốt,Cần Thơ')
INSERT INTO KhachHang VALUES (N'KH05',N'Nguyễn Nhã Phương','phuongnnpc05871@gmail.com','0903971102',1,N'Ninh Kiều, Cần Thơ')

/*INSERT INTO KhachHang (MaKH, TenKH, Email, SDT, GioiTinh) VALUES (?, ?, ?, ?, ?)
UPDATE KhachHang SET TenKH = ?, Email = ?, SDT = ?, GioiTinh = ? WHERE MaKH = ?
DELETE FROM KhachHang WHERE MaKH = ?
SELECT * FROM KhachHang WHERE MaKH = ?
*/
GO
GO
INSERT INTO KhuVuc VALUES (N'KV01',N'Tầng trệt',null,'DV01')
INSERT INTO KhuVuc VALUES (N'KV02',N'Tầng 1',null,'DV01')
INSERT INTO KhuVuc VALUES (N'KV03',N'Tầng trệt',null,'DV02')
select * from KhuVuc
/*INSERT INTO KhuVuc (MaKV, TenKV, MoTa) VALUES (?, ?, ?)
UPDATE KhuVuc SET TenKV = ?, MoTa = ?? WHERE MaKV = ?
DELETE FROM KhuVuc WHERE MaKV = ?
SELECT * FROM KhuVuc WHERE MaKV = ?
*/

--INSERT DỮ LIỆU BÀN
INSERT INTO Ban VALUES (N'B01',N'Bàn 1',N'Trống',N'KV01')
INSERT INTO Ban VALUES (N'B02',N'Bàn 2',N'Trống',N'KV01')
INSERT INTO Ban VALUES (N'B03',N'Bàn 3',N'Có khách',N'KV01')
INSERT INTO Ban VALUES (N'B04',N'Bàn 4',N'Trống',N'KV01')
INSERT INTO Ban VALUES (N'B05',N'Bàn 5',N'Trống',N'KV01')
INSERT INTO Ban VALUES (N'B06',N'Bàn 6',N'Có khách',N'KV01')
INSERT INTO Ban VALUES (N'B07',N'Bàn 7',N'Trống',N'KV01')
INSERT INTO Ban VALUES (N'B08',N'Bàn 8',N'Trống',N'KV01')
INSERT INTO Ban VALUES (N'B09',N'Bàn 9',N'Trống',N'KV01')
INSERT INTO Ban VALUES (N'B10',N'Bàn 10',N'Trống',N'KV01')
INSERT INTO Ban VALUES (N'B11',N'Bàn 11',N'Trống',N'KV02')
INSERT INTO Ban VALUES (N'B12',N'Bàn 12',N'Trống',N'KV02')
INSERT INTO Ban VALUES (N'B13',N'Bàn 13',N'Có khách',N'KV02')
INSERT INTO Ban VALUES (N'B14',N'Bàn 14',N'Trống',N'KV02')
INSERT INTO Ban VALUES (N'B15',N'Bàn 15',N'Trống',N'KV02')
INSERT INTO Ban VALUES (N'B16',N'Bàn 16',N'Trống',N'KV02')
INSERT INTO Ban VALUES (N'B17',N'Bàn 17',N'Có khách',N'KV02')
INSERT INTO Ban VALUES (N'B18',N'Bàn 18',N'Trống',N'KV02')
INSERT INTO Ban VALUES (N'B19',N'Bàn 19',N'Trống',N'KV02')
INSERT INTO Ban VALUES (N'B20',N'Bàn 20',N'Trống',N'KV02')
INSERT INTO Ban VALUES (N'B21',N'Bàn 1',N'Trống',N'KV03')
INSERT INTO Ban VALUES (N'B22',N'Bàn 2',N'Trống',N'KV03')
INSERT INTO Ban VALUES (N'B23',N'Bàn 3',N'Có khách',N'KV03')
INSERT INTO Ban VALUES (N'B24',N'Bàn 4',N'Trống',N'KV03')
INSERT INTO Ban VALUES (N'B25',N'Bàn 1',N'Trống',N'KV03')
INSERT INTO Ban VALUES (N'B26',N'Bàn 2',N'Trống',N'KV03')
INSERT INTO Ban VALUES (N'B27',N'Bàn 3',N'Có khách',N'KV03')
INSERT INTO Ban VALUES (N'B28',N'Bàn 4',N'Trống',N'KV03')
SELECT * FROM Ban
GO

/*INSERT INTO Ban (MaBan, TenBan, TrangThai, MaKV) VALUES (?, ?, ?, ?)
UPDATE Ban SET TenBan = ?, TrangThai = ? MaKV = ? WHERE MaBan = ?
DELETE FROM Ban WHERE MaBan = ?
SELECT * FROM Ban WHERE MaBan = ?
*/

GO

--INSERT DỮ LIỆU HÓA ĐƠN
INSERT INTO HoaDon VALUES (null,'2023-10-13','7:12:33','8:50:12',52000,1,'phatdv','B05','KH01')
INSERT INTO HoaDon VALUES (null,'2023-10-13','7:20:01','8:00:57',100000,1,'nghiatv','B11','KH03')
INSERT INTO HoaDon VALUES (null,'2023-10-13','8:51:56','10:12:12',40000,1,'nghiatv','B09','KH02')
INSERT INTO HoaDon VALUES (null,'2023-10-14','8:20:01','9:15:38',40000,1,'phatdv','B03','KH01')
INSERT INTO HoaDon VALUES ('2023-10-12 20:10:01','2023-10-14','11:40:28','12:50:12',32000,1,'trieuttp','B08','KH03')
INSERT INTO HoaDon VALUES (null,null,'18:20:33',null,30000,0,'vinhvtt','B06','KH03')
INSERT INTO HoaDon VALUES (null,null,'19:05:33',null,20000,0,'kietlt','B03','KH01')
INSERT INTO HoaDon VALUES (null,null,'8:10:33',null,95000,0,'lylc','B13','KH04')
INSERT INTO HoaDon VALUES (null,null,'9:35:19',null,40000,0,'trieuttp','B17','KH05')

SELECT * FROM HoaDon

/*INSERT INTO HoaDon (MaHD, NgayDatBan, NgayThanhToan, ThoiGianTaoHD, ThoiGianThanhToan, TongTien, TrangThai VALUES (?, ?, ?, ?, ?, ?, ?)
UPDATE HoaDon SET NgayDatBan = ?, NgayThanhToan = ?, ThoiGianTaoHD = ?, ThoiGianThanhToan = ?, TongTien = ?, TrangThai = ? WHERE MaHD = ?
DELETE FROM HoaDon WHERE MaHD = ?
SELECT * FROM HoaDon WHERE MaHD = ?
*/
GO 
INSERT INTO ChiTietHoaDon VALUES (1,'SP01',2,40000,null) 
INSERT INTO ChiTietHoaDon VALUES (1,'SP03',1,12000,N'Ngọt')
INSERT INTO ChiTietHoaDon VALUES (2,'SP20',3,60000,null)
INSERT INTO ChiTietHoaDon VALUES (2,'SP19',2,40000,null)
INSERT INTO ChiTietHoaDon VALUES (3,'SP11',2,40000,null)
INSERT INTO ChiTietHoaDon VALUES (4,'SP20',2,40000,null)
INSERT INTO ChiTietHoaDon VALUES (5,'SP05',1,12000,null)
INSERT INTO ChiTietHoaDon VALUES (5,'SP03',1,20000,null)
INSERT INTO ChiTietHoaDon VALUES (6,'SP02',2,30000,N'Ít sữa')
INSERT INTO ChiTietHoaDon VALUES (7,'SP01',1,20000,null)
INSERT INTO ChiTietHoaDon VALUES (8,'SP09',3,45000,null)
INSERT INTO ChiTietHoaDon VALUES (8,'SP15',2,50000,null)
INSERT INTO ChiTietHoaDon VALUES (9,'SP10',2,40000,null)

SELECT * FROM ChiTietHoaDon
/*INSERT INTO ChiTietHoaDon (MaCTHD,MaHD,MaSP,SoLuong,TongTienSP,GhiChu) VALUES (?, ?, ?, ?, ?, ?)
UPDATE ChiTietHoaDon SET MaCTHD = ? WHERE MaHD = ?
DELETE FROM ChiTietHoaDon WHERE MaCTHD = ?
SELECT * FROM ChiTietHoaDon WHERE MaCTHD = ?
*/

-- SELECT * FROM Ban WHERE MaKV LIKE 'KV01'
-- GO
-- SELECT * FROM HoaDon WHERE MaBan LIKE 'B10'
-- SELECT cthd.* FROM ChiTietHoaDon cthd INNER JOIN HoaDon hd ON cthd.MaHD = hd.MaHD  WHERE MaBan LIKE 'B06' AND hd.TrangThai = 0


CREATE OR ALTER PROC Proc_ThongKe @NgayMin DATE = null, @NgayMax DATE = null
AS 
	BEGIN
		SELECT 
			A.MaSP, TenSP, LoaiSP, Gia, SUM(SoLuong) AS SoLuong , SUM(Gia*SoLuong) AS TongDoanhThu, NgayThanhToan	
		FROM ChiTietHoaDon A
			INNER JOIN SanPham B ON B.MaSP = A.MaSP
			INNER JOIN HoaDon C ON C.MaHD = A.MaHD
		WHERE NgayThanhToan between @NgayMin and  @NgayMax
		GROUP BY A.MaSP,TenSP, LoaiSP, Gia, NgayThanhToan
	END
GO

EXEC Proc_ThongKe  '2023-10-13' , '2023-10-14'
GO

CREATE OR ALTER PROC Proc_DoanhThuSP 
AS 
	BEGIN
		SELECT A.MaSP, TenSP, LoaiSP, Gia, SUM(SoLuong) AS SoLuong , SUM(Gia*SoLuong) AS TongDoanhThu	 
		FROM SanPham A
			FULL JOIN ChiTietHoaDon B ON B.MaSP = A.MaSP
			FULL JOIN HoaDon C ON C.MaHD = B.MaHD
		GROUP BY A.MaSP,TenSP, LoaiSP, Gia
	END
GO
EXEC Proc_DoanhThuSP
GO

CREATE OR ALTER PROC  Proc_DoanhThuSPTheoLoai @LoaiSP NVARCHAR(200) = null
AS 
	BEGIN
		SELECT A.MaSP, TenSP, LoaiSP, Gia, SUM(SoLuong) AS SoLuong , SUM(Gia*SoLuong) AS TongDoanhThu	 
		FROM SanPham A
			FULL JOIN ChiTietHoaDon B ON B.MaSP = A.MaSP
			FULL JOIN HoaDon C ON C.MaHD = B.MaHD
			WHERE LoaiSP = @LoaiSP
		GROUP BY A.MaSP,TenSP, LoaiSP, Gia
	END

EXEC Proc_DoanhThuSPTheoLoai @LoaiSP = 'Cà phê'

CREATE OR ALTER PROC Proc_HoaDon @NgayMin DATE = null, @NgayMax DATE = null
AS 
BEGIN
    SELECT 
        C.MaHD, C.NgayDatBan, C.NgayThanhToan, C.ThoiGianTaoHD, C.ThoiGianThanhToan, C.TongTien, C.TrangThai,
        C.MaNV, C.MaBan, C.MaKH
    FROM HoaDon C
    WHERE C.NgayThanhToan BETWEEN @NgayMin AND @NgayMax
END
GO
EXEC Proc_HoaDon '2023-10-13', '2023-10-14'

CREATE OR ALTER PROC Proc_SanPham @NgayMin DATE = null, @NgayMax DATE = null
AS 
	BEGIN
		SELECT A.MaSP, TenSP, LoaiSP, Gia, SUM(SoLuong) AS SoLuong , SUM(Gia*SoLuong) AS TongTien
		FROM SanPham A
			FULL JOIN ChiTietHoaDon B ON B.MaSP = A.MaSP
			FULL JOIN HoaDon C ON C.MaHD = B.MaHD
			WHERE NgayThanhToan between @NgayMin and  @NgayMax
		GROUP BY A.MaSP,TenSP, LoaiSP, Gia
	END
GO	

EXEC Proc_SanPham '2023-10-13', '2023-10-14'

CREATE OR ALTER PROC Proc_ThongKe @NgayMin DATE = null, @NgayMax DATE = null
AS 
	BEGIN
		SELECT 
			  NgayThanhToan	AS NgayThanhToan,TenSP, SUM(SoLuong) AS SoLuong , SUM(Gia*SoLuong) AS TongTien
		FROM ChiTietHoaDon A
			INNER JOIN SanPham B ON B.MaSP = A.MaSP
			INNER JOIN HoaDon C ON C.MaHD = A.MaHD
		WHERE NgayThanhToan between @NgayMin and  @NgayMax
		GROUP BY  NgayThanhToan,TenSP
		ORDER BY NgayThanhToan ASC
	END
GO

EXEC Proc_ThongKe  '2023-10-13' , '2023-10-14'
GO
