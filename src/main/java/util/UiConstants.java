package util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class UiConstants {

    // Cores Primárias (Base do Backoffice)
    public static final Color BACKGROUND_COLOR = new Color(58, 64, 78);
    public static final Color FOREGROUND_COLOR = new Color(74, 82, 94);
    public static final Color BORDER_COLOR = new Color(40, 44, 52);

    // Cores de Destaque/Acento
    public static final Color ACCENT_COLOR = new Color(97, 107, 128); // Uma cor de acento mais geral
    public static final Color ACCENT_BACKGROUND_COLOR = new Color(81, 90, 106); // Cor de fundo para botões primários
    public static final Color BORDER_ACCENT_COLOR = new Color(100, 149, 237); // Cor azul para borda de botões

    // Cores de Texto
    public static final Color TEXT_COLOR = Color.WHITE;
    public static final Color TEXT_ACCENT_COLOR = new Color(200, 200, 200);

    // Cores de Mensagens
    public static final Color SUCCESS_COLOR = new Color(46, 204, 113);
    public static final Color ERROR_COLOR = new Color(231, 76, 60);
    public static final Color WARNING_COLOR = new Color(241, 196, 15);
    public static final Color INFO_COLOR = new Color(52, 152, 219);

    // Fontes
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // Bordas Padrão
    public static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(BORDER_COLOR, 1);
    public static final Border ACCENT_BORDER = BorderFactory.createLineBorder(BORDER_ACCENT_COLOR, 1);
    public static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_ACCENT_COLOR.darker(), 1), // Borda mais escura para campos
            new EmptyBorder(5, 5, 5, 5) // Preenchimento interno
    );

    // Espaçamento Padrão
    public static final Dimension VERTICAL_SPACER_SMALL = new Dimension(0, 5);
    public static final Dimension VERTICAL_SPACER_MEDIUM = new Dimension(0, 10);
    public static final Dimension HORIZONTAL_SPACER_SMALL = new Dimension(5, 0);
    public static final Dimension HORIZONTAL_SPACER_MEDIUM = new Dimension(10, 0);
}