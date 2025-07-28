package view;

import view.theme.ThemedView;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class LoginView extends ThemedView {

    private JTextField txtUtilizador;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblTitulo;
    private JLabel lblUtilizador;
    private JLabel lblPassword;
    private JButton btnThemeToggle;

    public LoginView() {
        super("Login de Utilizador");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(550, 400));
        setSize(550, 400);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    @Override
    protected void initializeComponents() {
        lblTitulo = new JLabel("Login de Utilizador");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36)); // <<--- ALTERAÇÃO AQUI
        lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);

        lblUtilizador = new JLabel("Utilizador:");
        txtUtilizador = new JTextField(20);
        lblPassword = new JLabel("Password:");
        txtPassword = new JPasswordField(20);
        btnLogin = new JButton("Entrar");
        btnThemeToggle = createThemeToggleButton();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(btnThemeToggle, BorderLayout.EAST);

        JPanel loginCard = createCard();
        loginCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        loginCard.add(lblUtilizador, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        loginCard.add(txtUtilizador, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        loginCard.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        loginCard.add(txtPassword, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnLogin);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginCard, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(mainPanel);

        getRootPane().setDefaultButton(btnLogin);
        txtPassword.addActionListener(e -> btnLogin.doClick());
    }

    public String getUtilizador() {
        return txtUtilizador.getText().trim();
    }

    public String getPalavraChave() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnEntrar() {
        return btnLogin;
    }

    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    public void limparCampos() {
        txtUtilizador.setText("");
        txtPassword.setText("");
        txtUtilizador.requestFocus();
    }

    public void focarCampoUtilizador() {
        SwingUtilities.invokeLater(() -> txtUtilizador.requestFocusInWindow());
    }

    public void setFieldsEnabled(boolean enabled) {
        txtUtilizador.setEnabled(enabled);
        txtPassword.setEnabled(enabled);
        btnLogin.setEnabled(enabled);
    }

    public void mostrarErro(String mensagem) {
        showThemedMessage(this, mensagem, "Erro de Login", JOptionPane.ERROR_MESSAGE);
        limparCampos();
        focarCampoUtilizador();
    }

    public void mostrarSucesso(String mensagem) {
        showThemedMessage(this, mensagem, "Login Realizado", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarAviso(String mensagem) {
        showThemedMessage(this, mensagem, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public void mostrar() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            focarCampoUtilizador();
        });
    }

    public void fecharJanela() {
        dispose();
    }

    @Override
    protected void applyTheme() {
        super.applyTheme();

        if (lblTitulo != null) {
            lblTitulo.setHorizontalAlignment(SwingConstants.LEFT);
            lblTitulo.setForeground(currentTheme.getTextColor());
        }

        if (btnLogin != null) {
            btnLogin.setPreferredSize(new Dimension(120, 35));
        }
    }
}
