package View;

import Service.UserAccountService;

import javax.swing.*;

public class MainApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Tạo JFrame cho giao diện Login
                JFrame loginFrame = new JFrame("Login");
                loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                loginFrame.setBounds(100, 100, 400, 200);

                // Tạo UserAccountService để quản lý thông tin tài khoản
                UserAccountService userAccountService = new UserAccountService();
                LoginPanel loginPanel = new LoginPanel(userAccountService, loginFrame);

                loginFrame.add(loginPanel);
                loginFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
