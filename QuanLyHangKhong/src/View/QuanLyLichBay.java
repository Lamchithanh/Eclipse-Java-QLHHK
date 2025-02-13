package View;

import Model.LichBay;
import Service.LichBayService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuanLyLichBay extends JPanel {
    private DefaultTableModel tableModel;
    private LichBayService lichBayService;
    private JTextField flightCodeField;
    private JTextField departureTimeField;
    private JTextField arrivalTimeField;
    private JTextField flightDurationField;
    private JTextField searchField;
    private JTable table;

    public QuanLyLichBay(TrangChuPanel trangChuPanel) {
        lichBayService = new LichBayService();
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

        JLabel headerLabel = new JLabel("Quản Lý Lịch Bay", SwingConstants.LEFT);
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

        // Mã Chuyến Bay
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Chuyến Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        flightCodeField = createStyledTextField();
        inputSearchPanel.add(flightCodeField, gbc);

        // Giờ Khởi Hành
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Giờ Khởi Hành:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        departureTimeField = createStyledTextField();
        inputSearchPanel.add(departureTimeField, gbc);

        // Giờ Hạ Cánh
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Giờ Hạ Cánh:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        arrivalTimeField = createStyledTextField();
        inputSearchPanel.add(arrivalTimeField, gbc);

        // Thời Gian Bay
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Thời Gian Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        flightDurationField = createStyledTextField();
        inputSearchPanel.add(flightDurationField, gbc);

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
                searchFlights(searchQuery);
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

        updateFlightList();
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
            String flightCode = flightCodeField.getText();
            String departureTime = departureTimeField.getText();
            String arrivalTime = arrivalTimeField.getText();
            int flightDuration = Integer.parseInt(flightDurationField.getText());

            // Kiểm tra trùng mã chuyến bay và trùng giờ khởi hành hoặc hạ cánh
            if (lichBayService.isDepartureOrArrivalTimeConflict(flightCode, departureTime, arrivalTime)) {
                JOptionPane.showMessageDialog(this, "Giờ khởi hành hoặc giờ hạ cánh đã trùng với lịch bay hiện có!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra giờ khởi hành trước giờ hạ cánh
            if (!isValidFlightTime(departureTime, arrivalTime)) {
                JOptionPane.showMessageDialog(this, "Giờ khởi hành phải sớm hơn giờ hạ cánh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LichBay lichBay = new LichBay(flightCode, departureTime, arrivalTime, flightDuration);
            lichBayService.addLichBay(lichBay);
            updateFlightList();
            clearFields();
            JOptionPane.showMessageDialog(this, "Thêm lịch bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        updateButton.addActionListener(e -> {
            String flightCode = flightCodeField.getText();
            String departureTime = departureTimeField.getText();
            String arrivalTime = arrivalTimeField.getText();
            int flightDuration = Integer.parseInt(flightDurationField.getText());

            // Kiểm tra giờ khởi hành trước giờ hạ cánh
            if (!isValidFlightTime(departureTime, arrivalTime)) {
                JOptionPane.showMessageDialog(this, "Giờ khởi hành phải sớm hơn giờ hạ cánh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LichBay lichBay = new LichBay(flightCode, departureTime, arrivalTime, flightDuration);
            lichBayService.updateLichBay(lichBay);
            updateFlightList();
            clearFields();
            JOptionPane.showMessageDialog(this, "Cập nhật lịch bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });

        deleteButton.addActionListener(e -> {
            String flightCode = flightCodeField.getText();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa lịch bay này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                lichBayService.deleteLichBay(flightCode);
                updateFlightList();
                clearFields();
                JOptionPane.showMessageDialog(this, "Xóa lịch bay thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
        String[] columnNames = {"Mã Chuyến Bay", "Giờ Khởi Hành", "Giờ Hạ Cánh", "Thời Gian Bay"};
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
                    flightCodeField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    departureTimeField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    arrivalTimeField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    flightDurationField.setText(tableModel.getValueAt(selectedRow, 3).toString());
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
        flightCodeField.setText("");
        departureTimeField.setText("");
        arrivalTimeField.setText("");
        flightDurationField.setText("");
        searchField.setText("");
    }

    // Existing methods with styling-related updates
    private void updateFlightList() {
        List<LichBay> lichBayList = lichBayService.getAllLichBay();
        tableModel.setRowCount(0);
        for (LichBay lichBay : lichBayList) {
            tableModel.addRow(new Object[] {
                lichBay.getMaChuyenBay(),
                lichBay.getGioKhoiHanh(),
                lichBay.getGioHaCanh(),
                lichBay.getThoiGianBay()
            });
        }
    }

    private void searchFlights(String searchQuery) {
        List<LichBay> lichBayList = lichBayService.getAllLichBay();
        tableModel.setRowCount(0);
        for (LichBay lichBay : lichBayList) {
            if (lichBay.getMaChuyenBay().toLowerCase().contains(searchQuery) ||
                lichBay.getGioKhoiHanh().toLowerCase().contains(searchQuery) ||
                lichBay.getGioHaCanh().toLowerCase().contains(searchQuery)) {
                tableModel.addRow(new Object[] {
                    lichBay.getMaChuyenBay(),
                    lichBay.getGioKhoiHanh(),
                    lichBay.getGioHaCanh(),
                    lichBay.getThoiGianBay()
                });
            }
        }
    }

    // Existing time validation method
    private boolean isValidFlightTime(String departureTime, String arrivalTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        try {
            Date depTime = sdf.parse(departureTime);
            Date arrTime = sdf.parse(arrivalTime);
            return depTime.before(arrTime);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng giờ không hợp lệ. Vui lòng nhập giờ theo định dạng HH:mm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
