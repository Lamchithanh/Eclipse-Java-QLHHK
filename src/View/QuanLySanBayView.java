package View;

import Controller.SanBayController;
import Model.SanBay;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

@SuppressWarnings("unused")
public class QuanLySanBayView extends JPanel {
    private DefaultTableModel tableModel;
    private JTextField maSanBayField;
    private JTextField tenSanBayField;
    private JTextField searchField;
    private JTable table;
    private SanBayController controller;
    
    // Colors matching ThongKePanel
    private Color primaryColor = new Color(41, 128, 185);
    private Color accentColor = new Color(52, 152, 219);
    private Color backgroundColor = new Color(236, 240, 241);

    public QuanLySanBayView(TrangChuPanel trangChuPanel) {
        controller = new SanBayController(this);
        setLayout(new BorderLayout(0, 0));
        setBackground(backgroundColor);

        // Modern Header
        JPanel headerPanel = createModernHeader();
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainContent = new JPanel(new BorderLayout(10, 10));
        mainContent.setBackground(backgroundColor);
        mainContent.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create Input Panel with Modern Design
        JPanel inputPanel = createModernInputPanel();
        
        // Create Table Panel with Enhanced Design
        JPanel tablePanel = createModernTablePanel();

        // Add components to main content
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, tablePanel);
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);
        mainContent.add(splitPane, BorderLayout.CENTER);

        // Action Panel
        JPanel actionPanel = createModernActionPanel();
        mainContent.add(actionPanel, BorderLayout.SOUTH);

        add(mainContent, BorderLayout.CENTER);
        
        controller.updateSanBayList();
    }

    private JPanel createModernHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel headerLabel = new JLabel("Quản Lý Sân Bay");
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

        // Create modern text fields
        maSanBayField = createModernTextField("Nhập mã sân bay...");
        tenSanBayField = createModernTextField("Nhập tên sân bay...");
        searchField = createModernTextField("Tìm kiếm...");

        // Add search field with realtime search functionality
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText().toLowerCase();
                if (searchQuery.equals("tìm kiếm...")) searchQuery = "";
                controller.searchSanBay(searchQuery);
            }
        });

        // Add fields with modern labels
        addModernInputField(inputPanel, "Mã Sân Bay", maSanBayField, gbc, 0);
        addModernInputField(inputPanel, "Tên Sân Bay", tenSanBayField, gbc, 1);
        // addModernInputField(inputPanel, "Tìm Kiếm", searchField, gbc, 2);

        return inputPanel;
    }

    private JPanel createModernTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"Mã Sân Bay", "Tên Sân Bay"};
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
        header.setResizingAllowed(true);

        // Add selection listener
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    maSanBayField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    tenSanBayField.setText(tableModel.getValueAt(selectedRow, 1).toString());
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
        searchPanel.add(searchField);

        // Action Buttons
        JButton addButton = createModernButton("Thêm", new Color(46, 204, 113));
        JButton updateButton = createModernButton("Cập Nhật", accentColor);
        JButton deleteButton = createModernButton("Xóa", new Color(231, 76, 60));

        addButton.addActionListener(e -> controller.addSanBay());
        updateButton.addActionListener(e -> controller.updateSanBay());
        deleteButton.addActionListener(e -> controller.deleteSanBay());

        actionPanel.add(searchPanel);
        actionPanel.add(addButton);
        actionPanel.add(updateButton);
        actionPanel.add(deleteButton);

        return actionPanel;
    }

    private JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Add placeholder functionality
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

    private void addModernInputField(JPanel panel, String labelText, JTextField field, GridBagConstraints gbc, int row) {
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

    // Getter methods for controller
    public String getMaSanBayText() {
        String text = maSanBayField.getText();
        return text.equals(maSanBayField.getClientProperty("placeholder")) ? "" : text;
    }

    public String getTenSanBayText() {
        String text = tenSanBayField.getText();
        return text.equals(tenSanBayField.getClientProperty("placeholder")) ? "" : text;
    }

    @SuppressWarnings("exports")
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    @SuppressWarnings("exports")
    public JTable getTable() {
        return table;
    }

    public void clearFields() {
        maSanBayField.setText((String)maSanBayField.getClientProperty("placeholder"));
        maSanBayField.setForeground(Color.GRAY);
        tenSanBayField.setText((String)tenSanBayField.getClientProperty("placeholder"));
        tenSanBayField.setForeground(Color.GRAY);
        searchField.setText((String)searchField.getClientProperty("placeholder"));
        searchField.setForeground(Color.GRAY);
    }
}