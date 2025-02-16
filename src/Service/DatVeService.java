package Service;

import Model.DatVe;
import Model.KhachHang;
import Model.ChuyenBay;
import Model.VeMayBay;
import Database.MYSQLDB;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class DatVeService {
	private ChuyenBayService chuyenBayService = new ChuyenBayService();
	private PreparedStatement stmtVeMayBay;

	// Thêm phương thức kiểm tra VeMayBay tồn tại
	private boolean isVeMayBayExists(Connection connection, String maVe) throws SQLException {
		String sql = "SELECT COUNT(*) FROM VeMayBay WHERE MaVe = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setString(1, maVe);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}
		}
		return false;
	}

	public void addDatVe(DatVe datVe) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = MYSQLDB.getConnection();
			connection.setAutoCommit(false);

			// 1. Sinh mã đặt vé
			String maDatVe = generateMaDatVe();
			datVe.setMaDatVe(maDatVe);

			// 2. Sinh và lưu mã khách hàng
			String maKhachHang = generateMaKhachHang(connection, datVe);
			
			if (!datVe.getMaKhachHang().equals(maKhachHang)) {
			    throw new SQLException("Mã khách hàng đã bị thay đổi: " + datVe.getMaKhachHang() + 
			                         " (Mã gốc: " + maKhachHang + ")");
			}

			
			// 3. Dùng mã vừa sinh để insert
			insertKhachHang(connection, datVe);

			// 4. INSERT đặt vé
			String sqlDatVe = "INSERT INTO DatVe (MaDatVe, MaKhachHang, MaChuyenBay, NgayBay, "
					+ "HangVe, SoLuong, TongGia, TrangThai, PhuongThucThanhToan, MaGiamGia, "
					+ "XacNhanThanhToan, NgayDat) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = connection.prepareStatement(sqlDatVe);
			int paramIndex = 1;
			stmt.setString(paramIndex++, maDatVe);
			stmt.setString(paramIndex++, maKhachHang);
			stmt.setString(paramIndex++, datVe.getChuyenBay().getMaChuyenBay());
			stmt.setDate(paramIndex++, datVe.getNgayBay());
			stmt.setString(paramIndex++, datVe.getHangVe());
			stmt.setInt(paramIndex++, datVe.getSoLuong());
			stmt.setDouble(paramIndex++, datVe.getTongGia());
			stmt.setString(paramIndex++, "Đã đặt");
			stmt.setString(paramIndex++, datVe.getPhuongThucThanhToan());
			stmt.setString(paramIndex++, datVe.getMaGiamGia());
			stmt.setBoolean(paramIndex++, datVe.isXacNhanThanhToan());
			stmt.setTimestamp(paramIndex++, Timestamp.valueOf(LocalDateTime.now()));
			stmt.executeUpdate();

			// 5. INSERT vé máy bay
			String sqlVeMayBay = "INSERT INTO VeMayBay (MaVe, MaChuyenBay, MaKhachHang, NgayDat, "
					+ "GiaVe, HangVe, TrangThai, XacNhanThanhToan) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

			try (PreparedStatement stmtVeMayBay = connection.prepareStatement(sqlVeMayBay)) {
				paramIndex = 1;
				stmtVeMayBay.setString(paramIndex++, maDatVe);
				stmtVeMayBay.setString(paramIndex++, datVe.getChuyenBay().getMaChuyenBay());
				stmtVeMayBay.setString(paramIndex++, maKhachHang);
				stmtVeMayBay.setTimestamp(paramIndex++, Timestamp.valueOf(LocalDateTime.now()));
				stmtVeMayBay.setDouble(paramIndex++, datVe.getTongGia());
				stmtVeMayBay.setString(paramIndex++, datVe.getHangVe());
				stmtVeMayBay.setString(paramIndex++, "Đã đặt");
				stmtVeMayBay.setBoolean(paramIndex++, datVe.isXacNhanThanhToan());
				stmtVeMayBay.executeUpdate();
			}

			// 6. INSERT yêu cầu đặc biệt
			String sqlYeuCau = "INSERT INTO YeuCauDacBiet (MaVe, SuatAnDacBiet, HoTroYTe, "
					+ "ChoNgoiUuTien, HanhLyDacBiet) VALUES (?, ?, ?, ?, ?)";

			try (PreparedStatement stmtYeuCau = connection.prepareStatement(sqlYeuCau)) {
				stmtYeuCau.setString(1, maDatVe);
				stmtYeuCau.setBoolean(2, datVe.isSuatAnDacBiet());
				stmtYeuCau.setBoolean(3, datVe.isHoTroYTe());
				stmtYeuCau.setBoolean(4, datVe.isChoNgoiUuTien());
				stmtYeuCau.setBoolean(5, datVe.isHanhLyDacBiet());
				stmtYeuCau.executeUpdate();
			}

			// 7. Cập nhật số ghế của chuyến bay
			updateChuyenBaySeats(connection, datVe.getChuyenBay().getMaChuyenBay(), datVe.getSoLuong());

			// 8. Commit transaction
			connection.commit();

		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					throw new SQLException("Lỗi khi rollback: " + ex.getMessage());
				}
			}
			throw new SQLException("Lỗi khi thêm đặt vé: " + e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.setAutoCommit(true);
				} catch (SQLException e) {
					throw new SQLException("Lỗi khi reset auto commit: " + e.getMessage());
				}
			}
			closeResources(connection, stmt, null);
		}
	}

	// Phương thức hỗ trợ cập nhật số ghế còn trống
	private void updateChuyenBaySeats(Connection connection, String maChuyenBay, int soLuongVe) throws SQLException {
		String sqlUpdateSeats = "UPDATE ChuyenBay SET SoGhe = SoGhe - ? WHERE MaChuyenBay = ?";
		try (PreparedStatement stmt = connection.prepareStatement(sqlUpdateSeats)) {
			stmt.setInt(1, soLuongVe);
			stmt.setString(2, maChuyenBay);
			stmt.executeUpdate();
		}
	}

	private String generateMaKhachHang(Connection connection, DatVe datVe) throws SQLException {
	    String cmnd = datVe.getCMND().trim();
	    String hoTen = datVe.getHoTen().trim();
	    
	    // Loại bỏ khoảng trắng thừa và chuyển về chữ thường để chuẩn hóa
	    hoTen = hoTen.replaceAll("\\s+", "").toLowerCase();
	    
	    // Lấy chữ cái đầu của họ và tên
	    String firstChar = "";
	    String[] parts = hoTen.split(" ");
	    if (parts.length > 0) {
	        firstChar = parts[0].substring(0, 1).toUpperCase();
	        if (parts.length > 1) {
	            firstChar += parts[parts.length - 1].substring(0, 1).toUpperCase();
	        }
	    }
	    
	    // Tạo mã khách hàng: KH + 5 số cuối CMND + firstChar 
	    String maKhachHang = "KH" + 
	        (cmnd.length() > 5 ? cmnd.substring(cmnd.length() - 5) : cmnd) + 
	        firstChar;

	    // Kiểm tra mã khách hàng đã tồn tại chưa
	    String checkExistSql = "SELECT COUNT(*) FROM KhachHang WHERE MaKhachHang = ?";
	    try (PreparedStatement checkStmt = connection.prepareStatement(checkExistSql)) {
	        checkStmt.setString(1, maKhachHang);
	        try (ResultSet rs = checkStmt.executeQuery()) {
	            if (rs.next() && rs.getInt(1) > 0) {
	                // Nếu mã đã tồn tại, thêm một số ngẫu nhiên
	                maKhachHang += new Random().nextInt(10);
	            }
	        }
	    }

	    return maKhachHang;
	}

	private void insertKhachHang(Connection connection, DatVe datVe) throws SQLException {
	    PreparedStatement stmtKhachHang = null;
	    try {
	        // Kiểm tra khách hàng tồn tại qua CMND
	        String checkSql = "SELECT MaKhachHang FROM KhachHang WHERE CMND = ?";
	        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
	            String cmnd = datVe.getCMND();
	            checkStmt.setString(1, cmnd);
	            ResultSet rs = checkStmt.executeQuery();

	            if (rs.next()) {
	                // Nếu đã tồn tại, cập nhật thông tin
	                String updateSql = "UPDATE KhachHang SET " 
	                    + "TenKhachHang = ?, GioiTinh = ?, NgaySinh = ?, "
	                    + "SoDienThoai = ?, Email = ?, DiaChi = ?, QuocTich = ?, "
	                    + "TenNguoiLienHe = ?, SoDienThoaiNguoiLienHe = ? "
	                    + "WHERE CMND = ?";

	                stmtKhachHang = connection.prepareStatement(updateSql);
	                int paramIndex = 1;
	                stmtKhachHang.setString(paramIndex++, datVe.getHoTen());
	                stmtKhachHang.setString(paramIndex++, datVe.getGioiTinh());
	                stmtKhachHang.setDate(paramIndex++, datVe.getNgaySinh());
	                stmtKhachHang.setString(paramIndex++, datVe.getSoDienThoai());
	                stmtKhachHang.setString(paramIndex++, datVe.getEmail());
	                stmtKhachHang.setString(paramIndex++, datVe.getDiaChi());
	                stmtKhachHang.setString(paramIndex++, datVe.getQuocTich());
	                stmtKhachHang.setString(paramIndex++, datVe.getNguoiLienHe());
	                stmtKhachHang.setString(paramIndex++, datVe.getSDTNguoiLienHe());
	                stmtKhachHang.setString(paramIndex++, cmnd);
	            } else {
	                // Nếu chưa tồn tại, thêm mới
	                String insertSql = "INSERT INTO KhachHang ("
	                    + "MaKhachHang, TenKhachHang, CMND, GioiTinh, NgaySinh, "
	                    + "SoDienThoai, Email, DiaChi, QuocTich, "
	                    + "TenNguoiLienHe, SoDienThoaiNguoiLienHe"
	                    + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	                stmtKhachHang = connection.prepareStatement(insertSql);
	                int paramIndex = 1;
	                // Sử dụng mã khách hàng được sinh ra
	                stmtKhachHang.setString(paramIndex++, datVe.getMaKhachHang());
	                stmtKhachHang.setString(paramIndex++, datVe.getHoTen());
	                stmtKhachHang.setString(paramIndex++, cmnd);
	                stmtKhachHang.setString(paramIndex++, datVe.getGioiTinh());
	                stmtKhachHang.setDate(paramIndex++, datVe.getNgaySinh());
	                stmtKhachHang.setString(paramIndex++, datVe.getSoDienThoai());
	                stmtKhachHang.setString(paramIndex++, datVe.getEmail());
	                stmtKhachHang.setString(paramIndex++, datVe.getDiaChi());
	                stmtKhachHang.setString(paramIndex++, datVe.getQuocTich());
	                stmtKhachHang.setString(paramIndex++, datVe.getNguoiLienHe());
	                stmtKhachHang.setString(paramIndex++, datVe.getSDTNguoiLienHe());
	            }

	            // Thực thi câu lệnh
	            stmtKhachHang.executeUpdate();
	        }
	    } catch (SQLException e) {
	        System.err.println("Lỗi khi thêm/cập nhật khách hàng: " + e.getMessage());
	        throw e;
	    } finally {
	        if (stmtKhachHang != null) {
	            stmtKhachHang.close();
	        }
	    }
	}

	// Cập nhật đặt vé
	public void updateDatVe(DatVe datVe) throws SQLException {
		Connection connection = null;
		PreparedStatement stmtDatVe = null;
		PreparedStatement stmtVeMayBay = null;
		PreparedStatement stmtYeuCau = null;

		try {
			connection = MYSQLDB.getConnection();
			connection.setAutoCommit(false);

			// Update DatVe
			String sqlDatVe = "UPDATE DatVe SET MaChuyenBay=?, NgayBay=?, HangVe=?, SoLuong=?, "
					+ "TongGia=?, TrangThai=?, PhuongThucThanhToan=?, MaGiamGia=?, XacNhanThanhToan=?, "
					+ "NgayCapNhat=? WHERE MaDatVe=?";

			stmtDatVe = connection.prepareStatement(sqlDatVe);
			int index = 1;
			stmtDatVe.setString(index++, datVe.getChuyenBay().getMaChuyenBay());
			stmtDatVe.setDate(index++, datVe.getNgayBay());
			stmtDatVe.setString(index++, datVe.getHangVe());
			stmtDatVe.setInt(index++, datVe.getSoLuong());
			stmtDatVe.setDouble(index++, datVe.getTongGia());
			stmtDatVe.setString(index++, datVe.getTrangThai());
			stmtDatVe.setString(index++, datVe.getPhuongThucThanhToan());
			stmtDatVe.setString(index++, datVe.getMaGiamGia());
			stmtDatVe.setBoolean(index++, datVe.isXacNhanThanhToan());
			stmtDatVe.setTimestamp(index++, Timestamp.valueOf(LocalDateTime.now()));
			stmtDatVe.setString(index++, datVe.getMaDatVe());

			stmtDatVe.executeUpdate();

			// Update YeuCauDacBiet
			String sqlYeuCau = "UPDATE YeuCauDacBiet SET SuatAnDacBiet=?, HoTroYTe=?, "
					+ "ChoNgoiUuTien=?, HanhLyDacBiet=? WHERE MaVe=?";

			stmtYeuCau = connection.prepareStatement(sqlYeuCau);
			stmtYeuCau.setBoolean(1, datVe.isSuatAnDacBiet());
			stmtYeuCau.setBoolean(2, datVe.isHoTroYTe());
			stmtYeuCau.setBoolean(3, datVe.isChoNgoiUuTien());
			stmtYeuCau.setBoolean(4, datVe.isHanhLyDacBiet());
			stmtYeuCau.setString(5, datVe.getMaDatVe());

			// Update VeMayBay
			String sqlVeMayBay = "UPDATE VeMayBay SET MaChuyenBay=?, MaKhachHang=?, "
					+ "GiaVe=?, HangVe=?, TrangThai=?, XacNhanThanhToan=?, " + "NgayCapNhat=? WHERE MaVe=?";

			stmtVeMayBay = connection.prepareStatement(sqlVeMayBay);
			stmtVeMayBay.setString(1, datVe.getChuyenBay().getMaChuyenBay());
			stmtVeMayBay.setString(2, datVe.getMaKhachHang());
			stmtVeMayBay.setDouble(3, datVe.getTongGia());
			stmtVeMayBay.setString(4, datVe.getHangVe());
			stmtVeMayBay.setString(5, datVe.getTrangThai());
			stmtVeMayBay.setBoolean(6, datVe.isXacNhanThanhToan());
			stmtVeMayBay.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
			stmtVeMayBay.setString(8, datVe.getMaDatVe());
			stmtVeMayBay.executeUpdate();

			stmtYeuCau.executeUpdate();

			connection.commit();
		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			closeResources(connection, stmtDatVe, null);
			if (stmtYeuCau != null) {
				stmtYeuCau.close();
			}
		}
	}

	// Xóa đặt vé
	public void deleteDatVe(String maDatVe) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;

		try {
			connection = MYSQLDB.getConnection();
			connection.setAutoCommit(false);

			// Delete from YeuCauDacBiet first due to foreign key constraint
			String sqlYeuCau = "DELETE FROM YeuCauDacBiet WHERE MaVe = ?";
			stmt = connection.prepareStatement(sqlYeuCau);
			stmt.setString(1, maDatVe);
			stmt.executeUpdate();

			// Xóa VeMayBay
			String sqlVeMayBay = "DELETE FROM VeMayBay WHERE MaVe = ?";
			stmt = connection.prepareStatement(sqlVeMayBay);
			stmt.setString(1, maDatVe);
			stmt.executeUpdate();

			// Then delete from DatVe
			String sqlDatVe = "DELETE FROM DatVe WHERE MaDatVe = ?";
			stmt = connection.prepareStatement(sqlDatVe);
			stmt.setString(1, maDatVe);
			stmt.executeUpdate();

			connection.commit();
		} catch (SQLException e) {
			if (connection != null) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			closeResources(connection, stmt, null);
		}
	}

	// Lấy tất cả đặt vé
	public List<DatVe> getAllDatVe() throws SQLException {
		List<DatVe> datVeList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = MYSQLDB.getConnection();
			// Cập nhật câu query để join các bảng liên quan
			String sql = "SELECT d.*, k.*, y.*, c.* " + "FROM DatVe d "
					+ "LEFT JOIN KhachHang k ON d.MaKhachHang = k.MaKhachHang "
					+ "LEFT JOIN YeuCauDacBiet y ON d.MaDatVe = y.MaVe "
					+ "LEFT JOIN ChuyenBay c ON d.MaChuyenBay = c.MaChuyenBay " + "ORDER BY d.NgayDat DESC";

			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				DatVe datVe = mapResultSetToDatVe(rs);
				datVeList.add(datVe);
			}
		} finally {
			closeResources(connection, stmt, rs);
		}

		return datVeList;
	}

	public DatVe getDatVeByMa(String maDatVe) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = MYSQLDB.getConnection();

			// Cập nhật câu query để lấy thông tin từ tất cả các bảng liên quan
			String sql = "SELECT d.*, k.*, y.*, c.*, " + "k.TenKhachHang, k.CMND, k.GioiTinh, k.NgaySinh, "
					+ "k.SoDienThoai, k.Email, k.DiaChi, k.QuocTich, " + "k.TenNguoiLienHe, k.SoDienThoaiNguoiLienHe, "
					+ "y.SuatAnDacBiet, y.HoTroYTe, y.ChoNgoiUuTien, y.HanhLyDacBiet, "
					+ "c.DiemDi, c.DiemDen, c.ChangBay, c.NgayBay as NgayKhoiHanh, "
					+ "c.NhaGa, c.GiaVe, c.TinhTrang as TinhTrangChuyenBay " + "FROM DatVe d "
					+ "LEFT JOIN KhachHang k ON d.MaKhachHang = k.MaKhachHang "
					+ "LEFT JOIN YeuCauDacBiet y ON d.MaDatVe = y.MaVe "
					+ "LEFT JOIN ChuyenBay c ON d.MaChuyenBay = c.MaChuyenBay " + "WHERE d.MaDatVe = ?";

			stmt = connection.prepareStatement(sql);
			stmt.setString(1, maDatVe);

			rs = stmt.executeQuery();

			if (rs.next()) {
				return mapResultSetToDatVe(rs);
			}

			return null;

		} catch (SQLException e) {
			throw new SQLException("Lỗi khi tìm đặt vé với mã " + maDatVe + ": " + e.getMessage());
		} finally {
			closeResources(connection, stmt, rs);
		}
	}

	// Tìm kiếm đặt vé
	public List<DatVe> searchDatVe(String searchQuery) throws SQLException {
		List<DatVe> datVeList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = MYSQLDB.getConnection();
			String sql = "SELECT d.*, k.*, y.* " + "FROM DatVe d "
					+ "LEFT JOIN KhachHang k ON d.MaKhachHang = k.MaKhachHang "
					+ "LEFT JOIN YeuCauDacBiet y ON d.MaDatVe = y.MaVe "
					+ "WHERE d.MaDatVe LIKE ? OR k.TenKhachHang LIKE ? OR k.CMND LIKE ? "
					+ "OR k.SoDienThoai LIKE ? OR d.TrangThai LIKE ? " + "ORDER BY d.NgayDat DESC";

			stmt = connection.prepareStatement(sql);
			String searchPattern = "%" + searchQuery + "%";
			for (int i = 1; i <= 5; i++) {
				stmt.setString(i, searchPattern);
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				DatVe datVe = mapResultSetToDatVe(rs);
				datVeList.add(datVe);
			}
		} finally {
			closeResources(connection, stmt, rs);
		}

		return datVeList;
	}

	// Lấy đặt vé theo mã
	public DatVe getDatVeById(String maDatVe) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = MYSQLDB.getConnection();
			String sql = "SELECT d.*, y.SuatAnDacBiet, y.HoTroYTe, y.ChoNgoiUuTien, y.HanhLyDacBiet " + "FROM DatVe d "
					+ "LEFT JOIN YeuCauDacBiet y ON d.MaDatVe = y.MaVe " + "WHERE d.MaDatVe = ?";

			stmt = connection.prepareStatement(sql);
			stmt.setString(1, maDatVe);

			rs = stmt.executeQuery();

			if (rs.next()) {
				return mapResultSetToDatVe(rs);
			}
		} finally {
			closeResources(connection, stmt, rs);
		}

		return null;
	}

	// Helper method to map ResultSet to DatVe object
	private DatVe mapResultSetToDatVe(ResultSet rs) throws SQLException {
		DatVe datVe = new DatVe();

		// Thông tin đặt vé cơ bản
		datVe.setMaDatVe(rs.getString("MaDatVe"));
		datVe.setMaKhachHang(rs.getString("MaKhachHang"));

		// Thông tin chuyến bay
		String maChuyenBay = rs.getString("MaChuyenBay");
		ChuyenBay chuyenBay = chuyenBayService.getChuyenBayById(maChuyenBay);
		datVe.setChuyenBay(chuyenBay);

		// Thông tin khách hàng từ bảng KhachHang
		datVe.setHoTen(rs.getString("TenKhachHang")); // Điều chỉnh theo tên cột trong bảng KhachHang
		datVe.setCMND(rs.getString("CMND"));
		datVe.setGioiTinh(rs.getString("GioiTinh"));
		datVe.setNgaySinh(rs.getDate("NgaySinh"));

		// Thông tin liên hệ
		datVe.setSoDienThoai(rs.getString("SoDienThoai"));
		datVe.setEmail(rs.getString("Email"));
		datVe.setDiaChi(rs.getString("DiaChi"));
		datVe.setQuocTich(rs.getString("QuocTich"));

		// Thông tin đặt vé
		datVe.setNgayBay(rs.getDate("NgayBay"));
		datVe.setHangVe(rs.getString("HangVe"));
		datVe.setSoLuong(rs.getInt("SoLuong"));
		datVe.setTongGia(rs.getDouble("TongGia"));
		datVe.setTrangThai(rs.getString("TrangThai"));
		datVe.setPhuongThucThanhToan(rs.getString("PhuongThucThanhToan"));
		datVe.setMaGiamGia(rs.getString("MaGiamGia"));
		datVe.setXacNhanThanhToan(rs.getBoolean("XacNhanThanhToan"));

		// Thông tin liên hệ khẩn cấp
		datVe.setNguoiLienHe(rs.getString("TenNguoiLienHe")); // Điều chỉnh theo tên cột chính xác
		datVe.setSDTNguoiLienHe(rs.getString("SoDienThoaiNguoiLienHe")); // Điều chỉnh theo tên cột chính xác

		// Yêu cầu đặc biệt
		datVe.setSuatAnDacBiet(rs.getBoolean("SuatAnDacBiet"));
		datVe.setHoTroYTe(rs.getBoolean("HoTroYTe"));
		datVe.setChoNgoiUuTien(rs.getBoolean("ChoNgoiUuTien"));
		datVe.setHanhLyDacBiet(rs.getBoolean("HanhLyDacBiet"));

		// Thêm các trường thời gian
		Timestamp ngayDat = rs.getTimestamp("NgayDat");
		if (ngayDat != null) {
			datVe.setNgayDat(ngayDat.toLocalDateTime());
		}

		Timestamp ngayCapNhat = rs.getTimestamp("NgayCapNhat");
		if (ngayCapNhat != null) {
			datVe.setNgayCapNhat(ngayCapNhat.toLocalDateTime());
		}

		return datVe;
	}

	// Helper method to close database resources
	private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Helper method to generate mã đặt vé
	public String generateMaDatVe() throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = MYSQLDB.getConnection();

			// Tìm mã lớn nhất hiện tại
			String sql = "SELECT COALESCE(MAX(CAST(SUBSTRING(MaDatVe, 3) AS UNSIGNED)), 0) + 1 AS NextId FROM DatVe WHERE MaDatVe LIKE 'DV%'";
			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();

			int nextId = 1;
			if (rs.next()) {
				nextId = rs.getInt("NextId");
			}

			// Format mã với độ dài cố định
			String newMaDatVe = String.format("DV%03d", nextId);

			// Kiểm tra xem mã có bị trùng không
			while (isMaDatVeExists(newMaDatVe)) {
				nextId++;
				newMaDatVe = String.format("DV%03d", nextId);
			}

			System.out.println("Đã tạo mã đặt vé mới: " + newMaDatVe);
			return newMaDatVe;

		} catch (SQLException e) {
			System.err.println("Lỗi khi sinh mã đặt vé: " + e.getMessage());
			e.printStackTrace();
			throw new SQLException("Không thể tạo mã đặt vé mới: " + e.getMessage());
		} finally {
			closeResources(connection, stmt, rs);
		}
	}

	// Thêm phương thức kiểm tra mã tồn tại
	private boolean isMaDatVeExists(String maDatVe) throws SQLException {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			connection = MYSQLDB.getConnection();
			String sql = "SELECT COUNT(*) FROM DatVe WHERE MaDatVe = ?";
			stmt = connection.prepareStatement(sql);
			stmt.setString(1, maDatVe);
			rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
			return false;
		} finally {
			closeResources(connection, stmt, rs);
		}
	}

	public List<ChuyenBay> getAllChuyenBays() {
		// Gọi Service của ChuyenBay để lấy danh sách chuyến bay
		ChuyenBayService chuyenBayService = new ChuyenBayService();
		return chuyenBayService.getAllChuyenBays();
	}

	public KhachHang findKhachHangByCMND(String cmnd) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = MYSQLDB.getConnection();
			String sql = "SELECT * FROM KhachHang WHERE CMND = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, cmnd);
			rs = stmt.executeQuery();

			if (rs.next()) {
				KhachHang khachHang = new KhachHang();
				khachHang.setMaKhachHang(rs.getString("MaKhachHang"));
				khachHang.setTenKhachHang(rs.getString("TenKhachHang"));
				khachHang.setSoDienThoai(rs.getString("SoDienThoai"));
				khachHang.setEmail(rs.getString("Email"));
				khachHang.setDiaChi(rs.getString("DiaChi"));
				khachHang.setCmnd(rs.getString("CMND"));
				khachHang.setNgaySinh(rs.getDate("NgaySinh"));
				khachHang.setQuocTich(rs.getString("QuocTich"));
				return khachHang;
			}
			return null;
		} finally {
			closeResources(conn, stmt, rs);
		}
	}
}