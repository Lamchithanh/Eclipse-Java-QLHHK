CREATE DATABASE QuanLyChuyenBay;
USE QuanLyChuyenBay;

CREATE TABLE UserAccount (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE SanBay (
    MaSanBay VARCHAR(10) PRIMARY KEY,
    TenSanBay VARCHAR(100) NOT NULL
);

CREATE TABLE HangHangKhong (
    MaHang VARCHAR(10) PRIMARY KEY,
    TenHang VARCHAR(100) NOT NULL,
    DiaChi VARCHAR(200),
    SoDienThoai VARCHAR(15),
    Email VARCHAR(100)
);

CREATE TABLE ChuyenBay (
    MaChuyenBay VARCHAR(10) PRIMARY KEY,
    MaSanBay VARCHAR(10) NOT NULL,
    MaHang VARCHAR(10) NOT NULL,
    MaMaybay VARCHAR(10) NOT NULL,
    DiemDi VARCHAR(100) NOT NULL,
    DiemDen VARCHAR(100) NOT NULL,
    ChangBay VARCHAR(100) NOT NULL,
    NgayBay DATE NOT NULL,
    NhaGa VARCHAR(5) NOT NULL,
    SoGhe INT NOT NULL CHECK (SoGhe > 0),
    Quay INT NOT NULL,
    GiaVe DECIMAL(10,2) NOT NULL,
    TinhTrang ENUM('Sắp khởi hành', 'Đã khởi hành', 'Delay', 'Đã hủy') NOT NULL,
    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (MaSanBay) REFERENCES SanBay(MaSanBay),
    FOREIGN KEY (MaHang) REFERENCES HangHangKhong(MaHang)
);

CREATE TABLE KhachHang (
    MaKhachHang VARCHAR(10) PRIMARY KEY,
    TenKhachHang VARCHAR(100) NOT NULL,
    CMND VARCHAR(12) NOT NULL UNIQUE,
    GioiTinh ENUM('Nam', 'Nữ') NOT NULL,
    NgaySinh DATE NOT NULL,
    SoDienThoai VARCHAR(15),
    Email VARCHAR(100),
    DiaChi VARCHAR(200),
    QuocTich VARCHAR(50),
    TenNguoiLienHe VARCHAR(100),
    SoDienThoaiNguoiLienHe VARCHAR(15),
    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE DatVe (
    MaDatVe VARCHAR(10) PRIMARY KEY,
    MaKhachHang VARCHAR(10) NOT NULL,
    MaChuyenBay VARCHAR(10) NOT NULL,
    NgayDat DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayBay DATE NOT NULL,
    HangVe ENUM('Phổ thông', 'Thương gia', 'Hạng nhất') DEFAULT 'Phổ thông',
    SoLuong INT NOT NULL,
    TongGia DECIMAL(10,2) NOT NULL,
    TrangThai ENUM('Đã đặt', 'Đã thanh toán', 'Đã hủy') DEFAULT 'Đã đặt',
    PhuongThucThanhToan ENUM('Tiền mặt', 'Chuyển khoản', 'Thẻ'),
    MaGiamGia VARCHAR(20),
    XacNhanThanhToan BOOLEAN DEFAULT FALSE,
    GhiChu TEXT,
    NgayCapNhat DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang),
    FOREIGN KEY (MaChuyenBay) REFERENCES ChuyenBay(MaChuyenBay)
);

CREATE TABLE VeMayBay (
    MaVe VARCHAR(10) PRIMARY KEY,
    MaChuyenBay VARCHAR(10) NOT NULL,
    MaKhachHang VARCHAR(10) NOT NULL,
    NgayDat DATETIME NOT NULL,
    GiaVe DECIMAL(10,2) NOT NULL,
    HangVe ENUM('Phổ thông', 'Thương gia', 'Hạng nhất') DEFAULT 'Phổ thông',
    SoGhe VARCHAR(10),
    TrangThai ENUM('Đã đặt', 'Đã thanh toán', 'Đã hủy') DEFAULT 'Đã đặt',
    MaGiamGia VARCHAR(20),
    XacNhanThanhToan BOOLEAN DEFAULT FALSE,
    NgayCapNhat DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (MaChuyenBay) REFERENCES ChuyenBay(MaChuyenBay),
    FOREIGN KEY (MaKhachHang) REFERENCES KhachHang(MaKhachHang)
);

CREATE TABLE YeuCauDacBiet (
    MaVe VARCHAR(10) NOT NULL,
    SuatAnDacBiet BOOLEAN DEFAULT FALSE,
    HoTroYTe BOOLEAN DEFAULT FALSE,
    ChoNgoiUuTien BOOLEAN DEFAULT FALSE,
    HanhLyDacBiet BOOLEAN DEFAULT FALSE,
    GhiChu TEXT,
    NgayTao DATETIME DEFAULT CURRENT_TIMESTAMP,
    NgayCapNhat DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (MaVe) REFERENCES VeMayBay(MaVe)
);

CREATE TABLE LichSuThanhToan (
    MaLichSu INT AUTO_INCREMENT PRIMARY KEY,
    MaVe VARCHAR(10) NOT NULL,
    NgayThanhToan DATETIME NOT NULL,
    SoTien DECIMAL(10,2) NOT NULL,
    PhuongThucThanhToan ENUM('Tiền mặt', 'Chuyển khoản', 'Thẻ') NOT NULL,
    FOREIGN KEY (MaVe) REFERENCES VeMayBay(MaVe)
);


SELECT * FROM quanlychuyenbay.chuyenbay;

SET SQL_SAFE_UPDATES = 0;

DELETE FROM ChuyenBay;

SET SQL_SAFE_UPDATES = 1;

UPDATE ChuyenBay
SET NgayBay = STR_TO_DATE(NgayBay, '%d-%m-%Y');

ALTER TABLE ChuyenBay
MODIFY COLUMN NgayBay DATE;
