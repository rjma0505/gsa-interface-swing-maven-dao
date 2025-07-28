package view.theme;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LightTheme implements Theme {
    @Override
    public String getName() {
        return "Light";
    }

    // Cores Primárias
    @Override
    public Color getBackgroundColor() {
        return new Color(240, 240, 240); // Fundo claro
    }

    @Override
    public Color getForegroundColor() {
        return new Color(255, 255, 255); // Branco para componentes
    }

    @Override
    public Color getBorderColor() {
        return new Color(180, 180, 180); // Borda clara para elementos gerais
    }

    @Override
    public Color getCardBackgroundColor() {
        return new Color(230, 230, 230); // Ligeiramente mais escuro que o fundo para cartões
    }

    // Cores de Destaque/Acento
    @Override
    public Color getAccentColor() {
        return new Color(30, 144, 255); // Azul Dodger para acento
    }

    @Override
    public Color getAccentBackgroundColor() {
        return new Color(65, 105, 225); // Azul Royal para fundo de botão
    }

    @Override
    public Color getAccentBorderColor() {
        return getAccentColor(); // Cor de acento para bordas de botão
    }

    @Override
    public Color getButtonHoverColor() {
        return getAccentBackgroundColor().brighter(); // Mais claro no hover
    }

    @Override
    public Color getButtonPressedColor() {
        return getAccentBackgroundColor().darker(); // Mais escuro ao pressionar
    }

    // Cores de Texto
    @Override
    public Color getTextColor() {
        return new Color(50, 50, 50); // Texto escuro
    }

    @Override
    public Color getTextAccentColor() {
        return new Color(100, 100, 100); // Texto escuro ligeiramente mais claro
    }

    @Override
    public Color getButtonTextColor() {
        return Color.WHITE; // Texto branco para botões
    }

    // Cores de Mensagens
    @Override
    public Color getSuccessColor() {
        return new Color(39, 174, 96);
    }

    @Override
    public Color getErrorColor() {
        return new Color(192, 57, 43);
    }

    @Override
    public Color getWarningColor() {
        return new Color(243, 156, 18);
    }

    @Override
    public Color getInfoColor() {
        return new Color(41, 128, 185);
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