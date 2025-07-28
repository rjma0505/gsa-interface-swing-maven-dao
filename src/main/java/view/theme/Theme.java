package view.theme; // Certifica-te que este package está correto

import javax.swing.border.Border;
import java.awt.*;

public interface Theme {
    String getName();

    // Cores Primárias
    Color getBackgroundColor();
    Color getForegroundColor();
    Color getBorderColor();
    Color getCardBackgroundColor(); // Novo: para painéis tipo cartão

    // Cores de Destaque/Acento
    Color getAccentColor();
    Color getAccentBackgroundColor();
    Color getAccentBorderColor();
    Color getButtonHoverColor(); // Novo: para efeito hover do botão
    Color getButtonPressedColor(); // Novo: para efeito pressed do botão

    // Cores de Texto
    Color getTextColor();
    Color getTextAccentColor();
    Color getButtonTextColor(); // Novo: cor de texto específica para botões

    // Cores de Mensagens
    Color getSuccessColor();
    Color getErrorColor();
    Color getWarningColor();
    Color getInfoColor();

    // Fontes
    Font getMainFont();
    Font getTitleFont();
    Font getSubtitleFont();
    Font getButtonFont();

    // Bordas Padrão
    Border getDefaultBorder();
    Border getAccentBorder();
    Border getFieldBorder();
    Border getCardBorder(); // Novo: para painéis de cartão
}