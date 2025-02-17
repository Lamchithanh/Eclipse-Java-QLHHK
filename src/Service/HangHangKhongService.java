package Service;

import Database.MYSQLDB;
import Model.HangHangKhong;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HangHangKhongService {
    public List<HangHangKhong> getAllHangHangKhong() {
        List<HangHangKhong> hangList = new ArrayList<>();
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "SELECT * FROM HangHangKhong";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    HangHangKhong hang = new HangHangKhong(
                        rs.getString("MaHang"),
                        rs.getString("TenHang"),
                        rs.getString("DiaChi"),
                        rs.getString("SoDienThoai"),
                        rs.getString("Email")
                    );
                    hangList.add(hang);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        
        return hangList;
    }

    public boolean isHangInfoDuplicated(HangHangKhong hang, String excludeMaHang) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "SELECT COUNT(*) FROM HangHangKhong WHERE " +
                        "(TenHang = ? OR SoDienThoai = ? OR Email = ?) " +
                        (excludeMaHang != null ? "AND MaHang != ?" : "");
                        
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, hang.getTenHang());
                pstmt.setString(2, hang.getSoDienThoai());
                pstmt.setString(3, hang.getEmail());
                if (excludeMaHang != null) {
                    pstmt.setString(4, excludeMaHang);
                }
                
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return false;
    }
    
    // Thêm phương thức để lấy thông tin chi tiết về trùng lặp
    public String getDuplicateInfo(HangHangKhong hang, String excludeMaHang) {
        Connection connection = MYSQLDB.getConnection();
        StringBuilder duplicateInfo = new StringBuilder();
        
        if (connection != null) {
            String sql = "SELECT * FROM HangHangKhong WHERE " +
                        "(TenHang = ? OR SoDienThoai = ? OR Email = ?) " +
                        (excludeMaHang != null ? "AND MaHang != ?" : "");
                        
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, hang.getTenHang());
                pstmt.setString(2, hang.getSoDienThoai());
                pstmt.setString(3, hang.getEmail());
                if (excludeMaHang != null) {
                    pstmt.setString(4, excludeMaHang);
                }
                
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    if (rs.getString("TenHang").equals(hang.getTenHang())) {
                        duplicateInfo.append("Tên hãng đã tồn tại\n");
                    }
                    if (rs.getString("SoDienThoai").equals(hang.getSoDienThoai())) {
                        duplicateInfo.append("Số điện thoại đã được sử dụng\n");
                    }
                    if (rs.getString("Email").equals(hang.getEmail())) {
                        duplicateInfo.append("Email đã được sử dụng\n");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return duplicateInfo.toString();
    }

 // In HangHangKhongService.java
    public boolean addHangHangKhong(HangHangKhong hang) {
        Connection connection = MYSQLDB.getConnection();

        if (connection != null) {
            String sql = "INSERT INTO HangHangKhong (MaHang, TenHang, DiaChi, SoDienThoai, Email) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, hang.getMaHang());
                pstmt.setString(2, hang.getTenHang());
                pstmt.setString(3, hang.getDiaChi());
                pstmt.setString(4, hang.getSoDienThoai());
                pstmt.setString(5, hang.getEmail());

                int rowsAffected = pstmt.executeUpdate(); // Get the number of rows affected
                return rowsAffected > 0; // Return true if at least one row was inserted
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Return false if any exception occurred
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return false; // Return false if connection is null
    }

    public boolean updateHangHangKhong(HangHangKhong hang) {
        Connection connection = MYSQLDB.getConnection();

        if (connection != null) {
            String sql = "UPDATE HangHangKhong SET TenHang=?, DiaChi=?, SoDienThoai=?, Email=? WHERE MaHang=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, hang.getTenHang());
                pstmt.setString(2, hang.getDiaChi());
                pstmt.setString(3, hang.getSoDienThoai());
                pstmt.setString(4, hang.getEmail());
                pstmt.setString(5, hang.getMaHang());

                int rowsAffected = pstmt.executeUpdate(); // Get the number of rows affected
                return rowsAffected > 0; // Return true if at least one row was updated
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Return false if any exception occurred
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return false; // Return false if connection is null
    }

    public boolean deleteHangHangKhong(String maHang) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "DELETE FROM HangHangKhong WHERE MaHang=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maHang);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0; // Trả về true nếu xóa thành công
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Trả về false nếu có lỗi
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return false; // Trả về false nếu không có kết nối
    }

    public HangHangKhong findHangHangKhongByCode(String maHang) {
        Connection connection = MYSQLDB.getConnection();
        HangHangKhong hang = null;
        
        if (connection != null) {
            String sql = "SELECT * FROM HangHangKhong WHERE MaHang=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maHang);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        hang = new HangHangKhong(
                            rs.getString("MaHang"),
                            rs.getString("TenHang"),
                            rs.getString("DiaChi"),
                            rs.getString("SoDienThoai"),
                            rs.getString("Email")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        
        return hang;
    }
}