package View;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.sql.Date;
import View.JDateChooser;

import Controller.DatVeController;
import Model.ChuyenBay;
import Model.DatVe;

public class QuanLyDatVeView extends JPanel {
    // Existing core components
    private DefaultTableModel tableModel;
    private JTable table;
    private DatVeController controller;

    // Booking Information
    private JTextField txtMaDatVe;
    private JComboBox<ChuyenBay> cbChuyenBay;
    private JTextField txtSoLuong;
    private JTextField txtTongGia;
    
    // Personal Information
    private JTextField txtHoTen;
    private JTextField txtCMND;
    private JDateChooser dateNgaySinh;
    private JComboBox<String> cbGioiTinh;
    private JTextField txtQuocTich;
    
    // Contact Information
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextArea txtDiaChi;
    
    // Flight Details
    private JTextField txtDiemDi;
    private JTextField txtDiemDen;
    private JDateChooser dateNgayBay;
    private JComboBox<String> cbHangVe;
    
    // Payment Information
    private JComboBox<String> cbPhuongThucThanhToan;
    private JTextField txtMaGiamGia;
    private JCheckBox chkXacNhanThanhToan;
    private JComboBox<String> cbTrangThai;
    
    // Special Requests
    private JCheckBox chkSuatAnDacBiet;
    private JCheckBox chkHoTroYTe;
    private JCheckBox chkChoNgoiUuTien;
    private JCheckBox chkHanhLyDacBiet;
    
    // Emergency Contact
    private JTextField txtNguoiLienHe;
    private JTextField txtSDTNguoiLienHe;

    // UI Colors
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color backgroundColor = new Color(236, 240, 241);
    private Color textColor = new Color(44, 62, 80);

    // Flight Details Labels
    private JLabel lblFlightDetails;
    private JLabel lblAvailableSeats;
    private JLabel lblDepartureTime;
    private JLabel lblArrivalTime;
    private JLabel lblAirline;

    public QuanLyDatVeView(TrangChuPanel trangChuPanel) {
        initializeComponents();
        setupUI();
        controller = new DatVeController(this);
        setupListeners();
        loadInitialData();
    }

    private void initializeComponents() {
        // Initialize booking components
        txtMaDatVe = createStyledTextField("Mã đặt vé");
        cbChuyenBay = new JComboBox<>();
        txtSoLuong = createStyledTextField("Số lượng vé");
        txtTongGia = createStyledTextField("Tổng giá");
        txtTongGia.setEditable(false);

        // Initialize personal information components
        txtHoTen = createStyledTextField("Họ và tên");
        txtCMND = createStyledTextField("CMND/CCCD");
        dateNgaySinh = new JDateChooser();
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        txtQuocTich = createStyledTextField("Quốc tịch");

        // Initialize contact information components
        txtSoDienThoai = createStyledTextField("Số điện thoại");
        txtEmail = createStyledTextField("Email");
        txtDiaChi = new JTextArea(3, 20);
        txtDiaChi.setLineWrap(true);
        txtDiaChi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(204, 204, 204)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Initialize flight details components
        txtDiemDi = createStyledTextField("Điểm đi");
        txtDiemDen = createStyledTextField("Điểm đến");
        dateNgayBay = new JDateChooser();
        cbHangVe = new JComboBox<>(new String[]{"Phổ thông", "Thương gia", "Hạng nhất"});

        // Initialize payment components
        cbPhuongThucThanhToan = new JComboBox<>(new String[]{
            "Tiền mặt", "Chuyển khoản", "Thẻ tín dụng"
        });
        txtMaGiamGia = createStyledTextField("Mã giảm giá");
        chkXacNhanThanhToan = new JCheckBox("Xác nhận thanh toán");
        cbTrangThai = new JComboBox<>(new String[]{"Đã đặt", "Đã thanh toán", "Đã hủy"});

        // Initialize special requests components
        chkSuatAnDacBiet = new JCheckBox("Suất ăn đặc biệt");
        chkHoTroYTe = new JCheckBox("Hỗ trợ y tế");
        chkChoNgoiUuTien = new JCheckBox("Chỗ ngồi ưu tiên");
        chkHanhLyDacBiet = new JCheckBox("Hành lý đặc biệt");

        // Initialize emergency contact components
        txtNguoiLienHe = createStyledTextField("Tên người liên hệ");
        txtSDTNguoiLienHe = createStyledTextField("Số điện thoại người liên hệ");

        // Initialize flight details labels
        lblFlightDetails = new JLabel();
        lblAvailableSeats = new JLabel();
        lblDepartureTime = new JLabel();
        lblArrivalTime = new JLabel();
        lblAirline = new JLabel();

        // Initialize table
        initializeTable();
    }

