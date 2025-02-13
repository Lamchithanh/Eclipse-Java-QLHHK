package View;

import Model.ChuyenBay;
import Service.ChuyenBayService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Database.MYSQLDB;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class QuanLyChuyenBay extends JPanel {

    private DefaultTableModel tableModel;
    private ChuyenBayService chuyenBayService;
    private JTextField maChuyenBayField, changBayField, ngayBayField, sanBayField, nhaGaField, soGheField, tinhTrangField, searchField;
    private JComboBox<String> maMayBayComboBox, maHangComboBox; 
    private JTable chuyenBayTable;

    public QuanLyChuyenBay(TrangChuPanel trangChuPanel) {
        chuyenBayService = new ChuyenBayService();
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

        // Load dữ liệu chuyến bay từ cơ sở dữ liệu
        loadFlightsFromDatabase();

        // Xử lý sự kiện 
        addActionListeners(); 
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
        maChuyenBayField = createStyledTextField();
        inputSearchPanel.add(maChuyenBayField, gbc);

        // Chặng Bay
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Chặng Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        changBayField = createStyledTextField();
        inputSearchPanel.add(changBayField, gbc);

        // Ngày Bay
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Ngày Bay (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        ngayBayField = createStyledTextField();
        inputSearchPanel.add(ngayBayField, gbc);

        // Sân Bay
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Sân Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        sanBayField = createStyledTextField();
        inputSearchPanel.add(sanBayField, gbc);

        // Nhà Ga
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Nhà Ga:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        nhaGaField = createStyledTextField();
        inputSearchPanel.add(nhaGaField, gbc);

        // Số Ghế
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Số Ghế:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        soGheField = createStyledTextField();
        inputSearchPanel.add(soGheField, gbc);

        // Tình Trạng
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tình Trạng:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        tinhTrangField = createStyledTextField();
        inputSearchPanel.add(tinhTrangField, gbc);

        // Mã Máy Bay (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 7; 
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Máy Bay:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maMayBayComboBox = new JComboBox<>(getMaMayBayData()); 
        inputSearchPanel.add(maMayBayComboBox, gbc);

        // Mã Hãng (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 8; 
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Mã Hãng:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        maHangComboBox = new JComboBox<>(getMaHangData()); 
        inputSearchPanel.add(maHangComboBox, gbc);

        // Search Field
        gbc.gridx = 0;
        gbc.gridy = 10; 
        gbc.weightx = 0.3;
        inputSearchPanel.add(createStyledLabel("Tìm Kiếm:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        searchField = createStyledTextField();
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String searchQuery = searchField.getText().toLowerCase();
                searchFlight(searchQuery); 
            }
        });
        inputSearchPanel.add(searchField, gbc);

        // Table
        chuyenBayTable = createStyledTable(); 
        JScrollPane scrollPane = new JScrollPane(chuyenBayTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 0, 10, 0), 
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1) 
        ));

        // Main Content Layout
        mainPanel.add(inputSearchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER); 

        return mainPanel;
    }

	private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 250)); 

        JButton addButton = createStyledButton("Thêm");
        JButton updateButton = createStyledButton("Cập Nhật");
        JButton deleteButton = createStyledButton("Xóa");

        addButton.addActionListener(e -> addFlight()); 
        updateButton.addActionListener(e -> updateFlight());
        addButton.addActionListener(e -> selectFlight()); 
        updateButton.addActionListener(e -> selectFlight()); 
        deleteButton.addActionListener(e -> deleteFlight()); 

        bottomPanel.add(addButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);

        return bottomPanel;
    }

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
        String[] columnNames = {"Mã Chuyến Bay", "Chặng Bay", "Ngày Bay", "Sân Bay", "Nhà Ga", "Số Ghế", "Tình Trạng", "Mã Máy Bay", "Mã Hãng"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(200, 220, 255));

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    maChuyenBayField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                    changBayField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                    ngayBayField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                    sanBayField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                    nhaGaField.setText(tableModel.getValueAt(selectedRow, 4).toString());
                    soGheField.setText(tableModel.getValueAt(selectedRow, 5).toString());
                    tinhTrangField.setText(tableModel.getValueAt(selectedRow, 6).toString());
                    maMayBayComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 7));
                    maHangComboBox.setSelectedItem(tableModel.getValueAt(selectedRow, 8));
                    // ... get data for other fields ...
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
        maChuyenBayField.setText("");
        changBayField.setText("");
        ngayBayField.setText("");
        sanBayField.setText("");
        nhaGaField.setText("");
        soGheField.setText("");
        tinhTrangField.setText("");
        maMayBayComboBox.setSelectedIndex(0); 
        maHangComboBox.setSelectedIndex(0); 
        searchField.setText(""); 
    }

    private boolean isValidAirportCode(String airportCode) {
        return airportCode != null && airportCode.length() <= 10 && !airportCode.trim().isEmpty();
    }

    private void addActionListeners() {
        chuyenBayTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectFlight();
            }
        });
    }

    private String[] getMaMayBayData() {
        List<String> maMayBayList = new ArrayList<>();
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT MaMayBay FROM MayBay");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                maMayBayList.add(rs.getString("MaMayBay"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            // Handle the exception appropriately (e.g., show an error message)
        }
        return maMayBayList.toArray(new String[0]);
    }

    private String[] getMaHangData() {
        List<String> maHangList = new ArrayList<>();
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT MaHang FROM HangHangKhong");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                maHangList.add(rs.getString("MaHang"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
        return maHangList.toArray(new String[0]);
    }

    private String[] getSanBayData() {
        List<String> sanBayList = new ArrayList<>();
        try (Connection conn = MYSQLDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT DISTINCT TenSanBay FROM SanBay");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sanBayList.add(rs.getString("TenSanBay"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., show an error message)
        }
        return sanBayList.toArray(new String[0]);
    }


    private boolean isValidDate(String ngayBay) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.parse(ngayBay);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void loadFlightsFromDatabase() {
        List<ChuyenBay> flights = chuyenBayService.getAllChuyenBays();
        tableModel.setRowCount(0);
        for (ChuyenBay flight : flights) {
            tableModel.addRow(new Object[] { flight.getMaChuyenBay(), flight.getChangBay(), flight.getNgayBay(),
                    flight.getSanBay(), flight.getNhaGa(), flight.getSoGhe(), flight.getTinhTrang(),
                    flight.getMaMaybay(), flight.getMaHang() });
        }
    }

    private void searchFlight(String searchQuery) {
        // Kiểm tra nếu searchQuery rỗng thì load lại toàn bộ danh sách
        if (searchQuery.trim().isEmpty()) {
            loadFlightsFromDatabase();
            return;
        }

        List<ChuyenBay> searchResults = new ArrayList<>();
        List<ChuyenBay> allFlights = chuyenBayService.getAllChuyenBays();

        // Tìm kiếm không phân biệt chữ hoa, chữ thường
        for (ChuyenBay flight : allFlights) {
            if (flight.getMaChuyenBay().toLowerCase().contains(searchQuery) ||
                flight.getChangBay().toLowerCase().contains(searchQuery) ||
                flight.getSanBay().toLowerCase().contains(searchQuery) ||
                flight.getNhaGa().toLowerCase().contains(searchQuery) ||
                flight.getMaHang().toLowerCase().contains(searchQuery)) {
                searchResults.add(flight);
            }
        }

        // Cập nhật bảng với kết quả tìm kiếm
        tableModel.setRowCount(0);
        for (ChuyenBay flight : searchResults) {
            tableModel.addRow(new Object[] { 
                flight.getMaChuyenBay(), flight.getChangBay(), flight.getNgayBay(),
                flight.getSanBay(), flight.getNhaGa(), flight.getSoGhe(), 
                flight.getTinhTrang(), flight.getMaMaybay(), flight.getMaHang() 
            });
        }
    }

    protected void selectFlight() {
        int selectedRow = chuyenBayTable.getSelectedRow();
        if (selectedRow >= 0) {
            maChuyenBayField.setText(tableModel.getValueAt(selectedRow, 0).toString());
            changBayField.setText(tableModel.getValueAt(selectedRow, 1).toString());
            ngayBayField.setText(tableModel.getValueAt(selectedRow, 2).toString());
            sanBayField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            nhaGaField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            soGheField.setText(tableModel.getValueAt(selectedRow, 5).toString());
            tinhTrangField.setText(tableModel.getValueAt(selectedRow, 6).toString());
            
            // Đảm bảo giá trị được chọn có tồn tại trong ComboBox
            String maMayBay = tableModel.getValueAt(selectedRow, 7).toString();
            String maHang = tableModel.getValueAt(selectedRow, 8).toString();
            String sanBay = tableModel.getValueAt(selectedRow, 3).toString();
            
            // Kiểm tra và đặt giá trị cho ComboBox
            if (Arrays.asList(getMaMayBayData()).contains(maMayBay)) {
                maMayBayComboBox.setSelectedItem(maMayBay);
            }
            
            if (Arrays.asList(getMaHangData()).contains(maHang)) {
                maHangComboBox.setSelectedItem(maHang);
            }
            
        }
    }

    private void deleteFlight() {
        String maChuyenBay = maChuyenBayField.getText().trim();
        
        if (maChuyenBay.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn chuyến bay để xóa", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa chuyến bay " + maChuyenBay + " không?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                chuyenBayService.deleteChuyenBay(maChuyenBay);
                loadFlightsFromDatabase(); // Tải lại danh sách sau khi xóa
                clearFields(); // Xóa các trường nhập liệu
                JOptionPane.showMessageDialog(this, 
                    "Đã xóa chuyến bay thành công", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi xóa chuyến bay: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
  
    
    private void addFlight() {
        // Kiểm tra tính hợp lệ của dữ liệu
        String maChuyenBay = maChuyenBayField.getText().trim();
        String changBay = changBayField.getText().trim();
        String ngayBay = ngayBayField.getText().trim();
        String sanBay = sanBayField.getText().trim();
        String nhaGa = nhaGaField.getText().trim();
        String soGhe = soGheField.getText().trim();
        String tinhTrang = tinhTrangField.getText().trim();
        String maMayBay = (String) maMayBayComboBox.getSelectedItem();
        String maHang = (String) maHangComboBox.getSelectedItem();


        // Kiểm tra các trường bắt buộc
        if (maChuyenBay.isEmpty() || changBay.isEmpty() || ngayBay.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Mã chuyến bay, chặng bay và ngày bay không được để trống", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra định dạng ngày
        if (!isValidDate(ngayBay)) {
            JOptionPane.showMessageDialog(this, 
                "Định dạng ngày bay không hợp lệ. Sử dụng định dạng yyyy-MM-dd", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra số ghế
        int soGheInt;
        try {
            soGheInt = Integer.parseInt(soGhe);
            if (soGheInt <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Số ghế phải là số nguyên dương", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng ChuyenBay
        ChuyenBay chuyenBay = new ChuyenBay(
            maChuyenBay, 
            sanBay, 
            changBay, 
            ngayBay, 
            nhaGa, 
            soGheInt, 
            tinhTrang, 
            maMayBay, 
            maHang
        );

        try {
            // Thêm chuyến bay
            chuyenBayService.addChuyenBay(chuyenBay);
            loadFlightsFromDatabase(); // Tải lại danh sách sau khi thêm
            clearFields(); // Xóa các trường nhập liệu

            JOptionPane.showMessageDialog(this, 
                "Thêm chuyến bay thành công", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi thêm chuyến bay: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateFlight() {
        // Kiểm tra tính hợp lệ của dữ liệu
        String maChuyenBay = maChuyenBayField.getText().trim();
        String changBay = changBayField.getText().trim();
        String ngayBay = ngayBayField.getText().trim();
        String sanBay = sanBayField.getText().trim();
        String nhaGa = nhaGaField.getText().trim();
        String soGhe = soGheField.getText().trim();
        String tinhTrang = tinhTrangField.getText().trim();
        String maMayBay = (String) maMayBayComboBox.getSelectedItem();
        String maHang = (String) maHangComboBox.getSelectedItem();

        // Kiểm tra mã chuyến bay để cập nhật
        if (maChuyenBay.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn chuyến bay để cập nhật", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra các trường bắt buộc
        if (changBay.isEmpty() || ngayBay.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Chặng bay và ngày bay không được để trống", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra định dạng ngày
        if (!isValidDate(ngayBay)) {
            JOptionPane.showMessageDialog(this, 
                "Định dạng ngày bay không hợp lệ. Sử dụng định dạng yyyy-MM-dd", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra số ghế
        int soGheInt;
        try {
            soGheInt = Integer.parseInt(soGhe);
            if (soGheInt <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Số ghế phải là số nguyên dương", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Tạo đối tượng ChuyenBay
        String quay = "Some Quay Value"; 
        ChuyenBay chuyenBay = new ChuyenBay(
            maChuyenBay, 
            sanBay, 
            changBay, 
            ngayBay, 
            nhaGa, 
            soGheInt, 
            tinhTrang, 
            maMayBay, 
            maHang
        );

        try {
            // Xóa bỏ kiểm tra updateSuccessful
            chuyenBayService.updateChuyenBay(chuyenBay);
            
            loadFlightsFromDatabase(); // Tải lại danh sách sau khi cập nhật
            clearFields(); // Xóa các trường nhập liệu

            JOptionPane.showMessageDialog(this, 
                "Cập nhật chuyến bay thành công", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi cập nhật chuyến bay: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}