package View;

import Controller.ThongKeController;
import Model.ThongKe;
import Model.UserAccount;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ThongKePanel extends JPanel {
    // Giao diện và điều khiển
    private ThongKeController controller;
    private UserAccount currentUser;

    // Các thành phần giao diện chính
    private JTabbedPane tabbedPane;
    private JPanel statisticsPanel;
    private JPanel chartPanel;

    // Thành phần nhập liệu
    private JTextField maChuyenBayField;
    private JTextField sanBayField;
    private JTextField changBayField;
    private JTextField ngayBayField;
    private JTextField soGheDaDatField;
    private JTextField tinhTrangField;
    private JTextField searchField;
    private JComboBox<String> filterBox;

    // Bảng và mô hình
    private JTable thongKeTable;
    private DefaultTableModel tableModel;

    // Biểu đồ
    private JPanel monthlyTicketSalesChart;
    private JPanel flightStatusChart;
    private JPanel flightOccupancyChart;

    // Màu sắc và phong cách
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color ACCENT_COLOR = new Color(52, 152, 219);
    private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
    private static final Color[] CHART_COLORS = {
        new Color(41, 128, 185),   // Xanh dương đậm
        new Color(52, 152, 219),   // Xanh dương sáng
        new Color(46, 204, 113),   // Xanh lá
        new Color(231, 76, 60),    // Đỏ
        new Color(241, 196, 15)    // Vàng
    };

    // Định dạng ngày
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public ThongKePanel(UserAccount user) {
        this.currentUser = user;
        this.controller = new ThongKeController(this, user);
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        // Tạo tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Tạo các panel chính
        createStatisticsPanel();
        createChartPanel();

        // Thêm các tab
        tabbedPane.addTab("Thống Kê Chi Tiết", statisticsPanel);
        tabbedPane.addTab("Phân Tích Biểu Đồ", chartPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Tải dữ liệu ban đầu
        controller.loadDataFromDatabase();
    }

    private void createStatisticsPanel() {
        statisticsPanel = new JPanel(new BorderLayout());
        statisticsPanel.setBackground(BACKGROUND_COLOR);

        // Panel nhập liệu
        JPanel inputPanel = createInputPanel();
        
        // Panel bảng
        JPanel tablePanel = createTablePanel();
        
        // Panel hành động
        JPanel actionPanel = createActionPanel();

        statisticsPanel.add(inputPanel, BorderLayout.NORTH);
        statisticsPanel.add(new JScrollPane(tablePanel), BorderLayout.CENTER);
        statisticsPanel.add(actionPanel, BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 15, 15, 15),
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tạo các trường nhập liệu
        maChuyenBayField = createTextField("Nhập mã chuyến bay...");
        sanBayField = createTextField("Nhập tên sân bay...");
        changBayField = createTextField("Nhập chặng bay...");
        ngayBayField = createTextField("DD/MM/YYYY");
        soGheDaDatField = createTextField("Số ghế...");
        tinhTrangField = createTextField("Tình trạng...");

        // Thêm các trường vào panel
        addInputField(inputPanel, "Mã Chuyến Bay", maChuyenBayField, gbc, 0);
        addInputField(inputPanel, "Sân Bay", sanBayField, gbc, 1);
        addInputField(inputPanel, "Chặng Bay", changBayField, gbc, 2);
        addInputField(inputPanel, "Ngày Bay", ngayBayField, gbc, 3);
        addInputField(inputPanel, "Số Ghế Đã Đặt", soGheDaDatField, gbc, 4);
        addInputField(inputPanel, "Tình Trạng", tinhTrangField, gbc, 5);

        return inputPanel;
    }

    private void addInputField(JPanel panel, String labelText, JTextField field, 
                                GridBagConstraints gbc, int row) {
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

    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Color.GRAY);
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });

        return field;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo mô hình bảng
        String[] columnNames = {"Mã CB", "Sân Bay", "Chặng Bay", "Ngày Bay", "Số Ghế ĐĐ", "Tình Trạng", "Tổng Số Ghế"};
        tableModel = new DefaultTableModel(columnNames, 0);
        
        // Tạo bảng
        thongKeTable = createConfiguredTable();
        
        // Thêm trình nghe lựa chọn
        thongKeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = thongKeTable.getSelectedRow();
                if (selectedRow >= 0) {
                    updateInputFieldsFromSelection(selectedRow);
                }
            }
        });

        // Thêm bảng vào scroll pane
        JScrollPane scrollPane = new JScrollPane(thongKeTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JTable createConfiguredTable() {
        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                }
                return comp;
            }
        };

        // Cấu hình giao diện bảng
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        // Cấu hình tiêu đề bảng
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));

        return table;
    }

    private void updateInputFieldsFromSelection(int selectedRow) {
        maChuyenBayField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        sanBayField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        changBayField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        ngayBayField.setText(tableModel.getValueAt(selectedRow, 3).toString());
        soGheDaDatField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        tinhTrangField.setText(tableModel.getValueAt(selectedRow, 5).toString());
    }

    private JPanel createActionPanel() {
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        actionPanel.setBackground(BACKGROUND_COLOR);

        // Panel tìm kiếm
        JPanel searchPanel = createSearchPanel();
        
        // Panel lọc
        JPanel filterPanel = createFilterPanel();

        // Nút hành động
        JButton updateButton = createButton("Cập Nhật", new Color(52, 152, 219));
        JButton exportButton = createButton("Xuất Báo Cáo", new Color(155, 89, 182));

        // Thêm các thành phần
        actionPanel.add(searchPanel);
        actionPanel.add(filterPanel);
        actionPanel.add(updateButton);
        actionPanel.add(exportButton);

        // Xử lý sự kiện
        updateButton.addActionListener(e -> controller.loadDataFromDatabase());
        exportButton.addActionListener(e -> controller.exportStatisticsToCSV());

        return actionPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(BACKGROUND_COLOR);
        
        searchField = createTextField("Tìm kiếm...");
        searchField.setPreferredSize(new Dimension(200, 35));
        
        JButton searchButton = createButton("Tìm Kiếm", new Color(52, 152, 219));
        searchButton.addActionListener(e -> controller.performSearch(searchField.getText()));
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        return searchPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(BACKGROUND_COLOR);
        
        filterBox = new JComboBox<>(new String[]{"Tất cả", "Sắp khởi hành", "Đã khởi hành", "Delay"});
        filterBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterBox.setPreferredSize(new Dimension(150, 35));
        
        JButton filterButton = createButton("Lọc", new Color(46, 204, 113));
        filterButton.addActionListener(e -> controller.filterData((String)filterBox.getSelectedItem()));
        
        filterPanel.add(filterBox);
        filterPanel.add(filterButton);
        
        return filterPanel;
    }

    private void createChartPanel() {
        chartPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        chartPanel.setBackground(BACKGROUND_COLOR);

        // Tạo các biểu đồ
        monthlyTicketSalesChart = createMonthlyTicketSalesChart();
        flightStatusChart = createFlightStatusChart();
        flightOccupancyChart = createFlightOccupancyChart();

        // Thêm biểu đồ vào panel
        chartPanel.add(createChartWrapper("Số Vé Bán Theo Tháng", monthlyTicketSalesChart));
        chartPanel.add(createChartWrapper("Phân Bổ Trạng Thái Chuyến Bay", flightStatusChart));
        chartPanel.add(createChartWrapper("Tỷ Lệ Lấp Đầy Chuyến Bay", flightOccupancyChart));
    }

    private JPanel createChartWrapper(String title, JPanel chartPanel) {
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        wrapperPanel.add(titleLabel, BorderLayout.NORTH);
        wrapperPanel.add(chartPanel, BorderLayout.CENTER);
        
        return wrapperPanel;
    }

    private JPanel createMonthlyTicketSalesChart() {
        return new JPanel() {
            private Map<String, Integer> monthlyData = new HashMap<>();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (monthlyData.isEmpty()) return;

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 40;

                // Tìm giá trị max
                int maxSales = monthlyData.values().stream().max(Integer::compare).orElse(1);

                // Vẽ trục
                g2d.setColor(Color.BLACK);
                g2d.drawLine(padding, height - padding, width - padding, height - padding);
                g2d.drawLine(padding, padding, padding, height - padding);

                // Vẽ các điểm dữ liệu
                int i = 0;
                int prevX = padding, prevY = height - padding;
                for (Map.Entry<String, Integer> entry : monthlyData.entrySet()) {
                    int x = padding + (i + 1) * (width - 2 * padding) / (monthlyData.size() + 1);
                    int y = height - padding - (entry.getValue() * (height - 2 * padding) / maxSales);

                    // Vẽ đường
                    g2d.setColor(CHART_COLORS[0]);
                    g2d.drawLine(prevX, prevY, x, y);

                    // Vẽ điểm
                    g2d.fillOval(x - 5, y - 5, 10, 10);

                    // Vẽ nhãn
                    g2d.drawString(entry.getKey(), x - 10, height - 10);

                    prevX = x;
                    prevY = y;
                    i++;
                }
            }

            public void updateData(Map<String, Integer> data) {
                this.monthlyData = data;
                repaint();
            }
        };
    }

    private JPanel createFlightStatusChart() {
        return new JPanel() {
            private Map<String, Integer> statusData = new HashMap<>();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (statusData.isEmpty()) return;

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int diameter = Math.min(width, height) - 100;
                int x = (width - diameter) / 2;
                int y = (height - diameter) / 2;

                // Tính tổng
                int total = statusData.values().stream().mapToInt(Integer::intValue).sum();

                // Vẽ các phần của biểu đồ tròn
                double startAngle = 0;
                int colorIndex = 0;
                for (Map.Entry<String, Integer> entry : statusData.entrySet()) {
                    double percentage = entry.getValue() / (double) total;
                    double sweepAngle = percentage * 360;

                    g2d.setColor(CHART_COLORS[colorIndex % CHART_COLORS.length]);
                    g2d.fillArc(x, y, diameter, diameter, (int)startAngle, (int)sweepAngle);

                    // Vẽ nhãn
                    double midAngle = Math.toRadians(startAngle + sweepAngle / 2);
                    int labelX = (int)(x + diameter / 2 + Math.cos(midAngle) * (diameter / 2 + 20));
                    int labelY = (int)(y + diameter / 2 + Math.sin(midAngle) * (diameter / 2 + 20));
                    g2d.drawString(entry.getKey() + " (" + entry.getValue() + ")", labelX, labelY);

                    startAngle += sweepAngle;
                    colorIndex++;
                }
            }

            public void updateData(Map<String, Integer> data) {
                this.statusData = data;
                repaint();
            }
        };
    }

    private JPanel createFlightOccupancyChart() {
        return new JPanel() {
            private Map<String, Double> occupancyData = new HashMap<>();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (occupancyData.isEmpty()) return;

                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 40;

                // Tìm giá trị max
                double maxOccupancy = occupancyData.values().stream().max(Double::compare).orElse(100.0);

                // Vẽ trục
                g2d.setColor(Color.BLACK);
                g2d.drawLine(padding, height - padding, width - padding, height - padding);
                g2d.drawLine(padding, padding, padding, height - padding);

                // Vẽ các cột
                int columnWidth = (width - 2 * padding) / (occupancyData.size() + 1);
                int i = 0;
                for (Map.Entry<String, Double> entry : occupancyData.entrySet()) {
                    int x = padding + (i + 1) * (width - 2 * padding) / (occupancyData.size() + 1);
                    int columnHeight = (int)((entry.getValue() / maxOccupancy) * (height - 2 * padding));

                    g2d.setColor(CHART_COLORS[i % CHART_COLORS.length]);
                    g2d.fillRect(x - columnWidth/2, height - padding - columnHeight, columnWidth, columnHeight);

                    // Vẽ nhãn
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(entry.getKey(), x - 10, height - 10);
                    g2d.drawString(String.format("%.1f%%", entry.getValue()), x - 10, height - padding - columnHeight - 10);

                    i++;
                }
            }

            public void updateData(Map<String, Double> data) {
                this.occupancyData = data;
                repaint();
            }
        };
    }

    // Phương thức tạo nút
    private JButton createButton(String text, Color bgColor) {
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

        // Hiệu ứng hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    // Các phương thức cập nhật dữ liệu cho biểu đồ
    // Trong class ThongKePanel, sửa lại các phương thức cập nhật biểu đồ:

public void updateMonthlyTicketSalesChart(Map<String, Integer> monthlyData) {
    if (monthlyTicketSalesChart instanceof MonthlyChart) {
        ((MonthlyChart) monthlyTicketSalesChart).updateData(monthlyData);
        monthlyTicketSalesChart.repaint();
    }
}

public void updateFlightStatusChart(Map<String, Integer> statusData) {
    if (flightStatusChart instanceof StatusChart) {
        ((StatusChart) flightStatusChart).updateData(statusData);
        flightStatusChart.repaint();
    }
}

public void updateFlightOccupancyChart(Map<String, Double> occupancyData) {
    if (flightOccupancyChart instanceof OccupancyChart) {
        ((OccupancyChart) flightOccupancyChart).updateData(occupancyData);
        flightOccupancyChart.repaint();
    }
}

// Tạo class riêng cho từng loại biểu đồ
private class MonthlyChart extends JPanel {
    private Map<String, Integer> monthlyData = new HashMap<>();
    private int padding = 50;
    private int labelPadding = 30;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (monthlyData.isEmpty()) return;

        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;
        int maxValue = monthlyData.values().stream().max(Integer::compare).orElse(0);

        // Vẽ trục
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        // Trục Y
        g2d.drawLine(padding, padding, padding, height + padding);
        // Trục X
        g2d.drawLine(padding, height + padding, width + padding, height + padding);

        // Vẽ các điểm dữ liệu
        int pointWidth = width / (monthlyData.size() + 1);
        int x = padding + pointWidth;
        int prevX = x;
        int prevY = 0;
        boolean first = true;

        // Vẽ đường
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(PRIMARY_COLOR);

        for (Map.Entry<String, Integer> entry : monthlyData.entrySet()) {
            int value = entry.getValue();
            int y = padding + height - (value * height / maxValue);

            // Vẽ điểm
            g2d.fillOval(x - 5, y - 5, 10, 10);

            // Vẽ đường nối
            if (!first) {
                g2d.drawLine(prevX, prevY, x, y);
            }

            // Vẽ nhãn tháng
            g2d.setColor(Color.BLACK);
            g2d.drawString(entry.getKey(), x - 10, height + padding + labelPadding);

            // Vẽ giá trị
            g2d.drawString(String.valueOf(value), x - 10, y - 10);

            prevX = x;
            prevY = y;
            x += pointWidth;
            first = false;
            g2d.setColor(PRIMARY_COLOR);
        }
    }

    public void updateData(Map<String, Integer> data) {
        this.monthlyData = new HashMap<>(data);
        repaint();
    }
}

