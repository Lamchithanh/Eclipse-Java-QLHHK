package Service;

import Model.VeMayBay;
import Database.MYSQLDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeMayBayService {

    // Thêm vé máy bay
    public boolean addVeMayBay(VeMayBay veMayBay) {
        String sqlVeMayBay = "INSERT INTO VeMayBay (MaVe, MaChuyenBay, MaKhachHang, NgayDat, GiaVe, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlKhachHang = "INSERT INTO KhachHang (MaKhachHang, TenKhachHang, CMND, SoDienThoai, DiaChi, Email, NgaySinh, QuocTich) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlCheckKhachHang = "SELECT MaKhachHang FROM KhachHang WHERE MaKhachHang = ?";
        
        Connection conn = null;
        try {
            conn = MYSQLDB.getConnection();
            conn.setAutoCommit(false);
    
            // Kiểm tra xem mã khách hàng đã tồn tại chưa
            String maKhachHang = veMayBay.getMaKhachHang();
            boolean khachHangExists = false;
            
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheckKhachHang)) {
                checkStmt.setString(1, maKhachHang);
                ResultSet rs = checkStmt.executeQuery();
                khachHangExists = rs.next();
            }
    
            // Nếu khách hàng đã tồn tại, tạo mã khách hàng mới
            if (khachHangExists) {
                maKhachHang = generateNewCustomerId();
                veMayBay.setMaKhachHang(maKhachHang);
            }
    
            // Thêm khách hàng mới
            try (PreparedStatement stmtKhachHang = conn.prepareStatement(sqlKhachHang)) {
                stmtKhachHang.setString(1, maKhachHang);
                stmtKhachHang.setString(2, veMayBay.getTenKhachHang());
                stmtKhachHang.setString(3, veMayBay.getCmnd());
                stmtKhachHang.setString(4, veMayBay.getSoDienThoai());
                stmtKhachHang.setString(5, veMayBay.getDiaChi());
                stmtKhachHang.setString(6, veMayBay.getEmail());
                stmtKhachHang.setDate(7, veMayBay.getNgaySinh());
                stmtKhachHang.setString(8, veMayBay.getQuocTich());
                stmtKhachHang.executeUpdate();
            }
    
            // Thêm vé máy bay
            try (PreparedStatement stmtVeMayBay = conn.prepareStatement(sqlVeMayBay)) {
                double giaVe = veMayBay.getGiaVe() > 0 ? 
                    veMayBay.getGiaVe() : 
                    getTicketPrice(veMayBay.getMaChuyenBay());
    
                stmtVeMayBay.setString(1, veMayBay.getMaVe());
                stmtVeMayBay.setString(2, veMayBay.getMaChuyenBay());
                stmtVeMayBay.setString(3, maKhachHang);
                stmtVeMayBay.setDate(4, veMayBay.getNgayDat());
                stmtVeMayBay.setDouble(5, giaVe);
                stmtVeMayBay.setString(6, 
                    veMayBay.getTrangThai() != null ? 
                    veMayBay.getTrangThai() : 
                    "Đã đặt");
                
                int rowsAffected = stmtVeMayBay.executeUpdate();
                
                conn.commit();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm vé máy bay: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Cập nhật vé máy bay
    public boolean updateVeMayBay(VeMayBay veMayBay) throws SQLException {
        String sqlUpdateVeMayBay = "UPDATE VeMayBay SET MaChuyenBay = ?, NgayDat = ?, GiaVe = ?, TrangThai = ? WHERE MaVe = ?";
        String sqlUpdateKhachHang = "UPDATE KhachHang SET TenKhachHang = ?, CMND = ?, SoDienThoai = ?, DiaChi = ?, " +
                                   "Email = ?, NgaySinh = ?, QuocTich = ? WHERE MaKhachHang = ?";
        String sqlCheckCMND = "SELECT MaKhachHang FROM KhachHang WHERE CMND = ? AND MaKhachHang != ?";
        
        Connection conn = null;
        try {
            conn = MYSQLDB.getConnection();
            conn.setAutoCommit(false);
    
            // Kiểm tra CMND trùng
            if (veMayBay.getCmnd() != null && !veMayBay.getCmnd().isEmpty()) {
                try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheckCMND)) {
                    checkStmt.setString(1, veMayBay.getCmnd());
                    checkStmt.setString(2, veMayBay.getMaKhachHang());
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        throw new SQLException("CMND đã tồn tại trong hệ thống với khách hàng khác!");
                    }
                }
            }
    
            // Cập nhật thông tin khách hàng trước
            try (PreparedStatement stmtKhachHang = conn.prepareStatement(sqlUpdateKhachHang)) {
                stmtKhachHang.setString(1, veMayBay.getTenKhachHang());
                stmtKhachHang.setString(2, veMayBay.getCmnd());
                stmtKhachHang.setString(3, veMayBay.getSoDienThoai());
                stmtKhachHang.setString(4, veMayBay.getDiaChi());
                stmtKhachHang.setString(5, veMayBay.getEmail());
                stmtKhachHang.setDate(6, veMayBay.getNgaySinh());
                stmtKhachHang.setString(7, veMayBay.getQuocTich());
                stmtKhachHang.setString(8, veMayBay.getMaKhachHang());
                
                int khachHangRowsAffected = stmtKhachHang.executeUpdate();
                if (khachHangRowsAffected <= 0) {
                    throw new SQLException("Không thể cập nhật thông tin khách hàng!");
                }
            }
    
            // Sau đó cập nhật vé máy bay
            try (PreparedStatement stmtVeMayBay = conn.prepareStatement(sqlUpdateVeMayBay)) {
                stmtVeMayBay.setString(1, veMayBay.getMaChuyenBay());
                stmtVeMayBay.setDate(2, veMayBay.getNgayDat());
                stmtVeMayBay.setDouble(3, veMayBay.getGiaVe());
                stmtVeMayBay.setString(4, veMayBay.getTrangThai());
                stmtVeMayBay.setString(5, veMayBay.getMaVe());
    
                int veMayBayRowsAffected = stmtVeMayBay.executeUpdate();
                if (veMayBayRowsAffected <= 0) {
                    throw new SQLException("Không thể cập nhật thông tin vé máy bay!");
                }
            }
    
            // Nếu cả hai câu lệnh UPDATE đều thành công, commit transaction
            conn.commit();
            return true;
    
        } catch (SQLException e) {
            // Rollback transaction nếu có lỗi
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // Phương thức phụ trợ để tạo mã khách hàng mới
    private String generateNewCustomerId() {
        String sql = "SELECT MAX(MaKhachHang) FROM KhachHang";
        try (Connection conn = MYSQLDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                String lastId = rs.getString(1);
                if (lastId != null) {
                    // Giả sử mã có định dạng "KHxxx"
                    int number = Integer.parseInt(lastId.substring(2)) + 1;
                    return String.format("KH%03d", number);
                }
            }
            return "KH001"; // Mã đầu tiên nếu bảng trống
        } catch (SQLException e) {
            e.printStackTrace();
            return "KH" + System.currentTimeMillis(); // Fallback nếu có lỗi
        }
    }
    
    public double getTicketPrice(String maChuyenBay) {
        Connection conn = null;
        String sql = "SELECT SoGhe FROM ChuyenBay WHERE MaChuyenBay = ?";
        
        try {
            conn = MYSQLDB.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, maChuyenBay);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int soGhe = rs.getInt("SoGhe");
                        // Ví dụ: Tính giá vé dựa trên số ghế
                        return soGhe * 100000.0; // Giả sử mỗi ghế 100,000 VND
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return 500000.0; // Giá vé mặc định nếu không tìm thấy
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

    // Lấy tất cả vé máy bay
    public List<VeMayBay> getAllVeMayBay() {
        List<VeMayBay> veMayBayList = new ArrayList<>();
        String sql = "SELECT v.MaVe, v.MaChuyenBay, v.MaKhachHang, v.NgayDat, v.GiaVe, v.TrangThai, " +
                     "k.TenKhachHang, k.CMND, k.SoDienThoai, k.DiaChi, k.Email, k.NgaySinh, k.QuocTich " +
                     "FROM VeMayBay v " +
                     "JOIN KhachHang k ON v.MaKhachHang = k.MaKhachHang";
    
        try (Connection conn = MYSQLDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                VeMayBay veMayBay = new VeMayBay(
                    rs.getString("MaVe"),
                    rs.getString("MaChuyenBay"),
                    rs.getString("MaKhachHang"),
                    rs.getDate("NgayDat"),
                    rs.getString("TenKhachHang"),
                    rs.getString("CMND"),
                    rs.getDouble("GiaVe"),
                    rs.getString("TrangThai"),
                    rs.getString("SoDienThoai"),
                    rs.getString("DiaChi"),
                    rs.getString("Email"),
                    rs.getDate("NgaySinh"),
                    rs.getString("QuocTich")
                );
                
                veMayBayList.add(veMayBay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return veMayBayList;
    }

    // Tìm kiếm vé máy bay
    public List<VeMayBay> searchVeMayBay(String searchQuery) {
        List<VeMayBay> filteredList = new ArrayList<>();
        String sql = "SELECT v.MaVe, v.MaChuyenBay, v.MaKhachHang, v.NgayDat, v.GiaVe, v.TrangThai, " +
                     "k.TenKhachHang, k.CMND " +
                     "FROM VeMayBay v " +
                     "JOIN KhachHang k ON v.MaKhachHang = k.MaKhachHang " +
                     "WHERE v.MaVe LIKE ? OR " +
                     "v.MaChuyenBay LIKE ? OR " +
                     "v.MaKhachHang LIKE ? OR " +
                     "k.TenKhachHang LIKE ? OR " +
                     "k.CMND LIKE ? OR " +
                     "v.TrangThai LIKE ? OR " +
                     "v.NgayDat LIKE ? OR " +
                     "CAST(v.GiaVe AS CHAR) LIKE ?";
    
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchParam = "%" + searchQuery + "%";
            
            // Đặt tất cả các tham số tìm kiếm
            for (int i = 1; i <= 8; i++) {
                stmt.setString(i, searchParam);
            }
    
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    VeMayBay veMayBay = new VeMayBay(
                        rs.getString("MaVe"),
                        rs.getString("MaChuyenBay"),
                        rs.getString("MaKhachHang"),
                        rs.getDate("NgayDat"),
                        rs.getString("TenKhachHang"),
                        rs.getString("CMND")
                    );
                    
                    // Bổ sung thêm thông tin
                    veMayBay.setGiaVe(rs.getDouble("GiaVe"));
                    veMayBay.setTrangThai(rs.getString("TrangThai"));
                    
                    filteredList.add(veMayBay);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredList;
    }

    public List<VeMayBay> advancedSearchVeMayBay(String maVe, String maChuyenBay, 
                                             String tenKhachHang, String cmnd, 
                                             String trangThai, String giaVeMin, String giaVeMax) {
        List<VeMayBay> filteredList = new ArrayList<>();
        
        // Xây dựng câu truy vấn động
        StringBuilder sqlBuilder = new StringBuilder(
            "SELECT v.MaVe, v.MaChuyenBay, v.MaKhachHang, v.NgayDat, v.GiaVe, v.TrangThai, " +
            "k.TenKhachHang, k.CMND " +
            "FROM VeMayBay v " +
            "JOIN KhachHang k ON v.MaKhachHang = k.MaKhachHang " +
            "WHERE 1=1 "
        );
        
        List<Object> params = new ArrayList<>();
        
        // Thêm điều kiện cho từng trường
        if (!maVe.isEmpty() && !maVe.equals("Mã vé")) {
            sqlBuilder.append("AND v.MaVe LIKE ? ");
            params.add("%" + maVe + "%");
        }
        
        if (!maChuyenBay.isEmpty() && !maChuyenBay.equals("Mã chuyến bay")) {
            sqlBuilder.append("AND v.MaChuyenBay LIKE ? ");
            params.add("%" + maChuyenBay + "%");
        }
        
        if (!tenKhachHang.isEmpty() && !tenKhachHang.equals("Tên khách hàng")) {
            sqlBuilder.append("AND k.TenKhachHang LIKE ? ");
            params.add("%" + tenKhachHang + "%");
        }
        
        if (!cmnd.isEmpty() && !cmnd.equals("CMND")) {
            sqlBuilder.append("AND k.CMND LIKE ? ");
            params.add("%" + cmnd + "%");
        }
        
        if (trangThai != null && !trangThai.equals("Tất cả")) {
            sqlBuilder.append("AND v.TrangThai = ? ");
            params.add(trangThai);
        }
        
        // Xử lý giá vé
        double minPrice = 0, maxPrice = Double.MAX_VALUE;
        try {
            if (!giaVeMin.isEmpty() && !giaVeMin.equals("Giá vé từ")) {
                minPrice = Double.parseDouble(giaVeMin);
                sqlBuilder.append("AND v.GiaVe >= ? ");
                params.add(minPrice);
            }
            
            if (!giaVeMax.isEmpty() && !giaVeMax.equals("Giá vé đến")) {
                maxPrice = Double.parseDouble(giaVeMax);
                sqlBuilder.append("AND v.GiaVe <= ? ");
                params.add(maxPrice);
            }
        } catch (NumberFormatException e) {
            // Bỏ qua nếu không parse được giá vé
        }

        try (Connection conn = MYSQLDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
            
            // Đặt các tham số
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    VeMayBay veMayBay = new VeMayBay(
                        rs.getString("MaVe"),
                        rs.getString("MaChuyenBay"),
                        rs.getString("MaKhachHang"),
                        rs.getDate("NgayDat"),
                        rs.getString("TenKhachHang"),
                        rs.getString("CMND")
                    );
                    
                    veMayBay.setGiaVe(rs.getDouble("GiaVe"));
                    veMayBay.setTrangThai(rs.getString("TrangThai"));
                    
                    filteredList.add(veMayBay);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return filteredList;
    }

    // Kiểm tra vé đã tồn tại
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