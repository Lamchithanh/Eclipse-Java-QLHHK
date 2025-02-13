package Service;

import Database.MYSQLDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TrangChuService {

    public void capNhatDuLieu() {
        try (Connection connection = MYSQLDB.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không thể cập nhật dữ liệu do lỗi kết nối!");
        }
    }

    public int getTodayFlightsCount() {
        int todayFlights = 0;
        LocalDate today = LocalDate.now();
        String query = "SELECT COUNT(*) FROM ChuyenBay WHERE DATE(NgayBay) = ?";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(today));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    todayFlights = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todayFlights;
    }

    public double getTodayRevenue() {
        double todayRevenue = 0;
        LocalDate today = LocalDate.now();
        String query = "SELECT SUM(TongGia) FROM DatVe WHERE DATE(NgayDat) = ? AND TrangThai = 'Đã thanh toán'";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(today));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    todayRevenue = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todayRevenue;
    }

    public int getTotalCustomers() {
        int totalCustomers = 0;
        String query = "SELECT COUNT(DISTINCT MaKhachHang) FROM VeMayBay";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalCustomers = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCustomers;
    }

    public int getTotalDestinations() {
        int totalDestinations = 0;
        String query = "SELECT COUNT(DISTINCT DiemDen) FROM chuyenbay";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalDestinations = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalDestinations;
    }

    public int layTongChuyenBay() {
        int totalFlights = 0;
        String query = "SELECT COUNT(*) FROM chuyenbay";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalFlights = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalFlights;
    }

    public int layTongNhanVien() {
        int totalEmployees = 0;
        String query = "SELECT COUNT(*) FROM nhanvien";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalEmployees = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalEmployees;
    }

    public int layTongVeDaBan() {
        int totalTicketsSold = 0;
        String query = "SELECT COUNT(*) FROM VeMayBay WHERE TrangThai = 'Đã thanh toán'";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                totalTicketsSold = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalTicketsSold;
    }

    public int getTodayTicketsSold() {
        int todayTickets = 0;
        LocalDate today = LocalDate.now();
        String query = "SELECT COUNT(*) FROM DatVe WHERE DATE(NgayDat) = ? AND TrangThai = 'Đã thanh toán'";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            
            stmt.setDate(1, java.sql.Date.valueOf(today));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    todayTickets = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todayTickets;
    }

}