package View;

import Controller.HangHangKhongController;
import Model.HangHangKhong;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class QuanLyHangHangKhong extends JPanel {
    private DefaultTableModel tableModel;
    private HangHangKhongController controller;
    private JTextField maHangField, tenHangField, diaChiField, soDienThoaiField, emailField, searchField;
    private JTable table;
    
    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(236, 240, 241);

    public QuanLyHangHangKhong(TrangChuPanel trangChuPanel) {
        this.controller = new HangHangKhongController(this);
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
        updateHangList();
    }

    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel headerLabel = new JLabel("Quản Lý Hãng Hàng Không");
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
        maHangField = createModernTextField("Nhập mã hãng...");
        tenHangField = createModernTextField("Nhập tên hãng...");
        diaChiField = createModernTextField("Nhập địa chỉ...");
        soDienThoaiField = createModernTextField("Nhập số điện thoại...");
        emailField = createModernTextField("Nhập email...");
        searchField = createModernTextField("Tìm kiếm...");

        // Add fields with labels
        addModernInputField(inputPanel, "Mã Hãng", maHangField, gbc, 0);
        addModernInputField(inputPanel, "Tên Hãng", tenHangField, gbc, 1);
        addModernInputField(inputPanel, "Địa Chỉ", diaChiField, gbc, 2);
        addModernInputField(inputPanel, "Số Điện Thoại", soDienThoaiField, gbc, 3);
        addModernInputField(inputPanel, "Email", emailField, gbc, 4);

        return inputPanel;
    }

    private JPanel createModernTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Mã Hãng", "Tên Hãng", "Địa Chỉ", "Số Điện Thoại", "Email"};
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
        table.setOpaque(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer((TableCellRenderer) new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, 
                        isSelected, hasFocus, row, column);
                        
                // Thiết lập style cho header
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setBackground(primaryColor.brighter());
                label.setForeground(Color.WHITE);
                label.setPreferredSize(new Dimension(label.getPreferredSize().width, 40));
                label.setHorizontalAlignment(JLabel.CENTER);
                
                // Chỉ set border một lần với các effect mong muốn
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(200, 200, 200)),
                        BorderFactory.createLineBorder(new Color(0, 0, 0, 30))
                    ),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                
                return label;
            }
        });

        // Tắt khả năng kéo thả và sắp xếp lại cột
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);

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

        addButton.addActionListener(e -> addHangHangKhong());
        updateButton.addActionListener(e -> updateHangHangKhong());
        deleteButton.addActionListener(e -> deleteHangHangKhong());

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

    private void updateFieldsFromSelection(int selectedRow) {
        maHangField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        tenHangField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        diaChiField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        soDienThoaiField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        
        // Cập nhật màu chữ
        maHangField.setForeground(Color.BLACK);
        tenHangField.setForeground(Color.BLACK);
        diaChiField.setForeground(Color.BLACK);
        soDienThoaiField.setForeground(Color.BLACK);
        emailField.setForeground(Color.BLACK);
    }

    private void clearFields() {
        // Đặt lại text và màu cho tất cả các trường
        maHangField.setText((String)maHangField.getClientProperty("placeholder"));
        tenHangField.setText((String)tenHangField.getClientProperty("placeholder"));
        diaChiField.setText((String)diaChiField.getClientProperty("placeholder"));
        soDienThoaiField.setText((String)soDienThoaiField.getClientProperty("placeholder"));
        emailField.setText((String)emailField.getClientProperty("placeholder"));
        searchField.setText((String)searchField.getClientProperty("placeholder"));

        // Đặt lại màu chữ
        maHangField.setForeground(Color.GRAY);
        tenHangField.setForeground(Color.GRAY);
        diaChiField.setForeground(Color.GRAY);
        soDienThoaiField.setForeground(Color.GRAY);
        emailField.setForeground(Color.GRAY);
        searchField.setForeground(Color.GRAY);
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

    private void updateHangList() {
        List<HangHangKhong> hangList = controller.getAllHangHangKhong();
        tableModel.setRowCount(0);
        for (HangHangKhong hang : hangList) {
            tableModel.addRow(new Object[]{
                hang.getMaHang(),
                hang.getTenHang(),
                hang.getDiaChi(),
                hang.getSoDienThoai(),
                hang.getEmail()
            });
        }
    }

    private void performSearch(String keyword) {
        List<HangHangKhong> searchResults = controller.searchHangHangKhong(keyword);
        tableModel.setRowCount(0);
        for (HangHangKhong hang : searchResults) {
            tableModel.addRow(new Object[]{
                hang.getMaHang(),
                hang.getTenHang(),
                hang.getDiaChi(),
                hang.getSoDienThoai(),
                hang.getEmail()
            });
        }
        
    }

    private void addHangHangKhong() {
        String maHang = getTextOrPlaceholder(maHangField);
        String tenHang = getTextOrPlaceholder(tenHangField);
        String diaChi = getTextOrPlaceholder(diaChiField);
        String soDienThoai = getTextOrPlaceholder(soDienThoaiField);
        String email = getTextOrPlaceholder(emailField);

        if (controller.addHangHangKhong(maHang, tenHang, diaChi, soDienThoai, email)) {
            showNotification("Thêm hãng hàng thành công", NotificationType.SUCCESS);
            updateHangList();
            clearFields();
        } else {
            showNotification("Thêm hãng hàng thất bại", NotificationType.ERROR);
        }
    }

    private void updateHangHangKhong() {
        String maHang = getTextOrPlaceholder(maHangField);
        String tenHang = getTextOrPlaceholder(tenHangField);
        String diaChi = getTextOrPlaceholder(diaChiField);
        String soDienThoai = getTextOrPlaceholder(soDienThoaiField);
        String email = getTextOrPlaceholder(emailField);

        if (controller.updateHangHangKhong(maHang, tenHang, diaChi, soDienThoai, email)) {
            showNotification("Cập nhật hãng hàng thành công", NotificationType.SUCCESS);
            updateHangList();
            clearFields();
        } else {
            showNotification("Cập nhật hãng hàng thất bại", NotificationType.ERROR);
        }
    }

    private void deleteHangHangKhong() {
        String maHang = getTextOrPlaceholder(maHangField);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa hãng hàng không này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.deleteHangHangKhong(maHang)) {
                showNotification("Xóa hãng hàng thành công", NotificationType.SUCCESS);
                updateHangList();
                clearFields();
            } else {
                showNotification("Xóa hãng hàng thất bại", NotificationType.ERROR);
            }
        }
    }

    private String getTextOrPlaceholder(JTextField field) {
        String text = field.getText();
        String placeholder = (String) field.getClientProperty("placeholder");
        return text.equals(placeholder) ? "" : text;
    }
}