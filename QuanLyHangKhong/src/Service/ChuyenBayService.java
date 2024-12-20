package Service;

import Model.ChuyenBay;
import Database.MYSQLDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChuyenBayService {

    private static final Logger LOGGER = Logger.getLogger(ChuyenBayService.class.getName());

    // Lấy danh sách chuyến bay
    public List<ChuyenBay> getAllChuyenBays() {
        List<ChuyenBay> danhSachChuyenBay = new ArrayList<>();
        Connection conn = MYSQLDB.getConnection();

        if (conn == null) {
            LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
            return danhSachChuyenBay;
        }

        String query = "SELECT MaChuyenBay, MaSanBay, ChangBay, NgayBay, NhaGa, SoGhe, TinhTrang, MaMayBay, MaHang FROM ChuyenBay";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                danhSachChuyenBay.add(new ChuyenBay(
                        rs.getString("MaChuyenBay"),
                        rs.getString("MaSanBay"),
                        rs.getString("ChangBay"),
                        rs.getString("NgayBay"),
                        rs.getString("NhaGa"),
                        rs.getInt("SoGhe"),
                        rs.getString("TinhTrang"),
                        rs.getString("MaMayBay"),
                        rs.getString("MaHang")
                ));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách chuyến bay", e);
        } finally {
            MYSQLDB.closeConnection(conn);
        }
        return danhSachChuyenBay;
    }

    // Thêm hoặc cập nhật chuyến bay
    public void addChuyenBay(ChuyenBay chuyenBay) {
        Connection conn = MYSQLDB.getConnection();

        if (conn == null) {
            LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
            return;
        }

        String insertOrUpdateQuery = """
                INSERT INTO ChuyenBay (MaChuyenBay, MaSanBay, ChangBay, NgayBay, NhaGa, SoGhe, TinhTrang, MaMayBay, MaHang) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) 
                ON DUPLICATE KEY UPDATE 
                MaSanBay = VALUES(MaSanBay), 
                ChangBay = VALUES(ChangBay), 
                NgayBay = VALUES(NgayBay), 
                NhaGa = VALUES(NhaGa), 
                SoGhe = VALUES(SoGhe), 
                TinhTrang = VALUES(TinhTrang), 
                MaMayBay = VALUES(MaMayBay),
                MaHang = VALUES(MaHang)
                """;

        try (PreparedStatement stmt = conn.prepareStatement(insertOrUpdateQuery)) {
            stmt.setString(1, chuyenBay.getMaChuyenBay());
            stmt.setString(2, chuyenBay.getSanBay());
            stmt.setString(3, chuyenBay.getChangBay());
            stmt.setString(4, chuyenBay.getNgayBay());
            stmt.setString(5, chuyenBay.getNhaGa());
            stmt.setInt(6, chuyenBay.getSoGhe());
            stmt.setString(7, chuyenBay.getTinhTrang());
            stmt.setString(8, chuyenBay.getMaMaybay());
            stmt.setString(9, chuyenBay.getMaHang());

            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi thêm hoặc cập nhật chuyến bay", e);
        } finally {
            MYSQLDB.closeConnection(conn);
        }
    }
    
    public void updateChuyenBay(ChuyenBay chuyenBay) {
        Connection conn = MYSQLDB.getConnection();

        if (conn == null) {
            LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
            return;
        }

        String updateQuery = """
                UPDATE ChuyenBay 
                SET MaSanBay = ?, MaHang = ?, MaMayBay = ?, ChangBay = ?, NgayBay = ?, 
                    NhaGa = ?, SoGhe = ?, TinhTrang = ? 
                WHERE MaChuyenBay = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, chuyenBay.getSanBay());
            stmt.setString(2, chuyenBay.getMaHang());
            stmt.setString(3, chuyenBay.getMaMaybay());
            stmt.setString(4, chuyenBay.getChangBay());
            stmt.setString(5, chuyenBay.getNgayBay());
            stmt.setString(6, chuyenBay.getNhaGa());
            stmt.setInt(7, chuyenBay.getSoGhe());
            stmt.setString(8, chuyenBay.getTinhTrang());
            stmt.setString(9, chuyenBay.getMaChuyenBay());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Không có hàng nào được cập nhật. Kiểm tra mã chuyến bay.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật chuyến bay", e);
            throw new RuntimeException("Lỗi khi cập nhật chuyến bay", e);
        } finally {
            MYSQLDB.closeConnection(conn);
        }
    }
    
    public List<ChuyenBay> searchChuyenBayByCode(String maChuyenBay) {
        List<ChuyenBay> danhSachKetQua = new ArrayList<>();
        Connection conn = MYSQLDB.getConnection();

        if (conn == null) {
            LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
            return danhSachKetQua;
        }

        String searchQuery = "SELECT MaChuyenBay, MaSanBay, ChangBay, NgayBay, NhaGa, SoGhe, TinhTrang, MaMayBay, MaHang FROM ChuyenBay WHERE MaChuyenBay = ?";
        try (PreparedStatement stmt = conn.prepareStatement(searchQuery)) {
            stmt.setString(1, maChuyenBay);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    danhSachKetQua.add(new ChuyenBay(
                            rs.getString("MaChuyenBay"),
                            rs.getString("MaSanBay"),
                            rs.getString("ChangBay"),
                            rs.getString("NgayBay"),
                            rs.getString("NhaGa"),
                            rs.getInt("SoGhe"),
                            rs.getString("TinhTrang"),
                            rs.getString("MaMayBay"),
                            rs.getString("MaHang")
                    ));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm chuyến bay", e);
        } finally {
            MYSQLDB.closeConnection(conn);
        }

        return danhSachKetQua;
    }


    // Xóa chuyến bay	
    public void deleteChuyenBay(String maChuyenBay) {
        Connection conn = MYSQLDB.getConnection();

        if (conn == null) {
            LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
            return;
        }

        String deleteQuery = "DELETE FROM ChuyenBay WHERE MaChuyenBay = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
            stmt.setString(1, maChuyenBay);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa chuyến bay", e);
        } finally {
            MYSQLDB.closeConnection(conn);
        }
    }
    
}
