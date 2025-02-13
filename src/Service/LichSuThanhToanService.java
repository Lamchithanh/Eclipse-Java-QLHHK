package Service;

import Model.LichSuThanhToan;
import Database.MYSQLDB;
import java.sql.*;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
public class LichSuThanhToanService {
    public void addPayment(LichSuThanhToan payment) {
        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            connection = MYSQLDB.getConnection();
            String sql = "INSERT INTO LichSuThanhToan (MaVe, NgayThanhToan, SoTien, PhuongThucThanhToan) " +
                        "VALUES (?, ?, ?, ?)";
            
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, payment.getMaVe());
            stmt.setTimestamp(2, Timestamp.valueOf(payment.getNgayThanhToan()));
            stmt.setDouble(3, payment.getSoTien());
            stmt.setString(4, payment.getPhuongThucThanhToan());
            
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