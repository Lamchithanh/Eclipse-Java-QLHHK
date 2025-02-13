CREATE DATABASE  IF NOT EXISTS `quanlychuyenbay` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `quanlychuyenbay`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: quanlychuyenbay
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chuyenbay`
--

DROP TABLE IF EXISTS `chuyenbay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chuyenbay` (
  `MaChuyenBay` varchar(10) NOT NULL,
  `MaSanBay` varchar(10) NOT NULL,
  `MaHang` varchar(10) NOT NULL,
  `ChangBay` varchar(100) NOT NULL,
  `NgayBay` date NOT NULL,
  `NhaGa` varchar(5) NOT NULL,
  `SoGhe` int NOT NULL,
  `Quay` int NOT NULL,
  `TinhTrang` enum('Sắp khởi hành','Đã khởi hành','Delay') NOT NULL,
  PRIMARY KEY (`MaChuyenBay`),
  KEY `MaSanBay` (`MaSanBay`),
  KEY `MaHang` (`MaHang`),
  CONSTRAINT `chuyenbay_ibfk_1` FOREIGN KEY (`MaSanBay`) REFERENCES `sanbay` (`MaSanBay`) ON DELETE CASCADE,
  CONSTRAINT `chuyenbay_ibfk_2` FOREIGN KEY (`MaHang`) REFERENCES `hanghangkhong` (`MaHang`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chuyenbay`
--

