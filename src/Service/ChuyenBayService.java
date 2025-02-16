package Service;

import Model.ChuyenBay;
import Database.MYSQLDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChuyenBayService {

	private static final Logger LOGGER = Logger.getLogger(ChuyenBayService.class.getName());

	// Lấy danh sách chuyến bay
	public List<ChuyenBay> getAllChuyenBays() {
	    List<ChuyenBay> danhSachChuyenBay = new ArrayList<>();
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conn = MYSQLDB.getConnection();
	        if (conn == null) {
	            LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
	            return danhSachChuyenBay;
	        }

	        // Thêm join với bảng LichBay và SanBay
	        String query = """
	            SELECT c.*, l.GioKhoiHanh, l.GioHaCanh, l.ThoiGianBay, s.TenSanBay
	            FROM ChuyenBay c
	            LEFT JOIN LichBay l ON c.MaChuyenBay = l.MaChuyenBay
	            JOIN SanBay s ON c.MaSanBay = s.MaSanBay
	        """;
	        stmt = conn.prepareStatement(query);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            ChuyenBay chuyenBay = new ChuyenBay(
	                rs.getString("MaChuyenBay"),
	                rs.getString("MaSanBay"),
	                rs.getString("ChangBay"),
	                rs.getString("NgayBay"),
	                rs.getString("NhaGa"),
	                rs.getInt("SoGhe"),
	                rs.getString("TinhTrang"),
	                rs.getString("MaMayBay"),
	                rs.getString("MaHang"),
	                rs.getString("DiemDi"),
	                rs.getString("DiemDen")
	            );
	            chuyenBay.setGiaVe(rs.getDouble("GiaVe"));

	            // Thêm thông tin từ LichBay
	            chuyenBay.setGioKhoiHanh(rs.getString("GioKhoiHanh"));
	            chuyenBay.setGioHaCanh(rs.getString("GioHaCanh"));
	            chuyenBay.setThoiGianBay(rs.getInt("ThoiGianBay"));

	            // Lấy tên sân bay từ bảng SanBay
	            String tenSanBay = rs.getString("TenSanBay");
	            chuyenBay.setTenSanBay(tenSanBay);

	            danhSachChuyenBay.add(chuyenBay);
	        }
	    } catch (SQLException e) {
	        LOGGER.log(Level.SEVERE, "Lỗi khi lấy danh sách chuyến bay", e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conn != null) MYSQLDB.closeConnection(conn);
	        } catch (SQLException e) {
	            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối", e);
	        }
	    }
	    return danhSachChuyenBay;
	}

	public ChuyenBay getChuyenBayById(String maChuyenBay) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conn = MYSQLDB.getConnection();
	        if (conn == null) {
	            LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
	            return null;
	        }

	        // Thêm join với bảng LichBay
	        String query = """
	            SELECT c.*, l.GioKhoiHanh, l.GioHaCanh, l.ThoiGianBay 
	            FROM ChuyenBay c 
	            LEFT JOIN LichBay l ON c.MaChuyenBay = l.MaChuyenBay 
	            WHERE c.MaChuyenBay = ?
	        """;
	        stmt = conn.prepareStatement(query);
	        stmt.setString(1, maChuyenBay);
	        
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            ChuyenBay chuyenBay = new ChuyenBay(
	                rs.getString("MaChuyenBay"),
	                rs.getString("MaSanBay"),
	                rs.getString("ChangBay"),
	                rs.getString("NgayBay"),
	                rs.getString("NhaGa"),
	                rs.getInt("SoGhe"),
	                rs.getString("TinhTrang"),
	                rs.getString("MaMayBay"),
	                rs.getString("MaHang"),
	                rs.getString("DiemDi"),
	                rs.getString("DiemDen")
	            );
	            chuyenBay.setGiaVe(rs.getDouble("GiaVe"));
	            
	            // Thêm thông tin từ LichBay
	            chuyenBay.setGioKhoiHanh(rs.getString("GioKhoiHanh"));
	            chuyenBay.setGioHaCanh(rs.getString("GioHaCanh"));
	            chuyenBay.setThoiGianBay(rs.getInt("ThoiGianBay"));
	            
	            return chuyenBay;
	        }
	    } catch (SQLException e) {
	        LOGGER.log(Level.SEVERE, "Lỗi khi lấy chuyến bay theo mã", e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conn != null) MYSQLDB.closeConnection(conn);
	        } catch (SQLException e) {
	            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối", e);
	        }
	    }
	    
	    return null;
	}

	// Thêm phương thức để thêm/cập nhật thông tin LichBay
	public void themLichBay(String maChuyenBay, String gioKhoiHanh, String gioHaCanh, int thoiGianBay) throws SQLException {
	    Connection conn = null;
	    PreparedStatement stmt = null;

	    try {
	        conn = MYSQLDB.getConnection();
	        if (conn == null) {
	            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
	        }

	        // Sử dụng INSERT ... ON DUPLICATE KEY UPDATE để xử lý trường hợp đã tồn tại
	        String insertQuery = """
	            INSERT INTO LichBay (MaChuyenBay, GioKhoiHanh, GioHaCanh, ThoiGianBay) 
	            VALUES (?, ?, ?, ?) 
	            ON DUPLICATE KEY UPDATE 
	            GioKhoiHanh = ?, 
	            GioHaCanh = ?, 
	            ThoiGianBay = ?
	        """;

	        stmt = conn.prepareStatement(insertQuery);
	        stmt.setString(1, maChuyenBay);
	        stmt.setString(2, gioKhoiHanh);
	        stmt.setString(3, gioHaCanh);
	        stmt.setInt(4, thoiGianBay);
	        
	        // Các tham số cho ON DUPLICATE KEY UPDATE
	        stmt.setString(5, gioKhoiHanh);
	        stmt.setString(6, gioHaCanh);
	        stmt.setInt(7, thoiGianBay);

	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        LOGGER.log(Level.SEVERE, "Lỗi khi thêm lịch bay", e);
	        throw e;
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            if (conn != null) MYSQLDB.closeConnection(conn);
	        } catch (SQLException e) {
	            LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối", e);
	        }
	    }
	}
	
	
	public boolean isChuyenBayExists(String maChuyenBay) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = MYSQLDB.getConnection();
            if (conn == null) {
                throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
            }
            
            String query = "SELECT COUNT(*) FROM ChuyenBay WHERE MaChuyenBay = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, maChuyenBay);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Lỗi khi đóng ResultSet", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Lỗi khi đóng PreparedStatement", e);
                }
            }
            if (conn != null) {
                MYSQLDB.closeConnection(conn);
            }
        }
    }


	// Thêm hoặc cập nhật chuyến bay
	public void addChuyenBay(ChuyenBay chuyenBay) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = MYSQLDB.getConnection();
			if (conn == null) {
				throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
			}
			
			// Bắt đầu transaction
			conn.setAutoCommit(false);
			
			String insertQuery = """
				INSERT INTO ChuyenBay (MaChuyenBay, MaSanBay, ChangBay, NgayBay, NhaGa, 
									 SoGhe, TinhTrang, MaMayBay, MaHang, DiemDi, DiemDen, GiaVe)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
			""";

			String changBay = chuyenBay.getDiemDi() + " - " + chuyenBay.getDiemDen();
			chuyenBay.setChangBay(changBay);
			
			stmt = conn.prepareStatement(insertQuery);
			stmt.setString(1, chuyenBay.getMaChuyenBay());
			stmt.setString(2, chuyenBay.getSanBay());
			stmt.setString(3, chuyenBay.getChangBay());
			stmt.setDate(3, chuyenBay.getNgayBay());
			stmt.setString(5, chuyenBay.getNhaGa());
			stmt.setInt(6, chuyenBay.getSoGhe());
			stmt.setString(7, chuyenBay.getTinhTrang());
			stmt.setString(8, chuyenBay.getMaMaybay());
			stmt.setString(9, chuyenBay.getMaHang());
			stmt.setString(10, chuyenBay.getDiemDi());
			stmt.setString(11, chuyenBay.getDiemDen());
			stmt.setDouble(12, chuyenBay.getGiaVe()); // Thêm giá vé
	
			int rowsAffected = stmt.executeUpdate();
			
			if (rowsAffected == 0) {
				throw new SQLException("Thêm chuyến bay thất bại, không có dòng nào được thêm vào.");
			}
			
			// Commit transaction nếu mọi thứ OK
			conn.commit();
			
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback(); // Rollback trong trường hợp có lỗi
				} catch (SQLException ex) {
					LOGGER.log(Level.SEVERE, "Lỗi khi rollback transaction", ex);
				}
			}
			LOGGER.log(Level.SEVERE, "Lỗi khi thêm chuyến bay", e);
			throw e; // Ném lại exception để UI xử lý
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, "Lỗi khi đóng PreparedStatement", e);
				}
			}
			if (conn != null) {
				try {
					conn.setAutoCommit(true); // Reset auto-commit về true
					MYSQLDB.closeConnection(conn);
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối", e);
				}
			}
		}
	}

	public boolean updateChuyenBay(ChuyenBay chuyenBay) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = MYSQLDB.getConnection();
			if (conn == null) {
				throw new SQLException("Không thể kết nối đến cơ sở dữ liệu.");
			}
			
			conn.setAutoCommit(false);
			
			String updateQuery = """
				UPDATE ChuyenBay
				SET MaSanBay = ?, 
					ChangBay = ?, 
					NgayBay = ?,
					NhaGa = ?, 
					SoGhe = ?, 
					TinhTrang = ?,
					MaMayBay = ?, 
					MaHang = ?,
					DiemDi = ?,
					DiemDen = ?,
					GiaVe = ?
				WHERE MaChuyenBay = ?
			""";
			String changBay = chuyenBay.getDiemDi() + " - " + chuyenBay.getDiemDen();
    		chuyenBay.setChangBay(changBay);
			
			stmt = conn.prepareStatement(updateQuery);
			stmt.setString(1, chuyenBay.getSanBay());
			stmt.setString(2, chuyenBay.getChangBay());
			stmt.setDate(3, chuyenBay.getNgayBay());
			stmt.setString(4, chuyenBay.getNhaGa());
			stmt.setInt(5, chuyenBay.getSoGhe());
			stmt.setString(6, chuyenBay.getTinhTrang());
			stmt.setString(7, chuyenBay.getMaMaybay());
			stmt.setString(8, chuyenBay.getMaHang());
			stmt.setString(9, chuyenBay.getDiemDi());
			stmt.setString(10, chuyenBay.getDiemDen());
			stmt.setDouble(11, chuyenBay.getGiaVe()); // Thêm giá vé
			stmt.setString(12, chuyenBay.getMaChuyenBay());
	
			int rowsAffected = stmt.executeUpdate();
			
			conn.commit();
			
			return rowsAffected > 0;
			
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					LOGGER.log(Level.SEVERE, "Lỗi khi rollback transaction", ex);
				}
			}
			LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật chuyến bay", e);
			throw e;
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, "Lỗi khi đóng PreparedStatement", e);
				}
			}
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					MYSQLDB.closeConnection(conn);
				} catch (SQLException e) {
					LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối", e);
				}
			}
		}
	}
	
	 
	public List<ChuyenBay> searchChuyenBayByCode(String maChuyenBay) {
		List<ChuyenBay> danhSachKetQua = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
	
		try {
			conn = MYSQLDB.getConnection();
			if (conn == null) {
				LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
				return danhSachKetQua;
			}
	
			String searchQuery = "SELECT MaChuyenBay, MaSanBay, ChangBay, NgayBay, NhaGa, SoGhe, TinhTrang, MaMayBay, MaHang, DiemDi, DiemDen, GiaVe FROM ChuyenBay WHERE MaChuyenBay = ?";
			stmt = conn.prepareStatement(searchQuery);
			stmt.setString(1, maChuyenBay);
			rs = stmt.executeQuery();
	
			while (rs.next()) {
				ChuyenBay chuyenBay = new ChuyenBay(
					rs.getString("MaChuyenBay"),
					rs.getString("MaSanBay"),
					rs.getString("ChangBay"),
					rs.getString("NgayBay"),
					rs.getString("NhaGa"),
					rs.getInt("SoGhe"),
					rs.getString("TinhTrang"),
					rs.getString("MaMayBay"),
					rs.getString("MaHang"),
					rs.getString("DiemDi"),
					rs.getString("DiemDen")
				);
				chuyenBay.setGiaVe(rs.getDouble("GiaVe")); // Thêm giá vé
				danhSachKetQua.add(chuyenBay);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm chuyến bay", e);
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
				if (conn != null) MYSQLDB.closeConnection(conn);
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, "Lỗi khi đóng kết nối", e);
			}
		}
	
		return danhSachKetQua;
	}
	

	// Xóa chuyến bay
	public void deleteChuyenBay(String maChuyenBay) {
		Connection conn = MYSQLDB.getConnection();

		if (conn == null) {
			LOGGER.severe("Không thể kết nối đến cơ sở dữ liệu.");
			return;
		}

		String deleteQuery = "DELETE FROM ChuyenBay WHERE MaChuyenBay = ?";
		try (PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {
			stmt.setString(1, maChuyenBay);
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Lỗi khi xóa chuyến bay", e);
		} finally {
			MYSQLDB.closeConnection(conn);
		}
	}
}
