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
            loadFlights();
            loadBookings();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tải dữ liệu ban đầu", e);
            showError("Không thể tải dữ liệu. Vui lòng thử lại!");
        }
    }

    private void loadFlights() throws SQLException {
        List<ChuyenBay> flights = chuyenBayService.getAllChuyenBays();
        if (flights != null && view != null) {
            view.updateFlightsList(flights);
        }
    }

    private void loadBookings() throws SQLException {
        if (view != null) {
            List<DatVe> bookings = datVeService.getAllDatVe();
            updateBookingTable(bookings);
        }
    }

    public void handleAddBooking() {
        try {
            if (!validateBooking()) {
                return;
            }

            DatVe booking = createBookingFromView();
            datVeService.addDatVe(booking);
            
            // Cập nhật số ghế của chuyến bay
            updateFlightSeats(booking.getChuyenBay(), booking.getSoLuong());
            
            // Cập nhật UI
            loadBookings();
            view.clearFields();
            showSuccess("Đặt vé thành công!");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi đặt vé", e);
            showError("Lỗi khi đặt vé: " + e.getMessage());
        }
    }

    public void handleUpdateBooking() {
        try {
            if (!validateBooking()) {
                return;
            }

            DatVe booking = createBookingFromView();
            booking.setNgayCapNhat(LocalDateTime.now());
            datVeService.updateDatVe(booking);

            loadBookings();
            view.clearFields();
            showSuccess("Cập nhật thành công!");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật đặt vé", e);
            showError("Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    public void handleDeleteBooking(String maDatVe) {
        try {
            if (maDatVe == null || maDatVe.trim().isEmpty()) {
                showWarning("Vui lòng chọn vé cần xóa!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc muốn xóa vé này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                datVeService.deleteDatVe(maDatVe);
                loadBookings();
                view.clearFields();
                showSuccess("Xóa thành công!");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi xóa đặt vé", e);
            showError("Lỗi khi xóa: " + e.getMessage());
        }
    }

    @SuppressWarnings("exports")
    public void handleSearch(String searchTerm, Date searchDate, String status) {
        try {
            List<DatVe> searchResults;
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                searchResults = datVeService.getAllDatVe();
            } else {
                searchResults = datVeService.searchDatVe(searchTerm);
            }
            
            // Lọc kết quả theo ngày và trạng thái nếu có
            if (searchDate != null || status != null) {
                searchResults = filterSearchResults(searchResults, searchDate, status);
            }
            
            updateBookingTable(searchResults);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi tìm kiếm", e);
            showError("Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }

    private List<DatVe> filterSearchResults(List<DatVe> results, Date searchDate, String status) {
        return results.stream()
            .filter(booking -> {
                boolean dateMatch = searchDate == null || 
                    booking.getNgayBay().toLocalDate().equals(searchDate.toLocalDate());
                boolean statusMatch = status == null || status.equals("Tất cả") || 
                    booking.getTrangThai().equals(status);
                return dateMatch && statusMatch;
            })
            .toList();
    }

    private void updateFlightSeats(ChuyenBay flight, int bookedSeats) {
        try {
            int currentSeats = flight.getSoGhe();
            flight.setSoGhe(currentSeats - bookedSeats);
            chuyenBayService.updateChuyenBay(flight);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Lỗi khi cập nhật số ghế", e);
        }
    }

    private DatVe createBookingFromView() throws SQLException {
        DatVe booking = new DatVe();
        
        // Nếu là đặt vé mới, tạo mã mới
        String maDatVe = view.getMaDatVeText();
        if (maDatVe == null || maDatVe.trim().isEmpty()) {
            maDatVe = datVeService.generateMaDatVe();
        }
        booking.setMaDatVe(maDatVe);

        // Set thông tin từ view
        booking.setChuyenBay(view.getSelectedChuyenBay());
        booking.setSoLuong(view.getSoLuongValue());
        booking.setHangVe(view.getHangVeText());
        booking.setPhuongThucThanhToan(view.getPhuongThucThanhToanText());
        booking.setMaGiamGia(view.getSelectedDiscountType());
        booking.setXacNhanThanhToan(view.isImmediatePayment());
        
        booking.setHoTen(view.getHoTenText());
        booking.setCMND(view.getCMNDText());
        booking.setNgaySinh(view.getNgaySinhDate());
        booking.setGioiTinh(view.getGioiTinhText());
        booking.setQuocTich(view.getQuocTichText());
        
        booking.setSoDienThoai(view.getSoDienThoaiText());
        booking.setEmail(view.getEmailText());
        booking.setDiaChi(view.getDiaChiText());
        
        booking.setSuatAnDacBiet(view.isSuatAnDacBiet());
        booking.setHoTroYTe(view.isHoTroYTe());
        booking.setChoNgoiUuTien(view.isChoNgoiUuTien());
        booking.setHanhLyDacBiet(view.isHanhLyDacBiet());
        
        booking.setNguoiLienHe(view.getNguoiLienHeText());
        booking.setSDTNguoiLienHe(view.getSDTNguoiLienHeText());
        
        booking.setNgayDat(LocalDateTime.now());
        
        return booking;
    }

    private boolean validateBooking() {
        if (!view.isValidInput()) {
            return false;
        }

        ChuyenBay flight = view.getSelectedChuyenBay();
        int requestedSeats = view.getSoLuongValue();
        
        if (requestedSeats > flight.getSoGhe()) {
            showError("Không đủ ghế trống cho số lượng vé yêu cầu!");
            return false;
        }

        return true;
    }

    private void updateBookingTable(List<DatVe> bookings) {
        if (view != null) {
            view.getTableModel().setRowCount(0);
            for (DatVe booking : bookings) {
                view.getTableModel().addRow(new Object[]{
                    booking.getMaDatVe(),
                    booking.getChuyenBay().getMaChuyenBay(),
                    booking.getHoTen(),
                    booking.getNgayDat(),
                    booking.getSoLuong(),
                    String.format("%,.0f VND", booking.getTongGia()),
                    booking.getTrangThai()
                });
            }
        }
    }

    private void showError(String message) {
        if (view != null) {
            view.showMessage(message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showWarning(String message) {
        if (view != null) {
            view.showMessage(message, "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showSuccess(String message) {
        if (view != null) {
            view.showMessage(message, "Thành công", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public List<ChuyenBay> getAllFlights() throws SQLException {
        return chuyenBayService.getAllChuyenBays();
    }

    public List<DatVe> getAllBookings() throws SQLException {
        return datVeService.getAllDatVe();
    }
}