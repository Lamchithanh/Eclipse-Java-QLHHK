package Service;

import Database.MYSQLDB;
import Model.MayBay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MayBayService {
    public List<MayBay> getAllMayBay() {
        List<MayBay> mayBays = new ArrayList<>();
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "SELECT MaMayBay, LoaiMayBay, SucChua, MaHang FROM MayBay";
            
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    MayBay mayBay = new MayBay(
                        rs.getString("MaMayBay"),
                        rs.getString("LoaiMayBay"),
                        rs.getInt("SucChua"),
                        rs.getString("MaHang")
                    );
                    mayBays.add(mayBay);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        
        return mayBays;
    }

    public void addMayBay(MayBay mayBay) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "INSERT INTO MayBay (MaMayBay, LoaiMayBay, SucChua, MaHang) " +
                         "VALUES (?, ?, ?, ?) " +
                         "ON DUPLICATE KEY UPDATE LoaiMayBay = VALUES(LoaiMayBay), " +
                         "SucChua = VALUES(SucChua), MaHang = VALUES(MaHang)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, mayBay.getMaMayBay());
                pstmt.setString(2, mayBay.getLoaiMayBay());
                pstmt.setInt(3, mayBay.getSucChua());
                pstmt.setString(4, mayBay.getMaHang());
                
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
    }
    
    public void updateMayBay(String maMayBay, String loaiMayBay, int i, String maHang) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "UPDATE MayBay SET LoaiMayBay = ?, SucChua = ?, MaHang = ? WHERE MaMayBay = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, loaiMayBay);
                pstmt.setLong(2, i);
                pstmt.setString(3, maHang);
                pstmt.setString(4, maMayBay);
                
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
    }
    
    public void deleteMayBay(String maMayBay) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "DELETE FROM MayBay WHERE MaMayBay = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maMayBay);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
    }

    public MayBay findMayBayByCode(String maMayBay) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "SELECT MaMayBay, LoaiMayBay, SucChua, MaHang FROM MayBay WHERE MaMayBay = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maMayBay);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return new MayBay(
                            rs.getString("MaMayBay"),
                            rs.getString("LoaiMayBay"),
                            rs.getInt("SucChua"),
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
        
        return null;
    }
}