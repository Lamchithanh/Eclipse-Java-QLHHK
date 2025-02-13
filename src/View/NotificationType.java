package View;

import java.awt.Color;

public enum NotificationType {
    SUCCESS(new Color(46, 204, 113), "✓"),
    WARNING(new Color(241, 196, 15), "!"),
    ERROR(new Color(231, 76, 60), "×"),
    INFO(new Color(52, 152, 219), "i");

    private final Color color;
    private final String symbol;

    NotificationType(Color color, String symbol) {
        this.color = color;
        this.symbol = symbol;
    }

    public Color getColor() {
        return color;
    }

    public String getSymbol() {
        return symbol;
    }
}