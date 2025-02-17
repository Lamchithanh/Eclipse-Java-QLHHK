
package Controller;

import Model.VeMayBay;
import Service.VeMayBayService;
import View.NotificationType;
import View.QuanLyVeMayBay;

import javax.swing.*;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class VeMayBayController {
    private QuanLyVeMayBay view;
    private VeMayBayService service;

    public VeMayBayController(QuanLyVeMayBay view) {
        this.view = view;
        this.service = new VeMayBayService();
    }

    public void addTicket(VeMayBay ticket) {
        if (service.isTicketExists(ticket.getMaVe())) {
            view.showMessage("Mã vé đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isAdded = service.addVeMayBay(ticket);
        if (isAdded) {
            updateTicketList();
            view.clearFields();
            view.showNotification("Thêm vé máy bay thành công!", NotificationType.SUCCESS);
        } else {
            view.showNotification("Không thể thêm vé máy bay!", NotificationType.ERROR);
        }
    }

    public void updateTicket(VeMayBay ticket) {
        try {
            boolean isUpdated = service.updateVeMayBay(ticket);
            if (isUpdated) {
                updateTicketList();
                view.clearFields();
                view.showNotification("Cập nhật vé máy bay thành công!", NotificationType.SUCCESS);
            }
        } catch (SQLException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("CMND đã tồn tại")) {
                view.showNotification("CMND đã tồn tại trong hệ thống với khách hàng khác!", 
                    NotificationType.ERROR);
            } else {
                view.showNotification("Lỗi khi cập nhật: " + errorMessage, 
                    NotificationType.ERROR);
            }
        }
    }

    public void deleteTicket(String maVe) {
        boolean isDeleted = service.deleteVeMayBay(maVe);
        if (isDeleted) {
            updateTicketList();
            view.clearFields();
            view.showNotification("Xóa vé máy bay thành công!", NotificationType.SUCCESS);
        } else {
            view.showNotification("Không thể xóa vé máy bay!", NotificationType.ERROR);
        }
    }

    public void searchTickets(String searchQuery) {
        List<VeMayBay> filteredList = service.searchVeMayBay(searchQuery);
        view.updateTableData(filteredList);
    }

    public void updateTicketList() {
        List<VeMayBay> veMayBayList = service.getAllVeMayBay();
        view.updateTableData(veMayBayList);
    }

    public VeMayBay createVeMayBayFromInput(String maVe, String maChuyenBay, 
    String maKhachHang, String tenKhachHang, String cmnd, String ngayDat, 
    String giaVe, String trangThai, String soDienThoai, String diaChi, 
    String email, String ngaySinh, String quocTich, String hangVe) throws ParseException {
    
    // Chuyển đổi ngày đặt vé
    java.sql.Date sqlNgayDat = null;
    if (!ngayDat.isEmpty()) {
        java.util.Date utilNgayDat = new SimpleDateFormat("yyyy-MM-dd").parse(ngayDat);
        sqlNgayDat = new java.sql.Date(utilNgayDat.getTime());
    }
    
    // Chuyển đổi ngày sinh
    java.sql.Date sqlNgaySinh = null;
    if (ngaySinh != null && !ngaySinh.isEmpty() && !ngaySinh.equals("YYYY-MM-DD")) {
        try {
            java.util.Date utilNgaySinh = new SimpleDateFormat("yyyy-MM-dd").parse(ngaySinh);
            sqlNgaySinh = new java.sql.Date(utilNgaySinh.getTime());
        } catch (ParseException e) {
            // Nếu không parse được ngày sinh, bỏ qua
        }
    }

    // Chuyển đổi giá vé
    double giaVeDouble = 0.0;
    try {
        if (!giaVe.isEmpty() && !giaVe.equals("Nhập giá vé...")) {
            giaVeDouble = Double.parseDouble(giaVe);
        } else {
            giaVeDouble = service.getTicketPrice(maChuyenBay);
        }
    } catch (NumberFormatException e) {
        giaVeDouble = service.getTicketPrice(maChuyenBay);
    }

    // Xử lý các trường thông tin khách hàng
    soDienThoai = soDienThoai.equals("Nhập số điện thoại...") ? null : soDienThoai;
    diaChi = diaChi.equals("Nhập địa chỉ...") ? null : diaChi;
    email = email.equals("Nhập email...") ? null : email;
    quocTich = quocTich.equals("Nhập quốc tịch...") ? null : quocTich;

    // Tạo và trả về đối tượng VeMayBay với đầy đủ thông tin
    VeMayBay veMayBay = new VeMayBay(maVe, maChuyenBay, maKhachHang, sqlNgayDat, 
        tenKhachHang, cmnd, giaVeDouble, trangThai, soDienThoai, 
        diaChi, email, sqlNgaySinh, quocTich);
    
    // Thêm thông tin hạng vé
    veMayBay.setHangVe(hangVe);
    
    // Số ghế sẽ được tự động cấp khi lưu vào database
    return veMayBay;
}
}