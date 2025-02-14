package View;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.print.*;

import Model.DatVe;
import Model.ChuyenBay;

public class HoaDonVeMayBayView extends JDialog {
    private DatVe datVe;
    private JPanel mainPanel;
    private final Color primaryColor = new Color(41, 128, 185);
    private final Color backgroundColor = new Color(236, 240, 241);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 16);
    private final Font contentFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public HoaDonVeMayBayView(JFrame parent, DatVe datVe) {
        super(parent, "Hóa Đơn Vé Máy Bay", true);
        this.datVe = datVe;
        initializeComponents();
        this.setSize(800, 900);
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
    }

    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        addHeader();

        // Flight Information
        addSection("THÔNG TIN CHUYẾN BAY", createFlightInfoPanel());

        // Passenger Information
        addSection("THÔNG TIN HÀNH KHÁCH", createPassengerInfoPanel());

        // Booking Information
        addSection("THÔNG TIN ĐẶT VÉ", createBookingInfoPanel());

        // Payment Information
        addSection("THÔNG TIN THANH TOÁN", createPaymentInfoPanel());

        // Special Requests
        if (hasSpecialRequests()) {
            addSection("YÊU CẦU ĐẶC BIỆT", createSpecialRequestsPanel());
        }

        // Terms and Conditions
        addTermsAndConditions();

        // Add buttons
        addButtonPanel();

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(scrollPane);
    }

    private JPanel createFlightInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBackground(Color.WHITE);
        
        ChuyenBay flight = datVe.getChuyenBay();
        addLabelValuePair(panel, "Mã Chuyến Bay:", flight.getMaChuyenBay());
        addLabelValuePair(panel, "Điểm Khởi Hành:", flight.getDiemDi());
        addLabelValuePair(panel, "Điểm Đến:", flight.getDiemDen());
        
        // Xử lý ngày bay an toàn
        String ngayBayStr = "N/A";
        if (flight.getNgayBay() != null) {
            try {
                ngayBayStr = dateFormat.format(flight.getNgayBay());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addLabelValuePair(panel, "Ngày Bay:", ngayBayStr);
        
        addLabelValuePair(panel, "Chặng Bay:", flight.getChangBay());
        addLabelValuePair(panel, "Nhà Ga:", flight.getNhaGa());
        
        return panel;
    }
    
    private JPanel createPassengerInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBackground(Color.WHITE);
        
        addLabelValuePair(panel, "Họ và Tên:", datVe.getHoTen());
        addLabelValuePair(panel, "CMND/CCCD:", datVe.getCMND());
        
        // Xử lý ngày sinh an toàn 
        String ngaySinhStr = "N/A"; 
        if (datVe.getNgaySinh() != null) {
            try {
                ngaySinhStr = dateFormat.format(datVe.getNgaySinh());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addLabelValuePair(panel, "Ngày Sinh:", ngaySinhStr);
        
        addLabelValuePair(panel, "Giới Tính:", datVe.getGioiTinh());
        addLabelValuePair(panel, "Quốc Tịch:", datVe.getQuocTich());
        addLabelValuePair(panel, "Số Điện Thoại:", datVe.getSoDienThoai());
        
        return panel;
    }
    
    private void addHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
    
        // Company Logo/Name
        JLabel titleLabel = new JLabel("AIRLINE TICKET");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        // Invoice Details
        JPanel invoiceDetails = new JPanel(new GridLayout(2, 2, 10, 5));
        invoiceDetails.setBackground(Color.WHITE);
        invoiceDetails.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
    
        addLabelValuePair(invoiceDetails, "Mã Đặt Vé:", datVe.getMaDatVe());
        
        // Xử lý ngày đặt an toàn
        String ngayDatStr = "N/A";
        if (datVe.getNgayDat() != null) {
            try {
                ngayDatStr = dateTimeFormat.format(Date.from(datVe.getNgayDat().atZone(java.time.ZoneId.systemDefault()).toInstant()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addLabelValuePair(invoiceDetails, "Ngày Đặt:", ngayDatStr);
    
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(invoiceDetails);
    
        mainPanel.add(headerPanel);
        mainPanel.add(createSeparator());
    }

    private JPanel createBookingInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBackground(Color.WHITE);
        
        addLabelValuePair(panel, "Hạng Vé:", datVe.getHangVe());
        addLabelValuePair(panel, "Số Lượng Vé:", String.valueOf(datVe.getSoLuong()));
        addLabelValuePair(panel, "Mã Giảm Giá:", 
            datVe.getMaGiamGia() != null ? datVe.getMaGiamGia() : "Không có");
        
        return panel;
    }

    private JPanel createPaymentInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBackground(Color.WHITE);
        
        addLabelValuePair(panel, "Tổng Tiền:", String.format("%,.0f VND", datVe.getTongGia()));
        addLabelValuePair(panel, "Phương Thức Thanh Toán:", datVe.getPhuongThucThanhToan());
        addLabelValuePair(panel, "Trạng Thái Thanh Toán:", 
            datVe.isXacNhanThanhToan() ? "Đã thanh toán" : "Chưa thanh toán");
        
        return panel;
    }

    private JPanel createSpecialRequestsPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBackground(Color.WHITE);
        
        if (datVe.isSuatAnDacBiet()) addLabelValuePair(panel, "Suất Ăn Đặc Biệt:", "Có");
        if (datVe.isHoTroYTe()) addLabelValuePair(panel, "Hỗ Trợ Y Tế:", "Có");
        if (datVe.isChoNgoiUuTien()) addLabelValuePair(panel, "Chỗ Ngồi Ưu Tiên:", "Có");
        if (datVe.isHanhLyDacBiet()) addLabelValuePair(panel, "Hành Lý Đặc Biệt:", "Có");
        
        return panel;
    }

    private void addTermsAndConditions() {
        JTextArea termsArea = new JTextArea(
            "Điều khoản và điều kiện:\n\n" +
            "1. Vé đã xuất không được hoàn lại hoặc thay đổi thông tin.\n" +
            "2. Hành khách cần có mặt tại sân bay ít nhất 2 tiếng trước giờ khởi hành.\n" +
            "3. Hành lý xách tay không được vượt quá 7kg.\n" +
            "4. Vui lòng mang theo giấy tờ tùy thân khi làm thủ tục."
        );
        termsArea.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        termsArea.setEditable(false);
        termsArea.setBackground(Color.WHITE);
        termsArea.setLineWrap(true);
        termsArea.setWrapStyleWord(true);
        termsArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(termsArea);
    }

    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
    
        // Tạo các nút với style thống nhất
        JButton printButton = createStyledButton("In Hóa Đơn", new Color(46, 204, 113));  // Màu xanh lá
        JButton saveButton = createStyledButton("Lưu PDF", new Color(52, 152, 219));      // Màu xanh dương
        JButton closeButton = createStyledButton("Đóng", new Color(231, 76, 60));         // Màu đỏ
    
        // Thêm xử lý sự kiện cho các nút
        printButton.addActionListener(e -> printInvoice());
        saveButton.addActionListener(e -> savePDF());
        closeButton.addActionListener(e -> dispose());
    
        // Thêm các nút vào panel
        buttonPanel.add(printButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(closeButton);
    
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
    }
    
    // Thêm phương thức tạo nút với style giống QuanLyDatVeView
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    
        // Thêm hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
    
        return button;
    }

    private void addSection(String title, JPanel content) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(headerFont);
        titleLabel.setForeground(primaryColor);
        
        sectionPanel.add(titleLabel);
        sectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sectionPanel.add(content);

        mainPanel.add(sectionPanel);
        mainPanel.add(createSeparator());
    }

    private void addLabelValuePair(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        JLabel valueComponent = new JLabel(value);
        
        labelComponent.setFont(contentFont);
        valueComponent.setFont(contentFont);
        
        labelComponent.setForeground(Color.GRAY);
        valueComponent.setForeground(Color.BLACK);
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(224, 224, 224));
        separator.setBackground(Color.WHITE);
        return separator;
    }

    private boolean hasSpecialRequests() {
        return datVe.isSuatAnDacBiet() || datVe.isHoTroYTe() || 
               datVe.isChoNgoiUuTien() || datVe.isHanhLyDacBiet();
    }

    private void printInvoice() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Hóa Đơn Vé Máy Bay");
        
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            
            // Scale to fit page width
            double scaleX = pageFormat.getImageableWidth() / mainPanel.getWidth();
            double scaleY = pageFormat.getImageableHeight() / mainPanel.getHeight();
            double scale = Math.min(scaleX, scaleY);
            
            g2d.scale(scale, scale);
            
            mainPanel.printAll(g2d);
            
            return Printable.PAGE_EXISTS;
        });
        
        try {
            if (job.printDialog()) {
                job.print();
                JOptionPane.showMessageDialog(this,
                    "In hóa đơn thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi in hóa đơn: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void savePDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Hóa Đơn PDF");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            // Implement PDF saving logic here
            JOptionPane.showMessageDialog(this,
                "Tính năng đang được phát triển",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void showInvoice(Window parent, DatVe datVe) {
        JFrame parentFrame;
        if (parent instanceof JFrame) {
            parentFrame = (JFrame) parent;
        } else {
            parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
        }
        
        HoaDonVeMayBayView dialog = new HoaDonVeMayBayView(parentFrame, datVe);
        dialog.setVisible(true);
    }

   
}