package Controller;

import Model.SanBay;
import Service.SanBayService;
import View.QuanLySanBayView;

import javax.swing.*;
import java.util.List;

public class SanBayController {
    private QuanLySanBayView view;
    private SanBayService sanBayService;

    public SanBayController(QuanLySanBayView view) {
        this.view = view;
        this.sanBayService = new SanBayService();
    }

    public void addSanBay() {
        String maSanBay = view.getMaSanBayText();
        String tenSanBay = view.getTenSanBayText();

        if (sanBayService.isMaSanBayExists(maSanBay)) {
            JOptionPane.showMessageDialog(view, "Mã sân bay đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SanBay sanBay = new SanBay(maSanBay, tenSanBay);
        sanBayService.addSanBay(sanBay);
        updateSanBayList();
        view.clearFields();
        JOptionPane.showMessageDialog(view, "Thêm sân bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateSanBay() {
        String maSanBay = view.getMaSanBayText();
        String tenSanBay = view.getTenSanBayText();

        SanBay sanBay = new SanBay(maSanBay, tenSanBay);
        sanBayService.updateSanBay(sanBay);
        updateSanBayList();
        view.clearFields();
        JOptionPane.showMessageDialog(view, "Cập nhật sân bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public void deleteSanBay() {
        String maSanBay = view.getMaSanBayText();
        int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc chắn muốn xóa sân bay này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            sanBayService.deleteSanBay(maSanBay);
            updateSanBayList();
            view.clearFields();
            JOptionPane.showMessageDialog(view, "Xóa sân bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void updateSanBayList() {
        List<SanBay> sanBayList = sanBayService.getAllSanBay();
        view.getTableModel().setRowCount(0);
        for (SanBay sanBay : sanBayList) {
            view.getTableModel().addRow(new Object[] {
                sanBay.getMaSanBay(),
                sanBay.getTenSanBay()
            });
        }
    }

    public void searchSanBay(String searchQuery) {
        List<SanBay> sanBayList = sanBayService.getAllSanBay();
        view.getTableModel().setRowCount(0);
        for (SanBay sanBay : sanBayList) {
            if (sanBay.getMaSanBay().toLowerCase().contains(searchQuery) || 
                sanBay.getTenSanBay().toLowerCase().contains(searchQuery)) {
                view.getTableModel().addRow(new Object[] {
                    sanBay.getMaSanBay(),
                    sanBay.getTenSanBay()
                });
            }
        }
    }
}