LOCK TABLES `chuyenbay` WRITE;
/*!40000 ALTER TABLE `chuyenbay` DISABLE KEYS */;
INSERT INTO `chuyenbay` VALUES ('CB001','SB01','HHK01','Hà Nội - TP HCM','2024-12-15','T1',180,9,'Delay'),('CB002','SB02','HHK02','TP HCM - Đà Nẵng','2024-12-16','T2',200,10,'Delay'),('CB003','SB03','HHK03','Đà Nẵng - Hà Nội','2024-12-17','T1',150,12,'Đã khởi hành'),('CB004','SB04','HHK04','Hải Phòng - Cần Thơ','2024-12-18','T3',220,8,'Sắp khởi hành');
/*!40000 ALTER TABLE `chuyenbay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chuyenbay_maybay`
--

DROP TABLE IF EXISTS `chuyenbay_maybay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chuyenbay_maybay` (
  `MaChuyenBay` varchar(10) NOT NULL,
  `MaMayBay` varchar(10) NOT NULL,
  PRIMARY KEY (`MaChuyenBay`,`MaMayBay`),
  KEY `MaMayBay` (`MaMayBay`),
  CONSTRAINT `chuyenbay_maybay_ibfk_1` FOREIGN KEY (`MaChuyenBay`) REFERENCES `chuyenbay` (`MaChuyenBay`) ON DELETE CASCADE,
  CONSTRAINT `chuyenbay_maybay_ibfk_2` FOREIGN KEY (`MaMayBay`) REFERENCES `maybay` (`MaMayBay`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chuyenbay_maybay`
--

LOCK TABLES `chuyenbay_maybay` WRITE;
/*!40000 ALTER TABLE `chuyenbay_maybay` DISABLE KEYS */;
INSERT INTO `chuyenbay_maybay` VALUES ('CB001','MB01'),('CB002','MB02'),('CB003','MB03'),('CB004','MB04');
/*!40000 ALTER TABLE `chuyenbay_maybay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datve`
--

DROP TABLE IF EXISTS `datve`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datve` (
  `MaDatVe` int NOT NULL AUTO_INCREMENT,
  `MaKhachHang` varchar(10) NOT NULL,
  `MaVe` varchar(10) NOT NULL,
  `SoLuong` int NOT NULL,
  PRIMARY KEY (`MaDatVe`),
  KEY `MaKhachHang` (`MaKhachHang`),
  KEY `MaVe` (`MaVe`),
  CONSTRAINT `datve_ibfk_1` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`) ON DELETE CASCADE,
  CONSTRAINT `datve_ibfk_2` FOREIGN KEY (`MaVe`) REFERENCES `vemaybay` (`MaVe`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datve`
--

LOCK TABLES `datve` WRITE;
/*!40000 ALTER TABLE `datve` DISABLE KEYS */;
INSERT INTO `datve` VALUES (1,'KH01','VE01',1),(2,'KH02','VE02',2),(3,'KH03','VE03',1),(4,'KH04','VE04',3);
/*!40000 ALTER TABLE `datve` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hanghangkhong`
--

DROP TABLE IF EXISTS `hanghangkhong`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hanghangkhong` (
  `MaHang` varchar(10) NOT NULL,
  `TenHang` varchar(100) NOT NULL,
  `DiaChi` varchar(200) DEFAULT NULL,
  `SoDienThoai` varchar(15) DEFAULT NULL,
  `Email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`MaHang`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hanghangkhong`
--

LOCK TABLES `hanghangkhong` WRITE;
/*!40000 ALTER TABLE `hanghangkhong` DISABLE KEYS */;
INSERT INTO `hanghangkhong` VALUES ('HHK01','Vietnam Airlines','Hà Nội','0123456789','vna@gmail.com'),('HHK02','VietJet Air','TP HCM','0987654321','vietjet@gmail.com'),('HHK03','Bamboo Airways','Đà Nẵng','0912345678','bamboo@gmail.com'),('HHK04','Pacific Airlines','Hải Phòng','0934567890','pacific@gmail.com'),('HHK05','AirAsia','TP HCM','0945678901','airasia@gmail.com');
/*!40000 ALTER TABLE `hanghangkhong` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `khachhang`
--

DROP TABLE IF EXISTS `khachhang`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `khachhang` (
  `MaKhachHang` varchar(10) NOT NULL,
  `TenKhachHang` varchar(100) NOT NULL,
  `CMND` varchar(12) NOT NULL,
  `SoDienThoai` varchar(15) DEFAULT NULL,
  `DiaChi` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`MaKhachHang`),
  UNIQUE KEY `CMND` (`CMND`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `khachhang`
--

LOCK TABLES `khachhang` WRITE;
/*!40000 ALTER TABLE `khachhang` DISABLE KEYS */;
INSERT INTO `khachhang` VALUES ('KH01','Nguyễn Văn F','123456789012','0901234567','Hà Nội'),('KH02','Trần Thị G','234567890123','0912345678','Đà Nẵng'),('KH03','Lê Văn H','345678901234','0923456789','TP HCM'),('KH04','Phạm Thị I','456789012345','0934567890','Cần Thơ'),('KH05','Hoàng Văn J','567890123456','0945678901','Hải Phòng');
/*!40000 ALTER TABLE `khachhang` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lichbay`
--

DROP TABLE IF EXISTS `lichbay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lichbay` (
  `MaLichBay` int NOT NULL AUTO_INCREMENT,
  `MaChuyenBay` varchar(10) NOT NULL,
  `GioKhoiHanh` time NOT NULL,
  `GioHaCanh` time NOT NULL,
  `ThoiGianBay` int DEFAULT NULL,
  PRIMARY KEY (`MaLichBay`),
  KEY `MaChuyenBay` (`MaChuyenBay`),
  CONSTRAINT `lichbay_ibfk_1` FOREIGN KEY (`MaChuyenBay`) REFERENCES `chuyenbay` (`MaChuyenBay`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lichbay`
--

LOCK TABLES `lichbay` WRITE;
/*!40000 ALTER TABLE `lichbay` DISABLE KEYS */;
INSERT INTO `lichbay` VALUES (1,'CB001','08:00:00','10:00:00',120),(2,'CB002','14:00:00','16:30:00',150),(3,'CB003','06:00:00','07:30:00',90),(4,'CB004','12:00:00','15:00:00',180);
/*!40000 ALTER TABLE `lichbay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maybay`
--

DROP TABLE IF EXISTS `maybay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maybay` (
  `MaMayBay` varchar(10) NOT NULL,
  `LoaiMayBay` varchar(50) NOT NULL,
  `SucChua` int NOT NULL,
  `MaHang` varchar(10) NOT NULL,
  PRIMARY KEY (`MaMayBay`),
  KEY `MaHang` (`MaHang`),
  CONSTRAINT `maybay_ibfk_1` FOREIGN KEY (`MaHang`) REFERENCES `hanghangkhong` (`MaHang`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maybay`
--

LOCK TABLES `maybay` WRITE;
/*!40000 ALTER TABLE `maybay` DISABLE KEYS */;
INSERT INTO `maybay` VALUES ('MB01','Boeing 747',300,'HHK01'),('MB02','Airbus A320',200,'HHK02'),('MB03','Boeing 777',350,'HHK03'),('MB04','Airbus A321',220,'HHK04'),('MB05','Boeing 737',250,'HHK05');
/*!40000 ALTER TABLE `maybay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nhanvien`
--

DROP TABLE IF EXISTS `nhanvien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nhanvien` (
  `MaNhanVien` varchar(10) NOT NULL,
  `TenNhanVien` varchar(100) NOT NULL,
  `ChucVu` enum('Phi công','Tiếp viên','Nhân viên mặt đất','Kỹ thuật') NOT NULL,
  `Luong` decimal(10,2) DEFAULT NULL,
  `MaHang` varchar(10) NOT NULL,
  PRIMARY KEY (`MaNhanVien`),
  KEY `MaHang` (`MaHang`),
  CONSTRAINT `nhanvien_ibfk_1` FOREIGN KEY (`MaHang`) REFERENCES `hanghangkhong` (`MaHang`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nhanvien`
--

LOCK TABLES `nhanvien` WRITE;
/*!40000 ALTER TABLE `nhanvien` DISABLE KEYS */;
INSERT INTO `nhanvien` VALUES ('NV01','Nguyễn Văn A','Phi công',50000000.00,'HHK01'),('NV02','Trần Thị B','Tiếp viên',20000000.00,'HHK02'),('NV03','Lê Văn C','Kỹ thuật',30000000.00,'HHK03'),('NV04','Phạm Thị D','Nhân viên mặt đất',15000000.00,'HHK04'),('NV05','Hoàng Văn E','Phi công',55000000.00,'HHK05');
/*!40000 ALTER TABLE `nhanvien` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sanbay`
--

DROP TABLE IF EXISTS `sanbay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sanbay` (
  `MaSanBay` varchar(10) NOT NULL,
  `TenSanBay` varchar(100) NOT NULL,
  PRIMARY KEY (`MaSanBay`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sanbay`
--

LOCK TABLES `sanbay` WRITE;
/*!40000 ALTER TABLE `sanbay` DISABLE KEYS */;
INSERT INTO `sanbay` VALUES ('SB01','Tân Sơn Nhất'),('SB02','Nội Bài'),('SB03','Đà Nẵng'),('SB04','Cần Thơ'),('SB05','Cam Ranh');
/*!40000 ALTER TABLE `sanbay` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vemaybay`
--

DROP TABLE IF EXISTS `vemaybay`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vemaybay` (
  `MaVe` varchar(10) NOT NULL,
  `MaChuyenBay` varchar(10) NOT NULL,
  `MaKhachHang` varchar(10) NOT NULL,
  `NgayDat` date NOT NULL,
  PRIMARY KEY (`MaVe`),
  KEY `MaChuyenBay` (`MaChuyenBay`),
  KEY `MaKhachHang` (`MaKhachHang`),
  CONSTRAINT `vemaybay_ibfk_1` FOREIGN KEY (`MaChuyenBay`) REFERENCES `chuyenbay` (`MaChuyenBay`) ON DELETE CASCADE,
  CONSTRAINT `vemaybay_ibfk_2` FOREIGN KEY (`MaKhachHang`) REFERENCES `khachhang` (`MaKhachHang`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vemaybay`
--

LOCK TABLES `vemaybay` WRITE;
/*!40000 ALTER TABLE `vemaybay` DISABLE KEYS */;
INSERT INTO `vemaybay` VALUES ('VE01','CB001','KH01','2024-12-01'),('VE02','CB002','KH02','2024-12-02'),('VE03','CB003','KH03','2024-12-03'),('VE04','CB004','KH04','2024-12-04');
/*!40000 ALTER TABLE `vemaybay` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-12-06  8:36:51