private class StatusChart extends JPanel {
    private Map<String, Integer> statusData = new HashMap<>();
    private int padding = 50;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (statusData.isEmpty()) return;

        int diameter = Math.min(getWidth(), getHeight()) - 2 * padding;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Tính tổng
        int total = statusData.values().stream().mapToInt(Integer::intValue).sum();
        double startAngle = 0;

        // Vẽ từng phần của pie chart
        int i = 0;
        for (Map.Entry<String, Integer> entry : statusData.entrySet()) {
            double percent = entry.getValue() / (double) total;
            double sweepAngle = 360 * percent;

            // Vẽ phần pie
            g2d.setColor(CHART_COLORS[i % CHART_COLORS.length]);
            g2d.fillArc(centerX - diameter/2, centerY - diameter/2, 
                       diameter, diameter, (int)startAngle, (int)sweepAngle);

            // Vẽ chú thích
            double midAngle = Math.toRadians(startAngle + sweepAngle/2);
            int labelX = (int)(centerX + (diameter/2 + 30) * Math.cos(midAngle));
            int labelY = (int)(centerY + (diameter/2 + 30) * Math.sin(midAngle));

            String label = String.format("%s: %d (%.1f%%)", 
                                       entry.getKey(), entry.getValue(), percent * 100);
            g2d.drawString(label, labelX, labelY);

            startAngle += sweepAngle;
            i++;
        }
    }

    public void updateData(Map<String, Integer> data) {
        this.statusData = new HashMap<>(data);
        repaint();
    }
}

