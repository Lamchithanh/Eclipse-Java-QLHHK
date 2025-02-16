package View;

import Model.UserAccount;
import Service.UserAccountService;
import Service.TrangChuService;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Components.ColumnChart;
import Components.LineChart;
import Components.PieChart;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.Timer;

public class TrangChuPanel extends JPanel {
    // Enhanced color scheme
    private final Color PRIMARY_DARK = new Color(15, 23, 42);      // Slate 900
    private final Color PRIMARY_MID = new Color(51, 65, 85);       // Slate 700
    @SuppressWarnings("unused")
	private final Color PRIMARY_LIGHT = new Color(241, 245, 249);  // Slate 100
    private final Color ACCENT = new Color(56, 189, 248);         // Sky 400
    @SuppressWarnings("unused")
    private final Color ACCENT_DARK = new Color(2, 132, 199);     // Sky 600
    private final Color SUCCESS = new Color(34, 197, 94);         // Green 500
    private final Color WARNING = new Color(234, 179, 8);         // Yellow 500
    private final Color DANGER = new Color(239, 68, 68);          // Red 500
    @SuppressWarnings("unused")
    private final Color GLASS_BG = new Color(255, 255, 255, 200);
    private final Color GLASS_BORDER = new Color(255, 255, 255, 80);
    
    // Layout constants
    private final int SIDEBAR_WIDTH = 300;
    private final int HEADER_HEIGHT = 80;
    private final int CARD_RADIUS = 24;
    private final int ANIMATION_DURATION = 300;
    
    // Component references
    private UserAccount currentUser;
    private JFrame parentFrame;
    private TrangChuService trangChuService;
    @SuppressWarnings("unused")
    private UserAccountService userAccountService;
    private JLabel clockLabel;
    @SuppressWarnings("unused")
    private Timer clockTimer;
    private Image backgroundImage;
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private JPanel contentArea;
    @SuppressWarnings("unused")
    private String activeMenuItem = "TrangChuPanel";
    private Map<String, JPanel> menuItems = new HashMap<>();
    private Map<String, JLabel> statLabels = new HashMap<>();
    @SuppressWarnings("deprecation")
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    @SuppressWarnings("exports")
    public TrangChuPanel(UserAccount user, JFrame parentFrame, TrangChuService trangChuService) {
        this.currentUser = user;
        this.parentFrame = parentFrame;
        this.trangChuService = trangChuService;
        this.userAccountService = new UserAccountService();
        
        initializeUI();
        loadBackgroundImage();
        startClock();
        startDataRefreshTimer();
        setupAnimations();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/src/image/airplane1.jpg")).getImage();
        } catch (Exception e) {
            System.err.println("Error loading background image: " + e.getMessage());
            backgroundImage = null;
        }
    }

    private void setupAnimations() {
        Timer animationTimer = new Timer(16, e -> repaint());
        animationTimer.start();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        JPanel mainContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    setupGraphicsQuality(g2d);
                    
                    // Parallax effect
                    Point mousePosition = getMousePosition();
                    int offsetX = mousePosition != null ? (getWidth() / 2 - mousePosition.x) / 20 : 0;
                    int offsetY = mousePosition != null ? (getHeight() / 2 - mousePosition.y) / 20 : 0;
                    
                    g2d.drawImage(backgroundImage, 
                        offsetX, offsetY, 
                        getWidth() + Math.abs(offsetX) * 2, 
                        getHeight() + Math.abs(offsetY) * 2, 
                        null);
                    
                    // Gradient overlay
                    drawGradientOverlay(g2d);
                    
                    // Subtle patterns
                    drawBackgroundPattern(g2d);
                }
            }
        };
        mainContainer.setOpaque(false);

        createEnhancedSidebar();
        createEnhancedMainContent();
        
        mainContainer.add(sidebarPanel, BorderLayout.WEST);
        mainContainer.add(mainContentPanel, BorderLayout.CENTER);
        
        add(mainContainer);
    }

    private void setupGraphicsQuality(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private void drawGradientOverlay(Graphics2D g2d) {
        GradientPaint overlay = new GradientPaint(
            0, 0, new Color(255, 255, 255, 180),
            0, getHeight(), new Color(255, 255, 255, 230)
        );
        g2d.setPaint(overlay);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawBackgroundPattern(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 30));
        int spacing = 20;
        for (int x = 0; x < getWidth(); x += spacing) {
            for (int y = 0; y < getHeight(); y += spacing) {
                g2d.fillOval(x, y, 2, 2);
            }
        }
    }

    // Add this method to the TrangChuPanel class
