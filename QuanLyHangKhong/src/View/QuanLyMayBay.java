package View;

import Model.MayBay;
import Service.MayBayService;
import Database.MYSQLDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class QuanLyMayBay extends JPanel {
    private DefaultTableModel tableModel;
    private MayBayService mayBayService;
    private JTextField maMayBayField;
    private JTextField loaiMayBayField;
    private JTextField sucChuaField;
    private JComboBox<String> maHangComboBox;
    private JTextField searchField;
    private JTable mayBayTable;

    public QuanLyMayBay(TrangChuPanel trangChuPanel) {
        mayBayService = new MayBayService();
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

        JLabel headerLabel = new JLabel("Quản Lý Máy Bay", SwingConstants.LEFT);
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

        // Mã Máy Bay
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Máy Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maMayBayField = createStyledTextField();
        inputSearchPanel.add(maMayBayField, gbc);

        // Loại Máy Bay
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Loại Máy Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        loaiMayBayField = createStyledTextField();
        inputSearchPanel.add(loaiMayBayField, gbc);

        // Sức Chứa
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Sức Chứa:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        sucChuaField = createStyledTextField();
        inputSearchPanel.add(sucChuaField, gbc);

        // Mã Hãng
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Hãng:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maHangComboBox = new JComboBox<>();
        populateMaHangComboBox();
        inputSearchPanel.add(maHangComboBox, gbc);

        // Search Field
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tìm Kiếm:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchField = createStyledTextField();
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText().toLowerCase();
                searchMayBay(searchQuery);
            }
        });
        inputSearchPanel.add(searchField, gbc);

        // Table
        mayBayTable = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(mayBayTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 10, 0),
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1)
        ));

        // Main Content Layout
        mainPanel.add(inputSearchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadMayBayFromDatabase();
        return mainPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 250));

        JButton addButton = createStyledButton("Thêm");
        JButton updateButton = createStyledButton("Cập Nhật");
        JButton deleteButton = createStyledButton("Xóa");

        // Button Actions
        addButton.addActionListener(e -> {
            try {
                if (validateInputFields()) {
                    String maMayBay = maMayBayField.getText().trim();

                    if (mayBayService.findMayBayByCode(maMayBay) == null) {
                        int sucChua = Integer.parseInt(sucChuaField.getText().trim());
                        MayBay mayBay = new MayBay(
                            maMayBay,
                            loaiMayBayField.getText().trim(),
                            sucChua,
                            maHangComboBox.getSelectedItem().toString()
                        );

                        mayBayService.addMayBay(mayBay);
                        loadMayBayFromDatabase();
                        JOptionPane.showMessageDialog(this, "Thêm máy bay thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Mã máy bay đã tồn tại.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm máy bay: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            try {
                if (validateInputFields()) {
                    String maMayBay = maMayBayField.getText().trim();

                    if (mayBayService.findMayBayByCode(maMayBay) != null) {
                        mayBayService.updateMayBay(
                            maMayBay,
                            loaiMayBayField.getText().trim(),
                            Integer.parseInt(sucChuaField.getText().trim()),
                            maHangComboBox.getSelectedItem().toString()
                        );
                        loadMayBayFromDatabase();
                        JOptionPane.showMessageDialog(this, "Cập nhật máy bay thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Máy bay không tồn tại.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật máy bay: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String maMayBay = maMayBayField.getText();
            if (maMayBay.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn máy bay để xóa");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa máy bay này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                mayBayService.deleteMayBay(maMayBay);
                loadMayBayFromDatabase();
                clearFields();
                JOptionPane.showMessageDialog(this, "Xóa máy bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        return bottomPanel;
    }

    // Helper Methods for Styled Components
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
        String[] columnNames = {"Mã Máy Bay", "Loại Máy Bay", "Sức Chứa", "Mã Hãng"};
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
                    maMayBayField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    loaiMayBayField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    sucChuaField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    maHangComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
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

    private void loadMayBayFromDatabase() {
        List<MayBay> mayBays = mayBayService.getAllMayBay();
        tableModel.setRowCount(0);

        for (MayBay mayBay : mayBays) {
            tableModel.addRow(new Object[]{
                mayBay.getMaMayBay(),
                mayBay.getLoaiMayBay(),
                mayBay.getSucChua(),
                mayBay.getMaHang()
            });
        }
        clearFields();
    }

    private void searchMayBay(String searchQuery) {
        List<MayBay> mayBays = mayBayService.getAllMayBay();
        tableModel.setRowCount(0);

        for (MayBay mayBay : mayBays) {
            if (mayBay.getMaMayBay().toLowerCase().contains(searchQuery) ||
                mayBay.getLoaiMayBay().toLowerCase().contains(searchQuery) ||
                mayBay.getMaHang().toLowerCase().contains(searchQuery)) {
                tableModel.addRow(new Object[]{
                    mayBay.getMaMayBay(),
                    mayBay.getLoaiMayBay(),
                    mayBay.getSucChua(),
                    mayBay.getMaHang()
                });
            }
        }
    }

    private void clearFields() {
        maMayBayField.setText("");
        maMayBayField.setEditable(true);
        loaiMayBayField.setText("");
        sucChuaField.setText("");
        maHangComboBox.setSelectedIndex(0);
        searchField.setText("");
    }

    private boolean validateInputFields() {
        if (maMayBayField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã máy bay không được để trống");
            return false;
        }

        if (loaiMayBayField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Loại máy bay không được để trống");
            return false;
        }

        if (sucChuaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Sức chứa không được để trống");
            return false;
        }

        try {
            int sucChua = Integer.parseInt(sucChuaField.getText().trim());
            if (sucChua <= 0) {
                JOptionPane.showMessageDialog(this, "Sức chứa phải là số dương");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Sức chứa phải là số nguyên");
            return false;
        }

        return true;
    }

    private void populateMaHangComboBox() {
        maHangComboBox.removeAllItems();
        Connection connection = MYSQLDB.getConnection();

        try {
            if (connection != null) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT MaHang FROM HangHangKhong");

                while (rs.next()) {
                    maHangComboBox.addItem(rs.getString("MaHang"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải mã hãng: " + e.getMessage());
        } finally {
            MYSQLDB.closeConnection(connection);
        }
    }
}