    private void initializeTable() {
        String[] columnNames = {
            "Mã Vé", "Chuyến Bay", "Khách Hàng", 
            "Ngày Đặt", "Số Lượng", "Tổng Giá", "Trạng Thái"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(primaryColor);
        table.getTableHeader().setForeground(Color.WHITE);
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);

        // Create header
        add(createHeader(), BorderLayout.NORTH);

        // Create main content panel
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBackground(backgroundColor);

        // Create form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add booking information section
        addSection(formPanel, createBookingInfoPanel(), "Thông tin đặt vé", gbc, 0);
        
        // Add personal information section
        addSection(formPanel, createPersonalInfoPanel(), "Thông tin cá nhân", gbc, 1);
        
        // Add contact information section
        addSection(formPanel, createContactInfoPanel(), "Thông tin liên hệ", gbc, 2);
        
        // Add flight details section
        addSection(formPanel, createFlightDetailsPanel(), "Chi tiết chuyến bay", gbc, 3);
        
        // Add payment section
        addSection(formPanel, createPaymentPanel(), "Thông tin thanh toán", gbc, 4);
        
        // Add special requests section
        addSection(formPanel, createSpecialRequestsPanel(), "Yêu cầu đặc biệt", gbc, 5);
        
        // Add emergency contact section
        addSection(formPanel, createEmergencyContactPanel(), "Thông tin liên hệ khẩn cấp", gbc, 6);

        // Add form to scroll pane
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(null);
        
        // Create split pane with form and table
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            formScrollPane, createTablePanel());
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.7);
        
        mainContent.add(splitPane, BorderLayout.CENTER);
        mainContent.add(createButtonPanel(), BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel headerLabel = new JLabel("Quản Lý Đặt Vé");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);

        headerPanel.add(headerLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createBookingInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = createStandardGBC();

        addFormField(panel, "Mã đặt vé:", txtMaDatVe, gbc, 0);
        addFormField(panel, "Chuyến bay:", cbChuyenBay, gbc, 1);
        addFormField(panel, "Số lượng vé:", txtSoLuong, gbc, 2);
        addFormField(panel, "Tổng giá:", txtTongGia, gbc, 3);

        return panel;
    }

    private JPanel createPersonalInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = createStandardGBC();

        addFormField(panel, "Họ và tên:", txtHoTen, gbc, 0);
        addFormField(panel, "CMND/CCCD:", txtCMND, gbc, 1);
        addFormField(panel, "Ngày sinh:", dateNgaySinh, gbc, 2);
        addFormField(panel, "Giới tính:", cbGioiTinh, gbc, 3);
        addFormField(panel, "Quốc tịch:", txtQuocTich, gbc, 4);

        return panel;
    }

    private JPanel createContactInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = createStandardGBC();

        addFormField(panel, "Số điện thoại:", txtSoDienThoai, gbc, 0);
        addFormField(panel, "Email:", txtEmail, gbc, 1);
        addFormField(panel, "Địa chỉ:", new JScrollPane(txtDiaChi), gbc, 2);

        return panel;
    }

    private JPanel createFlightDetailsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = createStandardGBC();

        addFormField(panel, "Điểm đi:", txtDiemDi, gbc, 0);
        addFormField(panel, "Điểm đến:", txtDiemDen, gbc, 1);
        addFormField(panel, "Ngày bay:", dateNgayBay, gbc, 2);
        addFormField(panel, "Hạng vé:", cbHangVe, gbc, 3);

        return panel;
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = createStandardGBC();

        addFormField(panel, "Phương thức:", cbPhuongThucThanhToan, gbc, 0);
        addFormField(panel, "Mã giảm giá:", txtMaGiamGia, gbc, 1);
        addFormField(panel, "Trạng thái:", cbTrangThai, gbc, 2);
        addFormField(panel, "", chkXacNhanThanhToan, gbc, 3);

        return panel;
    }

    private JPanel createSpecialRequestsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(Color.WHITE);

        panel.add(chkSuatAnDacBiet);
        panel.add(chkHoTroYTe);
        panel.add(chkChoNgoiUuTien);
        panel.add(chkHanhLyDacBiet);

        return panel;
    }

    private JPanel createEmergencyContactPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = createStandardGBC();

        addFormField(panel, "Tên người liên hệ:", txtNguoiLienHe, gbc, 0);
        addFormField(panel, "Số điện thoại:", txtSDTNguoiLienHe, gbc, 1);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(backgroundColor);

        JButton btnAdd = createStyledButton("Thêm", new Color(46, 204, 113));
        JButton btnEdit = createStyledButton("Sửa", new Color(52, 152, 219));
        JButton btnDelete = createStyledButton("Xóa", new Color(231, 76, 60));
        JButton btnClear = createStyledButton("Làm mới", new Color(241, 196, 15));

        btnAdd.addActionListener(e -> handleAddBooking());
        btnEdit.addActionListener(e -> handleEditBooking());
        btnDelete.addActionListener(e -> handleDeleteBooking());
        btnClear.addActionListener(e -> clearFields());

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnClear);

        return panel;
    }

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

    private JTextField createStyledTextField(String placeholder) {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setPreferredSize(new Dimension(200, 35));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224)),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        return textField;
    }

    private void addSection(JPanel container, JPanel section, String title, 
                          GridBagConstraints gbc, int gridy) {
        section.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(primaryColor),
            title,
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            primaryColor
        ));
        
        gbc.gridy = gridy;
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        container.add(section, gbc);
    }

    private GridBagConstraints createStandardGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    private void addFormField(JPanel panel, String labelText, Component component, 
                            GridBagConstraints gbc, int gridy) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(textColor);

        gbc.gridy = gridy;
        gbc.gridx = 0;
        gbc.weightx = 0.3;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(component, gbc);
    }

    private void setupListeners() {
        // Flight selection listener
        cbChuyenBay.addActionListener(e -> {
            ChuyenBay selectedFlight = (ChuyenBay) cbChuyenBay.getSelectedItem();
            if (selectedFlight != null) {
                updateFlightDetails(selectedFlight);
            }
        });

        // Quantity change listener
        txtSoLuong.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateTotalPrice(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateTotalPrice(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateTotalPrice(); }
        });

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    loadBookingData(selectedRow);
                }
            }
        });
    }

    private void updateFlightDetails(ChuyenBay flight) {
        if (flight != null) {
            // Sử dụng ChangBay cho cả điểm đi và điểm đến
            txtDiemDi.setText(flight.getChangBay());
            txtDiemDen.setText(flight.getChangBay());
            updateTotalPrice();
        }
    }

    private void updateTotalPrice() {
        try {
            ChuyenBay selectedFlight = (ChuyenBay) cbChuyenBay.getSelectedItem();
            int quantity = Integer.parseInt(txtSoLuong.getText().trim());
            
            if (selectedFlight != null && quantity > 0) {
                double basePrice = selectedFlight.getGiaVe();
                double totalPrice = calculateTotalPrice(basePrice, quantity);
                txtTongGia.setText(String.format("%,.0f VND", totalPrice));
            }
        } catch (NumberFormatException ex) {
            txtTongGia.setText("");
        }
    }

    private double calculateTotalPrice(double basePrice, int quantity) {
        double total = basePrice * quantity;
        
        // Apply discounts based on ticket class and special conditions
        String selectedClass = cbHangVe.getSelectedItem().toString();
        if ("Thương gia".equals(selectedClass)) {
            total *= 1.5; // 50% premium for business class
        } else if ("Hạng nhất".equals(selectedClass)) {
            total *= 2.0; // 100% premium for first class
        }
        
        // Apply any additional discounts
        if (!txtMaGiamGia.getText().trim().isEmpty()) {
            total *= 0.9; // 10% discount for promo code
        }
        
        return total;
    }

    private void handleAddBooking() {
        if (!validateInput()) {
            return;
        }

        try {
            DatVe booking = createBookingFromFields();
            controller.addDatVe(booking);
            refreshTable();
            clearFields();
            showMessage("Đặt vé thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showMessage("Lỗi khi đặt vé: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEditBooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showMessage("Vui lòng chọn vé cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateInput()) {
            return;
        }

        try {
            DatVe booking = createBookingFromFields();
            controller.updateDatVe(booking);
            refreshTable();
            clearFields();
            showMessage("Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showMessage("Lỗi khi cập nhật: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteBooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            showMessage("Vui lòng chọn vé cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa vé này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String maDatVe = table.getValueAt(selectedRow, 0).toString();
                controller.deleteDatVe(maDatVe);
                refreshTable();
                clearFields();
                showMessage("Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                showMessage("Lỗi khi xóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private boolean validateInput() {
        if (txtMaDatVe.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập mã đặt vé!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (cbChuyenBay.getSelectedItem() == null) {
            showMessage("Vui lòng chọn chuyến bay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtHoTen.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập họ tên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                showMessage("Số lượng vé phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Số lượng vé không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private DatVe createBookingFromFields() {
        DatVe booking = new DatVe();
        booking.setMaDatVe(txtMaDatVe.getText().trim());
        
        // Tạo mã khách hàng từ CMND 
        String cmnd = txtCMND.getText().trim();
        String maKhachHang = "KH" + cmnd.substring(Math.max(0, cmnd.length() - 5));
        booking.setMaKhachHang(maKhachHang);
        
        booking.setChuyenBay((ChuyenBay) cbChuyenBay.getSelectedItem());
        booking.setHoTen(txtHoTen.getText().trim());
        booking.setCMND(txtCMND.getText().trim());
        booking.setNgaySinh(new java.sql.Date(dateNgaySinh.getDate().getTime()));
        booking.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
        booking.setSoDienThoai(txtSoDienThoai.getText().trim());
        booking.setEmail(txtEmail.getText().trim());
        booking.setDiaChi(txtDiaChi.getText().trim());
        booking.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
        booking.setTongGia(Double.parseDouble(txtTongGia.getText().replaceAll("[^\\d.]", "")));
        booking.setTrangThai(cbTrangThai.getSelectedItem().toString());
        booking.setPhuongThucThanhToan(cbPhuongThucThanhToan.getSelectedItem().toString());
        booking.setMaGiamGia(txtMaGiamGia.getText().trim());
        booking.setXacNhanThanhToan(chkXacNhanThanhToan.isSelected());
        booking.setNguoiLienHe(txtNguoiLienHe.getText().trim());
        booking.setSDTNguoiLienHe(txtSDTNguoiLienHe.getText().trim());
        // Thêm các yêu cầu đặc biệt
        booking.setSuatAnDacBiet(chkSuatAnDacBiet.isSelected());
        booking.setHoTroYTe(chkHoTroYTe.isSelected());
        booking.setChoNgoiUuTien(chkChoNgoiUuTien.isSelected());
        booking.setHanhLyDacBiet(chkHanhLyDacBiet.isSelected());
        
        return booking;
    }

    private void loadBookingData(int selectedRow) {
        txtMaDatVe.setText(table.getValueAt(selectedRow, 0).toString());
        // Load other fields based on the selected booking
        // This will need to be implemented based on your data model
    }

    public void clearFields() {
        txtMaDatVe.setText("");
        txtHoTen.setText("");
        txtCMND.setText("");
        dateNgaySinh.setDate(null);
        cbGioiTinh.setSelectedIndex(0);
        txtQuocTich.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtSoLuong.setText("");
        txtTongGia.setText("");
        cbHangVe.setSelectedIndex(0);
        txtMaGiamGia.setText("");
        chkXacNhanThanhToan.setSelected(false);
        txtNguoiLienHe.setText("");
        txtSDTNguoiLienHe.setText("");
        chkSuatAnDacBiet.setSelected(false);
        chkHoTroYTe.setSelected(false);
        chkChoNgoiUuTien.setSelected(false);
        chkHanhLyDacBiet.setSelected(false);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    private void loadInitialData() {
        try {
            // Load flights
            List<ChuyenBay> flights = controller.getAllFlights();
            for (ChuyenBay flight : flights) {
                cbChuyenBay.addItem(flight);
            }

            // Refresh table
            refreshTable();
        } catch (Exception e) {
            showMessage("Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshTable() {
        try {
            List<DatVe> bookings = controller.getAllBookings();
            tableModel.setRowCount(0);
            for (DatVe booking : bookings) {
                tableModel.addRow(new Object[]{
                    booking.getMaDatVe(),
                    booking.getChuyenBay().getMaChuyenBay(),
                    booking.getHoTen(),
                    booking.getNgayDat(),
                    booking.getSoLuong(),
                    String.format("%,.0f VND", booking.getTongGia()),
                    booking.getTrangThai()
                });
            }
        } catch (Exception e) {
            showMessage("Lỗi khi tải danh sách đặt vé: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
 // Các phương thức getter
    public String getMaDatVeText() {
        return txtMaDatVe.getText().trim();
    }

    public ChuyenBay getSelectedChuyenBay() {
        return (ChuyenBay) cbChuyenBay.getSelectedItem();
    }

    public int getSoLuongValue() {
        return Integer.parseInt(txtSoLuong.getText().trim());
    }

    public String getTrangThaiText() {
        return cbTrangThai.getSelectedItem().toString();
    }

    public String getPhuongThucThanhToanText() {
        return cbPhuongThucThanhToan.getSelectedItem().toString();
    }

    public String getSelectedDiscountType() {
        return txtMaGiamGia.getText().trim();
    }

    public boolean isImmediatePayment() {
        return chkXacNhanThanhToan.isSelected();
    }

    public String getHoTenText() {
        return txtHoTen.getText().trim();
    }

    public String getCMNDText() {
        return txtCMND.getText().trim();
    }

    public Date getNgaySinhDate() {
        return new java.sql.Date(dateNgaySinh.getDate().getTime());
    }

    public String getGioiTinhText() {
        return cbGioiTinh.getSelectedItem().toString();
    }

    public String getQuocTichText() {
        return txtQuocTich.getText().trim();
    }

    public String getSoDienThoaiText() {
        return txtSoDienThoai.getText().trim();
    }

    public String getEmailText() {
        return txtEmail.getText().trim();
    }

    public String getDiaChiText() {
        return txtDiaChi.getText().trim();
    }

    public String getDiemDiText() {
        return txtDiemDi.getText().trim();
    }

    public String getDiemDenText() {
        return txtDiemDen.getText().trim();
    }

    public Date getNgayBayDate() {
        return new java.sql.Date(dateNgayBay.getDate().getTime());
    }

    public String getHangVeText() {
        return cbHangVe.getSelectedItem().toString();
    }

    public boolean isSuatAnDacBiet() {
        return chkSuatAnDacBiet.isSelected();
    }

    public boolean isHoTroYTe() {
        return chkHoTroYTe.isSelected();
    }

    public boolean isChoNgoiUuTien() {
        return chkChoNgoiUuTien.isSelected();
    }

    public boolean isHanhLyDacBiet() {
        return chkHanhLyDacBiet.isSelected();
    }

    public String getNguoiLienHeText() {
        return txtNguoiLienHe.getText().trim();
    }

    public String getSDTNguoiLienHeText() {
        return txtSDTNguoiLienHe.getText().trim();
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public boolean isValidInput() {
        return validateInput();
    }

    public void updateFlightsList(List<ChuyenBay> flights) {
        cbChuyenBay.removeAllItems();
        for (ChuyenBay flight : flights) {
            cbChuyenBay.addItem(flight);
        }
    }

}