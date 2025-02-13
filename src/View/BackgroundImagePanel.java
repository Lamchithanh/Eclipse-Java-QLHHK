package View;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class BackgroundImagePanel extends JPanel {
    private Image backgroundImage;

    public BackgroundImagePanel(String imagePath) {
        try {
            // Sử dụng getResource để load ảnh từ resources
            URL imageUrl = getClass().getResource(imagePath);
            if (imageUrl != null) {
                this.backgroundImage = new ImageIcon(imageUrl).getImage();
            } else {
                // Fallback nếu không tìm thấy ảnh
                System.err.println("Không tìm thấy file ảnh: " + imagePath);
                setDefaultBackground();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi load ảnh: " + e.getMessage());
            setDefaultBackground();
        }
    }

    private void setDefaultBackground() {
        setBackground(new Color(245, 245, 245));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}