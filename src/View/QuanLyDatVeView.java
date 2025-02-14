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
import Service.DatVeService;

@SuppressWarnings("unused")
public class QuanLyDatVeView extends JPanel {
    private DatVeController controller;
    private DatVeService service;
    
    // UI Components
    private DefaultTableModel tableModel;
    private JTable table;
    
    // Search Panel Components
    private JTextField txtSearch;
    private JDateChooser dateSearch;
    private JComboBox<String> cbFilterStatus;
    
    // Flight Selection
    private JComboBox<ChuyenBay> cbChuyenBay;
    private JLabel lblDiemDi;
    private JLabel lblDiemDen;
    private JLabel lblNgayBay;
    private JLabel lblGiaVe;
    private JLabel lblTinhTrang;
    private JLabel lblChangBay;
    private JLabel lblNhaGa;
    
    // Booking Information
    private JTextField txtMaDatVe;
    private JTextField txtSoLuong;
    private JTextField txtTongGia;
    
    // Customer Information
    private JTextField txtHoTen;
    private JTextField txtCMND;
    private JDateChooser dateNgaySinh;
    private JComboBox<String> cbGioiTinh;
    private JTextField txtQuocTich;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextArea txtDiaChi;
    
    // Ticket Type and Payment
    private JComboBox<String> cbHangVe;
    private JComboBox<String> cbPhuongThucThanhToan;
    private JTextField txtMaGiamGia;
    private JCheckBox chkXacNhanThanhToan;
    
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
    private Color backgroundColor = new Color(236, 240, 241);
    private Color textColor = new Color(44, 62, 80);

    public QuanLyDatVeView(TrangChuPanel trangChuPanel) {
        service = new DatVeService();
        controller = new DatVeController(this);
        initializeComponents();
        setupUI();
        setupListeners();
        loadInitialData();
            }
        
