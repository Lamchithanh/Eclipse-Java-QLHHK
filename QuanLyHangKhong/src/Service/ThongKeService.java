package Service;

import Database.MYSQLDB;
import Model.ThongKe;

import java.sql.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class ThongKeService {

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
    public void exportThongKeToCSV(List<ThongKe> thongKeList) {
        try (FileWriter writer = new FileWriter("ThongKeChuyenBay.csv")) {
            writer.append("Mã Chuyến Bay, Sân Bay, Chặng Bay, Ngày Bay, Số Vé Đã Đặt, Tình Trạng\n");
            for (ThongKe thongKe : thongKeList) {
                writer.append(thongKe.getMaChuyenBay()).append(", ")
                      .append(thongKe.getTenSanBay()).append(", ")
                      .append(thongKe.getChangBay()).append(", ")
                      .append(thongKe.getNgayBay()).append(", ")
                      .append(String.valueOf(thongKe.getSoVeDaDat())).append(", ")
                      .append(thongKe.getTinhTrang()).append("\n");
            }
            JOptionPane.showMessageDialog(null, "Đã xuất báo cáo ra file CSV!");
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