private void drawSidebarPattern(Graphics2D g2d) {
    // Create a subtle pattern for sidebar background
    g2d.setColor(new Color(255, 255, 255, 10));
    int patternSize = 40;
    for (int x = 0; x < sidebarPanel.getWidth(); x += patternSize) {
        for (int y = 0; y < sidebarPanel.getHeight(); y += patternSize) {
            double angle = Math.toRadians(45);
            g2d.draw(new Line2D.Double(
                x, y,
                x + patternSize * Math.cos(angle),
                y + patternSize * Math.sin(angle)
            ));
        }
    }
}

    private void createEnhancedSidebar() {
        sidebarPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                setupGraphicsQuality(g2d);
                
                // Enhanced gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(15, 23, 42, 250),
                    getWidth(), getHeight(), new Color(30, 41, 59, 250)
                );
                g2d.setPaint(gradient);
                
                // Rounded corners
                RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
                    0, 0, getWidth() + CARD_RADIUS, getHeight(),
                    0, 0
                );
                g2d.fill(rect);
                
                drawSidebarPattern(g2d);
            }
        };
        sidebarPanel.setPreferredSize(new Dimension(SIDEBAR_WIDTH, 0));

        sidebarPanel.add(createEnhancedBrandPanel(), BorderLayout.NORTH);
        sidebarPanel.add(createEnhancedMenuPanel(), BorderLayout.CENTER);
        sidebarPanel.add(createEnhancedProfilePanel(), BorderLayout.SOUTH);
    }

    private JPanel createEnhancedBrandPanel() {
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        brandPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel(loadIcon("system-logo-white"));
        logoLabel.setPreferredSize(new Dimension(40, 40));
        
        JLabel brandName = new JLabel("Quản Lý Hàng Không");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 20));
        brandName.setForeground(Color.WHITE);
        
        brandPanel.add(logoLabel);
        brandPanel.add(brandName);
        
        return brandPanel;
    }

    private JPanel createEnhancedMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setOpaque(false);
        
        String[][] menuItems = {
            {"Trang Chủ", "dashboard", "TrangChuPanel"},
            {"Đặt vé", "booking", "datVePanel"},
            {"Chuyến Bay", "flight", "ChuyenBayPanel"},
            {"Sân Bay", "airport", "SanBayPanel"},
            {"Vé Máy Bay", "ticket", "VeMayBayPanel"},
            {"Lịch Bay", "schedule", "LichBayPanel"},
            {"Hãng Hàng Không", "airline", "HangHangKhongPanel"},
            {"Nhân Viên", "staff", "NhanVienPanel"},
            {"Máy Bay", "plane", "MayBayPanel"},
            {"Thống Kê", "stats", "ThongKePanel"}
        };
        
        for (String[] item : menuItems) {
            addEnhancedMenuItem(menuPanel, item[0], item[1], item[2]);
        }
        
        menuPanel.add(Box.createVerticalGlue());
        
        return menuPanel;
    }

    private void addEnhancedMenuItem(JPanel container, String title, String iconName, String targetClass) {
        JPanel itemPanel = new JPanel(new BorderLayout(15, 0));
        itemPanel.setOpaque(false);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        itemPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel iconLabel = new JLabel(loadIcon(iconName + "-white"));
        iconLabel.setPreferredSize(new Dimension(24, 24));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        titleLabel.setForeground(Color.WHITE);
        
        itemPanel.add(iconLabel, BorderLayout.WEST);
        itemPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Simplified mouse listener - just for click handling
        itemPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setActiveMenuItem(targetClass);
                navigateToPanel(targetClass);
            }
        });
        
        menuItems.put(targetClass, itemPanel);
        container.add(itemPanel);
    }

    @SuppressWarnings("unused")
    private void startMenuItemAnimation(JPanel panel, boolean isHover) {
        Timer timer = new Timer(16, null);
        final float[] alpha = {isHover ? 0.0f : 1.0f};
        final float step = isHover ? 0.1f : -0.1f;
        
        timer.addActionListener(e -> {
            alpha[0] += step;
            
            if ((isHover && alpha[0] >= 1.0f) || (!isHover && alpha[0] <= 0.0f)) {
                timer.stop();
                alpha[0] = isHover ? 1.0f : 0.0f;
            }
            
            Color bgColor = new Color(255, 255, 255, (int)(alpha[0] * 40));
            panel.setBackground(bgColor);
            panel.setOpaque(alpha[0] > 0);
            panel.repaint();
        });
        
        timer.start();
    }

    @SuppressWarnings("unused")
    private void createRippleEffect(JPanel panel, Point clickPoint) {
        Timer rippleTimer = new Timer(16, null);
        final float[] radius = {0f};
        final float maxRadius = (float) Math.sqrt(
            Math.pow(panel.getWidth(), 2) + Math.pow(panel.getHeight(), 2)
        );
        
        rippleTimer.addActionListener(e -> {
            radius[0] += maxRadius / 15;
            if (radius[0] >= maxRadius) {
                rippleTimer.stop();
                radius[0] = 0f;
            }
            
            panel.repaint();
            
            Graphics2D g2d = (Graphics2D) panel.getGraphics();
            if (g2d != null) {
                setupGraphicsQuality(g2d);
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.fill(new Ellipse2D.Float(
                    clickPoint.x - radius[0],
                    clickPoint.y - radius[0],
                    radius[0] * 2,
                    radius[0] * 2
                ));
                g2d.dispose();
            }
        });
        
        rippleTimer.start();
    }

    private JPanel createEnhancedProfilePanel() {
        JPanel profilePanel = new JPanel(new BorderLayout(20, 15));
        profilePanel.setOpaque(false);
        profilePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, GLASS_BORDER),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JPanel userInfo = new JPanel(new BorderLayout(15, 5));
        userInfo.setOpaque(false);
        
        JLabel avatarLabel = new JLabel(loadIcon("user-circle-white"));
        avatarLabel.setPreferredSize(new Dimension(45, 45));
        
        JPanel userDetails = new JPanel(new GridLayout(2, 1, 0, 5));
        userDetails.setOpaque(false);
        
        JLabel nameLabel = new JLabel(currentUser.getUsername());
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        clockLabel.setForeground(new Color(255, 255, 255, 200));
        
        userDetails.add(nameLabel);
        userDetails.add(clockLabel);
        
        userInfo.add(avatarLabel, BorderLayout.WEST);
        userInfo.add(userDetails, BorderLayout.CENTER);
        
        JButton logoutButton = createEnhancedButton("Đăng Xuất", DANGER);
        logoutButton.addActionListener(e -> handleLogout());
        
        profilePanel.add(userInfo, BorderLayout.CENTER);
        profilePanel.add(logoutButton, BorderLayout.SOUTH);
        
        return profilePanel;
    }

    private JButton createEnhancedButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                setupGraphicsQuality(g2d);
                
                // Dynamic button color based on state
                Color buttonColor;
                if (getModel().isPressed()) {
                    buttonColor = color.darker();
                } else if (getModel().isRollover()) {
                    buttonColor = color.brighter();
                } else {
                    buttonColor = color;
                }
                
                // Draw button background with rounded corners
                g2d.setColor(buttonColor);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                
                // Draw text with shadow effect
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x + 1, y + 1);
                
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), x, y);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(0, 45));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void createEnhancedMainContent() {
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setOpaque(false);
        
        JPanel headerPanel = createEnhancedHeaderPanel();
        contentArea = new JPanel(new BorderLayout());
        contentArea.setOpaque(false);
        
        mainContentPanel.add(headerPanel, BorderLayout.NORTH);
        mainContentPanel.add(contentArea, BorderLayout.CENTER);
        
        showEnhancedDashboard();
    }

    private JPanel createEnhancedHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                setupGraphicsQuality(g2d);
                
                // Create glass effect background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 230),
                    0, getHeight(), new Color(255, 255, 255, 200)
                );
                g2d.setPaint(gradient);
                
                // Draw rounded rectangle with shadow
                RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
                    10, 10, getWidth() - 20, getHeight() - 20, 15, 15
                );
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 20));
                g2d.fill(new RoundRectangle2D.Float(
                    12, 12, getWidth() - 20, getHeight() - 20, 15, 15
                ));
                
                g2d.setPaint(gradient);
                g2d.fill(rect);
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, HEADER_HEIGHT));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel pageTitle = new JLabel("Dashboard");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        pageTitle.setForeground(PRIMARY_DARK);
        
        headerPanel.add(pageTitle, BorderLayout.WEST);
        
        return headerPanel;
    }

    private void showEnhancedDashboard() {
        contentArea.removeAll();
        
        JPanel dashboardPanel = new JPanel(new BorderLayout(20, 20));
        dashboardPanel.setOpaque(false);
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        dashboardPanel.add(createEnhancedQuickStats(), BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(createEnhancedDashboardContent());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        
        dashboardPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentArea.add(dashboardPanel);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private JPanel createEnhancedQuickStats() {
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        
        addEnhancedStatCard(statsPanel, "Chuyến Bay Hôm Nay", 
            String.valueOf(trangChuService.getTodayFlightsCount()),
            "Tổng: " + trangChuService.layTongChuyenBay(), ACCENT);
            
        addEnhancedStatCard(statsPanel, "Vé Đã Bán Hôm Nay", 
            String.valueOf(trangChuService.getTodayTicketsSold()),
            "Tổng: " + trangChuService.layTongVeDaBan(), SUCCESS);
            
        addEnhancedStatCard(statsPanel, "Doanh Thu Hôm Nay", 
            currencyFormatter.format(trangChuService.getTodayRevenue() / 1000000) + "M",
            "Tổng: " + currencyFormatter.format(trangChuService.getTodayRevenue() / 1000000) + "M", WARNING);
            
        addEnhancedStatCard(statsPanel, "Khách Hàng", 
            String.valueOf(trangChuService.getTotalCustomers()),
            "Điểm đến: " + trangChuService.getTotalDestinations(), DANGER);
        
        return statsPanel;
    }

    private void addEnhancedStatCard(JPanel container, String title, String value, String subtitle, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(15, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                setupGraphicsQuality(g2d);
                
                // Base background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 230),
                    0, getHeight(), new Color(255, 255, 255, 200)
                );
                g2d.setPaint(gradient);
                
                RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
                    0, 0, getWidth() - 1, getHeight() - 1, 20, 20
                );
                g2d.fill(rect);
                
                // Fixed border
                g2d.setColor(new Color(
                    accentColor.getRed(),
                    accentColor.getGreen(),
                    accentColor.getBlue(),
                    50
                ));
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(rect);
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(PRIMARY_DARK);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(accentColor);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(PRIMARY_MID);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(subtitleLabel, BorderLayout.SOUTH);
        
        container.add(card);
    }
    

    private JPanel createEnhancedDashboardContent() {
        // Tạo panel chính với BoxLayout theo chiều dọc
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Tạo grid layout 2x2 cho các biểu đồ
        JPanel chartsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        chartsPanel.setOpaque(false);

        // Chuẩn bị dữ liệu chung cho tháng
        int monthCount = 6;
        String[] months = new String[monthCount];
        for (int i = 0; i < monthCount; i++) {
            months[i] = "T" + (i + 1);
        }

        // 1. Column Chart: Thống kê chuyến bay theo tháng
        ColumnChart flightChart = createFlightChart(months, monthCount);

        // 2. Line Chart: Doanh thu theo tháng
        LineChart revenueChart = createRevenueChart(months, monthCount);

        // 3. Pie Chart: Phân bố vé theo hạng
        PieChart ticketClassChart = createTicketClassChart();

        // 4. Pie Chart: Top điểm đến phổ biến
        PieChart destinationChart = createDestinationChart();

        // Thêm các biểu đồ vào panel với wrapper
        chartsPanel.add(wrapChartInPanel(flightChart, "Thống kê chuyến bay", "Số lượng chuyến bay theo tháng"));
        chartsPanel.add(wrapChartInPanel(revenueChart, "Biểu đồ doanh thu", "Doanh thu theo tháng"));
        chartsPanel.add(wrapChartInPanel(ticketClassChart, "Phân bố vé", "Tỷ lệ các hạng vé"));
        chartsPanel.add(wrapChartInPanel(destinationChart, "Điểm đến phổ biến", "Top điểm đến được chọn nhiều nhất"));

        contentPanel.add(chartsPanel);
        return contentPanel;
    }

    private ColumnChart createFlightChart(String[] months, int monthCount) {
        ColumnChart flightChart = new ColumnChart();
        flightChart.setTitle("Thống kê chuyến bay theo tháng");
        flightChart.setTitleX("Tháng");
        flightChart.setTitleY("Số chuyến bay");
        
        int[] flightValues = new int[monthCount];
        for (int i = 0; i < monthCount; i++) {
            try {
                flightValues[i] = trangChuService.getFlightCountByMonth(i + 1);
            } catch (Exception e) {
                flightValues[i] = 0; // Giá trị mặc định nếu có lỗi
            }
        }
        
        flightChart.setValues(flightValues);
        flightChart.setColumnNames(months);
        flightChart.updateChart();
        
        return flightChart;
    }

    private LineChart createRevenueChart(String[] months, int monthCount) {
        LineChart revenueChart = new LineChart();
        revenueChart.setTitle("Doanh thu theo tháng");
        
        double[] revenueValues = new double[monthCount];
        for (int i = 0; i < monthCount; i++) {
            try {
                revenueValues[i] = trangChuService.getRevenueByMonth(i + 1) / 1000000.0; // Chuyển về đơn vị triệu
            } catch (Exception e) {
                revenueValues[i] = 0.0; // Giá trị mặc định nếu có lỗi
            }
        }
        
        revenueChart.setValues(convertDoubleArrayToInt1(revenueValues));
        revenueChart.setColumnNames(months);
        revenueChart.setSeries("Doanh thu (triệu VNĐ)");
        revenueChart.updateChart();
        
        return revenueChart;
    }

    private PieChart createTicketClassChart() {
        PieChart ticketClassChart = new PieChart();
        ticketClassChart.setTitle("Phân bố vé theo hạng");
        
        String[] ticketClasses = {"Phổ thông", "Thương gia", "Hạng nhất"};
        int[] ticketValues = new int[ticketClasses.length];
        
        for (int i = 0; i < ticketClasses.length; i++) {
            try {
                ticketValues[i] = trangChuService.getTicketCountByClass(ticketClasses[i]);
            } catch (Exception e) {
                ticketValues[i] = 0; // Giá trị mặc định nếu có lỗi
            }
        }
        
        ticketClassChart.setValues(ticketValues);
        ticketClassChart.setColumnNames(ticketClasses);
        ticketClassChart.updateChart();
        
        return ticketClassChart;
    }

    private PieChart createDestinationChart() {
        PieChart destinationChart = new PieChart();
        destinationChart.setTitle("Top điểm đến phổ biến");
        
        Map<String, Integer> topDestinations = trangChuService.getTopDestinations(5);
        if (topDestinations != null && !topDestinations.isEmpty()) {
            int[] destValues = new int[topDestinations.size()];
            String[] destNames = new String[topDestinations.size()];
            
            int index = 0;
            for (Map.Entry<String, Integer> entry : topDestinations.entrySet()) {
                destNames[index] = entry.getKey();
                destValues[index] = entry.getValue();
                index++;
            }
            
            destinationChart.setValues(destValues);
            destinationChart.setColumnNames(destNames);
            destinationChart.updateChart();
        } else {
            // Xử lý trường hợp không có dữ liệu
            destinationChart.setValues(new int[]{0});
            destinationChart.setColumnNames(new String[]{"Không có dữ liệu"});
            destinationChart.updateChart();
        }
        
        return destinationChart;
    }

    private JPanel wrapChartInPanel(JPanel chart, String title, String description) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                setupGraphicsQuality(g2d);
                
                // Glass effect background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 230),
                    0, getHeight(), new Color(255, 255, 255, 200)
                );
                g2d.setPaint(gradient);
                
                RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS
                );
                g2d.fill(rect);
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel for title and description
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_DARK);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(PRIMARY_MID);
        
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(descLabel);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        wrapper.add(headerPanel, BorderLayout.NORTH);
        wrapper.add(chart, BorderLayout.CENTER);
        
        return wrapper;
    }

    private int[] convertDoubleArrayToInt1(double[] doubleArray) {
        int[] intArray = new int[doubleArray.length];
        for (int i = 0; i < doubleArray.length; i++) {
            intArray[i] = (int) Math.round(doubleArray[i]);
        }
        return intArray;
    }

    @SuppressWarnings("unused")
    private int[] convertDoubleArrayToInt(double[] doubleArray) {
        int[] intArray = new int[doubleArray.length];
        for (int i = 0; i < doubleArray.length; i++) {
            intArray[i] = (int) Math.round(doubleArray[i]);
        }
        return intArray;
    }

    @SuppressWarnings("unused")
    private JPanel wrapChartInPanel(JPanel chart, String title) {
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                setupGraphicsQuality(g2d);
                
                // Glass effect background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 230),
                    0, getHeight(), new Color(255, 255, 255, 200)
                );
                g2d.setPaint(gradient);
                
                RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS
                );
                g2d.fill(rect);
            }
        };
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        wrapper.add(titleLabel, BorderLayout.NORTH);
        wrapper.add(chart, BorderLayout.CENTER);
        
        return wrapper;
    }

    @SuppressWarnings("unused")
    private JPanel createEnhancedSummarySection() {
        JPanel summaryPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                setupGraphicsQuality(g2d);
                
                // Create glass effect background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(255, 255, 255, 230),
                    0, getHeight(), new Color(255, 255, 255, 200)
                );
                g2d.setPaint(gradient);
                
                RoundRectangle2D.Float rect = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), CARD_RADIUS, CARD_RADIUS
                );
                g2d.fill(rect);
            }
        };
        summaryPanel.setOpaque(false);
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel summaryTitle = new JLabel("Tổng Quan Hệ Thống");
        summaryTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        summaryTitle.setForeground(PRIMARY_DARK);
        
        JPanel summaryGrid = new JPanel(new GridLayout(2, 2, 25, 25));
        summaryGrid.setOpaque(false);
        
        addEnhancedSummaryItem(summaryGrid, "Tổng Nhân Viên", 
            String.valueOf(trangChuService.layTongNhanVien()), "users-white");
        addEnhancedSummaryItem(summaryGrid, "Điểm Đến", 
            String.valueOf(trangChuService.getTotalDestinations()), "map-pin-white");
        addEnhancedSummaryItem(summaryGrid, "Tổng Chuyến Bay", 
            String.valueOf(trangChuService.layTongChuyenBay()), "plane-white");
        addEnhancedSummaryItem(summaryGrid, "Tổng Vé Đã Bán", 
            String.valueOf(trangChuService.layTongVeDaBan()), "ticket-white");

        summaryPanel.add(summaryTitle, BorderLayout.NORTH);
        summaryPanel.add(summaryGrid, BorderLayout.CENTER);

        return summaryPanel;
    }

    private void addEnhancedSummaryItem(JPanel container, String title, String value, String iconName) {
        JPanel itemPanel = new JPanel(new BorderLayout(20, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                setupGraphicsQuality(g2d);
                
                // Fixed background
                g2d.setColor(new Color(255, 255, 255, 150));
                g2d.fill(new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 15, 15
                ));
            }
        };
        itemPanel.setOpaque(false);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel iconLabel = new JLabel(loadIcon(iconName));
        iconLabel.setPreferredSize(new Dimension(36, 36));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 8));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(new Color(71, 85, 105));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valueLabel.setForeground(PRIMARY_DARK);
        
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);
        
        itemPanel.add(iconLabel, BorderLayout.WEST);
        itemPanel.add(textPanel, BorderLayout.CENTER);
        
        container.add(itemPanel);
    }

    @SuppressWarnings("unused")
    private void startSummaryItemAnimation(JPanel panel, boolean isHover) {
        Timer timer = new Timer(16, null);
        final float[] alpha = {isHover ? 0.0f : 1.0f};
        final float step = isHover ? 0.1f : -0.1f;
        
        timer.addActionListener(e -> {
            alpha[0] += step;
            
            if ((isHover && alpha[0] >= 1.0f) || (!isHover && alpha[0] <= 0.0f)) {
                timer.stop();
                alpha[0] = isHover ? 1.0f : 0.0f;
            }
            
            panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                BorderFactory.createLineBorder(new Color(0, 0, 0, (int)(alpha[0] * 20)))
            ));
            panel.repaint();
        });
        
        timer.start();
    }

    private void startClock() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Timer timer = new Timer(1000, e -> {
            if (clockLabel != null) {
                clockLabel.setText(dateFormat.format(new Date()));
            }
        });
        timer.start();
    }

    private void startDataRefreshTimer() {
        Timer timer = new Timer(60000, e -> {
            SwingUtilities.invokeLater(this::updateDashboardData);
        });
        timer.start();
    }

    private void updateDashboardData() {
        for (Map.Entry<String, JLabel> entry : statLabels.entrySet()) {
            String key = entry.getKey();
            JLabel label = entry.getValue();
            
            switch (key) {
                case "Chuyến Bay Hôm Nay":
                    animateValue(label, 
                        Integer.parseInt(label.getText()), 
                        trangChuService.getTodayFlightsCount(), 
                        ANIMATION_DURATION);
                    break;
                case "Vé Đã Bán Hôm Nay":
                    animateValue(label, 
                        Integer.parseInt(label.getText()), 
                        trangChuService.getTodayTicketsSold(), 
                        ANIMATION_DURATION);
                    break;
                case "Doanh Thu Hôm Nay":
                    double currentRevenue = Double.parseDouble(label.getText()
                        .replace("M", "").replace(",", ""));
                    animateValue(label, 
                        (int)currentRevenue, 
                        (int)(trangChuService.getTodayRevenue() / 1000000), 
                        ANIMATION_DURATION);
                    break;
                case "Khách Hàng":
                    animateValue(label, 
                        Integer.parseInt(label.getText()), 
                        trangChuService.getTotalCustomers(), 
                        ANIMATION_DURATION);
                    break;
            }
        }
    }

    private void animateValue(JLabel label, int startValue, int endValue, int duration) {
        Timer timer = new Timer(16, null);
        final long startTime = System.currentTimeMillis();
        
        timer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - startTime;
            float progress = Math.min(1f, (float) elapsed / duration);
            
            // Ease out cubic interpolation
            float t = 1 - (1 - progress) * (1 - progress) * (1 - progress);
            int currentValue = startValue + Math.round((endValue - startValue) * t);
            
            if (label.getText().contains("M")) {
                label.setText(currentValue + "M");
            } else {
                label.setText(String.valueOf(currentValue));
            }
            
            if (progress >= 1f) {
                timer.stop();
            }
        });
        
        timer.start();
    }

    private void setActiveMenuItem(String panelName) {
        activeMenuItem = panelName;
        menuItems.forEach((key, panel) -> {
            if (key.equals(panelName)) {
                panel.setBackground(new Color(55, 65, 81));
                panel.setOpaque(true);
            } else {
                panel.setBackground(null);
                panel.setOpaque(false);
            }
            panel.repaint();
        });
    }

    private void navigateToPanel(String panelName) {
        contentArea.removeAll();
        
        switch (panelName) {
            case "datVePanel":
                contentArea.add(new QuanLyDatVeView(this));
                break;
            case "ChuyenBayPanel":
                contentArea.add(new QuanLyChuyenBay(this));
                break;
            case "SanBayPanel":
                contentArea.add(new QuanLySanBayView(this));
                break;
            case "VeMayBayPanel":
                contentArea.add(new QuanLyVeMayBay(this));
                break;
            case "LichBayPanel":
                contentArea.add(new QuanLyLichBay(this));
                break;
            case "HangHangKhongPanel":
                contentArea.add(new QuanLyHangHangKhong(this));
                break;
            case "NhanVienPanel":
                contentArea.add(new QuanLyNhanVien(this));
                break;
            case "MayBayPanel":
                contentArea.add(new QuanLyMayBay(this));
                break;
            case "ThongKePanel":
                contentArea.add(new ThongKePanel(currentUser));
                break;
            default:
                showEnhancedDashboard();
                break;
        }
        
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void handleLogout() {
        int response = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            parentFrame.dispose();
            new DangNhapDialog(null, true).setVisible(true);
        }
    }

    private ImageIcon loadIcon(String name) {
        try {
            return new ImageIcon(getClass().getResource("/src/icon/" + name + ".png"));
        } catch (Exception e) {
            System.err.println("Error loading icon: " + name);
            return new ImageIcon();
        }
    }

    public void lamMoiQuickStats() {
        SwingUtilities.invokeLater(() -> {
            contentArea.removeAll();
            
            JPanel dashboardPanel = new JPanel(new BorderLayout(20, 20));
            dashboardPanel.setOpaque(false);
            dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            
            dashboardPanel.add(createEnhancedQuickStats(), BorderLayout.NORTH);
            
            JScrollPane scrollPane = new JScrollPane(createEnhancedDashboardContent());
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);
            scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
            
            dashboardPanel.add(scrollPane, BorderLayout.CENTER);
            
            contentArea.add(dashboardPanel);
            contentArea.revalidate();
            contentArea.repaint();
        });
    }

    private class ModernScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(156, 163, 175, 200);
            this.trackColor = new Color(241, 245, 249, 100);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }

            Graphics2D g2 = (Graphics2D) g.create();
            setupGraphicsQuality(g2);
            
            g2.setColor(thumbColor);
            g2.fill(new RoundRectangle2D.Float(
                thumbBounds.x, thumbBounds.y,
                thumbBounds.width, thumbBounds.height,
                8, 8
            ));
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            setupGraphicsQuality(g2);
            
            g2.setColor(trackColor);
            g2.fill(new RoundRectangle2D.Float(
                trackBounds.x, trackBounds.y,
                trackBounds.width, trackBounds.height,
                8, 8
            ));
            g2.dispose();
        }
    }

    private class DangNhapDialog extends JDialog {
        public DangNhapDialog(Frame parent, boolean modal) {
            super(parent, modal);
            initLoginDialog();
        }

        private void initLoginDialog() {
            setTitle("Đăng Nhập");
            setSize(350, 250);
            setLocationRelativeTo(null);
            setResizable(false);
            // Additional login dialog implementation...
        }
    }
}