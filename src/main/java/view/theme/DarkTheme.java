package view.theme;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DarkTheme implements Theme {
    @Override
    public String getName() {
        return "Dark";
    }

    // Cores Primárias
    @Override
    public Color getBackgroundColor() {
        return new Color(45, 45, 45); // Dark background
    }

    @Override
    public Color getForegroundColor() {
        return new Color(60, 63, 65); // Slightly lighter for components
    }

    @Override
    public Color getBorderColor() {
        return new Color(70, 70, 70); // Dark border for general elements
    }

    @Override
    public Color getCardBackgroundColor() {
        return new Color(50, 50, 50); // Slightly lighter than background for cards
    }

    // Cores de Destaque/Acento
    @Override
    public Color getAccentColor() {
        return new Color(0, 120, 215); // Blue accent
    }

    @Override
    public Color getAccentBackgroundColor() {
        return new Color(0, 84, 153); // Darker blue for button background
    }

    @Override
    public Color getAccentBorderColor() {
        return getAccentColor(); // Accent color for button borders
    }

    @Override
    public Color getButtonHoverColor() {
        return getAccentBackgroundColor().brighter(); // Lighter on hover
    }

    @Override
    public Color getButtonPressedColor() {
        return getAccentBackgroundColor().darker(); // Darker on press
    }

    // Cores de Texto
    @Override
    public Color getTextColor() {
        return new Color(220, 220, 220); // Light text
    }

    @Override
    public Color getTextAccentColor() {
        return new Color(180, 180, 180); // Slightly darker light text
    }

    @Override
    public Color getButtonTextColor() {
        return Color.WHITE; // White text for buttons
    }

    // Cores de Mensagens
    @Override
    public Color getSuccessColor() {
        return new Color(46, 204, 113);
    }

    @Override
    public Color getErrorColor() {
        return new Color(231, 76, 60);
    }

    @Override
    public Color getWarningColor() {
        return new Color(241, 196, 15);
    }

    @Override
    public Color getInfoColor() {
        return new Color(52, 152, 219);
    }

    // Fontes
    @Override
    public Font getMainFont() {
        return new Font("Segoe UI", Font.PLAIN, 14);
    }

    @Override
    public Font getTitleFont() {
        return new Font("Segoe UI", Font.BOLD, 24);
    }

    @Override
    public Font getSubtitleFont() {
        return new Font("Segoe UI", Font.BOLD, 16);
    }

    @Override
    public Font getButtonFont() {
        return new Font("Segoe UI", Font.BOLD, 14);
    }

    // Bordas Padrão
    @Override
    public Border getDefaultBorder() {
        return BorderFactory.createLineBorder(getBorderColor(), 1);
    }

    @Override
    public Border getAccentBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getAccentBorderColor(), 2),
                new EmptyBorder(10, 20, 10, 20)
        );
    }

    @Override
    public Border getFieldBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBorderColor(), 1),
                new EmptyBorder(5, 10, 5, 10)
        );
    }

    @Override
    public Border getCardBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(getBorderColor(), 1),
                new EmptyBorder(10, 10, 10, 10)
        );
    }
}