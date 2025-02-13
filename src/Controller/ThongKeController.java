package Controller;

import Model.ThongKe;
import Model.UserAccount;
import Service.ThongKeService;
import View.ThongKePanel;
import View.NotificationType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ThongKeController {
    private ThongKePanel view;
    private ThongKeService thongKeService;
    private UserAccount currentUser;

    public ThongKeController(ThongKePanel view, UserAccount user) {
        this.view = view;
        this.currentUser = user;
        this.thongKeService = new ThongKeService();
    }

    // Các phương thức cũ từ phiên bản trước
    public void loadDataFromDatabase() {
        List<ThongKe> thongKeList = thongKeService.getAllThongKe();
        view.updateTableData(thongKeList);
    }

    public void performSearch(String searchText) {
        if (!searchText.isEmpty() && !searchText.equals("Tìm kiếm...")) {
            ThongKe thongKe = thongKeService.findThongKeByCode(searchText);
            if (thongKe != null) {
                List<ThongKe> singleResult = List.of(thongKe);
                view.updateTableData(singleResult);
                view.showNotification("Tìm thấy chuyến bay!", NotificationType.SUCCESS);
            } else {
                view.showNotification("Không tìm thấy chuyến bay!", NotificationType.WARNING);
            }
        }
    }

    public void filterData(String status) {
        List<ThongKe> thongKeList = thongKeService.getAllThongKe();
        List<ThongKe> filteredList;

        if (status.equals("Tất cả")) {
            filteredList = thongKeList;
        } else {
            filteredList = thongKeList.stream()
                .filter(thongKe -> thongKe.getTinhTrang().trim().equalsIgnoreCase(status.trim()))
                .collect(Collectors.toList());
        }

        view.updateTableData(filteredList);
        view.showNotification("Đã lọc dữ liệu theo: " + status, NotificationType.INFO);
    }

    // Phương thức xuất thống kê tổng quan
    public void exportOverallStatistics() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file thống kê tổng quan");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setSelectedFile(new java.io.File("thong_ke_tong_quan.csv"));

            if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new java.io.File(file.getAbsolutePath() + ".csv");
                }

                // Lấy thống kê tổng quan
                Map<String, Object> overallStats = thongKeService.getOverallStatistics();
                
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write('\ufeff'); // Write UTF-8 BOM

                    // Ghi số lượng bản ghi từng bảng
                    writer.write("THỐNG KÊ SỐ LƯỢNG BẢN GHI\n");
                    writer.write("Bảng,Số Lượng\n");
                    Map<String, Integer> tableCounts = (Map<String, Integer>) overallStats.get("TableRecordCounts");
                    for (Map.Entry<String, Integer> entry : tableCounts.entrySet()) {
                        writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                    }

                    // Ghi thống kê doanh thu
                    writer.write("\nTHỐNG KÊ DOANH THU\n");
                    writer.write("Chỉ Tiêu,Giá Trị\n");
                    writer.write("Tổng Doanh Thu," + overallStats.get("TotalRevenue") + "\n");
                    writer.write("Giá Vé Trung Bình," + overallStats.get("AverageTicketPrice") + "\n");
                    writer.write("Tổng Số Chuyến Bay," + overallStats.get("TotalFlights") + "\n");
                    writer.write("Tổng Số Khách Hàng," + overallStats.get("TotalCustomers") + "\n");

                    // Ghi thống kê nhân viên
                    writer.write("\nTHỐNG KÊ NHÂN VIÊN\n");
                    writer.write("Chức Vụ,Số Lượng,Lương Trung Bình\n");
                    List<Map<String, Object>> employeeStats = 
                        (List<Map<String, Object>>) overallStats.get("EmployeeStatistics");
                    for (Map<String, Object> stat : employeeStats) {
                        writer.write(
                            stat.get("ChucVu") + "," + 
                            stat.get("SoLuong") + "," + 
                            stat.get("LuongTrungBinh") + "\n"
                        );
                    }

                    // Ghi thống kê tình trạng chuyến bay
                    writer.write("\nTHỐNG KÊ TÌNH TRẠNG CHUYẾN BAY\n");
                    writer.write("Tình Trạng,Số Lượng\n");
                    List<Map<String, Object>> flightStatusStats = 
                        (List<Map<String, Object>>) overallStats.get("FlightStatusStatistics");
                    for (Map<String, Object> stat : flightStatusStats) {
                        writer.write(
                            stat.get("TinhTrang") + "," + 
                            stat.get("SoLuong") + "\n"
                        );
                    }

                    view.showNotification("Xuất báo cáo thống kê tổng quan thành công!", NotificationType.SUCCESS);
                }
            }
        } catch (IOException e) {
            view.showNotification("Lỗi khi xuất báo cáo: " + e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();
        }
    }

    // Phương thức xuất CSV cũ
    public void exportStatisticsToCSV() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn vị trí lưu file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setSelectedFile(new java.io.File("thong_ke_chuyen_bay.csv"));

            if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".csv")) {
                    file = new java.io.File(file.getAbsolutePath() + ".csv");
                }

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write('\ufeff'); // Write UTF-8 BOM
                    writer.write("Mã chuyến bay,Sân bay,Chặng bay,Ngày bay,Số ghế đã đặt,Tình trạng\n");

                    DefaultTableModel model = view.getTableModel();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        for (int col = 0; col < model.getColumnCount(); col++) {
                            String value = model.getValueAt(row, col).toString();
                            value = value.replace("\"", "\"\"");
                            if (value.contains(",")) {
                                value = "\"" + value + "\"";
                            }
                            writer.write(value);
                            if (col < model.getColumnCount() - 1) {
                                writer.write(",");
                            }
                        }
                        writer.write("\n");
                    }
                    view.showNotification("Xuất báo cáo thành công!", NotificationType.SUCCESS);
                }
            }
        } catch (IOException e) {
            view.showNotification("Lỗi khi xuất báo cáo: " + e.getMessage(), NotificationType.ERROR);
            e.printStackTrace();
        }
    }
}