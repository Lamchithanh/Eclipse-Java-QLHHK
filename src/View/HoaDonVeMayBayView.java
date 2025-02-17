package View;

import javax.swing.*;
import javax.swing.border.*;
import Controller.HoaDonPDFController;
import Database.MYSQLDB;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.awt.print.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import Model.DatVe;
import Service.VeMayBayService;
import Model.ChuyenBay;

@SuppressWarnings("unused")
public class HoaDonVeMayBayView extends JDialog {
    private DatVe datVe;
    private JPanel mainPanel;
    private final Color primaryColor = new Color(25, 118, 210);
    private final Color accentColor = new Color(41, 128, 185);
    private final Color backgroundColor = new Color(245, 245, 245);
    private final Color textColor = new Color(33, 33, 33);
    private final Color lightGray = new Color(224, 224, 224);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 18);
    private final Font contentFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @SuppressWarnings("exports")
    public HoaDonVeMayBayView(JFrame parent, DatVe datVe) {
        super(parent, "Vé Máy Bay", true);
        this.datVe = datVe;
        initializeComponents();
        // Giảm kích thước để phù hợp màn hình
        this.setSize(800, 700); // Giảm chiều cao xuống
        this.setLocationRelativeTo(parent);
        this.setResizable(false);
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new CompoundBorder(
            new EmptyBorder(20, 20, 20, 20),
            new LineBorder(primaryColor, 2, true)
        ));
    
        // Main content panel with gradient background
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, 
                    0, getHeight(), new Color(240, 240, 245));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(0, 15, 15, 15));
    
        // Add components
        contentPanel.add(createHeaderPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createRoutePanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createFlightInfoPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createPassengerInfoPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createTicketDetailsPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(createBarcodePanel());
    
        // Tạo ScrollPane với kích thước cố định
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // Thiết lập preferred size cho scrollPane
        scrollPane.setPreferredSize(new Dimension(750, 500));
        // Tăng tốc độ cuộn
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
    
        this.add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(primaryColor);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Airline Logo/Name
        JLabel airlineLabel = new JLabel("NTN AIRLINES");
        airlineLabel.setFont(new Font("Arial", Font.BOLD, 32));
        airlineLabel.setForeground(Color.WHITE);
        airlineLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Booking Reference
        JLabel bookingLabel = new JLabel("Booking Reference: " + datVe.getMaDatVe());
        bookingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bookingLabel.setForeground(Color.WHITE);
        bookingLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(airlineLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(bookingLabel);

        return panel;
    }

    private JPanel createRoutePanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw dotted line connecting cities
                g2d.setColor(primaryColor);
                float[] dash = {5.0f, 5.0f};
                g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, 
                    BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
                g2d.drawLine(100, getHeight()/2, getWidth()-100, getHeight()/2);
                
                // Draw plane icon
                g2d.setColor(primaryColor);
                int planeSize = 30;
                g2d.drawString("✈", getWidth()/2 - planeSize/2, getHeight()/2);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(0, 100));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Origin city
        JLabel originLabel = new JLabel(datVe.getChuyenBay().getDiemDi());
        originLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        originLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Destination city
        JLabel destLabel = new JLabel(datVe.getChuyenBay().getDiemDen());
        destLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        destLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        panel.add(originLabel, BorderLayout.WEST);
        panel.add(destLabel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFlightInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(createSectionBorder("Flight Information"));

        ChuyenBay flight = datVe.getChuyenBay();
        
        // Thêm hàm helper để lấy thông tin chi tiết
        String tenMayBay = getTenMayBay(flight.getMaMaybay());    // Ví dụ: "Boeing 787" thay vì "MB001"
        String tenHangBay = getTenHangBay(flight.getMaHang());    // Ví dụ: "Vietnam Airlines" thay vì "VNA"
        String tenSanbay = getTenSanBay(flight.getTenSanBay());    // Ví dụ: "Vietnam Airlines" thay vì "VNA"

        addStyledField(panel, "Flight No", flight.getMaChuyenBay());
        addStyledField(panel, "Airline", tenHangBay);
        addStyledField(panel, "Aircraft", tenMayBay);
        addStyledField(panel, "Route", flight.getChangBay());
        addStyledField(panel, "Date", dateFormat.format(flight.getNgayBay()));
        addStyledField(panel, "Departure Time", flight.getGioKhoiHanh());
        addStyledField(panel, "Arrival Time", flight.getGioHaCanh());
        addStyledField(panel, "Flight Duration", flight.getThoiGianBay() + " minutes");
        addStyledField(panel, "Airport", tenSanbay);
        addStyledField(panel, "Terminal", flight.getNhaGa());
        addStyledField(panel, "Flight Status", formatTinhTrang(flight.getTinhTrang()));
        addStyledField(panel, "Seat Class", formatHangVe(datVe.getHangVe()));

        return panel;
    }

    private String getTenSanBay(String maSanBay) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = MYSQLDB.getConnection();
            stmt = conn.prepareStatement(
                "SELECT TenSanBay FROM SanBay WHERE MaSanBay = ?"
            );
            stmt.setString(1, maSanBay);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("TenSanBay");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng tất cả các tài nguyên
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return maSanBay; // Trả về mã sân bay nếu không tìm thấy tên
    }

    // Thêm các phương thức helper
    private String getTenMayBay(String maMayBay) {
        try {
            Connection conn = MYSQLDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT LoaiMayBay FROM MayBay WHERE MaMayBay = ?"
            );
            stmt.setString(1, maMayBay);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("LoaiMayBay");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maMayBay; // Trả về mã nếu không tìm thấy tên
    }

    private String getTenHangBay(String maHang) {
        try {
            Connection conn = MYSQLDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT TenHang FROM HangHangKhong WHERE MaHang = ?"
            );
            stmt.setString(1, maHang);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("TenHang");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maHang; // Trả về mã nếu không tìm thấy tên
    }

    // Format trạng thái chuyến bay để dễ đọc hơn
    private String formatTinhTrang(String tinhTrang) {
        switch (tinhTrang) {
            case "Sắp khởi hành": return "Ready for Departure";
            case "Đã khởi hành": return "Departed";
            case "Delay": return "Delayed";
            case "Đã hủy": return "Cancelled";
            default: return tinhTrang;
        }
    }

    // Format hạng vé để hiển thị đẹp hơn
    private String formatHangVe(String hangVe) {
        switch (hangVe) {
            case "Phổ thông": return "Economy Class";
            case "Thương gia": return "Business Class";
            case "Hạng nhất": return "First Class";
            default: return hangVe;
        }
    }

    private JPanel createPassengerInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(createSectionBorder("Passenger Information"));
    
        addStyledField(panel, "Name", datVe.getHoTen());
        addStyledField(panel, "ID", datVe.getCMND());
        addStyledField(panel, "Date of Birth", dateFormat.format(datVe.getNgaySinh()));
        addStyledField(panel, "Gender", datVe.getGioiTinh());
        addStyledField(panel, "Nationality", datVe.getQuocTich());
        addStyledField(panel, "Phone", datVe.getSoDienThoai());
        addStyledField(panel, "Email", datVe.getEmail());
    
        return panel;
    }

    private JPanel createTicketDetailsPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(createSectionBorder("Ticket Details"));

        addStyledField(panel, "Booking Date", 
            dateTimeFormat.format(Date.from(datVe.getNgayDat().atZone(java.time.ZoneId.systemDefault()).toInstant())));
        addStyledField(panel, "Payment Method", datVe.getPhuongThucThanhToan());
        addStyledField(panel, "Amount", String.format("%,.0f VND", datVe.getTongGia()));

        return panel;
    }

    private JPanel createPaymentInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(createSectionBorder("Payment Details"));
    
        addStyledField(panel, "Booking Date", 
            dateTimeFormat.format(Date.from(datVe.getNgayDat().atZone(java.time.ZoneId.systemDefault()).toInstant())));
        addStyledField(panel, "Ticket Price", String.format("%,.0f VND", datVe.getChuyenBay().getGiaVe()));
        addStyledField(panel, "Quantity", String.valueOf(datVe.getSoLuong()));
        addStyledField(panel, "Payment Method", datVe.getPhuongThucThanhToan());
        if (datVe.getMaGiamGia() != null && !datVe.getMaGiamGia().isEmpty())
            addStyledField(panel, "Discount Code", datVe.getMaGiamGia());
        addStyledField(panel, "Total Amount", String.format("%,.0f VND", datVe.getTongGia()));
        addStyledField(panel, "Payment Status", 
            datVe.isXacNhanThanhToan() ? "Paid" : "Pending");
    
        return panel;
    }

    private JPanel createSpecialRequestsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(createSectionBorder("Special Services"));
    
        if (datVe.isSuatAnDacBiet()) 
            addStyledField(panel, "Special Meal", "Yes");
        if (datVe.isHoTroYTe()) 
            addStyledField(panel, "Medical Assistance", "Yes");
        if (datVe.isChoNgoiUuTien()) 
            addStyledField(panel, "Priority Seating", "Yes");
        if (datVe.isHanhLyDacBiet()) 
            addStyledField(panel, "Special Baggage", "Yes");
    
        return panel;
    }

    private JPanel createBarcodePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Barcode (simulated with text)
        JLabel barcodeLabel = new JLabel(datVe.getMaDatVe());
        barcodeLabel.setFont(new Font("Courier New", Font.BOLD, 20));
        barcodeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Important notes
        JTextArea notesArea = new JTextArea(
            "• Vui lòng đến sân bay trước giờ khởi hành 2 giờ -- Please arrive at the airport 2 hours before departure\n" +
            "• Cần có CMND/CCCD hợp lệ để làm thủ tục nhận phòng -- Valid ID required for check-in\n" +
            "• Hành lý xách tay tối đa: 7kg -- Maximum carry-on baggage: 7kg"
        );
        notesArea.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        notesArea.setEditable(false);
        notesArea.setBackground(new Color(250, 250, 250));
        notesArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        panel.add(barcodeLabel, BorderLayout.CENTER);
        panel.add(notesArea, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createEmergencyContactPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(createSectionBorder("Emergency Contact"));
    
        addStyledField(panel, "Contact Name", datVe.getNguoiLienHe());
        addStyledField(panel, "Contact Phone", datVe.getSDTNguoiLienHe());
    
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton printButton = createStyledButton("Print Ticket", new Color(46, 204, 113));
        JButton saveButton = createStyledButton("Save PDF", new Color(52, 152, 219));
        JButton closeButton = createStyledButton("Close", new Color(231, 76, 60));

        printButton.addActionListener(e -> printInvoice());
        saveButton.addActionListener(e -> savePDF());
        closeButton.addActionListener(e -> dispose());

        panel.add(printButton);
        panel.add(saveButton);
        panel.add(closeButton);

        return panel;
    }

    private void addStyledField(JPanel panel, String labelText, String value) {
        // Tạo label với text song ngữ
        JLabel label = new JLabel(getLocalizedLabel(labelText));
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(100, 100, 100));
    
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueLabel.setForeground(textColor);
    
        panel.add(label);
        panel.add(valueLabel);
    }
    
    // Helper method để tạo text song ngữ
    private String getLocalizedLabel(String key) {
        switch (key) {
            // Flight Information
            case "Flight No": return "Số Hiệu (Flight No):";
            case "Airline": return "Hãng Bay (Airline):";
            case "Aircraft": return "Máy Bay (Aircraft):";
            case "Route": return "Hành Trình (Route):";
            case "Date": return "Ngày Bay (Date):";
            case "Departure Time": return "Giờ Khởi Hành (Departure):";
            case "Arrival Time": return "Giờ Hạ Cánh (Arrival):";
            case "Flight Duration": return "Thời Gian Bay (Duration):";
            case "Airport": return "Sân Bay (Airport):";
            case "Terminal": return "Nhà Ga (Terminal):";
            case "Flight Status": return "Trạng Thái (Status):";
            case "Seat Class": return "Hạng Ghế (Class):";
    
            // Passenger Information
            case "Name": return "Họ Tên (Name):";
            case "ID": return "CMND/CCCD (ID):";
            case "Date of Birth": return "Ngày Sinh (Date of Birth):";
            case "Gender": return "Giới Tính (Gender):";
            case "Nationality": return "Quốc Tịch (Nationality):";
            case "Phone": return "Điện Thoại (Phone):";
            case "Email": return "Email (Email):";
    
            // Payment Details
            case "Booking Date": return "Ngày Đặt (Booking Date):";
            case "Payment Method": return "Phương Thức Thanh Toán (Payment):";
            case "Amount": return "Tổng Tiền (Amount):";
            case "Ticket Price": return "Giá Vé (Ticket Price):";
            case "Quantity": return "Số Lượng (Quantity):";
            case "Discount Code": return "Mã Giảm Giá (Discount):";
            case "Total Amount": return "Tổng Cộng (Total):";
            case "Payment Status": return "Trạng Thái (Status):";
    
            // Special Services
            case "Special Meal": return "Suất Ăn Đặc Biệt (Special Meal):";
            case "Medical Assistance": return "Hỗ Trợ Y Tế (Medical):";
            case "Priority Seating": return "Chỗ Ngồi Ưu Tiên (Priority):";
            case "Special Baggage": return "Hành Lý Đặc Biệt (Special Baggage):";
    
            // Emergency Contact
            case "Contact Name": return "Người Liên Hệ (Contact):";
            case "Contact Phone": return "SĐT Liên Hệ (Phone):";
    
            default: return key + ":";
        }
    }

    private Border createSectionBorder(String title) {
        // Chuyển đổi tiêu đề thành song ngữ
        String bilingualTitle = switch (title) {
            case "Flight Information" -> "Thông Tin Chuyến Bay - Flight Information";
            case "Passenger Information" -> "Thông Tin Hành Khách - Passenger Information";
            case "Ticket Details" -> "Chi Tiết Vé - Ticket Details";
            case "Payment Details" -> "Chi Tiết Thanh Toán - Payment Details";
            case "Special Services" -> "Dịch Vụ Đặc Biệt - Special Services";
            case "Emergency Contact" -> "Liên Hệ Khẩn Cấp - Emergency Contact";
            default -> title;
        };
    
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(lightGray),
            bilingualTitle
        );
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        titledBorder.setTitleColor(primaryColor);
        
        return BorderFactory.createCompoundBorder(
            titledBorder,
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        );
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
		button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(color.brighter()); }
            public void mouseExited(MouseEvent e) { button.setBackground(color); }
        });

        return button;
    }

    private List<String> getSeatNumbersForBooking(String maDatVe) {
        List<String> seatNumbers = new ArrayList<>();
        try {
            VeMayBayService service = new VeMayBayService();
            seatNumbers = service.getSeatNumbersByBookingId(maDatVe);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seatNumbers;
    }
 
    private void printInvoice() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Airline Ticket");
        
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
                    "In vé thành công!",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this,
                "Lỗi khi in vé: " + e.getMessage(),
                "Lỗi",
                JOptionPane.ERROR_MESSAGE);
        }
    }
 
    private void savePDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Vé PDF");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new File("NTN_AIRLINES_" + datVe.getMaDatVe() + ".pdf"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            
            try {
                HoaDonPDFController.xuatHoaDonPDF(datVe, filePath);
                JOptionPane.showMessageDialog(this,
                    "Xuất PDF thành công!\nFile được lưu tại: " + filePath,
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Lỗi khi xuất PDF: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
 
    @SuppressWarnings("exports")
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