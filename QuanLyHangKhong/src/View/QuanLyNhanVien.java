package View;

import View.TrangChuPanel;
import Model.NhanVien;
import Service.NhanVienService;
import Database.MYSQLDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class QuanLyNhanVien extends JPanel {
    private DefaultTableModel tableModel;
    private NhanVienService nhanVienService;
    private JTextField maNhanVienField;
    private JTextField tenNhanVienField;
    private JTextField chucVuField;
    private JComboBox<String> maHangComboBox;
    private JTable nhanVienTable;
    private JTextField searchField;

    public QuanLyNhanVien(TrangChuPanel trangChuPanel) {
        nhanVienService = new NhanVienService();
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

        JLabel headerLabel = new JLabel("Quản Lý Nhân Viên", SwingConstants.LEFT);
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

        // Mã Nhân Viên
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Nhân Viên:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maNhanVienField = createStyledTextField();
        inputSearchPanel.add(maNhanVienField, gbc);

        // Tên Nhân Viên
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tên Nhân Viên:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        tenNhanVienField = createStyledTextField();
        inputSearchPanel.add(tenNhanVienField, gbc);

        // Chức Vụ
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Chức Vụ:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        chucVuField = createStyledTextField();
        inputSearchPanel.add(chucVuField, gbc);

        // Mã Hãng
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Hãng:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maHangComboBox = createStyledComboBox();
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
                searchNhanVien(searchQuery);
            }
        });
        inputSearchPanel.add(searchField, gbc);

        // Table
        nhanVienTable = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(nhanVienTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 0, 10, 0),
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1)
        ));

        // Main Content Layout
        mainPanel.add(inputSearchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        loadNhanVienFromDatabase();
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
                    String maNhanVien = maNhanVienField.getText().trim();

                    if (nhanVienService.findNhanVienByCode(maNhanVien) == null) {
                        NhanVien nhanVien = new NhanVien(
                            maNhanVien,
                            tenNhanVienField.getText().trim(),
                            chucVuField.getText().trim(),
                            maHangComboBox.getSelectedItem().toString()
                        );

                        nhanVienService.addNhanVien(nhanVien);
                        loadNhanVienFromDatabase();
                        JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi thêm nhân viên: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        updateButton.addActionListener(e -> {
            try {
                if (validateInputFields()) {
                    String maNhanVien = maNhanVienField.getText().trim();

                    if (maNhanVien.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để cập nhật", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    NhanVien existingNhanVien = nhanVienService.findNhanVienByCode(maNhanVien);
                    if (existingNhanVien != null) {
                        nhanVienService.updateNhanVien(
                            maNhanVien,
                            tenNhanVienField.getText().trim(),
                            chucVuField.getText().trim(),
                            maHangComboBox.getSelectedItem().toString()
                        );

                        loadNhanVienFromDatabase();
                        JOptionPane.showMessageDialog(this, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Mã nhân viên không tồn tại trong cơ sở dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật nhân viên: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            if (maNhanVienField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                String maNhanVien = maNhanVienField.getText();
                nhanVienService.deleteNhanVien(maNhanVien);
                loadNhanVienFromDatabase();
                clearInputFields();
                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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

    private JComboBox<String> createStyledComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return comboBox;
    }

    private JTable createStyledTable() {
        String[] columnNames = {"Mã Nhân Viên", "Tên Nhân Viên", "Chức Vụ", "Mã Hãng"};
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
                    maNhanVienField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    tenNhanVienField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    chucVuField.setText(tableModel.getValueAt(selectedRow, 2).toString());
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

    // Existing methods remain the same as in the previous implementation
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

    private void loadNhanVienFromDatabase() {
        List<NhanVien> nhanViens = nhanVienService.getAllNhanVien();
        tableModel.setRowCount(0);  // Xóa dữ liệu cũ trong bảng

        for (NhanVien nhanVien : nhanViens) {
            tableModel.addRow(new Object[]{
                nhanVien.getMaNhanVien(),
                nhanVien.getTenNhanVien(),
                nhanVien.getChucVu(),
                nhanVien.getMaHang()
            });
        }
        
        // Đặt lại các trường nhập liệu sau khi tải lại dữ liệu
        clearInputFields();
    }

    private void searchNhanVien(String searchQuery) {
        List<NhanVien> nhanViens = nhanVienService.getAllNhanVien();
        tableModel.setRowCount(0);  // Xóa các hàng hiện tại

        // Duyệt qua danh sách các nhân viên và lọc theo từ khóa
        for (NhanVien nhanVien : nhanViens) {
            if (nhanVien.getMaNhanVien().toLowerCase().contains(searchQuery) ||
                nhanVien.getTenNhanVien().toLowerCase().contains(searchQuery) ||
                nhanVien.getChucVu().toLowerCase().contains(searchQuery) ||
                nhanVien.getMaHang().toLowerCase().contains(searchQuery)) {
                tableModel.addRow(new Object[] {
                    nhanVien.getMaNhanVien(),
                    nhanVien.getTenNhanVien(),
                    nhanVien.getChucVu(),
                    nhanVien.getMaHang()
                });
            }
        }
    }

    // Phương thức xóa các trường nhập liệu
    private void clearInputFields() {
        maNhanVienField.setText("");
        maNhanVienField.setEditable(true);
        tenNhanVienField.setText("");
        chucVuField.setText("");
        maHangComboBox.setSelectedIndex(0);
    }

    // Phương thức kiểm tra tính hợp lệ của các trường nhập liệu
    private boolean validateInputFields() {
        if (maNhanVienField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã nhân viên không được để trống");
            return false;
        }
        
        if (tenNhanVienField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhân viên không được để trống");
            return false;
        }
        
        if (chucVuField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chức vụ không được để trống");
            return false;
        }
        
        return true;
    }
}