package Components;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class ButtonHelper extends JButton {
    public ButtonHelper(String text, Color bgColor){
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.white);
        setOpaque(true);
        setBorderPainted(false);
        setPreferredSize(new Dimension(130, 35));
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(bgColor);
            }
        });
    }
}