            private void loadInitialData() {
                try {
                    // Tải danh sách chuyến bay
                    List<ChuyenBay> flights = service.getAllChuyenBays(); 
                    updateFlightsList(flights);
            
                    // Tải danh sách đặt vé
                    List<DatVe> bookings = service.getAllDatVe();
                    updateTable(bookings);
                } catch (Exception e) {
                    showMessage("Lỗi tải dữ liệu ban đầu: " + e.getMessage(), 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        
            private void initializeComponents() {
        // Initialize Search Components
        txtSearch = createStyledTextField("Tìm kiếm...");
        dateSearch = new JDateChooser();
        cbFilterStatus = new JComboBox<>(new String[]{"Tất cả", "Sắp khởi hành", "Đã khởi hành", "Đã hủy"});
        
        // Initialize Flight Information Components
        cbChuyenBay = new JComboBox<>();
        lblDiemDi = new JLabel();
        lblDiemDen = new JLabel();
        lblNgayBay = new JLabel();
        lblGiaVe = new JLabel();
        lblTinhTrang = new JLabel();
        lblChangBay = new JLabel();
        lblNhaGa = new JLabel();
        
        // Initialize Booking Components
        txtMaDatVe = createStyledTextField("");
        txtMaDatVe.setEditable(false);
        txtSoLuong = createStyledTextField("");
                txtTongGia = createStyledTextField("");
                txtTongGia.setEditable(false);
                
                // Initialize Customer Information Components
                txtHoTen = createStyledTextField("");
                txtCMND = createStyledTextField("");
                dateNgaySinh = new JDateChooser();
                cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
                txtQuocTich = createStyledTextField("");
                txtSoDienThoai = createStyledTextField("");
                txtEmail = createStyledTextField("");
                txtDiaChi = new JTextArea(3, 20);
                setupTextArea(txtDiaChi);
                
                // Initialize Ticket and Payment Components
                cbHangVe = new JComboBox<>(new String[]{"Phổ thông", "Thương gia", "Hạng nhất"});
                cbPhuongThucThanhToan = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản", "Thẻ tín dụng"});
                txtMaGiamGia = createStyledTextField("");
                chkXacNhanThanhToan = new JCheckBox("Xác nhận thanh toán");
                
                // Initialize Special Request Components
                chkSuatAnDacBiet = new JCheckBox("Suất ăn đặc biệt");
                chkHoTroYTe = new JCheckBox("Hỗ trợ y tế");
                chkChoNgoiUuTien = new JCheckBox("Chỗ ngồi ưu tiên");
                chkHanhLyDacBiet = new JCheckBox("Hành lý đặc biệt");
                
                // Initialize Emergency Contact Components
                txtNguoiLienHe = createStyledTextField("");
                txtSDTNguoiLienHe = createStyledTextField("");
                
                // Initialize Table
                initializeTable();
                            }
                        
                            private void initializeTable() {
                                // Định nghĩa các cột cho bảng
                                String[] columnNames = {
                                    "Mã Đặt Vé", 
                                    "Mã Chuyến Bay", 
                                    "Họ Tên", 
                                    "Ngày Đặt", 
                                    "Số Lượng", 
                                    "Tổng Giá", 
                                    "Trạng Thái"
                                };
                                
                                // Tạo DefaultTableModel với các cột đã định nghĩa
                                tableModel = new DefaultTableModel(columnNames, 0) {
                                    // Không cho phép chỉnh sửa trực tiếp trên bảng
                                    @Override
                                    public boolean isCellEditable(int row, int column) {
                                        return false;
                                    }
                                };
                                
                                // Tạo JTable với tableModel
                                table = new JTable(tableModel);
                                
                                // Tùy chỉnh giao diện bảng
                                table.setRowHeight(35);
                                table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                                
                                // Cài đặt renderer để định dạng một số cột
                                table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
                                    @Override
                                    public void setValue(Object value) {
                                        if (value instanceof Number) {
                                            setText(String.format("%,.0f VND", ((Number)value).doubleValue()));
                                        } else {
                                            super.setValue(value);
                                        }
                                    }
                                });
                                
                                // Thêm sự kiện chọn dòng
                                table.getSelectionModel().addListSelectionListener(e -> {
                                    if (!e.getValueIsAdjusting()) {
                                        int selectedRow = table.getSelectedRow();
                                        if (selectedRow != -1) {
                                            // Lấy dữ liệu của dòng được chọn
                                            String maDatVe = table.getValueAt(selectedRow, 0).toString();
                                            // Có thể thêm logic để tải chi tiết đặt vé dựa trên mã đặt vé
                                            // Ví dụ: loadBookingDetails(maDatVe);
                                        }
                                    }
                                });
                            }
                
                            private JTextField createStyledTextField(String placeholder) {
                JTextField textField = new JTextField(20);
                textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                textField.setPreferredSize(new Dimension(200, 35));
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(224, 224, 224)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
                
                // Thêm placeholder text
                if (!placeholder.isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                    textField.addFocusListener(new FocusAdapter() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            if (textField.getText().equals(placeholder)) {
                                textField.setText("");
                                textField.setForeground(Color.BLACK);
                            }
                        }
                        
                        @Override
                        public void focusLost(FocusEvent e) {
                            if (textField.getText().isEmpty()) {
                                textField.setForeground(Color.GRAY);
                                textField.setText(placeholder);
                            }
                        }
                    });
                }
                
