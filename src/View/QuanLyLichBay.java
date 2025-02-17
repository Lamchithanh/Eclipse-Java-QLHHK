package View;

import Controller.LichBayController;
import Model.LichBay;
import Model.UserAccount;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

public class QuanLyLichBay extends JPanel {
    private LichBayController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField flightCodeField;
    private JComboBox<String> flightCodeComboBox;
    private JTextField departureTimeField;
    private JTextField arrivalTimeField;
    private JTextField flightDurationField;
    private JTextField searchField;
    
    private Color primaryColor = new Color(41, 128, 185);
    private Color backgroundColor = new Color(236, 240, 241);
    public QuanLyLichBay(TrangChuPanel trangChuPanel) {
        initializePanel();
    }

    public QuanLyLichBay(UserAccount user) {
        initializePanel();
    }

    private void initializePanel() {
        controller = new LichBayController(this);
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
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);
        mainContent.add(splitPane, BorderLayout.CENTER);

        // Action Panel
        mainContent.add(createModernActionPanel(), BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
        DocumentListener timeListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateDuration();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateDuration();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateDuration();
            }
        };

        departureTimeField.getDocument().addDocumentListener(timeListener);
        arrivalTimeField.getDocument().addDocumentListener(timeListener);

        // Initialize data
        controller.updateFlightList();
    }

    private void updateDuration() {
        String depTime = getTextOrPlaceholder(departureTimeField);
        String arrTime = getTextOrPlaceholder(arrivalTimeField);
        
        if (!depTime.isEmpty() && !arrTime.isEmpty() && 
            !depTime.equals("HH:mm") && !arrTime.equals("HH:mm")) {
            int duration = controller.calculateFlightDuration(depTime, arrTime);
            if (duration > 0) {
                flightDurationField.setText(controller.formatDuration(duration));
            } else {
                flightDurationField.setText("");
            }
        }
    }

    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel headerLabel = new JLabel("Quản Lý Lịch Bay");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setIconTextGap(15);

        headerPanel.add(headerLabel, BorderLayout.WEST);
        return headerPanel;
    }

    private void updateFlightCodeComboBox() {
        List<String> flightCodes = controller.getAllFlightCodes();
        flightCodeComboBox.removeAllItems();
        for (String code : flightCodes) {
            flightCodeComboBox.addItem(code);
        }
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
    
        // Tạo JComboBox cho mã chuyến bay
        flightCodeComboBox = new JComboBox<>();
        flightCodeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        flightCodeComboBox.setPreferredSize(new Dimension(200, 35));
        updateFlightCodeComboBox(); // Cập nhật danh sách mã chuyến bay
    
        // Tạo các trường nhập liệu khác
        departureTimeField = createModernTextField("HH:mm");
        arrivalTimeField = createModernTextField("HH:mm");
        flightDurationField = createModernTextField("Thời gian bay (tự động tính)");
        flightDurationField.setEditable(false);
        flightDurationField.setBackground(new Color(245, 245, 245));
    
        // Thêm các trường vào panel
        addModernInputField(inputPanel, "Mã Chuyến Bay", flightCodeComboBox, gbc, 0);
        addModernInputField(inputPanel, "Giờ Khởi Hành (HH:mm)", departureTimeField, gbc, 1);
        addModernInputField(inputPanel, "Giờ Hạ Cánh (HH:mm)", arrivalTimeField, gbc, 2);
        addModernInputField(inputPanel, "Thời Gian Bay", flightDurationField, gbc, 3);
    
        return inputPanel;
    }

    private JPanel createModernTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Mã Chuyến Bay", "Giờ Khởi Hành", "Giờ Hạ Cánh", "Thời Gian Bay"};
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
        header.setResizingAllowed(true);  // cho phép resize cột nếu cần

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
                    controller.searchFlights(searchQuery.toLowerCase());
                }
            }
        });
        searchPanel.add(searchField);

        // Action Buttons
        JButton addButton = createModernButton("Thêm", new Color(46, 204, 113));
        JButton updateButton = createModernButton("Cập Nhật", new Color(52, 152, 219));
        JButton deleteButton = createModernButton("Xóa", new Color(231, 76, 60));

        addButton.addActionListener(e -> handleAddFlight());
        updateButton.addActionListener(e -> handleUpdateFlight());
        deleteButton.addActionListener(e -> handleDeleteFlight());

        actionPanel.add(searchPanel);
        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        return actionPanel;
    }

    // UI Component Helpers
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

    // Event Handlers
    private void handleAddFlight() {
        String selectedFlightCode = (String) flightCodeComboBox.getSelectedItem();
        if (selectedFlightCode == null || selectedFlightCode.trim().isEmpty()) {
            showNotification("Vui lòng chọn mã chuyến bay", NotificationType.ERROR);
            return;
        }
    
        LichBay flight = controller.createFlightFromInput(
            selectedFlightCode,
            getTextOrPlaceholder(departureTimeField),
            getTextOrPlaceholder(arrivalTimeField),
            getTextOrPlaceholder(flightDurationField)
        );
        
        if (flight != null) {
            controller.addFlight(flight);
            updateFlightCodeComboBox(); // Cập nhật lại danh sách sau khi thêm
        }
    }

    private void handleUpdateFlight() {
        String selectedFlightCode = (String) flightCodeComboBox.getSelectedItem();
        if (selectedFlightCode == null || selectedFlightCode.trim().isEmpty()) {
            showNotification("Vui lòng chọn mã chuyến bay", NotificationType.ERROR);
            return;
        }
    
        LichBay flight = controller.createFlightFromInput(
            selectedFlightCode,
            getTextOrPlaceholder(departureTimeField),
            getTextOrPlaceholder(arrivalTimeField),
            getTextOrPlaceholder(flightDurationField)
        );
        
        if (flight != null) {
            controller.updateFlight(flight);
            updateFlightCodeComboBox(); // Cập nhật lại danh sách sau khi cập nhật
        }
    }

    private void handleDeleteFlight() {
        String flightCode = getTextOrPlaceholder(flightCodeField);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn xóa lịch bay này?", 
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deleteFlight(flightCode);
        }
    }

    // Helper Methods
    private String getTextOrPlaceholder(JTextField field) {
        String text = field.getText();
        String placeholder = (String) field.getClientProperty("placeholder");
        return text.equals(placeholder) ? "" : text;
    }

    public void updateFieldsFromSelection(int selectedRow) {
        String flightCode = tableModel.getValueAt(selectedRow, 0).toString();
        flightCodeComboBox.setSelectedItem(flightCode);
        departureTimeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        arrivalTimeField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        updateDuration();
    }

    public void updateTableData(List<LichBay> flightList) {
        tableModel.setRowCount(0);
        for (LichBay flight : flightList) {
            tableModel.addRow(new Object[]{
                flight.getMaChuyenBay(),
                flight.getGioKhoiHanh(),
                flight.getGioHaCanh(),
                flight.getThoiGianBay()
            });
        }
    }

    public void clearFields() {
        flightCodeComboBox.setSelectedIndex(-1);
        departureTimeField.setText((String) departureTimeField.getClientProperty("placeholder"));
        arrivalTimeField.setText((String) arrivalTimeField.getClientProperty("placeholder"));
        flightDurationField.setText((String) flightDurationField.getClientProperty("placeholder"));
        searchField.setText((String) searchField.getClientProperty("placeholder"));
    
        // Reset text colors
        for (JTextField field : new JTextField[]{departureTimeField, 
                                               arrivalTimeField, 
                                               flightDurationField, 
                                               searchField}) {
            field.setForeground(Color.GRAY);
        }
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
}