package Controller;

import Model.HangHangKhong;
import Service.HangHangKhongService;
import View.QuanLyHangHangKhong;

import javax.swing.*;
import java.util.List;

public class HangHangKhongController {
    private QuanLyHangHangKhong view;
    private HangHangKhongService service;

    public HangHangKhongController(QuanLyHangHangKhong view) {
        this.view = view;
        this.service = new HangHangKhongService();
    }

    public boolean validateInput(String maHang, String tenHang, String diaChi, String soDienThoai, String email) {
        if (maHang.isEmpty() || tenHang.isEmpty() || diaChi.isEmpty() || soDienThoai.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "Vui lòng điền đầy đủ thông tin",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate phone number
        if (!soDienThoai.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(view,
                    "Số điện thoại không hợp lệ",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(view,
                    "Email không hợp lệ",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean addHangHangKhong(String maHang, String tenHang, String diaChi, String soDienThoai, String email) {
        try {
            if (!validateInput(maHang, tenHang, diaChi, soDienThoai, email)) {
                return false;
            }

            if (service.findHangHangKhongByCode(maHang) != null) {
                JOptionPane.showMessageDialog(view,
                        "Mã hãng đã tồn tại",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            HangHangKhong hang = new HangHangKhong(maHang, tenHang, diaChi, soDienThoai, email);
            return service.addHangHangKhong(hang);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateHangHangKhong(String maHang, String tenHang, String diaChi, String soDienThoai, String email) {
        try {
            if (!validateInput(maHang, tenHang, diaChi, soDienThoai, email)) {
                return false;
            }

            HangHangKhong hang = new HangHangKhong(maHang, tenHang, diaChi, soDienThoai, email);
            return service.updateHangHangKhong(hang);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHangHangKhong(String maHang) {
        try {
            return service.deleteHangHangKhong(maHang);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<HangHangKhong> getAllHangHangKhong() {
        return service.getAllHangHangKhong();
    }

    public List<HangHangKhong> searchHangHangKhong(String keyword) {
        List<HangHangKhong> allHang = service.getAllHangHangKhong();
        return allHang.stream()
                .filter(hang -> 
                        hang.getMaHang().toLowerCase().contains(keyword.toLowerCase()) ||
                        hang.getTenHang().toLowerCase().contains(keyword.toLowerCase()) ||
                        hang.getDiaChi().toLowerCase().contains(keyword.toLowerCase()) ||
                        hang.getSoDienThoai().toLowerCase().contains(keyword.toLowerCase()) ||
                        hang.getEmail().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }
}