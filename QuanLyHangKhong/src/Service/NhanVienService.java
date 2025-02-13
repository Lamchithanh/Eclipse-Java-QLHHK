package Service;

import Database.MYSQLDB;
import Model.NhanVien;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhanVienService {
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

    public void addNhanVien(NhanVien nhanVien) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "INSERT INTO NhanVien (MaNhanVien, TenNhanVien, ChucVu, MaHang) " +
                         "VALUES (?, ?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE TenNhanVien = VALUES(TenNhanVien), " +
                         "ChucVu = VALUES(ChucVu), MaHang = VALUES(MaHang)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, nhanVien.getMaNhanVien());
                pstmt.setString(2, nhanVien.getTenNhanVien());
                pstmt.setString(3, nhanVien.getChucVu());
                pstmt.setString(4, nhanVien.getMaHang());
                
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
    }

    public void updateNhanVien(String maNhanVien, String tenNhanVien, String chucVu, String maHang) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "UPDATE NhanVien SET TenNhanVien = ?, ChucVu = ?, MaHang = ? WHERE MaNhanVien = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, tenNhanVien);
                pstmt.setString(2, chucVu);
                pstmt.setString(3, maHang);
                pstmt.setString(4, maNhanVien);
                
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
    }

    public void deleteNhanVien(String maNhanVien) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "DELETE FROM NhanVien WHERE MaNhanVien = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maNhanVien);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
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
        
        return nhanVien; // Trả về null nếu không tìm thấy nhân viên
    }

}