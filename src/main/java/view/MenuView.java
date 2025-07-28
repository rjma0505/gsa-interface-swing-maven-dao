package view;

import view.theme.ThemedView;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuView extends ThemedView {

    private JButton btnInserir;
    private JButton btnListar;
    private JButton btnLogout;
    private JButton btnCriarCliente;
    private JButton btnThemeToggle;

    public MenuView() {
        super("Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        btnInserir = new JButton("Inserir Veículo");
        btnListar = new JButton("Listar Veículos");
        btnLogout = new JButton("Logout");
        btnCriarCliente = new JButton("Criar Novo Cliente");
        btnThemeToggle = createThemeToggleButton();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = createTitleLabel("Menu Principal");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(btnThemeToggle, BorderLayout.EAST);

        JPanel menuCard = createCard();
        menuCard.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- ORDEM DOS BOTÕES ATUALIZADA ---

        // 1. Inserir Veículo (topo)
        gbc.gridx = 0;
        gbc.gridy = 0;
        menuCard.add(btnInserir, gbc);

        // 2. Criar Novo Cliente
        gbc.gridy = 1;
        menuCard.add(btnCriarCliente, gbc);

        // 3. Listar Veículos (agora antes do Logout)
        gbc.gridy = 2;
        menuCard.add(btnListar, gbc);

        // 4. Logout (agora no fim de tudo)
        gbc.gridy = 3;
        menuCard.add(btnLogout, gbc);

        // --- FIM DA ORDEM ATUALIZADA ---

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuCard, BorderLayout.CENTER);

        this.add(mainPanel);

        // Configurar tamanhos preferidos dos botões para consistência
        Dimension buttonSize = new Dimension(200, 40);
        btnInserir.setPreferredSize(buttonSize);
        btnListar.setPreferredSize(buttonSize);
        btnCriarCliente.setPreferredSize(buttonSize);
        btnLogout.setPreferredSize(buttonSize);
    }

    public void mostrar() {
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }

    public JButton getBtnInserir() {
        return btnInserir;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public JButton getBtnLogout() {
        return btnLogout;
    }

    public JButton getBtnCriarCliente() {
        return btnCriarCliente;
    }

    public JButton getBtnAlugar() {
        return null;
    }

    public JButton getBtnDevolver() {
        return null;
    }

    public JButton getBtnRelatorio() {
        return null;
    }

    public void addInserirListener(ActionListener listener) {
        btnInserir.addActionListener(listener);
    }

    public void addListarListener(ActionListener listener) {
        btnListar.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) {
        btnLogout.addActionListener(listener);
    }

    public void addCriarClienteListener(ActionListener listener) {
        btnCriarCliente.addActionListener(listener);
    }

    public void addAlugarListener(ActionListener listener) {
        // Método vazio - botão não existe mais
    }

    public void addDevolverListener(ActionListener listener) {
        // Método vazio - botão não existe mais
    }

    public void addRelatorioListener(ActionListener listener) {
        // Método vazio - botão não existe mais
    }

    public void setInserirButtonVisible(boolean visible) {
        if (btnInserir != null) {
            btnInserir.setVisible(visible);
        }
    }

    public void setListarButtonVisible(boolean visible) {
        if (btnListar != null) {
            btnListar.setVisible(visible);
        }
    }

    public void setCriarClienteButtonVisible(boolean visible) {
        if (btnCriarCliente != null) {
            btnCriarCliente.setVisible(visible);
        }
    }

    public void setLogoutButtonVisible(boolean visible) {
        if (btnLogout != null) {
            btnLogout.setVisible(visible);
        }
    }

    public void setAlugarButtonVisible(boolean visible) {
        // Método vazio - botão não existe mais
    }

    public void setDevolverButtonVisible(boolean visible) {
        // Método vazio - botão não existe mais
    }

    public void setRelatorioButtonVisible(boolean visible) {
        // Método vazio - botão não existe mais
    }
}