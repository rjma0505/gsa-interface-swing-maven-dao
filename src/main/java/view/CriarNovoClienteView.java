package view;

import view.theme.ThemedView;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriarNovoClienteView extends ThemedView {

    private JTextField txtNomeCompleto;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JButton btnCriarCliente;
    private JButton btnVoltar;
    private JButton btnThemeToggle;

    public CriarNovoClienteView() {
        super("Criar Novo Cliente");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        txtNomeCompleto = createStyledTextField(25);
        txtTelefone = createStyledTextField(15);
        txtEmail = createStyledTextField(25);
        btnCriarCliente = createStyledButton("Criar Cliente");
        btnVoltar = createStyledButton("Voltar");
        btnThemeToggle = createThemeToggleButton();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(btnThemeToggle, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        gbc.gridy++;
        mainPanel.add(createStyledLabel("Nome Completo:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtNomeCompleto, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Telefone:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtTelefone, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(btnCriarCliente, gbc);

        gbc.gridy++;
        mainPanel.add(btnVoltar, gbc);

        this.add(mainPanel);
    }

    public String getNomeCompleto() {
        return txtNomeCompleto.getText();
    }

    public String getTelefone() {
        return txtTelefone.getText();
    }

    public String getEmail() {
        return txtEmail.getText();
    }

    public void adicionarAcaoCriarCliente(ActionListener listener) {
        btnCriarCliente.addActionListener(listener);
    }

    public void adicionarAcaoVoltar(ActionListener listener) {
        btnVoltar.addActionListener(listener);
    }

    public boolean validarCampos() {
        String nome = getNomeCompleto();
        String telefone = getTelefone();
        String email = getEmail();

        if (nome.trim().isEmpty()) {
            exibirMensagem("O campo 'Nome Completo' é obrigatório.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (telefone.trim().isEmpty()) {
            exibirMensagem("O campo 'Telefone' é obrigatório.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!telefone.matches("^\\d{9,}$")) {
            exibirMensagem("O campo 'Telefone' deve conter apenas números e ter pelo menos 9 dígitos.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (email.trim().isEmpty()) {
            exibirMensagem("O campo 'Email' é obrigatório.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            exibirMensagem("O campo 'Email' não tem um formato válido.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    public void exibirMensagem(String message, String title, int messageType) {
        showThemedMessage(this, message, title, messageType);
    }
}