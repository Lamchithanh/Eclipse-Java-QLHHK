package View;

import Model.UserAccount;
import Service.TrangChuService;
import Service.UserAccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserAccountService userAccountService;
    private JFrame mainFrame;
    private TrangChuPanel trangChuPanel;
    
    // Màu sắc chính
    private Color primaryColor = new Color(25, 118, 210);      // Xanh dương đậm
    private Color secondaryColor = new Color(251, 128, 185);    // Xanh dương nhạt
    private Color accentColor = new Color(57, 123, 24);        // Cam
    private Color textColor = new Color(55, 71, 79);           // Xám đen
    private Color backgroundColor = new Color(240, 242, 245);  // Xám nhạt

    public LoginPanel(UserAccountService userAccountService, JFrame mainFrame) {
        this.userAccountService = userAccountService;
        this.mainFrame = mainFrame;
        setupMainFrame();
        createLoginInterface();

        addEnterKeyListener();
    }
    
    private void addEnterKeyListener() {
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
    
        // Thêm KeyListener cho cả hai trường
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);
    }

    private void setupMainFrame() {
        mainFrame.setSize(1200, 700);
        mainFrame.setMinimumSize(new Dimension(1000, 600));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createLoginInterface() {
        setLayout(new GridBagLayout());
        setBackground(backgroundColor);

        // Container chính
        JPanel mainContainer = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, primaryColor,
                    getWidth(), getHeight(), secondaryColor
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }
        };
        mainContainer.setOpaque(false);
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Panel bên trái (hình ảnh)
        JPanel leftPanel = createLeftPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        mainContainer.add(leftPanel, gbc);

        // Panel bên phải (form đăng nhập)
        JPanel rightPanel = createRightPanel();
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        mainContainer.add(rightPanel, gbc);

        add(mainContainer);
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ các đường bay
                g2d.setColor(new Color(255, 255, 255, 30));
                BasicStroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
                    0, new float[]{9}, 0);
                g2d.setStroke(dashed);
                
                for (int i = 0; i < 7; i++) {
                    int startX = -50 + (i * 40);
                    int startY = getHeight() - (i * 70);
                    int endX = getWidth() + 50;
                    int endY = 0 + (i * 70);
                    g2d.drawLine(startX, startY, endX, endY);
                }
            }
        };
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 20, 20, 20);

        // Logo máy bay
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream("/image/airplane9.png"));
            BufferedImage resizedImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resizedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(originalImage, 0, 0, 250, 250, null);
            g.dispose();
            
            ImageIcon scaledIcon = new ImageIcon(resizedImage);
            JLabel airplaneLogo = new JLabel(scaledIcon);
            panel.add(airplaneLogo, gbc);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Tiêu đề chính
        JLabel mainTitle = new JLabel("HỆ THỐNG QUẢN LÝ");
        mainTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        mainTitle.setForeground(Color.WHITE);
        panel.add(mainTitle, gbc);

        // Tiêu đề phụ
        JLabel subTitle = new JLabel("SÂN BAY");
        subTitle.setFont(new Font("Segoe UI", Font.BOLD, 48));
        subTitle.setForeground(accentColor);
        panel.add(subTitle, gbc);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Vẽ background bo góc
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                super.paintComponent(g);
            }
        };
        panel.setOpaque(false); // Quan trọng để hiệu ứng bo góc hiển thị
        
        // Thêm border để tạo khoảng cách
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(30, 40, 30, 40),
            BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(230, 230, 230))
        ));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
    
        // Tiêu đề đăng nhập
        JLabel loginTitle = new JLabel("Đăng Nhập Hệ Thống");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        loginTitle.setForeground(primaryColor);
        loginTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(loginTitle, gbc);
    
        // Tên đăng nhập
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(createInputLabel("Tên đăng nhập"), gbc);
        usernameField = createStyledTextField();
        panel.add(usernameField, gbc);
    
        // Mật khẩu
        panel.add(createInputLabel("Mật khẩu"), gbc);
        passwordField = createStyledPasswordField();
        panel.add(passwordField, gbc);
    
        // Nút đăng nhập
        JButton loginButton = createLoginButton();
        gbc.insets = new Insets(30, 0, 10, 0);
        panel.add(loginButton, gbc);
    
        // Điều hướng giữa username và password
        usernameField.addActionListener(e -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(e -> performLogin());
    
        return panel;
    }


    private JLabel createInputLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(100, 100, 100));
        return label;
    }

    private JLabel createIconLabel(String text, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, size));
        label.setForeground(color);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setPreferredSize(new Dimension(350, 50));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setBackground(Color.WHITE);
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return field;
    }

    private JButton createLoginButton() {
        JButton button = new JButton("ĐĂNG NHẬP") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, primaryColor,
                    getWidth(), 0, new Color(41, 128, 185)
                );
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10));
                
                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(300, 45));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(accentColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(primaryColor);
            }
        });

        // Add action listener
        button.addActionListener(e -> performLogin());

        return button;
    }

    private void showErrorDialog(String message) {
        JDialog dialog = new JDialog(mainFrame, "Thông báo", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);

        // Icon cảnh báo
         JLabel iconLabel = new JLabel(UIManager.getIcon("OptionPane.warningIcon"));
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        iconLabel.setForeground(accentColor);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(iconLabel, BorderLayout.NORTH);

        // Message
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentPanel.add(messageLabel, BorderLayout.CENTER);

        // OK button
        JButton okButton = new JButton("Đồng ý");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.setBackground(primaryColor);
        okButton.setForeground(Color.WHITE);
        okButton.setBorderPainted(false);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(contentPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setVisible(true);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        UserAccount user = userAccountService.login(username, password);

        if (user != null) {
            mainFrame.dispose();
            openManagementUI(user);
        } else {
            showErrorDialog("Tên đăng nhập hoặc mật khẩu không chính xác!");
        }
    }

    public void capNhatThongKe() {
        if (trangChuPanel != null) {
            trangChuPanel.lamMoiQuickStats();
        }
    }

    private void openManagementUI(UserAccount user) {
        JFrame managementFrame = new JFrame("Hệ Thống Quản Lý Vé Máy Bay");
        managementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managementFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        TrangChuService trangChuService = new TrangChuService();
        trangChuPanel = new TrangChuPanel(user, managementFrame, trangChuService);
        
        // Thêm các panel quản lý
        QuanLyDatVeView datVePanel = new QuanLyDatVeView(trangChuPanel);
        QuanLyChuyenBay chuyenBayPanel = new QuanLyChuyenBay(trangChuPanel);
        QuanLySanBayView sanBayPanel = new QuanLySanBayView(trangChuPanel);
        QuanLyVeMayBay veMayBayPanel = new QuanLyVeMayBay(trangChuPanel);
        QuanLyLichBay lichBayPanel = new QuanLyLichBay(trangChuPanel);
        QuanLyHangHangKhong hangHangKhongPanel = new QuanLyHangHangKhong(trangChuPanel);
        QuanLyNhanVien nhanVienPanel = new QuanLyNhanVien(trangChuPanel);
        QuanLyMayBay mayBayPanel = new QuanLyMayBay(trangChuPanel);
        ThongKePanel thongKePanel = new ThongKePanel(user);

        managementFrame.add(trangChuPanel, BorderLayout.CENTER);
        managementFrame.setVisible(true);
    }
}