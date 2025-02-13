package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class test extends JFrame {
    public test() {
        // Cấu hình cửa sổ chính
        setTitle("Records Editor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Tạo panel chính
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Panel nhập liệu
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Thông tin chuyến bay"));
        
        // Các ô nhập liệu
        inputPanel.add(new JLabel("Mã chuyến bay:"));
        JTextField flightCodeField = new JTextField();
        inputPanel.add(flightCodeField);

        inputPanel.add(new JLabel("Quầy:"));
        JTextField gateField = new JTextField();
        inputPanel.add(gateField);

        inputPanel.add(new JLabel("Chặng bay:"));
        JTextField routeField = new JTextField();
        inputPanel.add(routeField);

        inputPanel.add(new JLabel("Ngày bay:"));
        JTextField flightDateField = new JTextField();
        inputPanel.add(flightDateField);

        inputPanel.add(new JLabel("Sân bay:"));
        JTextField airportField = new JTextField();
        inputPanel.add(airportField);

        inputPanel.add(new JLabel("Nhà ga:"));
        JTextField terminalField = new JTextField();
        inputPanel.add(terminalField);

        inputPanel.add(new JLabel("Số ghế:"));
        JTextField seatsField = new JTextField();
        inputPanel.add(seatsField);

        inputPanel.add(new JLabel("Tình trạng:"));
        JTextField statusField = new JTextField();
        inputPanel.add(statusField);
        
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        
        // Thêm panel chính vào frame
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	test editor = new test();
            editor.setVisible(true);
        });
    }
}
