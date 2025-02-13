package Service;

import Database.MYSQLDB;
import Model.ThongKe;

import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

public class ThongKeService {

    public Map<String, Object> getOverallStatistics() {
        Map<String, Object> overallStats = new HashMap<>();
        Connection connection = MYSQLDB.getConnection();

        if (connection != null) {
            try {
                // Thống kê số lượng bản ghi cho từng bảng
                Map<String, Integer> tableRecordCounts = new HashMap<>();
                String[] tables = {
                    "UserAccount", "SanBay", "HangHangKhong", "ChuyenBay", 
                    "LichBay", "NhanVien", "KhachHang", "VeMayBay", 
                    "MayBay", "LichSuThanhToan"
                };

                for (String table : tables) {
                    String countSql = "SELECT COUNT(*) AS RecordCount FROM " + table;
                    try (Statement stmt = connection.createStatement();
                         ResultSet rs = stmt.executeQuery(countSql)) {
                        if (rs.next()) {
                            tableRecordCounts.put(table, rs.getInt("RecordCount"));
                        }
                    }
                }
                overallStats.put("TableRecordCounts", tableRecordCounts);

                // Thống kê doanh thu
                String revenueSql = "SELECT " +
                    "SUM(GiaVe) AS TongDoanhThu, " +
                    "AVG(GiaVe) AS DoanhThuTrungBinh, " +
                    "COUNT(DISTINCT MaChuyenBay) AS SoChuyenBay, " +
                    "COUNT(DISTINCT MaKhachHang) AS SoKhachHang " +
                    "FROM VeMayBay WHERE TrangThai = 'Đã thanh toán'";
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(revenueSql)) {
                    if (rs.next()) {
                        overallStats.put("TotalRevenue", rs.getDouble("TongDoanhThu"));
                        overallStats.put("AverageTicketPrice", rs.getDouble("DoanhThuTrungBinh"));
                        overallStats.put("TotalFlights", rs.getInt("SoChuyenBay"));
                        overallStats.put("TotalCustomers", rs.getInt("SoKhachHang"));
                    }
                }

                // Thống kê nhân viên theo chức vụ
                String employeeSql = "SELECT ChucVu, COUNT(*) AS SoLuong, AVG(Luong) AS LuongTrungBinh " +
                    "FROM NhanVien GROUP BY ChucVu";
                List<Map<String, Object>> employeeStats = new ArrayList<>();
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(employeeSql)) {
                    while (rs.next()) {
                        Map<String, Object> employeeTypeStats = new HashMap<>();
                        employeeTypeStats.put("ChucVu", rs.getString("ChucVu"));
                        employeeTypeStats.put("SoLuong", rs.getInt("SoLuong"));
                        employeeTypeStats.put("LuongTrungBinh", rs.getDouble("LuongTrungBinh"));
                        employeeStats.add(employeeTypeStats);
                    }
                }
                overallStats.put("EmployeeStatistics", employeeStats);

                // Thống kê tình trạng chuyến bay
                String flightStatusSql = "SELECT TinhTrang, COUNT(*) AS SoLuong " +
                    "FROM ChuyenBay GROUP BY TinhTrang";
                List<Map<String, Object>> flightStatusStats = new ArrayList<>();
                try (Statement stmt = connection.createStatement();
                     ResultSet rs = stmt.executeQuery(flightStatusSql)) {
                    while (rs.next()) {
                        Map<String, Object> statusStats = new HashMap<>();
                        statusStats.put("TinhTrang", rs.getString("TinhTrang"));
                        statusStats.put("SoLuong", rs.getInt("SoLuong"));
                        flightStatusStats.add(statusStats);
                    }
                }
                overallStats.put("FlightStatusStatistics", flightStatusStats);

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }

