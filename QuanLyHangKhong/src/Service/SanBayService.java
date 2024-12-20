package Service;

import Model.SanBay;
import Database.MYSQLDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SanBayService {
    // Kiểm tra xem mã sân bay đã tồn tại chưa
    public boolean isMaSanBayExists(String maSanBay) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "SELECT COUNT(*) FROM SanBay WHERE MaSanBay = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, maSanBay);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }

    // Thêm sân bay mới
    public void addSanBay(SanBay sanBay) {
        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "INSERT INTO SanBay (MaSanBay, TenSanBay) VALUES (?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, sanBay.getMaSanBay());
            stmt.setString(2, sanBay.getTenSanBay());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Cập nhật thông tin sân bay
    public void updateSanBay(SanBay sanBay) {
        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "UPDATE SanBay SET TenSanBay = ? WHERE MaSanBay = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, sanBay.getTenSanBay());
            stmt.setString(2, sanBay.getMaSanBay());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Xóa sân bay
    public void deleteSanBay(String maSanBay) {
        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "DELETE FROM SanBay WHERE MaSanBay = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, maSanBay);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Lấy tất cả các sân bay
    public List<SanBay> getAllSanBay() {
        List<SanBay> sanBayList = new ArrayList<>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            stmt = connection.createStatement();
            String sql = "SELECT MaSanBay, TenSanBay FROM SanBay";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String maSanBay = rs.getString("MaSanBay");
                String tenSanBay = rs.getString("TenSanBay");
                
                SanBay sanBay = new SanBay(maSanBay, tenSanBay);
                sanBayList.add(sanBay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng kết nối
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return sanBayList;
    }
}