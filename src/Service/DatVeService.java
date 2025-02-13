package Service;

import Model.DatVe;
import Model.ChuyenBay;
import Model.VeMayBay;
import Database.MYSQLDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatVeService {
    private ChuyenBayService chuyenBayService = new ChuyenBayService();

    // Kiểm tra mã đặt vé tồn tại
    public boolean isMaDatVeExists(String maDatVe) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "SELECT COUNT(*) FROM DatVe WHERE MaDatVe = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, maDatVe);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection, stmt, rs);
        }
        return false;
    }

        // Thêm đặt vé mới
        public void addDatVe(DatVe datVe) throws SQLException {
            Connection connection = null;
            PreparedStatement stmt = null;
            
            try {
                connection = MYSQLDB.getConnection();
                connection.setAutoCommit(false);
        
                // Insert vào bảng KhachHang trước
                String sqlKhachHang = "INSERT INTO KhachHang (MaKhachHang, TenKhachHang, CMND, GioiTinh, " +
                    "NgaySinh, SoDienThoai, Email, DiaChi, QuocTich, TenNguoiLienHe, SoDienThoaiNguoiLienHe) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "TenKhachHang=VALUES(TenKhachHang), SoDienThoai=VALUES(SoDienThoai), " +
                    "Email=VALUES(Email), DiaChi=VALUES(DiaChi)";
        
                PreparedStatement stmtKhachHang = connection.prepareStatement(sqlKhachHang);
                stmtKhachHang.setString(1, datVe.getMaKhachHang());
                stmtKhachHang.setString(2, datVe.getHoTen());
                stmtKhachHang.setString(3, datVe.getCMND());
                stmtKhachHang.setString(4, datVe.getGioiTinh());
                stmtKhachHang.setDate(5, datVe.getNgaySinh());
                stmtKhachHang.setString(6, datVe.getSoDienThoai());
                stmtKhachHang.setString(7, datVe.getEmail());
                stmtKhachHang.setString(8, datVe.getDiaChi());
                stmtKhachHang.setString(9, datVe.getQuocTich());
                stmtKhachHang.setString(10, datVe.getNguoiLienHe());
                stmtKhachHang.setString(11, datVe.getSDTNguoiLienHe());
                stmtKhachHang.executeUpdate();
        
                // Insert vào bảng DatVe
                String sqlDatVe = "INSERT INTO DatVe (MaDatVe, MaKhachHang, MaChuyenBay, NgayBay, " +
                    "HangVe, SoLuong, TongGia, TrangThai, PhuongThucThanhToan, MaGiamGia, " +
                    "XacNhanThanhToan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
                stmt = connection.prepareStatement(sqlDatVe);
                stmt.setString(1, datVe.getMaDatVe());
                stmt.setString(2, datVe.getMaKhachHang());
                stmt.setString(3, datVe.getChuyenBay().getMaChuyenBay());
                stmt.setDate(4, datVe.getNgayBay());
                stmt.setString(5, datVe.getHangVe());
                stmt.setInt(6, datVe.getSoLuong());
                stmt.setDouble(7, datVe.getTongGia());
                stmt.setString(8, datVe.getTrangThai());
                stmt.setString(9, datVe.getPhuongThucThanhToan());
                stmt.setString(10, datVe.getMaGiamGia());
                stmt.setBoolean(11, datVe.isXacNhanThanhToan());
                stmt.executeUpdate();
        
                // Insert vào bảng YeuCauDacBiet
                String sqlYeuCau = "INSERT INTO YeuCauDacBiet (MaVe, SuatAnDacBiet, HoTroYTe, " +
                    "ChoNgoiUuTien, HanhLyDacBiet) VALUES (?, ?, ?, ?, ?)";
                
                PreparedStatement stmtYeuCau = connection.prepareStatement(sqlYeuCau);
                stmtYeuCau.setString(1, datVe.getMaDatVe());
                stmtYeuCau.setBoolean(2, datVe.isSuatAnDacBiet());
                stmtYeuCau.setBoolean(3, datVe.isHoTroYTe());
                stmtYeuCau.setBoolean(4, datVe.isChoNgoiUuTien());
                stmtYeuCau.setBoolean(5, datVe.isHanhLyDacBiet());
                stmtYeuCau.executeUpdate();
        
                connection.commit();
            } catch (SQLException e) {
                if (connection != null) {
                    connection.rollback();
                }
                throw e;
            } finally {
                closeResources(connection, stmt, null);
            }
        }


    // Cập nhật đặt vé
    public void updateDatVe(DatVe datVe) throws SQLException {
        Connection connection = null;
        PreparedStatement stmtDatVe = null;
        PreparedStatement stmtYeuCau = null;
        
        try {
            connection = MYSQLDB.getConnection();
            connection.setAutoCommit(false);

            // Update DatVe
            String sqlDatVe = "UPDATE DatVe SET MaChuyenBay=?, HoTen=?, CMND=?, NgaySinh=?, " +
                "GioiTinh=?, QuocTich=?, SoDienThoai=?, Email=?, DiaChi=?, DiemDi=?, DiemDen=?, " +
                "NgayBay=?, HangVe=?, SoLuong=?, TongGia=?, TrangThai=?, PhuongThucThanhToan=?, " +
                "MaGiamGia=?, XacNhanThanhToan=?, NguoiLienHe=?, SDTNguoiLienHe=?, NgayCapNhat=? " +
                "WHERE MaDatVe=?";
            
            stmtDatVe = connection.prepareStatement(sqlDatVe);
            int index = 1;
            stmtDatVe.setString(index++, datVe.getChuyenBay().getMaChuyenBay());
            stmtDatVe.setString(index++, datVe.getHoTen());
            stmtDatVe.setString(index++, datVe.getCMND());
            stmtDatVe.setDate(index++, datVe.getNgaySinh());
            stmtDatVe.setString(index++, datVe.getGioiTinh());
            stmtDatVe.setString(index++, datVe.getQuocTich());
            stmtDatVe.setString(index++, datVe.getSoDienThoai());
            stmtDatVe.setString(index++, datVe.getEmail());
            stmtDatVe.setString(index++, datVe.getDiaChi());
            stmtDatVe.setString(index++, datVe.getDiemDi());
            stmtDatVe.setString(index++, datVe.getDiemDen());
            stmtDatVe.setDate(index++, datVe.getNgayBay());
            stmtDatVe.setString(index++, datVe.getHangVe());
            stmtDatVe.setInt(index++, datVe.getSoLuong());
            stmtDatVe.setDouble(index++, datVe.getTongGia());
            stmtDatVe.setString(index++, datVe.getTrangThai());
            stmtDatVe.setString(index++, datVe.getPhuongThucThanhToan());
            stmtDatVe.setString(index++, datVe.getMaGiamGia());
            stmtDatVe.setBoolean(index++, datVe.isXacNhanThanhToan());
            stmtDatVe.setString(index++, datVe.getNguoiLienHe());
            stmtDatVe.setString(index++, datVe.getSDTNguoiLienHe());
            stmtDatVe.setTimestamp(index++, Timestamp.valueOf(LocalDateTime.now()));
            stmtDatVe.setString(index++, datVe.getMaDatVe());
            
            stmtDatVe.executeUpdate();

            // Update YeuCauDacBiet
            String sqlYeuCau = "UPDATE YeuCauDacBiet SET SuatAnDacBiet=?, HoTroYTe=?, " +
                "ChoNgoiUuTien=?, HanhLyDacBiet=? WHERE MaVe=?";
            
            stmtYeuCau = connection.prepareStatement(sqlYeuCau);
            stmtYeuCau.setBoolean(1, datVe.isSuatAnDacBiet());
            stmtYeuCau.setBoolean(2, datVe.isHoTroYTe());
            stmtYeuCau.setBoolean(3, datVe.isChoNgoiUuTien());
            stmtYeuCau.setBoolean(4, datVe.isHanhLyDacBiet());
            stmtYeuCau.setString(5, datVe.getMaDatVe());
            
            stmtYeuCau.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            closeResources(connection, stmtDatVe, null);
            if (stmtYeuCau != null) {
                stmtYeuCau.close();
            }
        }
    }

    // Xóa đặt vé
    public void deleteDatVe(String maDatVe) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            connection = MYSQLDB.getConnection();
            connection.setAutoCommit(false);

            // Delete from YeuCauDacBiet first due to foreign key constraint
            String sqlYeuCau = "DELETE FROM YeuCauDacBiet WHERE MaVe = ?";
            stmt = connection.prepareStatement(sqlYeuCau);
            stmt.setString(1, maDatVe);
            stmt.executeUpdate();

            // Then delete from DatVe
            String sqlDatVe = "DELETE FROM DatVe WHERE MaDatVe = ?";
            stmt = connection.prepareStatement(sqlDatVe);
            stmt.setString(1, maDatVe);
            stmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
            }
            closeResources(connection, stmt, null);
        }
    }

    // Lấy tất cả đặt vé
    public List<DatVe> getAllDatVe() throws SQLException {
        List<DatVe> datVeList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "SELECT d.*, y.SuatAnDacBiet, y.HoTroYTe, y.ChoNgoiUuTien, y.HanhLyDacBiet " +
                        "FROM DatVe d LEFT JOIN YeuCauDacBiet y ON d.MaDatVe = y.MaVe " +
                        "ORDER BY d.NgayDat DESC";
            
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                DatVe datVe = mapResultSetToDatVe(rs);
                datVeList.add(datVe);
            }
        } finally {
            closeResources(connection, stmt, rs);
        }
        
        return datVeList;
    }

    // Tìm kiếm đặt vé
    public List<DatVe> searchDatVe(String searchQuery) throws SQLException {
        List<DatVe> datVeList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "SELECT d.*, k.*, y.* " +
                        "FROM DatVe d " +
                        "LEFT JOIN KhachHang k ON d.MaKhachHang = k.MaKhachHang " +
                        "LEFT JOIN YeuCauDacBiet y ON d.MaDatVe = y.MaVe " +
                        "WHERE d.MaDatVe LIKE ? OR k.TenKhachHang LIKE ? OR k.CMND LIKE ? " +
                        "OR k.SoDienThoai LIKE ? OR d.TrangThai LIKE ? " +
                        "ORDER BY d.NgayDat DESC";
            
            stmt = connection.prepareStatement(sql);
            String searchPattern = "%" + searchQuery + "%";
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, searchPattern);
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                DatVe datVe = mapResultSetToDatVe(rs);
                datVeList.add(datVe);
            }
        } finally {
            closeResources(connection, stmt, rs);
        }
        
        return datVeList;
    }

    // Lấy đặt vé theo mã
    public DatVe getDatVeById(String maDatVe) throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "SELECT d.*, y.SuatAnDacBiet, y.HoTroYTe, y.ChoNgoiUuTien, y.HanhLyDacBiet " +
                        "FROM DatVe d " +
                        "LEFT JOIN YeuCauDacBiet y ON d.MaDatVe = y.MaVe " +
                        "WHERE d.MaDatVe = ?";
            
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, maDatVe);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToDatVe(rs);
            }
        } finally {
            closeResources(connection, stmt, rs);
        }
        
        return null;
    }

    // Helper method to map ResultSet to DatVe object
    private DatVe mapResultSetToDatVe(ResultSet rs) throws SQLException {
        DatVe datVe = new DatVe();
        
        // Thông tin đặt vé cơ bản
        datVe.setMaDatVe(rs.getString("MaDatVe"));
        datVe.setMaKhachHang(rs.getString("MaKhachHang"));
        
        // Thông tin chuyến bay
        String maChuyenBay = rs.getString("MaChuyenBay");
        ChuyenBay chuyenBay = chuyenBayService.getChuyenBayById(maChuyenBay);
        datVe.setChuyenBay(chuyenBay);
        
        // Thông tin khách hàng
        datVe.setHoTen(rs.getString("TenKhachHang"));
        datVe.setCMND(rs.getString("CMND"));
        datVe.setGioiTinh(rs.getString("GioiTinh"));
        datVe.setNgaySinh(rs.getDate("NgaySinh"));
        
        // Thông tin liên hệ
        datVe.setSoDienThoai(rs.getString("SoDienThoai"));
        datVe.setEmail(rs.getString("Email"));
        datVe.setDiaChi(rs.getString("DiaChi"));
        datVe.setQuocTich(rs.getString("QuocTich"));
        
        // Thông tin chuyến bay
        datVe.setNgayBay(rs.getDate("NgayBay"));
        datVe.setHangVe(rs.getString("HangVe"));
        datVe.setSoLuong(rs.getInt("SoLuong"));
        datVe.setTongGia(rs.getDouble("TongGia"));
        
        // Thông tin thanh toán
        datVe.setTrangThai(rs.getString("TrangThai"));
        datVe.setPhuongThucThanhToan(rs.getString("PhuongThucThanhToan"));
        datVe.setMaGiamGia(rs.getString("MaGiamGia"));
        datVe.setXacNhanThanhToan(rs.getBoolean("XacNhanThanhToan"));
        
        // Thông tin liên hệ khẩn cấp
        datVe.setNguoiLienHe(rs.getString("TenNguoiLienHe"));
        datVe.setSDTNguoiLienHe(rs.getString("SoDienThoaiNguoiLienHe"));
        
        // Yêu cầu đặc biệt
        datVe.setSuatAnDacBiet(rs.getBoolean("SuatAnDacBiet"));
        datVe.setHoTroYTe(rs.getBoolean("HoTroYTe"));
        datVe.setChoNgoiUuTien(rs.getBoolean("ChoNgoiUuTien"));
        datVe.setHanhLyDacBiet(rs.getBoolean("HanhLyDacBiet"));
        
        // Thời gian
        Timestamp ngayDat = rs.getTimestamp("NgayDat");
        if (ngayDat != null) {
            datVe.setNgayDat(ngayDat.toLocalDateTime());
        }
        
        Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
        if (ngayCapNhat != null) {
            datVe.setNgayCapNhat(ngayCapNhat.toLocalDateTime());
        }
        
        return datVe;
    }

    // Helper method to close database resources
    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to generate mã đặt vé
    public String generateMaDatVe() throws SQLException {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "SELECT MAX(MaDatVe) FROM DatVe WHERE MaDatVe LIKE 'DV%'";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId == null) {
                    return "DV0001";
                }
                
                int number = Integer.parseInt(maxId.substring(2)) + 1;
                return String.format("DV%04d", number);
            }
            
            return "DV0001";
        } finally {
            closeResources(connection, stmt, rs);
        }
    }
}