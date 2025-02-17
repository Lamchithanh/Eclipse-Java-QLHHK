package Controller;

import Model.ChuyenBay;
import Service.ChuyenBayService;
import View.QuanLyChuyenBay;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class ChuyenBayController {
    private QuanLyChuyenBay view;
    private ChuyenBayService chuyenBayService;
    private DefaultTableModel tableModel;

    public ChuyenBayController(QuanLyChuyenBay view) {
        this.view = view;
        this.chuyenBayService = new ChuyenBayService();
    }

    @SuppressWarnings("exports")
    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    // Validate methods
    public boolean isValidDate(String ngayBay) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.parse(ngayBay);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean validateFlightInput(String maChuyenBay, String changBay, String ngayBay, 
                               String sanBay, String soGhe, String maMayBay, String maHang,
                               String diemDi, String diemDen) {
    // Validate required fields
    if (maChuyenBay.isEmpty() || changBay.isEmpty() || ngayBay.isEmpty() || sanBay.isEmpty() 
        || diemDi.isEmpty() || diemDen.isEmpty()) {
        JOptionPane.showMessageDialog(view,
                "Các trường bắt buộc không được để trống:\n- Mã chuyến bay\n- Chặng bay\n- Ngày bay\n- Sân bay\n- Điểm đi\n- Điểm đến",
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        return false;
    }

        // Validate date format
        if (!isValidDate(ngayBay)) {
            JOptionPane.showMessageDialog(view,
                    "Định dạng ngày bay không hợp lệ.\nVui lòng sử dụng định dạng: yyyy-MM-dd",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate seat number
        int soGheInt;
        try {
            soGheInt = Integer.parseInt(soGhe);
            if (soGheInt <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Số ghế phải là số nguyên dương",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate mã máy bay và mã hãng
        if (maMayBay == null || maHang == null) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn mã máy bay và mã hãng",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean addFlight(String maChuyenBay, String changBay, String ngayBay, String sanBay, 
                    String nhaGa, String soGhe, String tinhTrang, 
                    String maMayBay, String maHang, String diemDi, String diemDen, String giaVe) {
        try {
            // Tạo chặng bay từ điểm đi và điểm đến
            changBay = diemDi + " - " + diemDen;

            // Convert soGhe to integer
            int soGheInt = Integer.parseInt(soGhe);
            
            // Xử lý giá vé
            // Loại bỏ dấu phẩy và khoảng trắng từ chuỗi giá vé
            String cleanedGiaVe = giaVe.replaceAll("[,\\s]", "");
            // Loại bỏ các số 0 ở đầu
            cleanedGiaVe = cleanedGiaVe.replaceFirst("^0+(?!$)", "");
            // Chuyển đổi sang double
            double giaVeDouble = Double.parseDouble(cleanedGiaVe);

            // Check if flight already exists when adding
            if (chuyenBayService.isChuyenBayExists(maChuyenBay)) {
                JOptionPane.showMessageDialog(view,
                        "Mã chuyến bay đã tồn tại.\nVui lòng sử dụng mã khác.",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Create ChuyenBay object
            ChuyenBay chuyenBay = new ChuyenBay(
                maChuyenBay, sanBay, changBay, ngayBay, 
                nhaGa, soGheInt, tinhTrang, maMayBay, maHang,
                diemDi, diemDen, null // Thêm tham số này
            );
            
            // Set giá vé
            chuyenBay.setGiaVe(giaVeDouble);

            // Add flight
            chuyenBayService.addChuyenBay(chuyenBay);

            return true;

        } catch (SQLException e) {
            // Handle SQL exceptions
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm chuyến bay: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Giá vé không hợp lệ. Vui lòng nhập số.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateFlight(String maChuyenBay, String changBay, String ngayBay, String sanBay, 
                            String nhaGa, String soGhe, String tinhTrang, 
                            String maMayBay, String maHang, String diemDi, String diemDen, String giaVe) {
        try {
            // Tạo chặng bay từ điểm đi và điểm đến
            changBay = diemDi + " - " + diemDen;

            // Convert soGhe to integer
            int soGheInt = Integer.parseInt(soGhe);
            
            // Xử lý giá vé
            // Loại bỏ dấu phẩy và khoảng trắng từ chuỗi giá vé
            String cleanedGiaVe = giaVe.replaceAll("[,\\s]", "");
            // Loại bỏ các số 0 ở đầu
            cleanedGiaVe = cleanedGiaVe.replaceFirst("^0+(?!$)", "");
            // Chuyển đổi sang double
            double giaVeDouble = Double.parseDouble(cleanedGiaVe);

            // Create ChuyenBay object
            ChuyenBay chuyenBay = new ChuyenBay(
                maChuyenBay, sanBay, changBay, ngayBay, 
                nhaGa, soGheInt, tinhTrang, maMayBay, maHang,
                diemDi, diemDen, null // Thêm tham số này
            );
            
            // Set giá vé
            chuyenBay.setGiaVe(giaVeDouble);

            // Update flight
            chuyenBayService.updateChuyenBay(chuyenBay);

            return true;

        } catch (SQLException e) {
            // Handle SQL exceptions
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi cập nhật chuyến bay: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                    "Giá vé không hợp lệ. Vui lòng nhập số.",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteFlight(String maChuyenBay) {
        if (maChuyenBay.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng chọn chuyến bay để xóa",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
                "Bạn có chắc chắn muốn xóa chuyến bay " + maChuyenBay + " không?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                chuyenBayService.deleteChuyenBay(maChuyenBay);
                return true;

            } catch (Exception e) {
                // Handle exceptions
                JOptionPane.showMessageDialog(view,
                        "Lỗi khi xóa chuyến bay: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    public List<ChuyenBay> searchFlights(String searchQuery) {
        List<ChuyenBay> allFlights = chuyenBayService.getAllChuyenBays();
        List<ChuyenBay> searchResults = new ArrayList<>();

        // Search case-insensitive
        for (ChuyenBay flight : allFlights) {
            if (flight.getMaChuyenBay().toLowerCase().contains(searchQuery.toLowerCase()) ||
                flight.getChangBay().toLowerCase().contains(searchQuery.toLowerCase()) ||
                flight.getSanBay().toLowerCase().contains(searchQuery.toLowerCase()) ||
                flight.getNhaGa().toLowerCase().contains(searchQuery.toLowerCase()) ||
                flight.getMaHang().toLowerCase().contains(searchQuery.toLowerCase())) {
                searchResults.add(flight);
            }
        }

        return searchResults;
    }

    public List<ChuyenBay> getAllFlights() {
        return chuyenBayService.getAllChuyenBays();
    }
}