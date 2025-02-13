package Service;

import Database.MYSQLDB;
import Model.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienService {
    public boolean addNhanVien(NhanVien nhanVien) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "INSERT INTO NhanVien (MaNhanVien, TenNhanVien, ChucVu, MaHang) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, nhanVien.getMaNhanVien());
                pstmt.setString(2, nhanVien.getTenNhanVien());
                pstmt.setString(3, nhanVien.getChucVu());
                pstmt.setString(4, nhanVien.getMaHang());
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return false;
    }

    public boolean updateNhanVien(String maNhanVien, String tenNhanVien, String chucVu, String maHang) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "UPDATE NhanVien SET TenNhanVien = ?, ChucVu = ?, MaHang = ? WHERE MaNhanVien = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, tenNhanVien);
                pstmt.setString(2, chucVu);
                pstmt.setString(3, maHang);
                pstmt.setString(4, maNhanVien);
                
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return false;
    }

    public boolean deleteNhanVien(String maNhanVien) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "DELETE FROM NhanVien WHERE MaNhanVien = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maNhanVien);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return false;
    }

    // Các phương thức khác giữ nguyên
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> nhanVienList = new ArrayList<>();
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "SELECT MaNhanVien, TenNhanVien, ChucVu, MaHang FROM NhanVien";
            
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    NhanVien nhanVien = new NhanVien(
                        rs.getString("MaNhanVien"),
                        rs.getString("TenNhanVien"),
                        rs.getString("ChucVu"),
                        rs.getString("MaHang")
                    );
                    nhanVienList.add(nhanVien);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return nhanVienList;
    }

    public NhanVien findNhanVienByCode(String maNhanVien) {
        Connection connection = MYSQLDB.getConnection();
        NhanVien nhanVien = null;
        
        if (connection != null) {
            String sql = "SELECT * FROM NhanVien WHERE MaNhanVien = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maNhanVien);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        nhanVien = new NhanVien(
                            rs.getString("MaNhanVien"),
                            rs.getString("TenNhanVien"),
                            rs.getString("ChucVu"),
                            rs.getString("MaHang")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return nhanVien;
    }
}