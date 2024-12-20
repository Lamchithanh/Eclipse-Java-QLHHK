package View;

import Model.UserAccount;
import Service.UserAccountService;
import Service.TrangChuService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class TrangChuPanel extends JPanel {
    private UserAccount currentUser;
    private JFrame parentFrame;
    private TrangChuService trangChuService;

    public TrangChuPanel(UserAccount user, JFrame parentFrame, TrangChuService trangChuService) {
        this.currentUser = user;
        this.parentFrame = parentFrame;
        this.trangChuService = trangChuService;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 250));

        // Top Panel with Header and User Info
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainContentPanel = createMainContentPanel();
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(240, 240, 250));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // System Name
        JLabel headerLabel = new JLabel("Hệ Thống Quản Lý Hàng Không", SwingConstants.LEFT);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        headerLabel.setForeground(new Color(40, 40, 90));

        // User Info and Logout Panel
        JPanel userPanel = new JPanel();
        userPanel.setBackground(new Color(240, 240, 250));
        userPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JLabel userLabel = new JLabel("Xin chào, " + currentUser.getUsername());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        userLabel.setForeground(new Color(70, 130, 180));

        JButton logoutButton = createLogoutButton();

        userPanel.add(userLabel);
        userPanel.add(Box.createHorizontalStrut(10));
        userPanel.add(logoutButton);

        topPanel.add(headerLabel, BorderLayout.WEST);
        topPanel.add(userPanel, BorderLayout.EAST);

        return topPanel;
    }

    private JButton createLogoutButton() {
        JButton logoutButton = new JButton("Đăng Xuất");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(255, 83, 99));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69));
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        TrangChuPanel.this,
                        "Bạn có chắc chắn muốn đăng xuất?",
                        "Xác Nhận Đăng Xuất",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    // Close the current management frame
                    parentFrame.dispose();

                    // Open login frame
                    SwingUtilities.invokeLater(() -> {
                        JFrame loginFrame = new JFrame("Airline Management System");
                        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        UserAccountService userAccountService = new UserAccountService();
                        LoginPanel loginPanel = new LoginPanel(userAccountService, loginFrame);

                        loginFrame.add(loginPanel);
                        loginFrame.pack();
                        loginFrame.setLocationRelativeTo(null);
                        loginFrame.setVisible(true);
                    });
                }
            }
        });

        return logoutButton;
    }

    private JPanel createMainContentPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome Message
        JLabel welcomeLabel = new JLabel("Tổng Quan Hệ Thống", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(40, 40, 90));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        mainPanel.add(welcomeLabel, gbc);

        // Quick Stats Panel
        JPanel statsPanel = createQuickStatsPanel();
        gbc.gridy = 1;
        mainPanel.add(statsPanel, gbc);

        // Dashboard Cards
        String[] cardTitles = {
            "Quản Lý Chuyến Bay", 
            "Quản Lý Sân Bay", 
            "Quản Lý Vé Máy Bay", 
            "Quản Lý Lịch Bay",
            "Quản Lý Hãng Hàng Không", 
            "Quản Lý Nhân Viên", 
            "Quản Lý Máy Bay"
        };

        gbc.gridwidth = 1;
        for (int i = 0; i < cardTitles.length; i++) {
            gbc.gridx = i % 3;
            gbc.gridy = i / 3 + 2;
            mainPanel.add(createDashboardCard(cardTitles[i]), gbc);
        }

        return mainPanel;
    }

    private JPanel createQuickStatsPanel() {
        trangChuService.capNhatDuLieu(); // Cập nhật dữ liệu trước khi tạo bảng

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        statsPanel.setBackground(new Color(240, 240, 250));

        String[] statLabels = {"Tổng Chuyến Bay", "Tổng Nhân Viên", "Tổng Vé Đã Bán"};
        String[] statValues = {
            String.valueOf(trangChuService.layTongChuyenBay()), 
            String.valueOf(trangChuService.layTongNhanVien()), 
            String.valueOf(trangChuService.layTongVeDaBan())
        };
        Color[] statColors = {
            new Color(70, 130, 180),   // Blue
            new Color(40, 167, 69),    // Green
            new Color(220, 53, 69)     // Red
        };

        for (int i = 0; i < statLabels.length; i++) {
            JPanel statCard = new JPanel(new BorderLayout());
            statCard.setBackground(Color.WHITE);
            statCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));

            JLabel titleLabel = new JLabel(statLabels[i], SwingConstants.CENTER);
            titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            titleLabel.setForeground(statColors[i]);

            JLabel valueLabel = new JLabel(statValues[i], SwingConstants.CENTER);
            valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            valueLabel.setForeground(statColors[i]);

            statCard.add(titleLabel, BorderLayout.NORTH);
            statCard.add(valueLabel, BorderLayout.CENTER);

            statsPanel.add(statCard);
        }

        return statsPanel;
    }

    private JPanel createDashboardCard(String title) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Dimension arcs = new Dimension(20, 20); 
                int width = getWidth();
                int height = getHeight();
                Graphics2D graphics = (Graphics2D) g;
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                graphics.setColor(getBackground());
                graphics.fillRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
                graphics.setColor(new Color(200, 200, 220));
                graphics.drawRoundRect(0, 0, width-1, height-1, arcs.width, arcs.height);
            }
        };
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(200, 100));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        card.add(label);

        return card;
    }

    public void lamMoiQuickStats() {
        removeAll();

        trangChuService.capNhatDuLieu(); 

        // Tạo lại topPanel
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel mainContentPanel = createMainContentPanel(); 
        add(mainContentPanel, BorderLayout.CENTER);

        revalidate(); 
        repaint(); 
    }

}