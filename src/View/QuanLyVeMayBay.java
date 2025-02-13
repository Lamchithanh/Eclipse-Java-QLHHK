package View;

import Controller.VeMayBayController;
import Model.VeMayBay;
import Service.VeMayBayService;
import Model.UserAccount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.ParseException;
import java.util.List;

public class QuanLyVeMayBay extends JPanel {
    private VeMayBayController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField maVeField;
    private JTextField maChuyenBayField;
    private JTextField maKhachHangField;
    private JTextField tenKhachHangField;
    private JTextField cmndField;
    private JTextField ngayDatField;
    private JTextField searchField;
    private JTextField giaVeField;
    private JTextField trangThaiField;
    private JTextField soDienThoaiField;
    private JTextField diaChiField;
    private JTextField emailField;
    private JTextField ngaySinhField;
    private JTextField quocTichField;
    private VeMayBayService service;
    private JPanel actionPanel;
    
    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(236, 240, 241);
    private TrangChuPanel trangChuPanel;

    public QuanLyVeMayBay(TrangChuPanel trangChuPanel) {
        this.trangChuPanel = trangChuPanel;
        this.service = new VeMayBayService(); // Khởi tạo service
        initializePanel();
    }

    public QuanLyVeMayBay(UserAccount user) {
        this.service = new VeMayBayService(); // Khởi tạo service
        initializePanel();
    }

    private void initializePanel() {
        controller = new VeMayBayController(this);
        setLayout(new BorderLayout(0, 0));
        setBackground(backgroundColor);

        // Header Panel
        add(createModernHeader(), BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBackground(backgroundColor);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Tạo action panel trước khi sử dụng
        JPanel actionPanel = createModernActionPanel();

        // Thêm nút tìm kiếm nâng cao vào action panel
        JButton advancedSearchButton = createModernButton("Tìm Nâng Cao", new Color(241, 196, 15));
        advancedSearchButton.addActionListener(e -> showAdvancedSearchDialog());
        actionPanel.add(advancedSearchButton);
        // Create Input Panel
        JPanel inputPanel = createModernInputPanel();
        
        // Create Table Panel
        JPanel tablePanel = createModernTablePanel();

        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, tablePanel);
        splitPane.setDividerLocation(280);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);
        mainContent.add(splitPane, BorderLayout.CENTER);

        // Action Panel
        mainContent.add(createModernActionPanel(), BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
        
        // Initialize data
        controller.updateTicketList();
    }

    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel headerLabel = new JLabel("Quản Lý Vé Máy Bay");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setIconTextGap(15);

        headerPanel.add(headerLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JPanel createModernInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10),
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create fields
        maVeField = createModernTextField("Nhập mã vé...");
        maChuyenBayField = createModernTextField("Nhập mã chuyến bay...");
        maKhachHangField = createModernTextField("Nhập mã khách hàng...");
        tenKhachHangField = createModernTextField("Nhập tên khách hàng...");
        giaVeField = createModernTextField("Nhập giá vé...");
        trangThaiField = createModernTextField("Trạng thái...");
        cmndField = createModernTextField("Nhập CMND...");
        ngayDatField = createModernTextField("YYYY-MM-DD");
        
        // Thêm các trường mới cho thông tin khách hàng
        soDienThoaiField = createModernTextField("Nhập số điện thoại...");
        diaChiField = createModernTextField("Nhập địa chỉ...");
        emailField = createModernTextField("Nhập email...");
        ngaySinhField = createModernTextField("YYYY-MM-DD");
        quocTichField = createModernTextField("Nhập quốc tịch...");
        
        // Column 1
        gbc.gridx = 0; // First column
        addModernInputField(inputPanel, "Mã Vé", maVeField, gbc, 0);
        addModernInputField(inputPanel, "Mã Chuyến Bay", maChuyenBayField, gbc, 1);
        addModernInputField(inputPanel, "Mã Khách Hàng", maKhachHangField, gbc, 2);
        addModernInputField(inputPanel, "Tên Khách Hàng", tenKhachHangField, gbc, 3);
        addModernInputField(inputPanel, "CMND", cmndField, gbc, 4);
        addModernInputField(inputPanel, "Số Điện Thoại", soDienThoaiField, gbc, 5);
        addModernInputField(inputPanel, "Email", emailField, gbc, 6);
    
