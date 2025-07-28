package view;

import view.theme.ThemedView;
import model.Veiculo;
import model.Estado;
import model.Cidade;
import model.Utilizador;
import model.Comprador;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import util.DateLabelFormatter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class EditarVeiculoView extends ThemedView {

    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextField txtMatricula;
    private JTextField txtPreco;
    private JComboBox<Estado> cmbEstado;
    private JComboBox<Cidade> cmbCidade;
    private JComboBox<Utilizador> cmbResponsavel;
    private JComboBox<Comprador> cmbComprador;

    private JDatePickerImpl datePickerDataVenda;
    private UtilDateModel dateModel;
    private JDatePanelImpl datePanel;

    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnThemeToggle;

    public EditarVeiculoView(JFrame parentFrame) {
        super("Editar Veículo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 750);
        setLocationRelativeTo(parentFrame);
    }

    @Override
    protected void initializeComponents() {
        txtMarca = createStyledTextField(20);
        txtModelo = createStyledTextField(20);
        txtMatricula = createStyledTextField(10);
        txtPreco = createStyledTextField(15);

        cmbEstado = createStyledComboBox();
        cmbCidade = createStyledComboBox();
        cmbResponsavel = createStyledComboBox();
        cmbComprador = createStyledComboBox();

        // Inicializar JDatePicker
        dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoje");
        p.put("text.month", "Mês");
        p.put("text.year", "Ano");
        datePanel = new JDatePanelImpl(dateModel, p);
        datePickerDataVenda = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        // --- INÍCIO DA ALTERAÇÃO RECOMENDADA ---
        JFormattedTextField dateTextField = datePickerDataVenda.getJFormattedTextField();

        // 1. Pega a borda de um campo de texto de referência para replicar o estilo
        javax.swing.border.Border referenceBorder = txtMarca.getBorder();
        dateTextField.setBorder(referenceBorder);

        // 2. Define a mesma altura base para garantir consistência
        int height = txtMarca.getPreferredSize().height;
        dateTextField.setPreferredSize(new Dimension(dateTextField.getPreferredSize().width, height));
        // --- FIM DA ALTERAÇÃO ---

        btnSalvar = createStyledButton("Guardar Alterações");
        btnCancelar = createStyledButton("Cancelar");
        btnThemeToggle = createThemeToggleButton();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Botão de tema no canto superior direito
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(5, 5, 5, 5);
        mainPanel.add(btnThemeToggle, gbc);

        // Resetar gbc para os campos principais
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

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
        mainPanel.add(createStyledLabel("Preço/Dia:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(txtPreco, gbc);

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
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Comprador:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(cmbComprador, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0;
        mainPanel.add(createStyledLabel("Data Venda:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(datePickerDataVenda, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(btnSalvar, gbc);

        gbc.gridy++;
        mainPanel.add(btnCancelar, gbc);

        this.add(mainPanel);
    }

    public void setVeiculoData(Veiculo veiculo, List<Estado> estados,
                               List<Cidade> cidades, List<Utilizador> responsaveis,
                               List<Comprador> compradores) {
        txtMarca.setText(veiculo.getMarca());
        txtModelo.setText(veiculo.getModelo());
        txtMatricula.setText(veiculo.getMatricula());
        txtPreco.setText(String.format(Locale.getDefault(), "%.2f", veiculo.getPreco()));

        popularComboBox(cmbEstado, estados, veiculo.getEstado());
        popularComboBox(cmbCidade, cidades, veiculo.getCidade());
        popularComboBox(cmbResponsavel, responsaveis, veiculo.getResponsavel());
        popularComboBox(cmbComprador, compradores, veiculo.getComprador());

        if (veiculo.getDataVenda() != null) {
            dateModel.setValue(veiculo.getDataVenda());
            dateModel.setSelected(true);
        } else {
            dateModel.setValue(null);
            dateModel.setSelected(false);
        }
    }

    private <T> void popularComboBox(JComboBox<T> comboBox, List<T> items, T selectedItem) {
        comboBox.removeAllItems();
        for (T item : items) {
            comboBox.addItem(item);
        }
        if (selectedItem != null) {
            comboBox.setSelectedItem(selectedItem);
        } else {
            comboBox.setSelectedIndex(-1); // Nenhum selecionado
        }
    }

    public void adicionarAcaoSalvar(ActionListener listener) {
        btnSalvar.addActionListener(listener);
    }

    public void adicionarAcaoCancelar(ActionListener listener) {
        btnCancelar.addActionListener(listener);
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

    public String getPreco() {
        return txtPreco.getText();
    }

    public Estado getSelectedEstado() {
        return (Estado) cmbEstado.getSelectedItem();
    }

    public Cidade getSelectedCidade() {
        return (Cidade) cmbCidade.getSelectedItem();
    }

    public Utilizador getSelectedResponsavel() {
        return (Utilizador) cmbResponsavel.getSelectedItem();
    }

    public Comprador getSelectedComprador() {
        return (Comprador) cmbComprador.getSelectedItem();
    }

    public Date getDataVendaDate() {
        return (Date) datePickerDataVenda.getModel().getValue();
    }

    public void mostrarErro(String message) {
        showThemedMessage(this, message, "Erro de Validação", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarSucesso(String message) {
        showThemedMessage(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    public void fecharJanela() {
        dispose();
    }
}