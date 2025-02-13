package View;

import Model.VeMayBay;
import Service.VeMayBayService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class QuanLyVeMayBay extends JPanel {
    private DefaultTableModel tableModel;
    private VeMayBayService veMayBayService;
    private JTextField maVeField;
    private JTextField maChuyenBayField;
    private JTextField maKhachHangField;
    private JTextField tenKhachHangField;
    private JTextField cmndField;
    private JTextField ngayDatField;
    private JTextField searchField;
    private JTable table;

    public QuanLyVeMayBay(TrangChuPanel trangChuPanel) {
        veMayBayService = new VeMayBayService();
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

        JLabel headerLabel = new JLabel("Quản Lý Vé Máy Bay", SwingConstants.LEFT);
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

        // Mã Vé
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Vé:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maVeField = createStyledTextField();
        inputSearchPanel.add(maVeField, gbc);

        // Mã Chuyến Bay
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Chuyến Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maChuyenBayField = createStyledTextField();
        inputSearchPanel.add(maChuyenBayField, gbc);

        // Mã Khách Hàng
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Khách Hàng:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maKhachHangField = createStyledTextField();
        inputSearchPanel.add(maKhachHangField, gbc);

        // Tên Khách Hàng
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tên Khách Hàng:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        tenKhachHangField = createStyledTextField();
        inputSearchPanel.add(tenKhachHangField, gbc);

        // CMND
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("CMND:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cmndField = createStyledTextField();
        inputSearchPanel.add(cmndField, gbc);

        // Ngày Đặt
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Ngày Đặt (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ngayDatField = createStyledTextField();
        inputSearchPanel.add(ngayDatField, gbc);

        // Search Field
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tìm Kiếm:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchField = createStyledTextField();
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText().toLowerCase();
                searchTickets(searchQuery);
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

        updateTicketList();
        return mainPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 250));

        JButton addButton = createStyledButton("Thêm");
        JButton updateButton = createStyledButton("Cập Nhật");
        JButton deleteButton = createStyledButton("Xóa");

        addButton.addActionListener(e -> {
            try {
                VeMayBay ticket = createVeMayBayFromInput();

                // Kiểm tra trùng mã vé
                if (veMayBayService.isTicketExists(ticket.getMaVe())) {
                    JOptionPane.showMessageDialog(this, "Mã vé đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                addTicket(ticket); 
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            try {
                VeMayBay ticket = createVeMayBayFromInput();
                updateTicket(ticket); 
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            String maVe = maVeField.getText();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa vé này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteTicket(maVe);
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
        String[] columnNames = {"Mã Vé", "Mã Chuyến Bay", "Mã Khách Hàng", "Tên Khách Hàng", "CMND", "Ngày Đặt"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

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
                    maVeField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    maChuyenBayField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    maKhachHangField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    tenKhachHangField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    cmndField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    ngayDatField.setText(tableModel.getValueAt(selectedRow, 5).toString());
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
        maVeField.setText("");
        maChuyenBayField.setText("");
        maKhachHangField.setText("");
        tenKhachHangField.setText("");
        cmndField.setText("");
        ngayDatField.setText("");
        searchField.setText("");
    }


    private VeMayBay createVeMayBayFromInput() throws ParseException {
        String maVe = maVeField.getText();
        String maChuyenBay = maChuyenBayField.getText();
        String maKhachHang = maKhachHangField.getText();
        String tenKhachHang = tenKhachHangField.getText();
        String cmnd = cmndField.getText();

        // Parse date
        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(ngayDatField.getText());
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

        return new VeMayBay(maVe, maChuyenBay, maKhachHang, sqlDate, tenKhachHang, cmnd);
    }

    private void updateTicketList() {
        List<VeMayBay> veMayBayList = veMayBayService.getAllVeMayBay();
        tableModel.setRowCount(0); 
        for (VeMayBay veMayBay : veMayBayList) {
            tableModel.addRow(new Object[]{
                veMayBay.getMaVe(),
                veMayBay.getMaChuyenBay(),
                veMayBay.getMaKhachHang(),
                veMayBay.getTenKhachHang(),
                veMayBay.getCmnd(),
                veMayBay.getNgayDat()
            });
        }
    }

    private void addTicket(VeMayBay ticket) {
        boolean isAdded = veMayBayService.addVeMayBay(ticket);
        if (isAdded) {
            updateTicketList();
            clearFields();
            JOptionPane.showMessageDialog(this, "Thêm vé máy bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Không thể thêm vé máy bay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTicket(VeMayBay ticket) {
        boolean isUpdated = veMayBayService.updateVeMayBay(ticket);
        if (isUpdated) {
            updateTicketList();
            clearFields();
            JOptionPane.showMessageDialog(this, "Cập nhật vé máy bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Không thể cập nhật vé máy bay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTicket(String maVe) {
        boolean isDeleted = veMayBayService.deleteVeMayBay(maVe);
        if (isDeleted) {
            updateTicketList();
            clearFields();
            JOptionPane.showMessageDialog(this, "Xóa vé máy bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Không thể xóa vé máy bay!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchTickets(String searchQuery) {
        List<VeMayBay> filteredList = veMayBayService.searchVeMayBay(searchQuery);
        tableModel.setRowCount(0); 
        for (VeMayBay ticket : filteredList) {
            tableModel.addRow(new Object[]{
                ticket.getMaVe(),
                ticket.getMaChuyenBay(),
                ticket.getMaKhachHang(),
                ticket.getTenKhachHang(),
                ticket.getCmnd(),
                ticket.getNgayDat()
            });
        }
    }
}