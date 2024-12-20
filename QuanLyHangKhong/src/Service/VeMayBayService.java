package Service;

import Model.VeMayBay;
import Database.MYSQLDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeMayBayService {

    // Thêm vé máy bay
	public boolean addVeMayBay(VeMayBay veMayBay) {
	    String sql = "INSERT INTO VeMayBay (MaVe, MaChuyenBay, MaKhachHang, NgayDat) VALUES (?, ?, ?, ?)";
	    try (Connection conn = MYSQLDB.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, veMayBay.getMaVe());
	        stmt.setString(2, veMayBay.getMaChuyenBay());
	        stmt.setString(3, veMayBay.getMaKhachHang());
	        stmt.setDate(4, new java.sql.Date(veMayBay.getNgayDat().getTime()));

	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public boolean updateKhachHang(VeMayBay veMayBay) {
	    String sql = "UPDATE KhachHang SET TenKhachHang = ?, CMND = ? WHERE MaKhachHang = ?";
	    try (Connection conn = MYSQLDB.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, veMayBay.getTenKhachHang());
	        stmt.setString(2, veMayBay.getCmnd());
	        stmt.setString(3, veMayBay.getMaKhachHang());

	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

    // Cập nhật vé máy bay
	public boolean updateVeMayBay(VeMayBay veMayBay) {
	    String sql = "UPDATE VeMayBay SET MaChuyenBay = ?, MaKhachHang = ?, NgayDat = ? WHERE MaVe = ?";
	    try (Connection conn = MYSQLDB.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, veMayBay.getMaChuyenBay());
	        stmt.setString(2, veMayBay.getMaKhachHang());
	        stmt.setDate(3, new java.sql.Date(veMayBay.getNgayDat().getTime()));
	        stmt.setString(4, veMayBay.getMaVe());

	        int rowsAffected = stmt.executeUpdate();
	        if (rowsAffected > 0) {
	            // Cập nhật thông tin khách hàng
	            return updateKhachHang(veMayBay); 
	        } else {
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

    // Xóa vé máy bay
    public boolean deleteVeMayBay(String maVe) {
        String sql = "DELETE FROM VeMayBay WHERE MaVe = ?";
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, maVe);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<VeMayBay> getAllVeMayBay() {
        List<VeMayBay> veMayBayList = new ArrayList<>();
        String sql = "SELECT v.MaVe, v.MaChuyenBay, v.MaKhachHang, v.NgayDat, k.TenKhachHang, k.CMND " +
                     "FROM VeMayBay v " +
                     "JOIN KhachHang k ON v.MaKhachHang = k.MaKhachHang"; // Lấy tên khách hàng từ bảng KhachHang

        try (Connection conn = MYSQLDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String maVe = rs.getString("MaVe");
                String maChuyenBay = rs.getString("MaChuyenBay");
                String maKhachHang = rs.getString("MaKhachHang");
                Date ngayDat = rs.getDate("NgayDat");
                String tenKhachHang = rs.getString("TenKhachHang");  // Lấy tên khách hàng
                String cmnd = rs.getString("CMND");  // Lấy CMND

                // Tạo đối tượng VeMayBay với đầy đủ thông tin
                VeMayBay veMayBay = new VeMayBay(maVe, maChuyenBay, maKhachHang, ngayDat, tenKhachHang, cmnd);
                veMayBayList.add(veMayBay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veMayBayList;
    }

    public List<VeMayBay> searchVeMayBay(String searchQuery) {
        List<VeMayBay> filteredList = new ArrayList<>();
        String sql = "SELECT v.MaVe, v.MaChuyenBay, v.MaKhachHang, v.NgayDat, k.TenKhachHang, k.CMND " +
                     "FROM VeMayBay v " +
                     "JOIN KhachHang k ON v.MaKhachHang = k.MaKhachHang " +
                     "WHERE v.MaVe LIKE ? OR k.CMND LIKE ? OR k.TenKhachHang LIKE ?";  // Tìm theo mã vé, CMND hoặc tên khách hàng

        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + searchQuery + "%");
            stmt.setString(2, "%" + searchQuery + "%");
            stmt.setString(3, "%" + searchQuery + "%");  // Thêm điều kiện tìm kiếm theo tên khách hàng

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String maVe = rs.getString("MaVe");
                    String maChuyenBay = rs.getString("MaChuyenBay");
                    String maKhachHang = rs.getString("MaKhachHang");
                    Date ngayDat = rs.getDate("NgayDat");
                    String tenKhachHang = rs.getString("TenKhachHang");  // Lấy tên khách hàng
                    String cmnd = rs.getString("CMND");  // Lấy CMND

                    // Tạo đối tượng VeMayBay với đầy đủ tham số
                    VeMayBay veMayBay = new VeMayBay(maVe, maChuyenBay, maKhachHang, ngayDat, tenKhachHang, cmnd);
                    filteredList.add(veMayBay);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredList;
    }

    public boolean isTicketExists(String maVe) {
        String sql = "SELECT COUNT(*) FROM VeMayBay WHERE MaVe = ?";
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maVe);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
