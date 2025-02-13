package Controller;

import Model.NhanVien;
import Service.NhanVienService;
import View.QuanLyNhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class NhanVienController {
    private QuanLyNhanVien view;
    private NhanVienService service;
    private DefaultTableModel tableModel;

    public NhanVienController(QuanLyNhanVien view) {
        this.view = view;
        this.service = new NhanVienService();
    }

    public void setTableModel(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
    }

    public boolean validateInput(String maNhanVien, String tenNhanVien, String chucVu, String maHang) {
        StringBuilder errorMessage = new StringBuilder("Vui lòng kiểm tra lại:\n");
        boolean isValid = true;

        if (maNhanVien.isEmpty()) {
            errorMessage.append("- Mã nhân viên không được để trống\n");
            isValid = false;
        } else if (!maNhanVien.matches("^[a-zA-Z0-9]+$")) {
            errorMessage.append("- Mã nhân viên chỉ được chứa chữ và số\n");
            isValid = false;
        }

        if (tenNhanVien.isEmpty()) {
            errorMessage.append("- Tên nhân viên không được để trống\n");
            isValid = false;
        }

        if (chucVu.isEmpty()) {
            errorMessage.append("- Chức vụ không được để trống\n");
            isValid = false;
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

    public boolean addNhanVien(String maNhanVien, String tenNhanVien, String chucVu, String maHang) {
        try {
            if (!validateInput(maNhanVien, tenNhanVien, chucVu, maHang)) {
                return false;
            }

            if (service.findNhanVienByCode(maNhanVien) != null) {
                JOptionPane.showMessageDialog(view,
                        "Mã nhân viên đã tồn tại trong hệ thống",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            NhanVien nhanVien = new NhanVien(maNhanVien, tenNhanVien, chucVu, maHang);
            boolean result = service.addNhanVien(nhanVien);
            
            if (result) {
                updateTable();
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi thêm nhân viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean updateNhanVien(String maNhanVien, String tenNhanVien, String chucVu, String maHang) {
        try {
            if (!validateInput(maNhanVien, tenNhanVien, chucVu, maHang)) {
                return false;
            }

            NhanVien existingNhanVien = service.findNhanVienByCode(maNhanVien);
            if (existingNhanVien == null) {
                JOptionPane.showMessageDialog(view,
                        "Không tìm thấy nhân viên để cập nhật",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            boolean result = service.updateNhanVien(maNhanVien, tenNhanVien, chucVu, maHang);
            
            if (result) {
                updateTable();
                return true;
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi cập nhật nhân viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean deleteNhanVien(String maNhanVien) {
        try {
            if (maNhanVien.isEmpty()) {
                JOptionPane.showMessageDialog(view,
                        "Vui lòng chọn nhân viên để xóa",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int confirm = JOptionPane.showConfirmDialog(view,
                    "Bạn có chắc chắn muốn xóa nhân viên này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean result = service.deleteNhanVien(maNhanVien);
                
                if (result) {
                    updateTable();
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi xóa nhân viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public List<NhanVien> getAllNhanVien() {
        try {
            return service.getAllNhanVien();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view,
                    "Lỗi khi tải danh sách nhân viên: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return List.of();
        }
    }

    public List<NhanVien> searchNhanVien(String keyword) {
        try {
            List<NhanVien> nhanViens = getAllNhanVien();
            return nhanViens.stream()
                    .filter(nv -> 
                        nv.getMaNhanVien().toLowerCase().contains(keyword.toLowerCase()) ||
                        nv.getTenNhanVien().toLowerCase().contains(keyword.toLowerCase()) ||
                        nv.getChucVu().toLowerCase().contains(keyword.toLowerCase()) ||
                        nv.getMaHang().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    private void updateTable() {
        if (tableModel != null) {
            tableModel.setRowCount(0);
            List<NhanVien> nhanViens = getAllNhanVien();
            for (NhanVien nv : nhanViens) {
                tableModel.addRow(new Object[]{
                    nv.getMaNhanVien(),
                    nv.getTenNhanVien(),
                    nv.getChucVu(),
                    nv.getMaHang()
                });
            }
        }
    }

    public NhanVien findNhanVienByCode(String maNhanVien) {
        try {
            return service.findNhanVienByCode(maNhanVien);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}