private class OccupancyChart extends JPanel {
    private Map<String, Double> occupancyData = new HashMap<>();
    private int padding = 50;
    private int labelPadding = 30;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (occupancyData.isEmpty()) return;

        int width = getWidth() - 2 * padding;
        int height = getHeight() - 2 * padding;
        double maxValue = occupancyData.values().stream().max(Double::compare).orElse(100.0);

        // Vẽ trục
        g2d.setColor(Color.BLACK);
        // Trục Y
        g2d.drawLine(padding, padding, padding, height + padding);
        // Trục X
        g2d.drawLine(padding, height + padding, width + padding, height + padding);

        // Vẽ cột
        int barWidth = width / (occupancyData.size() * 2);
        int x = padding + barWidth;

        for (Map.Entry<String, Double> entry : occupancyData.entrySet()) {
            double value = entry.getValue();
            int barHeight = (int)((value / maxValue) * height);

            // Vẽ cột
            g2d.setColor(CHART_COLORS[0]);
            g2d.fillRect(x, height + padding - barHeight, barWidth, barHeight);

            // Vẽ giá trị
            g2d.setColor(Color.BLACK);
            String valueStr = String.format("%.1f%%", value);
            g2d.drawString(valueStr, x, height + padding - barHeight - 5);

            // Vẽ nhãn
            g2d.drawString(entry.getKey(), x, height + padding + labelPadding);

            x += barWidth * 2;
        }
    }

    public void updateData(Map<String, Double> data) {
        this.occupancyData = new HashMap<>(data);
        repaint();
    }
}

    // Phương thức update bảng
    public void updateTableData(List<ThongKe> thongKeList) {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);

        // Thêm dữ liệu mới
        for (ThongKe thongKe : thongKeList) {
            tableModel.addRow(new Object[]{
                thongKe.getMaChuyenBay(),
                thongKe.getTenSanBay(),
                thongKe.getChangBay(),
                thongKe.getNgayBay(),
                thongKe.getSoVeDaDat(),
                thongKe.getTinhTrang(),
                // Giả sử có thêm cột tổng số ghế
                calculateTotalSeats(thongKe)
            });
        }
    }

    // Phương thức hỗ trợ tính tổng số ghế
    private int calculateTotalSeats(ThongKe thongKe) {
        // Logic tính toán tổng số ghế, có thể cần điều chỉnh
        return thongKe.getSoVeDaDat() + 10; // Ví dụ đơn giản
    }

    // Phương thức hiển thị thông báo
    public void showNotification(String message, NotificationType type) {
        Color backgroundColor;
        Color textColor;

        switch (type) {
            case SUCCESS:
                backgroundColor = new Color(76, 175, 80, 200);
                textColor = Color.WHITE;
                break;
            case WARNING:
                backgroundColor = new Color(255, 152, 0, 200);
                textColor = Color.WHITE;
                break;
            case ERROR:
                backgroundColor = new Color(244, 67, 54, 200);
                textColor = Color.WHITE;
                break;
            case INFO:
            default:
                backgroundColor = new Color(33, 150, 243, 200);
                textColor = Color.WHITE;
        }

        JLabel notificationLabel = new JLabel(message);
        notificationLabel.setForeground(textColor);
        notificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        notificationLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JPanel notificationPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(backgroundColor);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        notificationPanel.setLayout(new BorderLayout());
        notificationPanel.add(notificationLabel, BorderLayout.CENTER);
        notificationPanel.setPreferredSize(new Dimension(300, 50));

        JOptionPane optionPane = new JOptionPane(notificationPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = optionPane.createDialog(this, "Thông Báo");
        dialog.setModal(false);
        dialog.setUndecorated(true);
        dialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        dialog.setBackground(new Color(0, 0, 0, 0));

        // Đặt vị trí giữa màn hình
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(
            (screenSize.width - notificationPanel.getPreferredSize().width) / 2, 
            screenSize.height - notificationPanel.getPreferredSize().height - 50
        );

        dialog.setVisible(true);

        // Tự động đóng sau 3 giây
        Timer timer = new Timer(3000, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    // Getter cho table model để xuất CSV
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}