package Service;

import Database.MYSQLDB;
import Model.LichBay;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichBayService {
    // Fetch all flight schedules
    public List<LichBay> getAllLichBay() {
        List<LichBay> lichBayList = new ArrayList<>();
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "SELECT lb.MaChuyenBay, lb.GioKhoiHanh, lb.GioHaCanh, lb.ThoiGianBay " +
                         "FROM LichBay lb JOIN ChuyenBay cb ON lb.MaChuyenBay = cb.MaChuyenBay";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    // Assuming GioKhoiHanh and GioHaCanh are Time fields, you can retrieve them as Strings
                    LichBay lichBay = new LichBay(
                        rs.getString("MaChuyenBay"),
                        rs.getString("GioKhoiHanh"),
                        rs.getString("GioHaCanh"),
                        rs.getInt("ThoiGianBay")
                    );
                    lichBayList.add(lichBay);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        
        return lichBayList;
    }
    
    public boolean isFlightCodeExists(String flightCode) {
        // Kiểm tra mã chuyến bay trong cơ sở dữ liệu
        return getAllLichBay().stream()
                .anyMatch(lichBay -> lichBay.getMaChuyenBay().equalsIgnoreCase(flightCode));
    }

    // Add a new flight schedule
    public void addLichBay(LichBay lichBay) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "INSERT INTO LichBay (MaChuyenBay, GioKhoiHanh, GioHaCanh, ThoiGianBay) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, lichBay.getMaChuyenBay());
                pstmt.setString(2, lichBay.getGioKhoiHanh());  // Set Time as String
                pstmt.setString(3, lichBay.getGioHaCanh());    // Set Time as String
                pstmt.setInt(4, lichBay.getThoiGianBay());
                
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
    }

    // Kiểm tra trùng giờ khởi hành và giờ hạ cánh
    public boolean isDepartureOrArrivalTimeConflict(String flightCode, String departureTime, String arrivalTime) {
        List<LichBay> lichBayList = getAllLichBay();
        
        for (LichBay lichBay : lichBayList) {
            // Kiểm tra nếu cùng mã chuyến bay và trùng giờ khởi hành hoặc giờ hạ cánh
            if (lichBay.getMaChuyenBay().equals(flightCode)) {
                // Kiểm tra trùng giờ khởi hành
                if (lichBay.getGioKhoiHanh().equals(departureTime) || lichBay.getGioHaCanh().equals(arrivalTime)) {
                    return true; // Có trùng lặp giờ
                }
            }
        }
        
        return false; // Không có trùng lặp giờ
    }

    // Update an existing flight schedule
    public void updateLichBay(LichBay lichBay) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "UPDATE LichBay SET GioKhoiHanh=?, GioHaCanh=?, ThoiGianBay=? WHERE MaChuyenBay=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, lichBay.getGioKhoiHanh());  // Set Time as String
                pstmt.setString(2, lichBay.getGioHaCanh());    // Set Time as String
                pstmt.setInt(3, lichBay.getThoiGianBay());
                pstmt.setString(4, lichBay.getMaChuyenBay());
                
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
    }

    // Delete a flight schedule
    public void deleteLichBay(String maChuyenBay) {
        Connection connection = MYSQLDB.getConnection();
        
        if (connection != null) {
            String sql = "DELETE FROM LichBay WHERE MaChuyenBay=?";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, maChuyenBay);
                
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
    }
}
