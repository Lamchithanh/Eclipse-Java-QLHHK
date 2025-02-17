package Service;

import Database.MYSQLDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
        String query = "SELECT COUNT(*) FROM ChuyenBay WHERE DATE(NgayBay) = CURRENT_DATE";
    
        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                todayFlights = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todayFlights;
    }

    @SuppressWarnings("unused")
    public double getTodayRevenue() {
        double todayRevenue = 0;
        LocalDate today = LocalDate.now();
        // Sửa lại câu query để lấy dữ liệu từ cả DatVe và VeMayBay
        String query = "SELECT COALESCE(SUM(TongGia), 0) FROM DatVe WHERE DATE(NgayDat) = CURRENT_DATE " + 
                      "AND TrangThai = 'Đã thanh toán' " +
                      "UNION ALL " +
                      "SELECT COALESCE(SUM(GiaVe), 0) FROM VeMayBay WHERE DATE(NgayDat) = CURRENT_DATE " +
                      "AND TrangThai = 'Đã thanh toán'";
    
        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()) {
                    todayRevenue += rs.getDouble(1);
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
        String query = "SELECT COUNT(*) FROM VeMayBay WHERE DATE(NgayDat) = CURRENT_DATE " +
                      "AND TrangThai = 'Đã thanh toán'";
    
        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                todayTickets = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todayTickets;
    }

    public double getMonthRevenue() {
        double monthRevenue = 0;
        String query = "SELECT COALESCE(SUM(GiaVe), 0) as total_revenue " +
                      "FROM VeMayBay " +
                      "WHERE MONTH(NgayDat) = MONTH(CURRENT_DATE) " +
                      "AND YEAR(NgayDat) = YEAR(CURRENT_DATE) " + 
                      "AND TrangThai = 'Đã thanh toán'";
    
        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                monthRevenue = rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthRevenue;
    }
    
    public int getFlightCountByMonth(int month) {
        int count = 0;
        String query = "SELECT COUNT(*) as total FROM ChuyenBay cb " +
                      "LEFT JOIN VeMayBay v ON cb.MaChuyenBay = v.MaChuyenBay " +
                      "WHERE MONTH(cb.NgayBay) = ? " +
                      "AND YEAR(cb.NgayBay) = YEAR(CURRENT_DATE) " +
                      "AND v.TrangThai IN ('Đã đặt', 'Đã thanh toán')";
    
        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public double getRevenueByMonth(int month) {
        double revenue = 0;
        String query = "SELECT " +
                      "COALESCE(SUM(CASE " +
                      "    WHEN v.TrangThai = 'Đã thanh toán' THEN v.GiaVe " +
                      "    ELSE 0 " +
                      "END), 0) as total_revenue " +
                      "FROM VeMayBay v " +
                      "JOIN ChuyenBay cb ON v.MaChuyenBay = cb.MaChuyenBay " +
                      "WHERE MONTH(v.NgayDat) = ? " +
                      "AND YEAR(v.NgayDat) = YEAR(CURRENT_DATE)";
    
        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, month);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    revenue = rs.getDouble("total_revenue");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    public int getTicketCountByClass(String hangVe) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM VeMayBay WHERE HangVe = ? AND TrangThai IN ('Đã đặt', 'Đã thanh toán')";
    
        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, hangVe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getTicketCountByDestination(String destination) {
        int count = 0;
        String query = "SELECT COUNT(v.MaVe) FROM VeMayBay v " +
                      "JOIN ChuyenBay c ON v.MaChuyenBay = c.MaChuyenBay " +
                      "WHERE c.DiemDen = ? AND v.TrangThai = 'Đã thanh toán'";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, destination);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Thêm các phương thức chi tiết hơn
    public Map<String, Integer> getTopDestinations(int limit) {
        Map<String, Integer> destinations = new LinkedHashMap<>();
        String query = "SELECT c.DiemDen, COUNT(*) as count " +
                    "FROM VeMayBay v " +
                    "JOIN ChuyenBay c ON v.MaChuyenBay = c.MaChuyenBay " +
                    "WHERE v.TrangThai IN ('Đã đặt', 'Đã thanh toán') " +
                    "GROUP BY c.DiemDen " +
                    "ORDER BY count DESC " +
                    "LIMIT ?";

        try (Connection connection = MYSQLDB.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    destinations.put(rs.getString("DiemDen"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return destinations;
    }

    public Map<String, Double> getAirlineRevenue() {
        Map<String, Double> revenue = new HashMap<>();
        String query = "SELECT h.TenHang, SUM(d.TongGia) as total_revenue " +
                      "FROM DatVe d " +
                      "JOIN ChuyenBay c ON d.MaChuyenBay = c.MaChuyenBay " +
                      "JOIN HangHangKhong h ON c.MaHang = h.MaHang " +
                      "WHERE d.TrangThai = 'Đã thanh toán' " +
                      "GROUP BY h.TenHang";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                revenue.put(rs.getString("TenHang"), rs.getDouble("total_revenue"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenue;
    }

    public Map<String, Object> getFlightStatistics() {
        Map<String, Object> stats = new HashMap<>();
        String query = "SELECT " +
                      "COUNT(*) as total_flights, " +
                      "COUNT(DISTINCT DiemDen) as unique_destinations, " +
                      "COUNT(DISTINCT MaHang) as unique_airlines " +
                      "FROM ChuyenBay";

        try (Connection connection = MYSQLDB.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                stats.put("totalFlights", rs.getInt("total_flights"));
                stats.put("uniqueDestinations", rs.getInt("unique_destinations"));
                stats.put("uniqueAirlines", rs.getInt("unique_airlines"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

}