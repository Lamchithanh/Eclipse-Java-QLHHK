package View;

import Model.UserAccount;
import Service.UserAccountService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class QuanLyTaiKhoanNhanVienPanel extends JPanel {
    private UserAccountService userAccountService;
    private JFrame parentFrame;
    private Color primaryColor = new Color(40, 40, 90);
    private Color secondaryColor = new Color(240, 240, 240);
    private Color accentColor = new Color(70, 130, 180);
    private Color tableHeaderColor = new Color(70, 130, 180);

    public QuanLyTaiKhoanNhanVienPanel(JFrame parentFrame) {
        this.userAccountService = new UserAccountService();
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout());
        setBackground(secondaryColor);

        // Tiêu đề
        JLabel titleLabel = new JLabel("Quản Lý Tài Khoản Nhân Viên", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Bảng tài khoản nhân viên
        JScrollPane tableScrollPane = createEmployeeTable();
        add(tableScrollPane, BorderLayout.CENTER);

        // Nút thêm và xóa tài khoản
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JScrollPane createEmployeeTable() {
        // Dữ liệu mẫu (bạn có thể thay bằng dữ liệu thực từ database)
        String[] columnNames = {"ID", "Tên đăng nhập", "Họ và tên", "Vai trò"};
        Object[][] data = {
            {1, "nhanvien1", "Nguyễn Văn A", "Nhân Viên"},
            {2, "nhanvien2", "Trần Thị B", "Nhân Viên"},
            {3, "quanly1", "Phạm Văn C", "Quản Lý"}
        };

        JTable employeeTable = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Vô hiệu hóa chỉnh sửa trực tiếp trên bảng
            }
        };

        // Tùy chỉnh bảng
        employeeTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        employeeTable.getTableHeader().setBackground(tableHeaderColor);
        employeeTable.getTableHeader().setForeground(Color.WHITE);
        employeeTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        employeeTable.setRowHeight(30);

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(secondaryColor);

        // Nút thêm tài khoản
        JButton addButton = new JButton("Thêm Tài Khoản");
        styleButton(addButton, new Color(40, 167, 69)); // Màu xanh lá
        addButton.addActionListener(e -> {
            // Hiển thị TaiKhoanNhanVien
            JFrame registerFrame = new JFrame("Đăng ký tài khoản nhân viên");
            registerFrame.setContentPane(new TaiKhoanNhanVien(userAccountService, registerFrame)); 
            registerFrame.pack();
            registerFrame.setVisible(true);
        });

        // Nút xóa tài khoản
        JButton deleteButton = new JButton("Xóa Tài Khoản");
        styleButton(deleteButton, new Color(220, 53, 69)); // Màu đỏ
        deleteButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(parentFrame, "Chức năng xóa tài khoản chưa được triển khai!");
        });

        // Nút cập nhật tài khoản
        JButton updateButton = new JButton("Cập Nhật Tài Khoản");
        styleButton(updateButton, new Color(255, 193, 7)); // Màu vàng
        updateButton.addActionListener(e -> showUpdateDialog());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);

        return buttonPanel;
    }

    private void showUpdateDialog() {
        JTextField idField = new JTextField(10);
        JTextField usernameField = new JTextField(20);
        JTextField passwordField = new JTextField(20);
        JTextField roleField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Tên đăng nhập:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mật khẩu:"));
        panel.add(passwordField);
        panel.add(new JLabel("Vai trò:"));
        panel.add(roleField);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Cập Nhật Tài Khoản", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleField.getText();

            UserAccountService service = new UserAccountService();
            boolean success = service.updateUserAccount(id, username, password, role);

            if (success) {
                JOptionPane.showMessageDialog(parentFrame, "Cập nhật tài khoản thành công!");
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Cập nhật tài khoản thất bại!");
            }
        }
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}