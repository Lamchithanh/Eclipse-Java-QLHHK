package Controller;

import Model.*;
import Service.*;
import View.QuanLyDatVeView;

import javax.swing.*;
import java.time.LocalDateTime;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class DatVeController {
    private static final Logger LOGGER = Logger.getLogger(DatVeController.class.getName());
    
    private QuanLyDatVeView view;
    private DatVeService datVeService;
    private ChuyenBayService chuyenBayService;

    public DatVeController(QuanLyDatVeView view) {
        this.view = view;
        initializeServices();
    }

    private void initializeServices() {
        try {
            this.datVeService = new DatVeService();
            this.chuyenBayService = new ChuyenBayService();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khởi tạo services", e);
        }
    }

    public void loadInitialData() {
        try {
            if (view == null) {
                LOGGER.warning("View is null during loadInitialData");
                return;
            }

            // Load danh sách chuyến bay
            List<ChuyenBay> chuyenBays = chuyenBayService.getAllChuyenBays();
            if (chuyenBays != null) {
                view.updateFlightsList(chuyenBays);
            }
            
            // Cập nhật bảng đặt vé
            updateDatVeList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tải dữ liệu ban đầu", e);
            if (view != null) {
                view.showMessage("Không thể tải dữ liệu. Vui lòng thử lại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void addDatVe(DatVe booking) {
        try {
            // Validate input
            if (!validateInput()) {
                return;
            }

            // Kiểm tra chuyến bay tồn tại
            ChuyenBay chuyenBay = view.getSelectedChuyenBay();
            if (chuyenBay == null || !chuyenBayService.isChuyenBayExists(chuyenBay.getMaChuyenBay())) {
                view.showMessage("Chuyến bay không tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo mã đặt vé mới
            String maDatVe = datVeService.generateMaDatVe();

            // Tạo đối tượng đặt vé từ view
            DatVe datVe = createDatVeFromView(maDatVe);

            // Thêm đặt vé
            datVeService.addDatVe(datVe);

            // Cập nhật số ghế của chuyến bay
            updateFlightSeats(chuyenBay, datVe.getSoLuong());

            // Cập nhật UI
            updateDatVeList();
            view.clearFields();
            view.showMessage("Đặt vé thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đặt vé", e);
            view.showMessage("Lỗi khi đặt vé: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private DatVe createDatVeFromView(String maDatVe) {
        DatVe datVe = new DatVe();
        
        // Set core booking information
        datVe.setMaDatVe(maDatVe);
        datVe.setChuyenBay(view.getSelectedChuyenBay());
        datVe.setSoLuong(view.getSoLuongValue());
        datVe.setTongGia(calculateTotalPrice());
        datVe.setTrangThai(view.getTrangThaiText());
        datVe.setPhuongThucThanhToan(view.getPhuongThucThanhToanText());
        datVe.setMaGiamGia(view.getSelectedDiscountType());
        datVe.setXacNhanThanhToan(view.isImmediatePayment());
        
        // Set personal information
        datVe.setHoTen(view.getHoTenText());
        datVe.setCMND(view.getCMNDText());
        datVe.setNgaySinh(view.getNgaySinhDate());
        datVe.setGioiTinh(view.getGioiTinhText());
        datVe.setQuocTich(view.getQuocTichText());
        
        // Set contact information
        datVe.setSoDienThoai(view.getSoDienThoaiText());
        datVe.setEmail(view.getEmailText());
        datVe.setDiaChi(view.getDiaChiText());
        
        // Set flight details
        datVe.setDiemDi(view.getDiemDiText());
        datVe.setDiemDen(view.getDiemDenText());
        datVe.setNgayBay(view.getNgayBayDate());
        datVe.setHangVe(view.getHangVeText());
        
        // Set special requests
        datVe.setSuatAnDacBiet(view.isSuatAnDacBiet());
        datVe.setHoTroYTe(view.isHoTroYTe());
        datVe.setChoNgoiUuTien(view.isChoNgoiUuTien());
        datVe.setHanhLyDacBiet(view.isHanhLyDacBiet());
        
        // Set emergency contact
        datVe.setNguoiLienHe(view.getNguoiLienHeText());
        datVe.setSDTNguoiLienHe(view.getSDTNguoiLienHeText());
        
        // Set timestamps
        datVe.setNgayDat(LocalDateTime.now());
        
        return datVe;
    }

    public void updateDatVe() {
        try {
            if (!validateInput()) {
                return;
            }

            DatVe datVe = createDatVeFromView(view.getMaDatVeText());
            datVe.setNgayCapNhat(LocalDateTime.now());

            datVeService.updateDatVe(datVe);
            updateDatVeList();
            view.clearFields();
            view.showMessage("Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật đặt vé", e);
            view.showMessage("Lỗi khi cập nhật: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteDatVe() {
        try {
            String maDatVe = view.getMaDatVeText();
            if (maDatVe == null || maDatVe.trim().isEmpty()) {
                view.showMessage("Vui lòng chọn vé cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa vé này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                datVeService.deleteDatVe(maDatVe);
                updateDatVeList();
                view.clearFields();
                view.showMessage("Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa đặt vé", e);
            view.showMessage("Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void searchDatVe(String searchQuery) {
        try {
            if (searchQuery == null || searchQuery.trim().isEmpty()) {
                updateDatVeList();
                return;
            }

            List<DatVe> searchResults = datVeService.searchDatVe(searchQuery);
            updateTableWithResults(searchResults);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm", e);
            view.showMessage("Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTableWithResults(List<DatVe> datVeList) {
        view.getTableModel().setRowCount(0);
        for (DatVe datVe : datVeList) {
            view.getTableModel().addRow(new Object[]{
                datVe.getMaDatVe(),
                datVe.getChuyenBay().getMaChuyenBay(),
                datVe.getHoTen(),
                datVe.getNgayDat(),
                datVe.getSoLuong(),
                String.format("%,.0f VND", datVe.getTongGia()),
                datVe.getTrangThai()
            });
        }
    }

    private void updateFlightSeats(ChuyenBay chuyenBay, int bookedSeats) {
        try {
            chuyenBay.setSoGhe(chuyenBay.getSoGhe() - bookedSeats);
            chuyenBayService.updateChuyenBay(chuyenBay);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật số ghế", e);
        }
    }

    private double calculateTotalPrice() {
        double basePrice = view.getSelectedChuyenBay().getGiaVe();
        int quantity = view.getSoLuongValue();
        String discountType = view.getSelectedDiscountType();
        String hangVe = view.getHangVeText();
        
        double total = basePrice * quantity;
        
        // Áp dụng giá theo hạng vé
        switch (hangVe) {
            case "Thương gia":
                total *= 1.5; // Phụ thu 50%
                break;
            case "Hạng nhất":
                total *= 2.0; // Phụ thu 100%
                break;
        }
        
        // Áp dụng giảm giá
        switch (discountType) {
            case "Học sinh/Sinh viên":
                total *= 0.9; // Giảm 10%
                break;
            case "Người cao tuổi":
                total *= 0.85; // Giảm 15%
                break;
            case "Khuyết tật":
                total *= 0.8; // Giảm 20%
                break;
        }
        
        return total;
    }

    private boolean validateInput() {
        if (!view.isValidInput()) {
            return false;
        }

        // Validate số lượng ghế
        ChuyenBay chuyenBay = view.getSelectedChuyenBay();
        int requestedSeats = view.getSoLuongValue();
        if (requestedSeats > chuyenBay.getSoGhe()) {
            view.showMessage("Không đủ ghế trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void updateDatVeList() {
        try {
            List<DatVe> datVeList = datVeService.getAllDatVe();
            updateTableWithResults(datVeList);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật danh sách đặt vé", e);
            view.showMessage("Không thể cập nhật danh sách đặt vé!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("exports")
    public ChuyenBayService getChuyenBayService() {
        return chuyenBayService;
    }

	
	public List<ChuyenBay> getAllFlights() {
	    return chuyenBayService.getAllChuyenBays();
	}

	public List<DatVe> getAllBookings() throws SQLException {
	    return datVeService.getAllDatVe();
	}

	public void updateDatVe(DatVe datVe) throws SQLException {
	    datVeService.updateDatVe(datVe);
	}

	public void deleteDatVe(String maDatVe) throws SQLException {
	    datVeService.deleteDatVe(maDatVe);
	}
}