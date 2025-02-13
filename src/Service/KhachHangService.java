package Service;

import Model.KhachHang;
import Database.MYSQLDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhachHangService {
    // Lấy tất cả khách hàng
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> khachHangList = new ArrayList<>();
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            stmt = connection.createStatement();
            String sql = "SELECT * FROM KhachHang";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                KhachHang khachHang = new KhachHang(
                    rs.getString("MaKhachHang"),
                    rs.getString("TenKhachHang"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getString("DiaChi")
                );
                khachHangList.add(khachHang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return khachHangList;
    }

    // Lấy khách hàng theo ID
    public KhachHang getKhachHangById(String maKhachHang) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "SELECT * FROM KhachHang WHERE MaKhachHang = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, maKhachHang);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new KhachHang(
                    rs.getString("MaKhachHang"),
                    rs.getString("TenKhachHang"),
                    rs.getString("SoDienThoai"),
                    rs.getString("Email"),
                    rs.getString("DiaChi")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }

    // Các phương thức khác như thêm, sửa, xóa khách hàng
    public void addKhachHang(KhachHang khachHang) {
        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "INSERT INTO KhachHang " +
                         "(MaKhachHang, TenKhachHang, SoDienThoai, Email, DiaChi) " +
                         "VALUES (?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, khachHang.getMaKhachHang());
            stmt.setString(2, khachHang.getTenKhachHang());
            stmt.setString(3, khachHang.getSoDienThoai());
            stmt.setString(4, khachHang.getEmail());
            stmt.setString(5, khachHang.getDiaChi());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}