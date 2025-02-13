package View;

import Controller.NhanVienController;
import Model.NhanVien;
import Database.MYSQLDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class QuanLyNhanVien extends JPanel {
    private DefaultTableModel tableModel;
    private NhanVienController controller;
    private JTextField maNhanVienField, tenNhanVienField, chucVuField, searchField;
    private JComboBox<String> maHangComboBox;
    private JTable table;
    
    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(236, 240, 241);

    public enum NotificationType {
        SUCCESS("✓", new Color(46, 204, 113)),
        ERROR("✕", new Color(231, 76, 60)),
        WARNING("!", new Color(241, 196, 15)),
        INFO("i", new Color(52, 152, 219));

        private final String symbol;
        private final Color color;

        NotificationType(String symbol, Color color) {
            this.symbol = symbol;
            this.color = color;
        }

        public String getSymbol() { return symbol; }
        public Color getColor() { return color; }
    }

    public QuanLyNhanVien(TrangChuPanel trangChuPanel) {
        this.controller = new NhanVienController(this);
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(backgroundColor);

        // Header Panel
        add(createModernHeader(), BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBackground(backgroundColor);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        loadNhanVienFromDatabase();
    }

    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel headerLabel = new JLabel("Quản Lý Nhân Viên");
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
            BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialize fields with placeholders
        maNhanVienField = createModernTextField("Nhập mã nhân viên...");
        tenNhanVienField = createModernTextField("Nhập tên nhân viên...");
        chucVuField = createModernTextField("Nhập chức vụ...");
        searchField = createModernTextField("Tìm kiếm...");

        // Initialize ComboBox
        maHangComboBox = createModernComboBox();
        populateMaHangComboBox();

        // Add fields with labels
        addModernInputField(inputPanel, "Mã Nhân Viên", maNhanVienField, gbc, 0);
        addModernInputField(inputPanel, "Tên Nhân Viên", tenNhanVienField, gbc, 1);
        addModernInputField(inputPanel, "Chức Vụ", chucVuField, gbc, 2);
        addModernInputField(inputPanel, "Mã Hãng", maHangComboBox, gbc, 3);

        return inputPanel;
    }

    private JPanel createModernTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Mã Nhân Viên", "Tên Nhân Viên", "Chức Vụ", "Mã Hãng"};
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
                updateFieldsFromSelection();
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

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(backgroundColor);
        searchField.setPreferredSize(new Dimension(200, 35));
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText();
                if (!searchQuery.equals("Tìm kiếm...")) {
                    performSearch(searchQuery.toLowerCase());
                }
            }
        });
        searchPanel.add(searchField);

        // Action Buttons
        JButton addButton = createModernButton("Thêm", new Color(46, 204, 113));
        JButton updateButton = createModernButton("Cập Nhật", new Color(52, 152, 219));
        JButton deleteButton = createModernButton("Xóa", new Color(231, 76, 60));

        addButton.addActionListener(e -> addNhanVien());
        updateButton.addActionListener(e -> updateNhanVien());
        deleteButton.addActionListener(e -> deleteNhanVien());

        actionPanel.add(searchPanel);
        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        return actionPanel;
    }

    private void addModernInputField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

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

    private JComboBox<String> createModernComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(200, 35));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return comboBox;
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

    // Helper methods
    private void populateMaHangComboBox() {
        maHangComboBox.removeAllItems();
        try (Connection conn = MYSQLDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT MaHang FROM HangHangKhong")) {
            
            while (rs.next()) {
                maHangComboBox.addItem(rs.getString("MaHang"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showNotification("Lỗi khi tải danh sách mã hãng", NotificationType.ERROR);
        }
    }

    private void updateFieldsFromSelection() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            maNhanVienField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            tenNhanVienField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            chucVuField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            maHangComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
            
            maNhanVienField.setForeground(Color.BLACK);
            tenNhanVienField.setForeground(Color.BLACK);
            chucVuField.setForeground(Color.BLACK);
        }
    }

    private void clearFields() {
        maNhanVienField.setText((String)maNhanVienField.getClientProperty("placeholder"));
        tenNhanVienField.setText((String)tenNhanVienField.getClientProperty("placeholder"));
        chucVuField.setText((String)chucVuField.getClientProperty("placeholder"));
        searchField.setText((String)searchField.getClientProperty("placeholder"));
        
        maNhanVienField.setForeground(Color.GRAY);
        tenNhanVienField.setForeground(Color.GRAY);
        chucVuField.setForeground(Color.GRAY);
        searchField.setForeground(Color.GRAY);
        
        if (maHangComboBox.getItemCount() > 0) {
            maHangComboBox.setSelectedIndex(0);
        }
    }

    public void showNotification(String message, NotificationType type) {
        JDialog dialog = new JDialog();
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));

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

        JLabel symbolLabel = new JLabel(type.getSymbol());
        symbolLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        symbolLabel.setForeground(type.getColor());
        panel.add(symbolLabel, BorderLayout.WEST);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(70, 70, 70));
        panel.add(messageLabel, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(
            screenSize.width - dialog.getWidth() - 20,
            screenSize.height - dialog.getHeight() - 50
        );

        dialog.setVisible(true);
        new Timer(3000, e -> dialog.dispose()).start();
    }

    private void loadNhanVienFromDatabase() {
        List<NhanVien> nhanViens = controller.getAllNhanVien();
        tableModel.setRowCount(0);
        
        for (NhanVien nv : nhanViens) {
            tableModel.addRow(new Object[]{
                nv.getMaNhanVien(),
                nv.getTenNhanVien(),
                nv.getChucVu(),
                nv.getMaHang()
            });
        }
        
        clearFields();
    }

    private void performSearch(String searchQuery) {
        List<NhanVien> searchResults = controller.searchNhanVien(searchQuery);
        tableModel.setRowCount(0);
        
        for (NhanVien nv : searchResults) {
            tableModel.addRow(new Object[]{
                nv.getMaNhanVien(),
                nv.getTenNhanVien(),
                nv.getChucVu(),
                nv.getMaHang()
            });
        }
        
      
    }

    private String getTextOrPlaceholder(JTextField field) {
        String text = field.getText();
        String placeholder = (String) field.getClientProperty("placeholder");
        return text.equals(placeholder) ? "" : text;
    }

    private void addNhanVien() {
        String maNhanVien = getTextOrPlaceholder(maNhanVienField);
        String tenNhanVien = getTextOrPlaceholder(tenNhanVienField);
        String chucVu = getTextOrPlaceholder(chucVuField);
        String maHang = (String) maHangComboBox.getSelectedItem();

        if (controller.addNhanVien(maNhanVien, tenNhanVien, chucVu, maHang)) {
            showNotification("Thêm nhân viên thành công", NotificationType.SUCCESS);
            loadNhanVienFromDatabase();
            clearFields();
        } else {
            showNotification("Thêm nhân viên thất bại", NotificationType.ERROR);
        }
    }

    private void updateNhanVien() {
        String maNhanVien = getTextOrPlaceholder(maNhanVienField);
        String tenNhanVien = getTextOrPlaceholder(tenNhanVienField);
        String chucVu = getTextOrPlaceholder(chucVuField);
        String maHang = (String) maHangComboBox.getSelectedItem();

        if (controller.updateNhanVien(maNhanVien, tenNhanVien, chucVu, maHang)) {
            showNotification("Cập nhật nhân viên thành công", NotificationType.SUCCESS);
            loadNhanVienFromDatabase();
            clearFields();
        } else {
            showNotification("Cập nhật nhân viên thất bại", NotificationType.ERROR);
        }
    }

    private void deleteNhanVien() {
        String maNhanVien = getTextOrPlaceholder(maNhanVienField);
        
        if (controller.deleteNhanVien(maNhanVien)) {
            showNotification("Xóa nhân viên thành công", NotificationType.SUCCESS);
            loadNhanVienFromDatabase();
            clearFields();
        } else {
            showNotification("Xóa nhân viên thất bại", NotificationType.ERROR);
        }
    }
}