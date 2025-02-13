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

    public boolean addMayBay(MayBay mayBay) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "INSERT INTO MayBay (MaMayBay, LoaiMayBay, SucChua, MaHang) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, mayBay.getMaMayBay());
                pstmt.setString(2, mayBay.getLoaiMayBay());
                pstmt.setInt(3, mayBay.getSucChua());
                pstmt.setString(4, mayBay.getMaHang());
                
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
    
    public boolean updateMayBay(String maMayBay, String loaiMayBay, int sucChua, String maHang) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "UPDATE MayBay SET LoaiMayBay = ?, SucChua = ?, MaHang = ? WHERE MaMayBay = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, loaiMayBay);
                pstmt.setInt(2, sucChua);
                pstmt.setString(3, maHang);
                pstmt.setString(4, maMayBay);
                
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
    
    public boolean deleteMayBay(String maMayBay) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "DELETE FROM MayBay WHERE MaMayBay = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maMayBay);
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