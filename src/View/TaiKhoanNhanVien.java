package View;

import Model.UserAccount;
import Service.UserAccountService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaiKhoanNhanVien extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private UserAccountService userAccountService;
    private JFrame registerFrame;

    public TaiKhoanNhanVien(UserAccountService userAccountService, JFrame registerFrame) {
        this.userAccountService = userAccountService;
        this.registerFrame = registerFrame;
        registerFrame.setSize(450, 400);
        registerFrame.setResizable(false);
        registerFrame.setLocationRelativeTo(null);

        // Thiết lập bố cục
        setLayout(new BorderLayout());

        // Tạo panel chính
        JPanel registerContainer = new JPanel(new GridBagLayout());
        registerContainer.setBorder(new EmptyBorder(40, 40, 40, 40));
        registerContainer.setBackground(new Color(240, 240, 250));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Tiêu đề
        JLabel titleLabel = new JLabel("Đăng ký tài khoản nhân viên mới", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(40, 40, 90));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        registerContainer.add(titleLabel, gbc);

        // Tên người dùng
        addFormField(registerContainer, "Tên người dùng:", usernameField = new JTextField(20), gbc, 1);

        // Mật khẩu
        addFormField(registerContainer, "Mật khẩu:", passwordField = new JPasswordField(20), gbc, 2);

        // Xác nhận mật khẩu
        addFormField(registerContainer, "Xác nhận mật khẩu:", confirmPasswordField = new JPasswordField(20), gbc, 3);

        // Vai trò được cố định là "staff", không cần combo box
        // Bỏ qua phần chọn vai trò và gán cứng vai trò là "staff"

        // Nút đăng ký
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        JButton registerButton = new JButton("Đăng ký tài khoản");
        styleButton(registerButton);
        registerContainer.add(registerButton, gbc);

        // Sự kiện nút đăng ký
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performRegistration();
            }
        });

        // Thêm panel đăng ký vào panel chính
        add(registerContainer, BorderLayout.CENTER);
    }

    private void addFormField(JPanel container, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridwidth = 1;
        gbc.gridy = row;

        // Nhãn
        gbc.gridx = 0;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        container.add(label, gbc);

        // Ô nhập liệu
        gbc.gridx = 1;
        if (field instanceof JTextField) {
            ((JTextField) field).setFont(new Font("Segoe UI", Font.PLAIN, 14));
            field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
        }
        container.add(field, gbc);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hiệu ứng hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 160, 210));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    private void performRegistration() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = "staff"; // Vai trò cố định là "staff"

        // Kiểm tra dữ liệu
        if (username.isEmpty() || password.isEmpty()) {
            showError("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Mật khẩu không khớp.");
            return;
        }

        // Đăng ký tài khoản sử dụng UserAccountService
        boolean success = userAccountService.register(username, password, role);
        if (success) {
            JOptionPane.showMessageDialog(
                    this,
                    "Tài khoản đã được đăng ký thành công!",
                    "Đăng ký thành công",
                    JOptionPane.INFORMATION_MESSAGE
            );
            registerFrame.dispose();
        } else {
            showError("Đăng ký tài khoản thất bại. Tên người dùng có thể đã tồn tại.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Lỗi đăng ký",
                JOptionPane.ERROR_MESSAGE
        );
    }
}