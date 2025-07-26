package view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginView extends JFrame {

    // Constantes de UI (cores, fontes, bordas)
    private static final Color BACKGROUND_COLOR = new Color(45, 45, 45);
    private static final Color FOREGROUND_COLOR = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color ACCENT_COLOR = new Color(0, 120, 215);
    private static final Color ACCENT_BACKGROUND_COLOR = new Color(0, 84, 153);
    private static final Color BORDER_COLOR = new Color(70, 70, 70);
    private static final Color BORDER_ACCENT_COLOR = ACCENT_COLOR;

    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private static final Border FIELD_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(5, 10, 5, 10)
    );

    private final JTextField txtUtilizador;
    private final JPasswordField txtPalavraChave;
    private final JButton btnEntrar;

    public LoginView() {
        setTitle("Projeto-GSA-Login");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridLayout(5, 1, 10, 10));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));
        painel.setBackground(BACKGROUND_COLOR); // Fundo do painel

        JLabel lblUser = new JLabel("Utilizador (Email):");
        lblUser.setForeground(TEXT_COLOR);
        lblUser.setFont(MAIN_FONT);
        painel.add(lblUser);

        txtUtilizador = new JTextField();
        txtUtilizador.setBackground(FOREGROUND_COLOR); // Fundo do campo
        txtUtilizador.setForeground(TEXT_COLOR); // Texto do campo
        txtUtilizador.setCaretColor(TEXT_COLOR);
        txtUtilizador.setBorder(FIELD_BORDER); // Borda do campo
        txtUtilizador.setFont(MAIN_FONT);
        painel.add(txtUtilizador);

        JLabel lblSenha = new JLabel("Password:");
        lblSenha.setForeground(TEXT_COLOR);
        lblSenha.setFont(MAIN_FONT);
        painel.add(lblSenha);

        txtPalavraChave = new JPasswordField();
        txtPalavraChave.setBackground(FOREGROUND_COLOR); // Fundo do campo
        txtPalavraChave.setForeground(TEXT_COLOR); // Texto do campo
        txtPalavraChave.setCaretColor(TEXT_COLOR);
        txtPalavraChave.setBorder(FIELD_BORDER); // Borda do campo
        txtPalavraChave.setFont(MAIN_FONT);
        painel.add(txtPalavraChave);

        btnEntrar = new JButton("Login");
        estilizarBotao(btnEntrar);
        painel.add(btnEntrar);

        setContentPane(painel);
    }

    private void estilizarBotao(JButton botao) {
        botao.setBackground(ACCENT_BACKGROUND_COLOR);
        botao.setForeground(TEXT_COLOR);
        botao.setFocusPainted(false);
        botao.setFont(BUTTON_FONT);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_ACCENT_COLOR, 2),
                new EmptyBorder(10, 20, 10, 20)
        ));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public String getUtilizador() {
        return txtUtilizador.getText();
    }

    public String getPalavraChave() {
        return new String(txtPalavraChave.getPassword());
    }

    public JButton getBtnEntrar() {
        return btnEntrar;
    }

    public void exibirMensagem(String mensagem, String titulo, int tipoMensagem) {
        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoMensagem);
    }
}
