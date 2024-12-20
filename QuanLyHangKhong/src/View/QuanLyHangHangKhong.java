package View;

import Model.HangHangKhong;
import Service.HangHangKhongService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class QuanLyHangHangKhong extends JPanel {
    private DefaultTableModel tableModel;
    private HangHangKhongService hangService;
    private JTextField maHangField;
    private JTextField tenHangField;
    private JTextField diaChiField;
    private JTextField soDienThoaiField;
    private JTextField emailField;
    private JTextField searchField;
    private JTable table; 

    public QuanLyHangHangKhong(TrangChuPanel trangChuPanel) {
        hangService = new HangHangKhongService();
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

        JLabel headerLabel = new JLabel("Quản Lý Hãng Hàng Không", SwingConstants.LEFT);
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

        // Mã Hãng
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3; 
        inputSearchPanel.add(createStyledLabel("Mã Hãng:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7; 
        maHangField = createStyledTextField();
        inputSearchPanel.add(maHangField, gbc);

        // Tên Hãng
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tên Hãng:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        tenHangField = createStyledTextField();
        inputSearchPanel.add(tenHangField, gbc);

        // Địa chỉ
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Địa chỉ:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        diaChiField = createStyledTextField();
        inputSearchPanel.add(diaChiField, gbc);

        // Số điện thoại
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Số điện thoại:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        soDienThoaiField = createStyledTextField();
        inputSearchPanel.add(soDienThoaiField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Email:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        emailField = createStyledTextField();
        inputSearchPanel.add(emailField, gbc);

        // Search Field
        gbc.gridx = 0;
        gbc.gridy = 5; 
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tìm Kiếm:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchField = createStyledTextField();
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText().toLowerCase();
                searchHang(searchQuery);
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

        updateHangList(); 
        return mainPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
        bottomPanel.setBackground(new Color(240, 240, 250)); 

        JButton addButton = createStyledButton("Thêm");
        JButton updateButton = createStyledButton("Cập Nhật");
        JButton deleteButton = createStyledButton("Xóa");

        addButton.addActionListener(e -> {
            String maHang = maHangField.getText();

            // Kiểm tra trùng mã hãng
            if (hangService.findHangHangKhongByCode(maHang) != null) {
                JOptionPane.showMessageDialog(this, "Mã hãng đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            HangHangKhong hang = new HangHangKhong(
                maHang,
                tenHangField.getText(),
                diaChiField.getText(),
                soDienThoaiField.getText(),
                emailField.getText()
            );
            if (hangService.addHangHangKhong(hang)) { // Check if adding was successful
                updateHangList();
                clearFields(); 
                JOptionPane.showMessageDialog(this, "Thêm hãng hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Thêm hãng hàng không không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            HangHangKhong hang = new HangHangKhong(
                maHangField.getText(),
                tenHangField.getText(),
                diaChiField.getText(),
                soDienThoaiField.getText(),
                emailField.getText()
            );
            if (hangService.updateHangHangKhong(hang)) { // Check if updating was successful
                updateHangList();
                clearFields(); 
                JOptionPane.showMessageDialog(this, "Cập nhật hãng hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật hãng hàng không không thành công!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String maHang = maHangField.getText();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa hãng hàng không này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                hangService.deleteHangHangKhong(maHang);
                updateHangList();
                clearFields(); // Clear input fields after deleting
                JOptionPane.showMessageDialog(this, "Xóa hãng hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
        String[] columnNames = {"Mã Hãng", "Tên Hãng", "Địa chỉ", "Số điện thoại", "Email"};
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
                    maHangField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    tenHangField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    diaChiField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    soDienThoaiField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    emailField.setText(tableModel.getValueAt(selectedRow, 4).toString());
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
        maHangField.setText("");
        tenHangField.setText("");
        diaChiField.setText("");
        soDienThoaiField.setText("");
        emailField.setText("");
        searchField.setText(""); 
    }

    private void updateHangList() {
        List<HangHangKhong> hangList = hangService.getAllHangHangKhong();
        tableModel.setRowCount(0); 
        for (HangHangKhong hang : hangList) {
            tableModel.addRow(new Object[] {
                hang.getMaHang(),
                hang.getTenHang(),
                hang.getDiaChi(),
                hang.getSoDienThoai(),
                hang.getEmail()
            });
        }
    }

    private void searchHang(String searchQuery) {
        List<HangHangKhong> hangList = hangService.getAllHangHangKhong();
        tableModel.setRowCount(0); 
        for (HangHangKhong hang : hangList) {
            if (hang.getMaHang().toLowerCase().contains(searchQuery) || 
                hang.getTenHang().toLowerCase().contains(searchQuery) ||
                hang.getDiaChi().toLowerCase().contains(searchQuery) ||
                hang.getSoDienThoai().toLowerCase().contains(searchQuery) ||
                hang.getEmail().toLowerCase().contains(searchQuery)) { 
                tableModel.addRow(new Object[] {
                    hang.getMaHang(),
                    hang.getTenHang(),
                    hang.getDiaChi(),
                    hang.getSoDienThoai(),
                    hang.getEmail()
                });
            }
        }
    }
}