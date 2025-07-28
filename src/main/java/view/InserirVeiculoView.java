package view;

import view.theme.ThemedView;
import model.Utilizador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class InserirVeiculoView extends ThemedView {

    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtMatricula;
    private JComboBox<String> cmbEstado;
    private JTextField txtPreco;
    private JComboBox<String> cmbCidade;
    private JComboBox<Utilizador> cmbResponsavel;
    private JButton btnInserir;
    private JButton btnVoltar;
    private JButton btnThemeToggle;

    public InserirVeiculoView() {
        super("Inserir Novo Veículo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 550);
        setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        txtMarca = createStyledTextField(20);
        txtModelo = createStyledTextField(20);
        txtMatricula = createStyledTextField(10);
        cmbEstado = createStyledComboBox();
        txtPreco = createStyledTextField(15);
        cmbCidade = createStyledComboBox();
        cmbResponsavel = createStyledComboBox();
        btnInserir = createStyledButton("Inserir Veículo");
        btnVoltar = createStyledButton("Voltar");
        btnThemeToggle = createThemeToggleButton();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
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
        gbc.gridy++;
        mainPanel.add(createStyledLabel("Marca:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtMarca, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtModelo, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Matrícula:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtMatricula, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Estado:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(cmbEstado, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Preço/Dia:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtPreco, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Cidade:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(cmbCidade, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Responsável:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(cmbResponsavel, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(btnInserir, gbc);

        gbc.gridy++;
        mainPanel.add(btnVoltar, gbc);

        this.add(mainPanel);
    }

    public String getMarca() {
        return txtMarca.getText();
    }

    public String getModelo() {
        return txtModelo.getText();
    }

    public String getMatricula() {
        return txtMatricula.getText();
    }

    public String getEstadoSelecionado() {
        return (String) cmbEstado.getSelectedItem();
    }

    public String getPreco() {
        return txtPreco.getText();
    }

    public String getCidadeSelecionada() {
        return (String) cmbCidade.getSelectedItem();
    }

    public Utilizador getResponsavelSelecionado() {
        return (Utilizador) cmbResponsavel.getSelectedItem();
    }

    public void adicionarAcaoInserir(ActionListener listener) {
        btnInserir.addActionListener(listener);
    }

    public void adicionarAcaoVoltar(ActionListener listener) {
        btnVoltar.addActionListener(listener);
    }

    public void popularEstados(List<String> descricoesEstados) {
        cmbEstado.removeAllItems();
        for (String estado : descricoesEstados) {
            cmbEstado.addItem(estado);
        }
    }

    public void popularCidades(List<String> descricoesCidades) {
        cmbCidade.removeAllItems();
        for (String cidade : descricoesCidades) {
            cmbCidade.addItem(cidade);
        }
    }

    public void popularResponsaveis(List<Utilizador> responsaveis) {
        cmbResponsavel.removeAllItems();
        for (Utilizador responsavel : responsaveis) {
            cmbResponsavel.addItem(responsavel);
        }
    }

    public void exibirMensagem(String message, String title, int messageType) {
        showThemedMessage(this, message, title, messageType);
    }
}