                return textField;
            }
            
            private void setupTextArea(JTextArea textArea) {
                textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(224, 224, 224)),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
            }
        
            private void setupUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(backgroundColor);

        // Add Header
        add(createHeaderPanel(), BorderLayout.NORTH);
        
        // Create Main Content Panel
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBackground(backgroundColor);
        
        // Add Search Panel
        mainContent.add(createSearchPanel(), BorderLayout.NORTH);
        
        // Create Center Panel with Form and Table
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            createFormPanel(),
            createTablePanel()
                    );
                    splitPane.setResizeWeight(0.6);
                    mainContent.add(splitPane, BorderLayout.CENTER);
                    
                    // Add Button Panel
                    mainContent.add(createButtonPanel(), BorderLayout.SOUTH);
                            
                            add(mainContent, BorderLayout.CENTER);
                        }
                    
                        private Component createTablePanel() {
                            // Khởi tạo table nếu chưa được khởi tạo
                            if (table == null) {
                                initializeTable();
                            }
                        
                            // Tạo JScrollPane để chứa bảng
                            JScrollPane scrollPane = new JScrollPane(table);
                            
                            // Tùy chỉnh giao diện ScrollPane
                            scrollPane.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                                BorderFactory.createLineBorder(new Color(224, 224, 224))
                            ));
                            scrollPane.setBackground(Color.WHITE);
                            scrollPane.getViewport().setBackground(Color.WHITE);
                        
                            // Tạo panel chứa bảng với border layout
                            JPanel tablePanel = new JPanel(new BorderLayout());
                            tablePanel.setBackground(Color.WHITE);
                            
                            // Thêm tiêu đề cho bảng
                            JLabel tableTitle = new JLabel("Danh Sách Đặt Vé", SwingConstants.LEFT);
                            tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
                            tableTitle.setForeground(primaryColor);
                            tableTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                            
                            // Thêm các thành phần vào panel
                            tablePanel.add(tableTitle, BorderLayout.NORTH);
                            tablePanel.add(scrollPane, BorderLayout.CENTER);
                        
                            return tablePanel;
                        }
            
                        private JPanel createButtonPanel() {
                            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
                            panel.setBackground(backgroundColor);
                        
                            // Tạo các nút với style thống nhất
                            JButton btnAdd = createStyledButton("Thêm", new Color(46, 204, 113));
                            JButton btnEdit = createStyledButton("Sửa", new Color(52, 152, 219));
                            JButton btnDelete = createStyledButton("Xóa", new Color(231, 76, 60));
                            JButton btnClear = createStyledButton("Làm mới", new Color(241, 196, 15));
                            JButton btnInvoice = createStyledButton("Xuất Hóa Đơn", new Color(142, 68, 173));
                        
                            // Thêm xử lý sự kiện cho các nút
                            btnAdd.addActionListener(e -> handleBooking());
                            btnEdit.addActionListener(e -> {
                                if (validateInput()) {
                                    try {
                                        DatVe booking = createBookingFromFields();
                                        service.updateDatVe(booking);
                                        refreshTable();
                                        clearFields();
                                        showMessage("Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                    } catch (Exception ex) {
                                        showMessage("Lỗi khi cập nhật: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            });
                            btnDelete.addActionListener(e -> {
                                try {
                                    String maDatVe = txtMaDatVe.getText().trim();
                                    if (maDatVe.isEmpty()) {
                                        showMessage("Vui lòng chọn vé cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                                        return;
                                    }
                                    int confirm = JOptionPane.showConfirmDialog(this,
                                        "Bạn có chắc muốn xóa vé này?",
                                        "Xác nhận xóa",
                                        JOptionPane.YES_NO_OPTION);
                                    if (confirm == JOptionPane.YES_OPTION) {
                                        service.deleteDatVe(maDatVe);
                                        refreshTable();
                                        clearFields();
                                        showMessage("Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                                    }
                                } catch (Exception ex) {
                                    showMessage("Lỗi khi xóa: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                                }
                            });
                            btnClear.addActionListener(e -> clearFields());
                            btnInvoice.addActionListener(e -> handleInvoice());
                        
                            // Thêm các nút vào panel
                            panel.add(btnAdd);
                            panel.add(btnEdit);
                            panel.add(btnDelete);
                            panel.add(btnClear);
                            panel.add(btnInvoice);
                        
                            return panel;
                        }
                        
                        private void handleInvoice() {
                            // Lấy dòng được chọn trong bảng
                            int selectedRow = table.getSelectedRow();
                            if (selectedRow == -1) {
                                showMessage("Vui lòng chọn một đặt vé để xuất hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        
                            try {
                                // Lấy mã đặt vé từ dòng được chọn
                                String maDatVe = table.getValueAt(selectedRow, 0).toString();
                                // Lấy thông tin đặt vé từ database
                                DatVe datVe = service.getDatVeByMa(maDatVe);
                                
                                if (datVe != null) {
                                    // Hiển thị form hóa đơn
                                    HoaDonVeMayBayView.showInvoice(SwingUtilities.getWindowAncestor(this), datVe);
                                } else {
                                    showMessage("Không tìm thấy thông tin đặt vé!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (Exception ex) {
                                showMessage("Lỗi khi xuất hóa đơn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                            }
                        }

                        public static void showInvoice(Window parent, DatVe datVe) {
                            // Kiểm tra và chuyển đổi parent thành JFrame
                            JFrame parentFrame;
                            if (parent instanceof JFrame) {
                                parentFrame = (JFrame) parent;
                            } else {
                                parentFrame = (JFrame) SwingUtilities.getWindowAncestor(parent);
                            }
                            
                            HoaDonVeMayBayView dialog = new HoaDonVeMayBayView(parentFrame, datVe);
                            dialog.setVisible(true);
                        }
                                        
        public void clearFields() {
                // Xóa thông tin đặt vé
                txtMaDatVe.setText("");
                cbChuyenBay.setSelectedIndex(-1);
                txtSoLuong.setText("");
                txtTongGia.setText("");
                                                
                // Xóa thông tin khách hàng
                txtHoTen.setText("");
                txtCMND.setText("");
                dateNgaySinh.setDate(null);
                cbGioiTinh.setSelectedIndex(0);
                txtQuocTich.setText("");
                                                
                // Xóa thông tin liên hệ
                txtSoDienThoai.setText("");
                txtEmail.setText("");
                txtDiaChi.setText("");
                                                
                // Xóa thông tin vé và thanh toán  
                cbHangVe.setSelectedIndex(0);
                cbPhuongThucThanhToan.setSelectedIndex(0);
                txtMaGiamGia.setText("");
                chkXacNhanThanhToan.setSelected(false);
                                                
                // Xóa yêu cầu đặc biệt
                chkSuatAnDacBiet.setSelected(false);
                chkHoTroYTe.setSelected(false);
                chkChoNgoiUuTien.setSelected(false);
                chkHanhLyDacBiet.setSelected(false);
                                            
                // Xóa thông tin liên hệ khẩn cấp
                txtNguoiLienHe.setText("");
                txtSDTNguoiLienHe.setText("");
                                            
                // Xóa thông tin chuyến bay
                lblDiemDi.setText("");
                lblDiemDen.setText("");
                lblNgayBay.setText("");
                lblGiaVe.setText("");
                lblTinhTrang.setText("");
                lblChangBay.setText("");
                lblNhaGa.setText("");
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
        
            private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(primaryColor);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JLabel headerLabel = new JLabel("Quản Lý Đặt Vé");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        panel.add(headerLabel, BorderLayout.WEST);
        
        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Tìm kiếm:"));
        panel.add(txtSearch);
        panel.add(new JLabel("Ngày:"));
        panel.add(dateSearch);
        panel.add(new JLabel("Trạng thái:"));
        panel.add(cbFilterStatus);
        
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.addActionListener(e -> performSearch());
        panel.add(btnSearch);
        
        return panel;
    }

    private JScrollPane createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Add Flight Selection Section
        addSection(formPanel, createFlightSelectionPanel(), "Thông tin chuyến bay", gbc, 0);
                
                // Add Customer Information Section
                addSection(formPanel, createCustomerInfoPanel(), "Thông tin khách hàng", gbc, 1);
                
                // Add Ticket and Payment Section
                addSection(formPanel, createTicketPaymentPanel(), "Thông tin vé và thanh toán", gbc, 2);
                                
                                // Add Special Requests Section
                                addSection(formPanel, createSpecialRequestsPanel(), "Yêu cầu đặc biệt", gbc, 3);
                                
                                // Add Emergency Contact Section
                                addSection(formPanel, createEmergencyContactPanel(), "Thông tin liên hệ khẩn cấp", gbc, 4);
                                
                                return new JScrollPane(formPanel);
                            }
                        
                            private JPanel createFlightSelectionPanel() {
                                JPanel panel = new JPanel(new GridBagLayout());
                                panel.setBackground(Color.WHITE);
                                GridBagConstraints gbc = createStandardGBC();
                            
                                addFormField(panel, "Chuyến bay:", cbChuyenBay, gbc, 0);
                                addFormField(panel, "Điểm đi:", lblDiemDi, gbc, 1);
                                addFormField(panel, "Điểm đến:", lblDiemDen, gbc, 2);
                                addFormField(panel, "Ngày bay:", lblNgayBay, gbc, 3);
                                addFormField(panel, "Giá vé:", lblGiaVe, gbc, 4);
                                addFormField(panel, "Trạng thái:", lblTinhTrang, gbc, 5);
                                addFormField(panel, "Sân bay:", lblChangBay, gbc, 6);
                                addFormField(panel, "Nhà ga:", lblNhaGa, gbc, 7);
                            
                                return panel;
                            }
                            
                            private JPanel createCustomerInfoPanel() {
                                JPanel panel = new JPanel(new GridBagLayout());
                                panel.setBackground(Color.WHITE);
                                GridBagConstraints gbc = createStandardGBC();
                            
                                addFormField(panel, "Họ và tên:", txtHoTen, gbc, 0);
                                addFormField(panel, "CMND/CCCD:", txtCMND, gbc, 1);
                                addFormField(panel, "Ngày sinh:", dateNgaySinh, gbc, 2);
                                addFormField(panel, "Giới tính:", cbGioiTinh, gbc, 3);
                                addFormField(panel, "Quốc tịch:", txtQuocTich, gbc, 4);
                                addFormField(panel, "Số điện thoại:", txtSoDienThoai, gbc, 5);
                                addFormField(panel, "Email:", txtEmail, gbc, 6);
                                addFormField(panel, "Địa chỉ:", new JScrollPane(txtDiaChi), gbc, 7);
                            
                                return panel;
                            }
        
                            private void addSection(JPanel container, JPanel section, String title, GridBagConstraints gbc, int gridy) {
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
        
                    private JPanel createTicketPaymentPanel() {
                JPanel panel = new JPanel(new GridBagLayout());
                panel.setBackground(Color.WHITE);
                GridBagConstraints gbc = createStandardGBC();
            
                addFormField(panel, "Hạng vé:", cbHangVe, gbc, 0);
                addFormField(panel, "Số lượng:", txtSoLuong, gbc, 1);
                addFormField(panel, "Tổng giá:", txtTongGia, gbc, 2);
                addFormField(panel, "Phương thức thanh toán:", cbPhuongThucThanhToan, gbc, 3);
                addFormField(panel, "Mã giảm giá:", txtMaGiamGia, gbc, 4);
                addFormField(panel, "", chkXacNhanThanhToan, gbc, 5);
            
                return panel;
            }
            
            private JPanel createSpecialRequestsPanel() {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
                panel.setBackground(Color.WHITE);
            
                panel.add(chkSuatAnDacBiet);
                panel.add(chkHoTroYTe);
                panel.add(chkChoNgoiUuTien);
                panel.add(chkHanhLyDacBiet);
            
                // Style cho các checkbox
                Font checkboxFont = new Font("Segoe UI", Font.PLAIN, 14);
                chkSuatAnDacBiet.setFont(checkboxFont);
                chkHoTroYTe.setFont(checkboxFont);
                chkChoNgoiUuTien.setFont(checkboxFont);
                chkHanhLyDacBiet.setFont(checkboxFont);
            
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
            
            private GridBagConstraints createStandardGBC() {
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.insets = new Insets(5, 10, 5, 10);
                gbc.anchor = GridBagConstraints.WEST;
                return gbc;
            }
            
            private void addFormField(JPanel panel, String labelText, Component component, GridBagConstraints gbc, int gridy) {
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
        // Flight Selection Listener
        cbChuyenBay.addActionListener(e -> {
            ChuyenBay selectedFlight = (ChuyenBay) cbChuyenBay.getSelectedItem();
            if (selectedFlight != null) {
                updateFlightDetails(selectedFlight);
            }
        });

        // Quantity Change Listener
        txtSoLuong.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateTotalPrice(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateTotalPrice(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateTotalPrice(); }
        });

        // Ticket Class Change Listener
        cbHangVe.addActionListener(e -> updateTotalPrice());

        // CMND Change Listener
        txtCMND.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { loadCustomerInfo(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { loadCustomerInfo(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { loadCustomerInfo(); }
        });
    }

    private void updateFlightDetails(ChuyenBay selectedFlight) {
        if (selectedFlight != null) {
            lblDiemDi.setText(selectedFlight.getDiemDi());
            lblDiemDen.setText(selectedFlight.getDiemDen());
            lblNgayBay.setText(selectedFlight.getNgayBay());
            lblGiaVe.setText(String.format("%,.0f VND", selectedFlight.getGiaVe()));
            lblTinhTrang.setText(selectedFlight.getTinhTrang());
            lblChangBay.setText(selectedFlight.getChangBay());
            lblNhaGa.setText(selectedFlight.getNhaGa());
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
        
        // Apply class multiplier
        String selectedClass = cbHangVe.getSelectedItem().toString();
        switch (selectedClass) {
            case "Thương gia":
                total *= 1.5;
                break;
            case "Hạng nhất":
                total *= 2.0;
                break;
        }
        
        // Apply discount if available
        if (!txtMaGiamGia.getText().trim().isEmpty()) {
            // Here you would typically validate the discount code
            // For now, we'll apply a simple 10% discount
            total *= 0.9;
        }
        
        return total;
    }

    private void loadCustomerInfo() {
        String cmnd = txtCMND.getText().trim();
        if (cmnd.length() >= 9) {
            // Here you would typically load customer information from the database
            // For now, we'll just clear the fields if CMND is changed
            clearCustomerFields();
                    }
                }
            
                private void clearCustomerFields() {
                    // Xóa thông tin khách hàng
                    txtHoTen.setText("");
                    txtCMND.setText("");
                    dateNgaySinh.setDate(null);
                    cbGioiTinh.setSelectedIndex(0);
                    txtQuocTich.setText("");
                    
                    // Xóa thông tin liên hệ
                    txtSoDienThoai.setText("");
                    txtEmail.setText("");
                    txtDiaChi.setText("");
                }
            
                private void performSearch() {
        try {
            String searchTerm = txtSearch.getText().trim();
            Date searchDate = dateSearch.getDate() != null ? 
                new Date(dateSearch.getDate().getTime()) : null;
            String status = cbFilterStatus.getSelectedItem().toString();
            
            List<DatVe> searchResults = service.searchDatVe(searchTerm);
            updateTable(searchResults);
        } catch (Exception e) {
            showMessage("Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBooking() {
        if (!validateInput()) {
            return;
        }

        try {
            DatVe booking = createBookingFromFields();
            if (txtMaDatVe.getText().trim().isEmpty()) {
                service.addDatVe(booking);
                showMessage("Đặt vé thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                service.updateDatVe(booking);
                showMessage("Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            refreshTable();
            clearFields();
        } catch (Exception e) {
            showMessage("Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        // Kiểm tra chuyến bay
        if (cbChuyenBay.getSelectedItem() == null) {
            showMessage("Vui lòng chọn chuyến bay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra thông tin khách hàng
        if (txtHoTen.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập họ tên khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (txtCMND.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập CMND/CCCD!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (dateNgaySinh.getDate() == null) {
            showMessage("Vui lòng chọn ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra thông tin liên hệ
        if (txtSoDienThoai.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra số lượng vé
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                showMessage("Số lượng vé phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Vui lòng nhập số lượng vé hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private DatVe createBookingFromFields() throws Exception {
        DatVe booking = new DatVe();
        
        // Nếu là đặt vé mới, tạo mã đặt vé mới
        if (txtMaDatVe.getText().trim().isEmpty()) {
            booking.setMaDatVe(service.generateMaDatVe());
        } else {
            booking.setMaDatVe(txtMaDatVe.getText().trim());
        }

        // Thông tin chuyến bay
        booking.setChuyenBay((ChuyenBay) cbChuyenBay.getSelectedItem());
        
        // Thông tin khách hàng
        booking.setHoTen(txtHoTen.getText().trim());
        booking.setCMND(txtCMND.getText().trim());
        booking.setNgaySinh(new Date(dateNgaySinh.getDate().getTime()));
        booking.setGioiTinh(cbGioiTinh.getSelectedItem().toString());
        booking.setQuocTich(txtQuocTich.getText().trim());
        
        // Thông tin liên hệ
        booking.setSoDienThoai(txtSoDienThoai.getText().trim());
        booking.setEmail(txtEmail.getText().trim());
        booking.setDiaChi(txtDiaChi.getText().trim());
        
        // Thông tin vé
        booking.setHangVe(cbHangVe.getSelectedItem().toString());
        booking.setSoLuong(Integer.parseInt(txtSoLuong.getText().trim()));
        booking.setTongGia(Double.parseDouble(txtTongGia.getText().trim()
            .replace("VND", "").replace(",", "").trim()));
        
        // Thông tin thanh toán
        booking.setPhuongThucThanhToan(cbPhuongThucThanhToan.getSelectedItem().toString());
        booking.setMaGiamGia(txtMaGiamGia.getText().trim());
        booking.setXacNhanThanhToan(chkXacNhanThanhToan.isSelected());
        
        // Thông tin liên hệ khẩn cấp
        booking.setNguoiLienHe(txtNguoiLienHe.getText().trim());
        booking.setSDTNguoiLienHe(txtSDTNguoiLienHe.getText().trim());
        
        // Yêu cầu đặc biệt
        booking.setSuatAnDacBiet(chkSuatAnDacBiet.isSelected());
        booking.setHoTroYTe(chkHoTroYTe.isSelected());
        booking.setChoNgoiUuTien(chkChoNgoiUuTien.isSelected());
        booking.setHanhLyDacBiet(chkHanhLyDacBiet.isSelected());

        return booking;
    }

    private void refreshTable() {
        try {
            List<DatVe> bookings = service.getAllDatVe();
            updateTable(bookings);
        } catch (Exception e) {
            showMessage("Lỗi khi tải danh sách đặt vé: " + e.getMessage(), 
                       "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<DatVe> bookings) {
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
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Các phương thức getter cho controller
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public void loadBookingData(DatVe booking) {
        if (booking != null) {
            txtMaDatVe.setText(booking.getMaDatVe());
            cbChuyenBay.setSelectedItem(booking.getChuyenBay());
            txtHoTen.setText(booking.getHoTen());
            txtCMND.setText(booking.getCMND());
            dateNgaySinh.setDate(booking.getNgaySinh());
            cbGioiTinh.setSelectedItem(booking.getGioiTinh());
            txtQuocTich.setText(booking.getQuocTich());
            txtSoDienThoai.setText(booking.getSoDienThoai());
            txtEmail.setText(booking.getEmail());
            txtDiaChi.setText(booking.getDiaChi());
            txtSoLuong.setText(String.valueOf(booking.getSoLuong()));
            txtTongGia.setText(String.format("%,.0f VND", booking.getTongGia()));
            cbHangVe.setSelectedItem(booking.getHangVe());
            cbPhuongThucThanhToan.setSelectedItem(booking.getPhuongThucThanhToan());
            txtMaGiamGia.setText(booking.getMaGiamGia());
            chkXacNhanThanhToan.setSelected(booking.isXacNhanThanhToan());
            txtNguoiLienHe.setText(booking.getNguoiLienHe());
            txtSDTNguoiLienHe.setText(booking.getSDTNguoiLienHe());
            chkSuatAnDacBiet.setSelected(booking.isSuatAnDacBiet());
            chkHoTroYTe.setSelected(booking.isHoTroYTe());
            chkChoNgoiUuTien.setSelected(booking.isChoNgoiUuTien());
            chkHanhLyDacBiet.setSelected(booking.isHanhLyDacBiet());
        }
    }

    public void updateFlightsList(List<ChuyenBay> flights) {
        cbChuyenBay.removeAllItems();
        for (ChuyenBay flight : flights) {
            cbChuyenBay.addItem(flight);
        }
    }

    public String getMaDatVeText() {
        return txtMaDatVe.getText().trim();
    }
    
    public boolean isImmediatePayment() {
        return chkXacNhanThanhToan.isSelected();
    }

        // Thông tin chuyến bay
    public ChuyenBay getSelectedChuyenBay() {
        return (ChuyenBay) cbChuyenBay.getSelectedItem();
    }

    public int getSoLuongValue() {
        try {
            return Integer.parseInt(txtSoLuong.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getHangVeText() {
        return (String) cbHangVe.getSelectedItem();
    }

    public String getPhuongThucThanhToanText() {
        return (String) cbPhuongThucThanhToan.getSelectedItem();
    }

    public String getSelectedDiscountType() {
        return txtMaGiamGia.getText().trim();
    }

    // Thông tin khách hàng
    public String getHoTenText() {
        return txtHoTen.getText().trim();
    }

    public String getCMNDText() {
        return txtCMND.getText().trim();
    }

    public Date getNgaySinhDate() {
        return dateNgaySinh.getDate() != null ? 
            new Date(dateNgaySinh.getDate().getTime()) : null;
    }

    public String getGioiTinhText() {
        return (String) cbGioiTinh.getSelectedItem();
    }

    public String getQuocTichText() {
        return txtQuocTich.getText().trim();
    }

    // Thông tin liên hệ
    public String getSoDienThoaiText() {
        return txtSoDienThoai.getText().trim();
    }

    public String getEmailText() {
        return txtEmail.getText().trim();
    }

    public String getDiaChiText() {
        return txtDiaChi.getText().trim();
    }

    // Yêu cầu đặc biệt
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

    // Thông tin liên hệ khẩn cấp
    public String getNguoiLienHeText() {
        return txtNguoiLienHe.getText().trim();
    }

    public String getSDTNguoiLienHeText() {
        return txtSDTNguoiLienHe.getText().trim();
    }

	public boolean isValidInput() {
        // Kiểm tra chuyến bay
        if (cbChuyenBay.getSelectedItem() == null) {
            showMessage("Vui lòng chọn chuyến bay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        // Kiểm tra thông tin khách hàng
        if (txtHoTen.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập họ tên khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        if (txtCMND.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập CMND/CCCD!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        if (dateNgaySinh.getDate() == null) {
            showMessage("Vui lòng chọn ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        // Kiểm tra thông tin liên hệ
        if (txtSoDienThoai.getText().trim().isEmpty()) {
            showMessage("Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        // Kiểm tra số lượng vé
        try {
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                showMessage("Số lượng vé phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            showMessage("Vui lòng nhập số lượng vé hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    
        return true;
    }
}