        return overallStats;
    }

    // Lấy tất cả thống kê chuyến bay
    public List<ThongKe> getAllThongKe() {
        List<ThongKe> thongKeList = new ArrayList<>();
        Connection connection = MYSQLDB.getConnection();

        if (connection != null) {
            String sql = "SELECT c.MaChuyenBay, sb.TenSanBay, c.ChangBay, c.NgayBay, " +
                         "COUNT(v.MaVe) AS SoVeDaDat, c.TinhTrang " +
                         "FROM ChuyenBay c " +
                         "JOIN SanBay sb ON c.MaSanBay = sb.MaSanBay " +
                         "LEFT JOIN VeMayBay v ON c.MaChuyenBay = v.MaChuyenBay " +
                         "GROUP BY c.MaChuyenBay, sb.TenSanBay, c.ChangBay, c.NgayBay, c.TinhTrang";

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    ThongKe thongKe = new ThongKe(
                        rs.getString("MaChuyenBay"),
                        rs.getString("TenSanBay"),
                        rs.getString("ChangBay"),
                        rs.getString("NgayBay"),
                        rs.getInt("SoVeDaDat"),
                        rs.getString("TinhTrang")
                    );
                    thongKeList.add(thongKe);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return thongKeList;
    }

    // Xuất thống kê ra file CSV
    @SuppressWarnings("unchecked")
    public void exportOverallStatisticsToCSV(Map<String, Object> overallStats) {
        try (FileWriter writer = new FileWriter("ThongKeHeThong.csv")) {
            // Ghi số lượng bản ghi từng bảng
            writer.append("Bảng, Số Lượng Bản Ghi\n");
            Map<String, Integer> tableCounts = (Map<String, Integer>) overallStats.get("TableRecordCounts");
            for (Map.Entry<String, Integer> entry : tableCounts.entrySet()) {
                writer.append(entry.getKey()).append(", ")
                      .append(String.valueOf(entry.getValue())).append("\n");
            }

            // Ghi thống kê doanh thu
            writer.append("\nThống Kê Doanh Thu\n");
            writer.append("Tổng Doanh Thu, ")
                  .append(String.valueOf(overallStats.get("TotalRevenue"))).append("\n");
            writer.append("Giá Vé Trung Bình, ")
                  .append(String.valueOf(overallStats.get("AverageTicketPrice"))).append("\n");
            writer.append("Tổng Số Chuyến Bay, ")
                  .append(String.valueOf(overallStats.get("TotalFlights"))).append("\n");
            writer.append("Tổng Số Khách Hàng, ")
                  .append(String.valueOf(overallStats.get("TotalCustomers"))).append("\n");

            // Ghi thống kê nhân viên
            writer.append("\nThống Kê Nhân Viên\n");
            writer.append("Chức Vụ, Số Lượng, Lương Trung Bình\n");
            List<Map<String, Object>> employeeStats = 
                (List<Map<String, Object>>) overallStats.get("EmployeeStatistics");
            for (Map<String, Object> stat : employeeStats) {
                writer.append(stat.get("ChucVu").toString()).append(", ")
                      .append(stat.get("SoLuong").toString()).append(", ")
                      .append(stat.get("LuongTrungBinh").toString()).append("\n");
            }

            // Ghi thống kê tình trạng chuyến bay
            writer.append("\nThống Kê Tình Trạng Chuyến Bay\n");
            writer.append("Tình Trạng, Số Lượng\n");
            List<Map<String, Object>> flightStatusStats = 
                (List<Map<String, Object>>) overallStats.get("FlightStatusStatistics");
            for (Map<String, Object> stat : flightStatusStats) {
                writer.append(stat.get("TinhTrang").toString()).append(", ")
                      .append(stat.get("SoLuong").toString()).append("\n");
            }

            JOptionPane.showMessageDialog(null, "Đã xuất báo cáo thống kê hệ thống ra file CSV!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất báo cáo!");
            e.printStackTrace();
        }
    }

    // Lọc dữ liệu theo tình trạng chuyến bay
    public List<ThongKe> filterThongKeByStatus(String status) {
        List<ThongKe> thongKeList = new ArrayList<>();
        Connection connection = MYSQLDB.getConnection();

        if (connection != null) {
            String sql = "SELECT c.MaChuyenBay, sb.TenSanBay, c.ChangBay, c.NgayBay, " +
                         "COUNT(v.MaVe) AS SoVeDaDat, c.TinhTrang " +
                         "FROM ChuyenBay c " +
                         "JOIN SanBay sb ON c.MaSanBay = sb.MaSanBay " +
                         "LEFT JOIN VeMayBay v ON c.MaChuyenBay = v.MaChuyenBay " +
                         "WHERE c.TinhTrang = ? " +
                         "GROUP BY c.MaChuyenBay, sb.TenSanBay, c.ChangBay, c.NgayBay, c.TinhTrang";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, status);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        ThongKe thongKe = new ThongKe(
                            rs.getString("MaChuyenBay"),
                            rs.getString("TenSanBay"),
                            rs.getString("ChangBay"),
                            rs.getString("NgayBay"),
                            rs.getInt("SoVeDaDat"),
                            rs.getString("TinhTrang")
                        );
                        thongKeList.add(thongKe);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return thongKeList;
    }
    
    public boolean updateThongKe(ThongKe thongKe) {
        Connection connection = MYSQLDB.getConnection();
        if (connection != null) {
            String sql = "UPDATE ChuyenBay SET " +
                         "ChangBay = ?, " +
                         "NgayBay = ?, " +
                         "TinhTrang = ? " +
                         "WHERE MaChuyenBay = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, thongKe.getChangBay());
                stmt.setString(2, thongKe.getNgayBay());
                stmt.setString(3, thongKe.getTinhTrang());
                stmt.setString(4, thongKe.getMaChuyenBay());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    // Kiểm tra xem liệu chuyến bay đã được cập nhật chưa
                    System.out.println("Cập nhật thành công: " + thongKe.getMaChuyenBay());
                    return true;
                } else {
                    // Nếu không có chuyến bay nào được cập nhật
                    System.out.println("Không tìm thấy chuyến bay với mã: " + thongKe.getMaChuyenBay());
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return false;  // Lỗi kết nối cơ sở dữ liệu
    }



    public ThongKe findThongKeByCode(String maChuyenBay) {
        ThongKe thongKe = null;
        Connection connection = MYSQLDB.getConnection();

        if (connection != null) {
            String sql = "SELECT c.MaChuyenBay, sb.TenSanBay, c.ChangBay, c.NgayBay, " +
                         "COUNT(v.MaVe) AS SoVeDaDat, c.TinhTrang " +
                         "FROM ChuyenBay c " +
                         "JOIN SanBay sb ON c.MaSanBay = sb.MaSanBay " +
                         "LEFT JOIN VeMayBay v ON c.MaChuyenBay = v.MaChuyenBay " +
                         "WHERE c.MaChuyenBay = ? " +
                         "GROUP BY c.MaChuyenBay, sb.TenSanBay, c.ChangBay, c.NgayBay, c.TinhTrang";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, maChuyenBay);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        thongKe = new ThongKe(
                            rs.getString("MaChuyenBay"),
                            rs.getString("TenSanBay"),
                            rs.getString("ChangBay"),
                            rs.getString("NgayBay"),
                            rs.getInt("SoVeDaDat"),
                            rs.getString("TinhTrang")
                        );
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MYSQLDB.closeConnection(connection);
            }
        }
        return thongKe;
    }
}
