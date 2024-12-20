package View;

import Database.MYSQLDB;
import Model.SanBay;
import Service.SanBayService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class QuanLySanBay extends JPanel {
    private DefaultTableModel tableModel;
    private SanBayService sanBayService;
    private JTextField maSanBayField;
    private JTextField tenSanBayField;
    private JTextField searchField;
    private JTable table;

    public QuanLySanBay(TrangChuPanel trangChuPanel) {
        sanBayService = new SanBayService();
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 250));

        // Top Panel with Header
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainContentPanel = createMainContentPanel();
        add(mainContentPanel, BorderLayout.CENTER);

        // Bottom Panel with Actions
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel headerLabel = new JLabel("Quản Lý Sân Bay", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(40, 40, 90));

        topPanel.add(headerLabel, BorderLayout.WEST);
        return topPanel;
    }

    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Input and Search Panel
        JPanel inputSearchPanel = new JPanel(new GridBagLayout());
        inputSearchPanel.setBackground(new Color(240, 240, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã Sân Bay
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Sân Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maSanBayField = createStyledTextField();
        inputSearchPanel.add(maSanBayField, gbc);

        // Tên Sân Bay
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tên Sân Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        tenSanBayField = createStyledTextField();
        inputSearchPanel.add(tenSanBayField, gbc);

        // Search Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tìm Kiếm:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchField = createStyledTextField();
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText().toLowerCase();
                searchSanBay(searchQuery);
            }
        });
        inputSearchPanel.add(searchField, gbc);

        // Table
        table = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 10, 0),
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1)
        ));

        // Main Content Layout
        mainPanel.add(inputSearchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        updateSanBayList();
        return mainPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 250));

        JButton addButton = createStyledButton("Thêm");
        JButton updateButton = createStyledButton("Cập Nhật");
        JButton deleteButton = createStyledButton("Xóa");

        // Button Actions (same as previous implementation)
        addButton.addActionListener(e -> {
            String maSanBay = maSanBayField.getText();
            String tenSanBay = tenSanBayField.getText();

            if (sanBayService.isMaSanBayExists(maSanBay)) {
                JOptionPane.showMessageDialog(this, "Mã sân bay đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SanBay sanBay = new SanBay(maSanBay, tenSanBay);
            sanBayService.addSanBay(sanBay);
            updateSanBayList();
            clearFields();
            JOptionPane.showMessageDialog(this, "Thêm sân bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        updateButton.addActionListener(e -> {
            String maSanBay = maSanBayField.getText();
            String tenSanBay = tenSanBayField.getText();

            SanBay sanBay = new SanBay(maSanBay, tenSanBay);
            sanBayService.updateSanBay(sanBay);
            updateSanBayList();
            clearFields();
            JOptionPane.showMessageDialog(this, "Cập nhật sân bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        deleteButton.addActionListener(e -> {
            String maSanBay = maSanBayField.getText();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sân bay này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                sanBayService.deleteSanBay(maSanBay);
                updateSanBayList();
                clearFields();
                JOptionPane.showMessageDialog(this, "Xóa sân bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        return bottomPanel;
    }

    // Styled Components Helpers
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(40, 40, 90));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }

    private JTable createStyledTable() {
        String[] columnNames = {"Mã Sân Bay", "Tên Sân Bay"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        
        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Table Row Styling
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(200, 220, 255));

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

        // Hover effect
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

    private void clearFields() {
        maSanBayField.setText("");
        tenSanBayField.setText("");
        searchField.setText("");
    }

    // Existing methods remain the same
    private void updateSanBayList() {
        List<SanBay> sanBayList = sanBayService.getAllSanBay();
        tableModel.setRowCount(0);
        for (SanBay sanBay : sanBayList) {
            tableModel.addRow(new Object[] {
                sanBay.getMaSanBay(),
                sanBay.getTenSanBay()
            });
        }
    }

    private void searchSanBay(String searchQuery) {
        List<SanBay> sanBayList = sanBayService.getAllSanBay();
        tableModel.setRowCount(0);
        for (SanBay sanBay : sanBayList) {
            if (sanBay.getMaSanBay().toLowerCase().contains(searchQuery) || 
                sanBay.getTenSanBay().toLowerCase().contains(searchQuery)) {
                tableModel.addRow(new Object[] {
                    sanBay.getMaSanBay(),
                    sanBay.getTenSanBay()
                });
            }
        }
    }
}