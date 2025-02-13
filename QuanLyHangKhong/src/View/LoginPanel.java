package View;

import Model.UserAccount;
import Service.TrangChuService;
import Service.UserAccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserAccountService userAccountService;
    private JFrame mainFrame;
    private TrangChuPanel trangChuPanel;  // Thêm trangChuPanel vào LoginPanel để có thể truy cập và cập nhật dữ liệu

    public LoginPanel(UserAccountService userAccountService, JFrame mainFrame) {
        this.userAccountService = userAccountService;
        this.mainFrame = mainFrame;
        mainFrame.setSize(400, 300); // Điều chỉnh kích thước hợp lý
        mainFrame.setResizable(false); // Không cho phép người dùng thay đổi kích thước
        mainFrame.setLocationRelativeTo(null); // Đặt form ở giữa màn hình

        // Set a modern, clean layout
        setLayout(new BorderLayout());

        // Create main panel with soft padding
        JPanel loginContainer = new JPanel(new GridBagLayout());
        loginContainer.setBorder(new EmptyBorder(40, 40, 40, 40));
        loginContainer.setBackground(new Color(240, 240, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH; // Giãn nở cả chiều ngang và dọc
        gbc.weightx = 1.0;

        // Title
        JLabel titleLabel = new JLabel("Airline Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(40, 40, 90));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginContainer.add(titleLabel, gbc);

        // Username Label
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginContainer.add(usernameLabel, gbc);

        // Username Field
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        loginContainer.add(usernameField, gbc);

        // Password Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginContainer.add(passwordLabel, gbc);

        // Password Field
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        loginContainer.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(200, 40)); // Thay đổi kích thước nút
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(100, 160, 210));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(70, 130, 180));
            }
        });

        loginContainer.add(loginButton, gbc);

        // Add Key Listener for Enter key
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        usernameField.addKeyListener(enterKeyListener);
        passwordField.addKeyListener(enterKeyListener);

        // Login Button Action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        // Add the login container to the main panel
        add(loginContainer, BorderLayout.CENTER);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        UserAccount user = userAccountService.login(username, password);

        if (user != null) {
            mainFrame.dispose(); // Close login window
            openManagementUI(user);
        } else {
            // Modern error dialog
            JOptionPane.showMessageDialog(
                this,
                "Invalid username or password. Please try again.",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void capNhatThongKe() {
        if (trangChuPanel != null) {
            trangChuPanel.lamMoiQuickStats();  // Cập nhật lại dữ liệu trong TrangChuPanel
        }
    }

    private void openManagementUI(UserAccount user) {
        JFrame managementFrame = new JFrame("Airline Management System");
        managementFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        managementFrame.setBounds(100, 100, 1200, 800);

        // Tạo đối tượng TrangChuService
        TrangChuService trangChuService = new TrangChuService();

        // Tạo TrangChuPanel
        trangChuPanel = new TrangChuPanel(user, managementFrame, trangChuService);

        // Tạo JTabbedPane chứa tất cả giao diện con
        JTabbedPane tabbedPane = new JTabbedPane();

        // Thêm Trang Chủ làm tab đầu tiên, truyền user, frame, và TrangChuService
        tabbedPane.addTab("Trang Chủ", trangChuPanel);

        // Thêm các giao diện con
        tabbedPane.addTab("Quản lý chuyến bay", new QuanLyChuyenBay(trangChuPanel));
        tabbedPane.addTab("Quản lý sân bay", new QuanLySanBay(trangChuPanel));
        tabbedPane.addTab("Quản lý vé máy bay", new QuanLyVeMayBay(trangChuPanel));
        tabbedPane.addTab("Quản lý lịch bay", new QuanLyLichBay(trangChuPanel));
        tabbedPane.addTab("Quản lý hãng hàng không", new QuanLyHangHangKhong(trangChuPanel));
        tabbedPane.addTab("Quản lý nhân viên", new QuanLyNhanVien(trangChuPanel));
        tabbedPane.addTab("Quản lý máy bay", new QuanLyMayBay(trangChuPanel));
        tabbedPane.addTab("Thống kê báo cáo", new ThongKePanel(user));

        // Đặt "Trang Chủ" là tab mặc định được chọn
        tabbedPane.setSelectedIndex(0);

        // Thêm tabbedPane vào giao diện
        managementFrame.add(tabbedPane, BorderLayout.CENTER);
        managementFrame.setVisible(true);
    }
}
