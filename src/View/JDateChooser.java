package View;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class JDateChooser extends JPanel {
    private Date date;
    private JTextField dateField;
    private JButton chooseButton;

    public JDateChooser() {
        setLayout(new BorderLayout());
        dateField = new JTextField(10);
        dateField.setEditable(false);
        
        chooseButton = new JButton("...");
        chooseButton.addActionListener(e -> {
            // Mở dialog chọn ngày
            Date selectedDate = showDatePickerDialog();
            if (selectedDate != null) {
                setDate(selectedDate);
            }
        });

        add(dateField, BorderLayout.CENTER);
        add(chooseButton, BorderLayout.EAST);
    }

    private Date showDatePickerDialog() {
        // Đơn giản hóa việc chọn ngày
        // Bạn có thể thay thế bằng JCalendar của bạn
        return new Date();
    }

    public void setDate(Date date) {
        this.date = date;
        dateField.setText(date.toString());
    }

    public Date getDate() {
        return date;
    }
}