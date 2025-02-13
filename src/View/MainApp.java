package View;

import Service.UserAccountService;
import Model.UserAccount;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.border.EmptyBorder;

@SuppressWarnings("unused")
public class MainApp extends JFrame {
    // Màu sắc chính
    private static final Color TOOLBAR_BG = new Color(47, 54, 64);           // Xám đen
    private static final Color SIDEBAR_BG = new Color(47, 54, 64);           // Xám đen
    private static final Color SIDEBAR_HOVER = new Color(67, 74, 84);        // Xám đen sáng

    // Màu nút (Gradients)
    private static final Color BTN_PRIMARY = new Color(0, 151, 230);        // Xanh dương
    private static final Color BTN_PRIMARY_HOVER = new Color(0, 184, 255);  // Xanh dương sáng
    private static final Color BTN_SUCCESS = new Color(46, 213, 115);       // Xanh lá
    private static final Color BTN_SUCCESS_HOVER = new Color(55, 235, 130); // Xanh lá sáng
    private static final Color BTN_DANGER = new Color(255, 71, 87);         // Đỏ
    private static final Color BTN_DANGER_HOVER = new Color(255, 99, 112);  // Đỏ sáng
    private static final Color BTN_WARNING = new Color(255, 165, 2);        // Cam
    private static final Color BTN_WARNING_HOVER = new Color(255, 182, 39); // Cam sáng
    
    // Kích thước và bo góc
    private static final int BUTTON_RADIUS = 8;
    private static final Dimension SIDEBAR_BUTTON_SIZE = new Dimension(230, 40);
    private static final Dimension TOOLBAR_BUTTON_SIZE = new Dimension(100, 35);
    
    // Kích thước mặc định của cửa sổ
    private static final int DEFAULT_WIDTH = 1200;
    private static final int DEFAULT_HEIGHT = 800;
    
    // Các thành phần giao diện
    private JPanel contentPane;
    private JPanel sidebarPanel;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private UserAccount currentUser;
    private UserAccountService userAccountService;

    // Constructor chính
    public MainApp(UserAccount user) {
        this.currentUser = user;
        this.userAccountService = new UserAccountService();
        initializeFrame();
        setupComponents();
    }

    // Khởi tạo cửa sổ chính
    private void initializeFrame() {
        setTitle("Hệ Thống Quản Lý Chuyến Bay");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Tạo contentPane chính
        contentPane = new JPanel(new BorderLayout());
        // contentPane.setBackground(BACKGROUND_COLOR);
        setContentPane(contentPane);
    }

    // Thiết lập các thành phần giao diện
    private void setupComponents() {
        // Tạo sidebar
        sidebarPanel = createSidebar();
        
        // Tạo panel chính với CardLayout
        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        // mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Thêm các panel chức năng
        mainPanel.add(new ThongKePanel(currentUser), "THONGKE");
        // Thêm các panel khác tùy theo chức năng
        
        // Tạo thanh công cụ phía trên
        JPanel toolbarPanel = createToolbar();
        
        // Thêm các thành phần vào contentPane
        contentPane.add(toolbarPanel, BorderLayout.NORTH);
        contentPane.add(sidebarPanel, BorderLayout.WEST);
        contentPane.add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createModernButton(String text, Color bgColor, Color hoverColor, Dimension size) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Vẽ gradient
                GradientPaint gradient;
                if (getModel().isPressed()) {
                    gradient = new GradientPaint(0, 0, bgColor.darker(), 0, getHeight(), bgColor);
                } else if (getModel().isRollover()) {
                    gradient = new GradientPaint(0, 0, hoverColor, 0, getHeight(), hoverColor.darker());
                } else {
                    gradient = new GradientPaint(0, 0, bgColor, 0, getHeight(), bgColor.darker());
                }
                
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), BUTTON_RADIUS, BUTTON_RADIUS));

                // Vẽ viền sáng
                if (getModel().isRollover() && !getModel().isPressed()) {
                    g2d.setColor(new Color(255, 255, 255, 50));
                    g2d.setStroke(new BasicStroke(1f));
                    g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth()-2, getHeight()-2, BUTTON_RADIUS, BUTTON_RADIUS));
                }

                // Vẽ text
                FontMetrics metrics = g2d.getFontMetrics(getFont());
                Rectangle stringBounds = metrics.getStringBounds(getText(), g2d).getBounds();
                int x = (getWidth() - stringBounds.width) / 2;
                int y = (getHeight() - stringBounds.height) / 2 + metrics.getAscent();

                g2d.setFont(getFont());
                g2d.setColor(Color.WHITE);
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(size);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // Tạo thanh công cụ
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(TOOLBAR_BG);
        toolbar.setPreferredSize(new Dimension(getWidth(), 50));
        toolbar.setBorder(new EmptyBorder(5, 15, 5, 15));

        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userInfo.setOpaque(false);

        // Icon người dùng (tùy chọn)
        JLabel userIcon = new JLabel("👤");
        userIcon.setForeground(Color.WHITE);
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JLabel userLabel = new JLabel(currentUser.getUsername());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton logoutButton = createToolbarButton("Đăng xuất");

        userInfo.add(userIcon);
        userInfo.add(Box.createHorizontalStrut(5));
        userInfo.add(userLabel);
        userInfo.add(Box.createHorizontalStrut(15));
        userInfo.add(logoutButton);

        // Logo ở góc trái toolbar
        JLabel logoLabel = new JLabel("✈ Flight Manager");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        toolbar.add(logoLabel, BorderLayout.WEST);
        toolbar.add(userInfo, BorderLayout.EAST);
        
        return toolbar;
    }

    // Tạo sidebar
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(250, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Thêm các nút menu với icon
        addSidebarButton(sidebar, "📊 Thống Kê", "THONGKE");
        addSidebarButton(sidebar, "✈ Quản Lý Chuyến Bay", "CHUYENBAY");
        addSidebarButton(sidebar, "🎫 Quản Lý Đặt Vé", "DATVE");
        addSidebarButton(sidebar, "📝 Báo Cáo", "BAOCAO");

        return sidebar;
    }

    // Tạo nút cho sidebar
    private void addSidebarButton(JPanel sidebar, String text, String cardName) {
        JButton button = createModernButton(text, SIDEBAR_BG, SIDEBAR_HOVER, SIDEBAR_BUTTON_SIZE);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.addActionListener(e -> cardLayout.show(mainPanel, cardName));
        
        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(5));
    }

    private JButton createToolbarButton(String text) {
        return createModernButton(text, BTN_DANGER, BTN_DANGER_HOVER, TOOLBAR_BUTTON_SIZE);
    }

    // Xử lý đăng xuất
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc chắn muốn đăng xuất?",
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            showLoginFrame();
        }
    }

    // Hiển thị màn hình đăng nhập
    private void showLoginFrame() {
        JFrame loginFrame = new JFrame("Đăng nhập");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setBounds(100, 100, 400, 300);
        loginFrame.setLocationRelativeTo(null);
        
        LoginPanel loginPanel = new LoginPanel(userAccountService, loginFrame);
        loginFrame.add(loginPanel);
        loginFrame.setVisible(true);
    }

    // Main method
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame loginFrame = new JFrame("Đăng nhập");
            loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            loginFrame.setBounds(100, 100, 400, 300);
            loginFrame.setLocationRelativeTo(null);

            UserAccountService userAccountService = new UserAccountService();
            LoginPanel loginPanel = new LoginPanel(userAccountService, loginFrame);
            
            loginFrame.add(loginPanel);
            loginFrame.setVisible(true);
        });
    }
}