        // Column 2
        gbc.gridx = 2; // Second column
        addModernInputField(inputPanel, "Ngày Đặt", ngayDatField, gbc, 0);
        addModernInputField(inputPanel, "Giá Vé", giaVeField, gbc, 1);
        addModernInputField(inputPanel, "Trạng Thái", trangThaiField, gbc, 2);
        addModernInputField(inputPanel, "Địa Chỉ", diaChiField, gbc, 3);
        addModernInputField(inputPanel, "Ngày Sinh", ngaySinhField, gbc, 4);
        addModernInputField(inputPanel, "Quốc Tịch", quocTichField, gbc, 5);

        return inputPanel;
    }

    private void addModernInputField(JPanel panel, String labelText, JTextField field, 
                                   GridBagConstraints gbc, int gridy) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));

        gbc.gridy = gridy;
        gbc.weightx = 0.1;
        panel.add(label, gbc);

        gbc.gridx++;
        gbc.weightx = 0.4;
        panel.add(field, gbc);
        gbc.gridx--; // Reset for next row in same column
    }

    private JPanel createModernTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Mã Vé", "Mã Chuyến Bay", "Mã Khách Hàng", "Tên Khách Hàng", "CMND", "Ngày Đặt"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return comp;
            }
        };

        // Style table
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        // Selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    updateFieldsFromSelection(selectedRow);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createModernActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(backgroundColor);

        // Search Panel with realtime search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(backgroundColor);
        searchField = createModernTextField("Tìm kiếm...");
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText();
                if (!searchQuery.equals("Tìm kiếm...")) {
                    controller.searchTickets(searchQuery.toLowerCase());
                }
            }
        });
        searchPanel.add(searchField);

        // Action Buttons
        JButton addButton = createModernButton("Thêm", new Color(46, 204, 113));
        JButton updateButton = createModernButton("Cập Nhật", new Color(52, 152, 219));
        JButton deleteButton = createModernButton("Xóa", new Color(231, 76, 60));

        addButton.addActionListener(e -> handleAddTicket());
        updateButton.addActionListener(e -> handleUpdateTicket());
        deleteButton.addActionListener(e -> handleDeleteTicket());

        actionPanel.add(searchPanel);
        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        return actionPanel;
    }

    // Helper methods for creating UI components
    private JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        field.putClientProperty("placeholder", placeholder);
        field.setForeground(Color.GRAY);
        field.setText(placeholder);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(field.getClientProperty("placeholder"))) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText((String)field.getClientProperty("placeholder"));
                }
            }
        });

        return field;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 35));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }
    // Event handlers
    private void handleAddTicket() {
        try {
            VeMayBay ticket = controller.createVeMayBayFromInput(
                getTextOrPlaceholder(maVeField),
                getTextOrPlaceholder(maChuyenBayField),
                getTextOrPlaceholder(maKhachHangField),
                getTextOrPlaceholder(tenKhachHangField),
                getTextOrPlaceholder(cmndField),
                getTextOrPlaceholder(ngayDatField),
                getTextOrPlaceholder(giaVeField), 
                getTextOrPlaceholder(trangThaiField),
                getTextOrPlaceholder(soDienThoaiField),
                getTextOrPlaceholder(diaChiField),
                getTextOrPlaceholder(emailField),
                getTextOrPlaceholder(ngaySinhField),
                getTextOrPlaceholder(quocTichField)
            );
            controller.addTicket(ticket);
        } catch (ParseException ex) {
            showNotification("Lỗi định dạng ngày!", NotificationType.ERROR);
        } catch (NumberFormatException ex) {
            showNotification("Giá vé không hợp lệ!", NotificationType.ERROR);
        }
    }
    
    private void handleUpdateTicket() {
        try {
            VeMayBay ticket = controller.createVeMayBayFromInput(
                getTextOrPlaceholder(maVeField),
                getTextOrPlaceholder(maChuyenBayField),
                getTextOrPlaceholder(maKhachHangField),
                getTextOrPlaceholder(tenKhachHangField),
                getTextOrPlaceholder(cmndField),
                getTextOrPlaceholder(ngayDatField),
                getTextOrPlaceholder(giaVeField),
                getTextOrPlaceholder(trangThaiField),
                getTextOrPlaceholder(soDienThoaiField),
                getTextOrPlaceholder(diaChiField),
                getTextOrPlaceholder(emailField),
                getTextOrPlaceholder(ngaySinhField),
                getTextOrPlaceholder(quocTichField)
            );
            controller.updateTicket(ticket);
        } catch (ParseException ex) {
            showNotification("Lỗi định dạng ngày!", NotificationType.ERROR);
        } catch (NumberFormatException ex) {
            showNotification("Giá vé không hợp lệ!", NotificationType.ERROR);
        }
    }

    private double parseGiaVe(String giaVeText) {
        try {
            return Double.parseDouble(giaVeText);
        } catch (NumberFormatException e) {
            showNotification("Giá vé không hợp lệ!", NotificationType.ERROR);
            return 0.0;
        }
    }

    private void handleDeleteTicket() {
        String maVe = getTextOrPlaceholder(maVeField);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa vé này?", 
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteTicket(maVe);
        }
    }

    // Helper methods
    private String getTextOrPlaceholder(JTextField field) {
        String text = field.getText();
        String placeholder = (String) field.getClientProperty("placeholder");
        return text.equals(placeholder) ? "" : text;
    }

    private interface BiFunction<T, U, R> {
        R apply(T t, U u);
    }

    public void updateFieldsFromSelection(int selectedRow) {
        // Helper function to safely get value
        BiFunction<Integer, Integer, String> getValue = (row, col) -> {
            Object value = tableModel.getValueAt(row, col);
            return value != null ? value.toString() : "";
        };
    
        // Update các trường thông tin
        maVeField.setText(getValue.apply(selectedRow, 0));
        maChuyenBayField.setText(getValue.apply(selectedRow, 1));
        maKhachHangField.setText(getValue.apply(selectedRow, 2));
        tenKhachHangField.setText(getValue.apply(selectedRow, 3));
        cmndField.setText(getValue.apply(selectedRow, 4));
        ngayDatField.setText(getValue.apply(selectedRow, 5));
        giaVeField.setText(getValue.apply(selectedRow, 6));
        trangThaiField.setText(getValue.apply(selectedRow, 7));
        
        // Thêm các trường mới
        soDienThoaiField.setText(getValue.apply(selectedRow, 8));
        diaChiField.setText(getValue.apply(selectedRow, 9));
        emailField.setText(getValue.apply(selectedRow, 10));
        
        // Xử lý riêng cho ngày sinh và quốc tịch
        Object ngaySinh = tableModel.getValueAt(selectedRow, 11);
        Object quocTich = tableModel.getValueAt(selectedRow, 12);
        
        ngaySinhField.setText(ngaySinh != null ? ngaySinh.toString() : "YYYY-MM-DD");
        quocTichField.setText(quocTich != null ? quocTich.toString() : "Nhập quốc tịch...");
    
        // Set text color for all fields
        for (JTextField field : new JTextField[]{
            maVeField, maChuyenBayField, maKhachHangField, tenKhachHangField, 
            cmndField, ngayDatField, giaVeField, trangThaiField,
            soDienThoaiField, diaChiField, emailField, ngaySinhField, quocTichField
        }) {
            String text = field.getText();
            if (text.isEmpty() || text.equals("YYYY-MM-DD") || text.equals("Nhập quốc tịch...")) {
                field.setForeground(Color.GRAY);
            } else {
                field.setForeground(Color.BLACK);
            }
        }
    }

    public void updateTableData(List<VeMayBay> veMayBayList) {
        String[] columnNames = {
            "Mã Vé", "Mã Chuyến Bay", "Mã Khách Hàng", "Tên Khách Hàng", 
            "CMND", "Ngày Đặt", "Giá Vé", "Trạng Thái",
            "Số Điện Thoại", "Địa Chỉ", "Email", "Ngày Sinh", "Quốc Tịch"
        };
        
        tableModel = new DefaultTableModel(columnNames, 0);
        
        for (VeMayBay veMayBay : veMayBayList) {
            tableModel.addRow(new Object[]{
                veMayBay.getMaVe(),
                veMayBay.getMaChuyenBay(),
                veMayBay.getMaKhachHang(),
                veMayBay.getTenKhachHang(),
                veMayBay.getCmnd(),
                veMayBay.getNgayDat(),
                veMayBay.getGiaVe(),
                veMayBay.getTrangThai(),
                veMayBay.getSoDienThoai(),
                veMayBay.getDiaChi(),
                veMayBay.getEmail(),
                veMayBay.getNgaySinh(),
                veMayBay.getQuocTich()
            });
        }
        
        table.setModel(tableModel);
        
        // Điều chỉnh kích thước các cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Mã Vé
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Mã Chuyến Bay
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Mã Khách Hàng
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Tên Khách Hàng
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // CMND
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Ngày Đặt
        table.getColumnModel().getColumn(6).setPreferredWidth(100); // Giá Vé
        table.getColumnModel().getColumn(7).setPreferredWidth(100); // Trạng Thái
        table.getColumnModel().getColumn(8).setPreferredWidth(100); // Số Điện Thoại
        table.getColumnModel().getColumn(9).setPreferredWidth(200); // Địa Chỉ
        table.getColumnModel().getColumn(10).setPreferredWidth(150); // Email
        table.getColumnModel().getColumn(11).setPreferredWidth(100); // Ngày Sinh
        table.getColumnModel().getColumn(12).setPreferredWidth(100); // Quốc Tịch
    }

    public void clearFields() {
        maVeField.setText((String) maVeField.getClientProperty("placeholder"));
        maChuyenBayField.setText((String) maChuyenBayField.getClientProperty("placeholder"));
        maKhachHangField.setText((String) maKhachHangField.getClientProperty("placeholder"));
        tenKhachHangField.setText((String) tenKhachHangField.getClientProperty("placeholder"));
        cmndField.setText((String) cmndField.getClientProperty("placeholder"));
        ngayDatField.setText((String) ngayDatField.getClientProperty("placeholder"));
        giaVeField.setText((String) giaVeField.getClientProperty("placeholder"));
        trangThaiField.setText((String) trangThaiField.getClientProperty("placeholder"));
        searchField.setText((String) searchField.getClientProperty("placeholder"));
        // Thêm clear cho các field mới
        soDienThoaiField.setText((String) soDienThoaiField.getClientProperty("placeholder"));
        diaChiField.setText((String) diaChiField.getClientProperty("placeholder"));
        emailField.setText((String) emailField.getClientProperty("placeholder"));
        ngaySinhField.setText((String) ngaySinhField.getClientProperty("placeholder"));
        quocTichField.setText((String) quocTichField.getClientProperty("placeholder"));
    
        // Reset text colors
        for (JTextField field : new JTextField[]{
            maVeField, maChuyenBayField, maKhachHangField, tenKhachHangField, 
            cmndField, ngayDatField, giaVeField, trangThaiField, searchField,
            soDienThoaiField, diaChiField, emailField, ngaySinhField, quocTichField
        }) {
            field.setForeground(Color.GRAY);
        }
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    public void showNotification(String message, NotificationType type) {
        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

        // Create panel with rounded corners
        JPanel panel = new JPanel(new BorderLayout(15, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Symbol label
        JLabel symbolLabel = new JLabel(type.getSymbol());
        symbolLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        symbolLabel.setForeground(type.getColor());
        panel.add(symbolLabel, BorderLayout.WEST);

        // Message label
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(70, 70, 70));
        panel.add(messageLabel, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.pack();

        // Position the dialog
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(
            screenSize.width - dialog.getWidth() - 20,
            screenSize.height - dialog.getHeight() - 50
        );

        // Show and auto-close
        dialog.setVisible(true);
        new Timer(3000, e -> dialog.dispose()).start();
    }

    private void showAdvancedSearchDialog() {
        JDialog searchDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Tìm Kiếm Nâng Cao", Dialog.ModalityType.APPLICATION_MODAL);
        searchDialog.setLayout(new GridBagLayout());
        searchDialog.setBackground(Color.WHITE);
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Các trường tìm kiếm
        JTextField maVeField = createModernTextField("Mã vé");
        JTextField maChuyenBayField = createModernTextField("Mã chuyến bay");
        JTextField tenKhachHangField = createModernTextField("Tên khách hàng");
        JTextField cmndField = createModernTextField("CMND");
        JComboBox<String> trangThaiComboBox = new JComboBox<>(new String[]{
            "Tất cả", "Đã đặt", "Đã thanh toán", "Đã hủy"
        });
        trangThaiComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JTextField giaVeMinField = createModernTextField("Giá vé từ");
        JTextField giaVeMaxField = createModernTextField("Giá vé đến");
    
        // Thêm các trường vào dialog
        gbc.gridx = 0;
        gbc.gridy = 0;
        searchDialog.add(createLabel("Mã Vé:"), gbc);
        gbc.gridx = 1;
        searchDialog.add(maVeField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 1;
        searchDialog.add(createLabel("Mã Chuyến Bay:"), gbc);
        gbc.gridx = 1;
        searchDialog.add(maChuyenBayField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 2;
        searchDialog.add(createLabel("Tên Khách Hàng:"), gbc);
        gbc.gridx = 1;
        searchDialog.add(tenKhachHangField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 3;
        searchDialog.add(createLabel("CMND:"), gbc);
        gbc.gridx = 1;
        searchDialog.add(cmndField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 4;
        searchDialog.add(createLabel("Trạng Thái:"), gbc);
        gbc.gridx = 1;
        searchDialog.add(trangThaiComboBox, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 5;
        searchDialog.add(createLabel("Giá Vé Từ:"), gbc);
        gbc.gridx = 1;
        searchDialog.add(giaVeMinField, gbc);
    
        gbc.gridx = 0;
        gbc.gridy = 6;
        searchDialog.add(createLabel("Giá Vé Đến:"), gbc);
        gbc.gridx = 1;
        searchDialog.add(giaVeMaxField, gbc);
    
        // Nút tìm kiếm và nút hủy
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
    
        JButton searchButton = createModernButton("Tìm Kiếm", new Color(46, 204, 113));
        JButton cancelButton = createModernButton("Hủy", new Color(231, 76, 60));
    
        searchButton.addActionListener(e -> {
            performAdvancedSearch(
                maVeField.getText(),
                maChuyenBayField.getText(),
                tenKhachHangField.getText(),
                cmndField.getText(),
                (String)trangThaiComboBox.getSelectedItem(),
                giaVeMinField.getText(),
                giaVeMaxField.getText()
            );
            searchDialog.dispose();
        });
    
        cancelButton.addActionListener(e -> searchDialog.dispose());
    
        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);
    
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        searchDialog.add(buttonPanel, gbc);
    
        // Cài đặt giao diện
        searchDialog.getContentPane().setBackground(Color.WHITE);
        searchDialog.pack();
        searchDialog.setLocationRelativeTo(this);
        searchDialog.setVisible(true);
    }
    
    // Phương thức hỗ trợ tạo nhãn
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }

    private void performAdvancedSearch(String maVe, String maChuyenBay, String tenKhachHang, 
                                   String cmnd, String trangThai, String giaVeMin, String giaVeMax) {
        List<VeMayBay> filteredList = service.advancedSearchVeMayBay(
            maVe, maChuyenBay, tenKhachHang, cmnd, trangThai, giaVeMin, giaVeMax
        );
        updateTableData(filteredList);
    }
}