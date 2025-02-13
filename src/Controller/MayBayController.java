package Controller;

import Model.MayBay;
import Service.MayBayService;
import View.QuanLyMayBay;

import javax.swing.*;
import java.util.List;

public class MayBayController {
    private QuanLyMayBay view;
    private MayBayService service;

    public MayBayController(QuanLyMayBay view) {
        this.view = view;
        this.service = new MayBayService();
    }

    public boolean validateInput(String maMayBay, String loaiMayBay, String sucChua, String maHang) {
        StringBuilder errorMessage = new StringBuilder("Vui lòng kiểm tra lại:\n");
        boolean isValid = true;

        if (maMayBay.isEmpty()) {
            errorMessage.append("- Mã máy bay không được để trống\n");
            isValid = false;
        }

        if (loaiMayBay.isEmpty()) {
            errorMessage.append("- Loại máy bay không được để trống\n");
            isValid = false;
        }

        if (sucChua.isEmpty()) {
            errorMessage.append("- Sức chứa không được để trống\n");
            isValid = false;
        } else {
            try {
                int capacity = Integer.parseInt(sucChua);
                if (capacity <= 0) {
                    errorMessage.append("- Sức chứa phải là số dương\n");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                errorMessage.append("- Sức chứa phải là số nguyên\n");
                isValid = false;
            }
        }

        if (maHang == null || maHang.isEmpty()) {
            errorMessage.append("- Vui lòng chọn mã hãng\n");
            isValid = false;
        }

        if (!isValid) {
            JOptionPane.showMessageDialog(view,
                    errorMessage.toString(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }

        return isValid;
    }

    public boolean addMayBay(String maMayBay, String loaiMayBay, String sucChua, String maHang) {
        try {
            if (!validateInput(maMayBay, loaiMayBay, sucChua, maHang)) {
                return false;
            }

            if (service.findMayBayByCode(maMayBay) != null) {
                JOptionPane.showMessageDialog(view,
                        "Mã máy bay đã tồn tại",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            MayBay mayBay = new MayBay(
                maMayBay,
                loaiMayBay,
                Integer.parseInt(sucChua),
                maHang
            );
            return service.addMayBay(mayBay);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm máy bay: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateMayBay(String maMayBay, String loaiMayBay, String sucChua, String maHang) {
        try {
            if (!validateInput(maMayBay, loaiMayBay, sucChua, maHang)) {
                return false;
            }

            if (service.findMayBayByCode(maMayBay) == null) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy máy bay để cập nhật",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return service.updateMayBay(
                maMayBay,
                loaiMayBay,
                Integer.parseInt(sucChua),
                maHang
            );

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi cập nhật máy bay: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteMayBay(String maMayBay) {
        try {
            if (maMayBay.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Vui lòng chọn máy bay để xóa",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (service.findMayBayByCode(maMayBay) == null) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy máy bay để xóa",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int confirm = JOptionPane.showConfirmDialog(view,
                    "Bạn có chắc chắn muốn xóa máy bay này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                return service.deleteMayBay(maMayBay);
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi xóa máy bay: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<MayBay> getAllMayBay() {
        return service.getAllMayBay();
    }

    public List<MayBay> searchMayBay(String keyword) {
        List<MayBay> mayBays = getAllMayBay();
        return mayBays.stream()
                .filter(mb -> 
                    mb.getMaMayBay().toLowerCase().contains(keyword.toLowerCase()) ||
                    mb.getLoaiMayBay().toLowerCase().contains(keyword.toLowerCase()) ||
                    mb.getMaHang().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
    }

    public MayBay findMayBayByCode(String maMayBay) {
        return service.findMayBayByCode(maMayBay);
    }
}