package Service;

import Database.MYSQLDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrangChuService {

    public void capNhatDuLieu() {
        // Kiểm tra kết nối để đảm bảo cơ sở dữ liệu sẵn sàng
        try (Connection connection = MYSQLDB.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Không thể cập nhật dữ liệu do lỗi kết nối!");
        }
    }

    public int layTongChuyenBay() {
        int totalFlights = 0;
        String query = "SELECT COUNT(*) FROM chuyenbay";  // Giả sử bảng chuyenbay chứa thông tin chuyến bay

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
        String query = "SELECT COUNT(*) FROM nhanvien";  // Giả sử bảng nhanvien chứa thông tin nhân viên

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
        String query = "SELECT COUNT(*) FROM VeMayBay";  // Giả sử bảng VeMayBay chứa thông tin vé máy bay

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
}
