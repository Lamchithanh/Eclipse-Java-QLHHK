package View;

import Controller.ChuyenBayController;
import Model.ChuyenBay;
import Service.ChuyenBayService;
import Database.MYSQLDB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class QuanLyChuyenBay extends JPanel {
    private DefaultTableModel tableModel;
    private ChuyenBayController controller;
    private JTextField maChuyenBayField, changBayField, ngayBayField, sanBayField, 
                       nhaGaField, soGheField, searchField, diemDiField, diemDenField, giaVeField;
    private JComboBox<String> maMayBayComboBox, maHangComboBox, sanBayComboBox;
    private JTable chuyenBayTable;
    private JComboBox<String> tinhTrangComboBox;
    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(236, 240, 241);

// Thêm enum NotificationType ở đây
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

        public String getSymbol() {
            return symbol;
        }

        @SuppressWarnings("exports")
        public Color getColor() {
            return color;
        }
    }

    public QuanLyChuyenBay(TrangChuPanel trangChuPanel) {
        controller = new ChuyenBayController(this);
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
        splitPane.setDividerLocation(320);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);
        mainContent.add(splitPane, BorderLayout.CENTER);

        // Action Panel
        mainContent.add(createModernActionPanel(), BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
        
        // Initialize data
        loadFlightsFromDatabase();
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

    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel headerLabel = new JLabel("Quản Lý Chuyến Bay");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setIconTextGap(15);

        headerPanel.add(headerLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private JTextField createLockedTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        field.setEditable(false);
        field.setBackground(new Color(240, 240, 240)); // Màu nền xám nhạt
        field.setForeground(Color.gray);
        field.setText(placeholder);
        
        // Ngăn chặn mọi sự kiện click
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume(); // Ngăn chặn sự kiện click
            }
        });
        
        return field;
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
    
        // Cột trái
        JPanel leftColumn = new JPanel(new GridBagLayout());
        leftColumn.setBackground(Color.WHITE);
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5, 5, 5, 5);
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Cột phải
        JPanel rightColumn = new JPanel(new GridBagLayout());
        rightColumn.setBackground(Color.WHITE);
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(5, 5, 5, 5);
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Thêm các trường nhập liệu cũ
        maChuyenBayField = createModernTextField("Nhập mã chuyến bay...");
        changBayField = createLockedTextField("Chặng bay tự động");
        ngayBayField = createModernTextField("YYYY-MM-DD");
        sanBayComboBox = createModernComboBox(getSanBayData());
        
        // Thêm trường điểm đi và điểm đến
        diemDiField = createModernTextField("Nhập điểm đi...");
        diemDenField = createModernTextField("Nhập điểm đến...");
        giaVeField = createModernTextField("Nhập giá vé...");
    
        // Cột trái
        addColumnInputField(leftColumn, "Mã Chuyến Bay", maChuyenBayField, leftGbc, 0);
        addColumnInputField(leftColumn, "Điểm Đi", diemDiField, leftGbc, 1);
        addColumnInputField(leftColumn, "Điểm Đến", diemDenField, leftGbc, 2);
        addColumnInputField(leftColumn, "Chặng Bay", changBayField, leftGbc, 3);
        addColumnInputField(leftColumn, "Ngày Bay", ngayBayField, leftGbc, 4);
        addColumnInputField(leftColumn, "Sân Bay", sanBayComboBox, leftGbc, 5);
    
        // Cột phải
        nhaGaField = createModernTextField("Nhập nhà ga...");
        soGheField = createModernTextField("Nhập số ghế...");
        String[] tinhTrangOptions = {"Sắp khởi hành", "Đã khởi hành", "Delay", "Đã hủy"};
        tinhTrangComboBox = createModernComboBox(tinhTrangOptions);
        searchField = createModernTextField("Tìm kiếm...");
    
        // Initialize ComboBoxes with modern styling
        maMayBayComboBox = createModernComboBox(getMaMayBayData());
        maHangComboBox = createModernComboBox(getMaHangData());
    
        // Cột phải
        addColumnInputField(rightColumn, "Nhà Ga", nhaGaField, rightGbc, 6);
        addColumnInputField(rightColumn, "Số Ghế", soGheField, rightGbc, 7);
        addColumnInputField(rightColumn, "Tình Trạng", tinhTrangComboBox , rightGbc, 8);
        addColumnInputField(rightColumn, "Mã Máy Bay", maMayBayComboBox, rightGbc, 9);
        addColumnInputField(rightColumn, "Mã Hãng", maHangComboBox, rightGbc, 10);
        addColumnInputField(rightColumn, "Giá Vé", giaVeField, rightGbc, 11);
        // Thêm hai cột vào panel chính
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        inputPanel.add(leftColumn, gbc);
    
        gbc.gridx = 1;
        gbc.weightx = 0.5;
        inputPanel.add(rightColumn, gbc);
    
        return inputPanel;
    }
    
    // Phương thức hỗ trợ thêm trường nhập vào cột
    private void addColumnInputField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
    
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);
    
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        
        // Đảm bảo kích thước nhất quán
        if (field instanceof JTextField) {
            field.setPreferredSize(new Dimension(200, 35));
        } else if (field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(200, 35));
        }
        
        panel.add(field, gbc);
    }
    
    // Điều chỉnh createModernTextField để giảm kích thước font
    private JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Giảm kích thước font
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
    
    // Điều chỉnh createModernComboBox tương tự
    private JComboBox<String> createModernComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Giảm kích thước font
        comboBox.setBackground(Color.WHITE);
        comboBox.setPreferredSize(new Dimension(200, 35));
        return comboBox;
    }

    private JPanel createModernTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {
            "Mã Chuyến Bay", 
            "Điểm Đi", 
            "Điểm Đến", 
            "Chặng Bay", 
            "Ngày Bay", 
            "Sân Bay", 
            "Nhà Ga", 
            "Số Ghế", 
            "Tình Trạng", 
            "Máy Bay", 
            "Hãng Bay", 
            "Giá Vé"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
        chuyenBayTable = new JTable(tableModel) {
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
        chuyenBayTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chuyenBayTable.setRowHeight(35);
        chuyenBayTable.setShowGrid(false);
        chuyenBayTable.setIntercellSpacing(new Dimension(0, 0));
        
        // Style header
        JTableHeader header = chuyenBayTable.getTableHeader();
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
        chuyenBayTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectFlight();
            }
        });

        JScrollPane scrollPane = new JScrollPane(chuyenBayTable);
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
                    searchFlight(searchQuery.toLowerCase());
                }
            }
        });
        searchPanel.add(searchField);

        // Action Buttons
        JButton addButton = createModernButton("Thêm", new Color(46, 204, 113));
        JButton updateButton = createModernButton("Cập Nhật", new Color(52, 152, 219));
        JButton deleteButton = createModernButton("Xóa", new Color(231, 76, 60));

        addButton.addActionListener(e -> addFlight());
        updateButton.addActionListener(e -> updateFlight());
        deleteButton.addActionListener(e -> deleteFlight());
        

        actionPanel.add(searchPanel);
        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        return actionPanel;
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

    

    private void addModernInputField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        // Create and style the label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        
        // Add padding to the label
        label.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 10));
    
        // Configure constraints for the label (left column)
        gbc.gridx = 0;  // First column
        gbc.gridy = row;  // Current row
        gbc.weightx = 0.3;  // 30% of the horizontal space
        gbc.anchor = GridBagConstraints.WEST;  // Align left
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);  // Add margins around the component
        panel.add(label, gbc);
    
        // Configure constraints for the field (right column)
        gbc.gridx = 1;  // Second column
        gbc.weightx = 0.7;  // 70% of the horizontal space
        
        // If the field is a JTextField or JComboBox, ensure consistent height
        if (field instanceof JTextField || field instanceof JComboBox) {
            field.setPreferredSize(new Dimension(field.getPreferredSize().width, 35));
        }
        
        // If it's a text field, add proper styling
        if (field instanceof JTextField) {
            JTextField textField = (JTextField) field;
            textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));
        }
        
        // If it's a combo box, add proper styling
        if (field instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) field;
            comboBox.setBackground(Color.WHITE);
            comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }
    
        // Add the field to the panel
        panel.add(field, gbc);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    
        JLabel headerLabel = new JLabel("Quản Lý Chuyến Bay", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(40, 40, 90));
    
        topPanel.add(headerLabel, BorderLayout.WEST);
        return topPanel;
    }
    
    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
    
        // Panel chứa các trường nhập liệu
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 240, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        // Khởi tạo các trường nhập liệu
        maChuyenBayField = createStyledTextField();
        changBayField = createStyledTextField();
        ngayBayField = createStyledTextField();
        sanBayField = createStyledTextField();
        nhaGaField = createStyledTextField();
        soGheField = createStyledTextField();
        String[] tinhTrangOptions = {"Sắp khởi hành", "Đã khởi hành", "Delay", "Đã hủy"};
        tinhTrangComboBox = createStyledComboBox(tinhTrangOptions);
    
        // Khởi tạo ComboBox
        maMayBayComboBox = new JComboBox<>(getMaMayBayData());
        maHangComboBox = new JComboBox<>(getMaHangData());
    
        // Mã Chuyến Bay
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Mã Chuyến Bay:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(maChuyenBayField, gbc);
    
        // Chặng Bay
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Chặng Bay:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(changBayField, gbc);
    
        // Ngày Bay
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Ngày Bay:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(ngayBayField, gbc);
    
        // Sân Bay
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Sân Bay:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(sanBayField, gbc);
    
        // Nhà Ga
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Nhà Ga:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(nhaGaField, gbc);
    
        // Số Ghế
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Số Ghế:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(soGheField, gbc);
    
        // Tình Trạng
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Tình Trạng:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(tinhTrangComboBox, gbc);
    
        // Mã Máy Bay
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Mã Máy Bay:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(maMayBayComboBox, gbc);
    
        // Mã Hãng
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Mã Hãng:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(maHangComboBox, gbc);
    
        // Ô tìm kiếm
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0.3;
        inputPanel.add(createStyledLabel("Tìm Kiếm:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        inputPanel.add(searchField, gbc);
    
        // Tạo bảng
        String[] columnNames = {"Mã Chuyến Bay", "Chặng Bay", "Ngày Bay", "Sân Bay", "Nhà Ga", "Số Ghế", "Tình Trạng", "Mã Máy Bay", "Mã Hãng"};
        tableModel = new DefaultTableModel(columnNames, 0);
        chuyenBayTable = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(chuyenBayTable);
    
        // Bố cục chính
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
    
        return mainPanel;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 250));
    
        JButton addButton = createStyledButton("Thêm");
        JButton updateButton = createStyledButton("Cập Nhật");
        JButton deleteButton = createStyledButton("Xóa");
    
        // Chỉ gán một action cho mỗi button
        addButton.addActionListener(e -> addFlight());
        updateButton.addActionListener(e -> updateFlight());
        deleteButton.addActionListener(e -> deleteFlight());
    
        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
    
        return bottomPanel;
    }
    
    private void addActionListeners() {
        // Add listener for table row selection
        chuyenBayTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectFlight();
            }
        });
    
        // Add search functionality
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText().toLowerCase();
                searchFlight(searchQuery);
            }
        });
    }
    
    private void clearFields() {
        maChuyenBayField.setText("");
        changBayField.setText("");
        ngayBayField.setText("");
        sanBayComboBox.setSelectedIndex(-1);
        nhaGaField.setText("");
        soGheField.setText("");
        tinhTrangComboBox.setSelectedIndex(0);
        maMayBayComboBox.setSelectedIndex(0);
        maHangComboBox.setSelectedIndex(0);
        searchField.setText("");
        diemDiField.setText("");
        diemDenField.setText("");
    }
    
    private void selectFlight() {
        int selectedRow = chuyenBayTable.getSelectedRow();
        if (selectedRow >= 0) {
            // Get the data from the selected row
            String maChuyenBay = tableModel.getValueAt(selectedRow, 0).toString();
            String diemDi = tableModel.getValueAt(selectedRow, 1).toString();
            String diemDen = tableModel.getValueAt(selectedRow, 2).toString();
            String changBay = tableModel.getValueAt(selectedRow, 3).toString();
            String ngayBay = tableModel.getValueAt(selectedRow, 4).toString();
            String sanBay = tableModel.getValueAt(selectedRow, 5).toString();
            String nhaGa = tableModel.getValueAt(selectedRow, 6).toString();
            String soGhe = tableModel.getValueAt(selectedRow, 7).toString();
            String tinhTrang = tableModel.getValueAt(selectedRow, 8).toString();
            String maMayBay = tableModel.getValueAt(selectedRow, 9).toString();
            String maHang = tableModel.getValueAt(selectedRow, 10).toString();
            String giaVe = tableModel.getValueAt(selectedRow, 11).toString().replace(",", "");

            // Set the values to the input fields
            maChuyenBayField.setText(maChuyenBay);
            diemDiField.setText(diemDi);
            diemDenField.setText(diemDen);
            changBayField.setText(changBay);
            ngayBayField.setText(ngayBay);
            String sanBayInfo = tableModel.getValueAt(selectedRow, 5).toString();
            for (int i = 0; i < sanBayComboBox.getItemCount(); i++) {
                if (sanBayComboBox.getItemAt(i).contains(extractMaSanBay(sanBayInfo))) {
                    sanBayComboBox.setSelectedIndex(i);
                    break;
                }
            }
            nhaGaField.setText(nhaGa);
            soGheField.setText(soGhe);
            tinhTrangComboBox.setSelectedItem(tinhTrang);
            giaVeField.setText(giaVe);


            for (int i = 0; i < maMayBayComboBox.getItemCount(); i++) {
                if (extractMaMayBay(maMayBayComboBox.getItemAt(i)).equals(maMayBay)) {
                    maMayBayComboBox.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < maHangComboBox.getItemCount(); i++) {
                if (extractMaHang(maHangComboBox.getItemAt(i)).equals(maHang)) {
                    maHangComboBox.setSelectedIndex(i);
                    break;
                }
            }

            // Set ComboBox values
            if (Arrays.asList(getMaMayBayData()).contains(maMayBay)) {
                maMayBayComboBox.setSelectedItem(maMayBay);
            }

            if (Arrays.asList(getMaHangData()).contains(maHang)) {
                maHangComboBox.setSelectedItem(maHang);
            }
        }
    }
    
    // Utility methods for populating ComboBoxes
    private String[] getMaMayBayData() {
        List<String> maMayBayList = new ArrayList<>();
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT MaMayBay, LoaiMayBay FROM MayBay");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String maMayBay = rs.getString("MaMayBay");
                String loaiMayBay = rs.getString("LoaiMayBay");
                maMayBayList.add(loaiMayBay + " (" + maMayBay + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maMayBayList.toArray(new String[0]);
    }
    
    // Khi lấy mã máy bay từ ComboBox để lưu vào DB:
    private String extractMaMayBay(String selectedItem) {
        if (selectedItem == null) return "";
        int start = selectedItem.lastIndexOf("(");
        int end = selectedItem.lastIndexOf(")");
        if (start >= 0 && end >= 0) {
            return selectedItem.substring(start + 1, end);
        }
        return selectedItem;
    }
    
    private String[] getMaHangData() {
        List<String> maHangList = new ArrayList<>();
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT MaHang, TenHang FROM HangHangKhong");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String maHang = rs.getString("MaHang");
                String tenHang = rs.getString("TenHang"); 
                maHangList.add(tenHang + " (" + maHang + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maHangList.toArray(new String[0]);
    }
    
    // Khi lấy mã hãng từ ComboBox để lưu vào DB:
    private String extractMaHang(String selectedItem) {
        if (selectedItem == null) return "";
        int start = selectedItem.lastIndexOf("(");
        int end = selectedItem.lastIndexOf(")");
        if (start >= 0 && end >= 0) {
            return selectedItem.substring(start + 1, end);
        }
        return selectedItem;
    }
    
    // Styling methods (can be moved to a utility class if needed)
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(40, 40, 90));
        return label;
    }
    
    // Phương thức tạo TextField đã có
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }

    // Phương thức mới tạo ComboBox
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return comboBox;
    }
    
    private JTable createStyledTable() {
        JTable table = new JTable(tableModel);
    
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(200, 220, 255));
    
        // Mouse click event for table
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectFlight(); 
            }
        });
    
        return table;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 160, 210));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    
        return button;
    }

    private void loadFlightsFromDatabase() {
        try {
            List<ChuyenBay> flights = controller.getAllFlights();
            tableModel.setRowCount(0);  // Clear existing data
            
            for (ChuyenBay flight : flights) {
                String tenMayBay = getTenMayBay(flight.getMaMaybay());
                String tenHang = getTenHang(flight.getMaHang());
                
                tableModel.addRow(new Object[] {
                    flight.getMaChuyenBay(),
                    flight.getDiemDi(),
                    flight.getDiemDen(),
                    flight.getChangBay(),
                    flight.getNgayBay(),
                    flight.getTenSanBay(),
                    flight.getNhaGa(),
                    flight.getSoGhe(),
                    flight.getTinhTrang(),
                    tenMayBay + " (" + flight.getMaMaybay() + ")", // Hiển thị tên và mã
                    tenHang + " (" + flight.getMaHang() + ")",     // Hiển thị tên và mã
                    String.format("%,.0f", flight.getGiaVe())
                });
            }
            
            tableModel.fireTableDataChanged(); 
            chuyenBayTable.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi tải dữ liệu: " + e.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchFlight(String searchQuery) {
        if (searchQuery.trim().isEmpty()) {
            loadFlightsFromDatabase();
            return;
        }
    
        List<ChuyenBay> searchResults = controller.searchFlights(searchQuery);
    
        tableModel.setRowCount(0); 
        for (ChuyenBay flight : searchResults) {
            String tenMayBay = getTenMayBay(flight.getMaMaybay());
            String tenHang = getTenHang(flight.getMaHang());
            
            tableModel.addRow(new Object[] {
                flight.getMaChuyenBay(),
                flight.getDiemDi(),
                flight.getDiemDen(),
                flight.getChangBay(),
                flight.getNgayBay(),
                flight.getSanBay(),
                flight.getNhaGa(),
                flight.getSoGhe(),
                flight.getTinhTrang(),
                tenMayBay + " (" + flight.getMaMaybay() + ")", // Hiển thị tên và mã
                tenHang + " (" + flight.getMaHang() + ")",     // Hiển thị tên và mã
                String.format("%,.0f", flight.getGiaVe())
            });
        }
    }

    private String getTenMayBay(String maMayBay) {
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT LoaiMayBay FROM MayBay WHERE MaMayBay = ?")) {
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
    
    private String getTenHang(String maHang) {
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT TenHang FROM HangHangKhong WHERE MaHang = ?")) {
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

    private void addFlight() {
        // Lấy dữ liệu từ các trường nhập
        String maChuyenBay = maChuyenBayField.getText().trim();
        String ngayBay = ngayBayField.getText().trim();
        String selectedSanBay = (String) sanBayComboBox.getSelectedItem();
        String maSanBay = extractMaSanBay(selectedSanBay);
        String nhaGa = nhaGaField.getText().trim();
        String soGhe = soGheField.getText().trim();
        String tinhTrang = (String) tinhTrangComboBox.getSelectedItem();
        String maMayBay = extractMaMayBay((String) maMayBayComboBox.getSelectedItem());
        String maHang = extractMaHang((String) maHangComboBox.getSelectedItem());
        String diemDi = diemDiField.getText().trim(); 
        String diemDen = diemDenField.getText().trim();
        String giaVe = giaVeField.getText().trim();
       
        
        String changBay = diemDi + " - " + diemDen;
        changBayField.setText(changBay);
    
        // Kiểm tra tính hợp lệ của dữ liệu
        if (!controller.validateFlightInput(maChuyenBay, changBay, ngayBay, maSanBay, soGhe, maMayBay, maHang, diemDi, diemDen)) {
            return;
        }
    
        if (controller.addFlight(maChuyenBay, changBay, ngayBay, maSanBay, nhaGa, soGhe, tinhTrang, maMayBay, maHang, diemDi, diemDen, giaVe)) {
            showNotification("Thêm chuyến bay thành công", NotificationType.SUCCESS);
        } else {
            showNotification("Thêm chuyến bay thất bại", NotificationType.ERROR);
        }
    
        // Tải lại danh sách chuyến bay từ cơ sở dữ liệu
        loadFlightsFromDatabase();
        
        // Xóa các trường nhập liệu
        clearFields();
    }
    
    private void updateFlight() {
        // Lấy dữ liệu từ các trường nhập
        String maChuyenBay = maChuyenBayField.getText().trim();
        String ngayBay = ngayBayField.getText().trim();
        String selectedSanBay = (String) sanBayComboBox.getSelectedItem();
        String maSanBay = extractMaSanBay(selectedSanBay);
        String nhaGa = nhaGaField.getText().trim();
        String soGhe = soGheField.getText().trim();
        String tinhTrang = (String) tinhTrangComboBox.getSelectedItem();
        String maMayBay = extractMaMayBay((String) maMayBayComboBox.getSelectedItem());
        String maHang = extractMaHang((String) maHangComboBox.getSelectedItem());
        String diemDi = diemDiField.getText().trim(); 
        String diemDen = diemDenField.getText().trim();
        String giaVe = giaVeField.getText().trim();
        
        String changBay = diemDi + " - " + diemDen;
        changBayField.setText(changBay);
    
        // Kiểm tra mã chuyến bay để cập nhật
        if (maChuyenBay.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn chuyến bay để cập nhật",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Kiểm tra tính hợp lệ của dữ liệu
        if (!controller.validateFlightInput(maChuyenBay, changBay, ngayBay, maSanBay, soGhe, maMayBay, maHang, diemDi, diemDen)) {
            return;
        }
    
        if (controller.updateFlight(maChuyenBay, changBay, ngayBay, maSanBay, nhaGa, soGhe, tinhTrang, maMayBay, maHang, diemDi, diemDen, giaVe)) {
            showNotification("Cập nhật chuyến bay thành công", NotificationType.SUCCESS);
        } else {
            showNotification("Cập nhật chuyến bay thất bại", NotificationType.ERROR);
        }
        
        // Tải lại danh sách chuyến bay từ cơ sở dữ liệu
        loadFlightsFromDatabase();
        
        // Xóa các trường nhập liệu
        clearFields();
    }

   private void deleteFlight() {
       // Lấy mã chuyến bay để xóa
       String maChuyenBay = maChuyenBayField.getText().trim();

       // Gọi phương thức xóa từ controller
       if (controller.deleteFlight(maChuyenBay)) {
        showNotification("Xóa chuyến bay thành công", NotificationType.SUCCESS);
        } else {
            showNotification("Xóa chuyến bay thất bại", NotificationType.ERROR);
        }
       
       // Tải lại danh sách chuyến bay từ cơ sở dữ liệu
       loadFlightsFromDatabase();
       
       // Xóa các trường nhập liệu
       clearFields();
   }

    // Thêm phương thức này vào class QuanLyChuyenBay
    private String[] getSanBayData() {
        List<String> sanBayList = new ArrayList<>();
        try (Connection conn = MYSQLDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT MaSanBay, TenSanBay FROM SanBay");
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String maSanBay = rs.getString("MaSanBay");
                String tenSanBay = rs.getString("TenSanBay"); 
                sanBayList.add(tenSanBay + " (" + maSanBay + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanBayList.toArray(new String[0]);
    }

    // Thêm phương thức để lấy mã sân bay từ chuỗi đã chọn
    private String extractMaSanBay(String selectedItem) {
        if (selectedItem == null) return "";
        int start = selectedItem.lastIndexOf("(");
        int end = selectedItem.lastIndexOf(")");
        if (start >= 0 && end >= 0) {
            return selectedItem.substring(start + 1, end);
        }
        return selectedItem;
    }
}