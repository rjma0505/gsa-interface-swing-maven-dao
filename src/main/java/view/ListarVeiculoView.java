package view;

import view.theme.ThemedView;
import model.Veiculo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ListarVeiculoView extends ThemedView {

    private JTable veiculosTable;
    private DefaultTableModel tableModel;
    private JTextField txtFiltroMarca;
    private JTextField txtFiltroModelo;
    private JTextField txtFiltroMatricula;
    private JButton btnVoltar;
    private JButton btnFiltrar;
    private JButton btnLimparFiltro;
    private JButton btnEditar;
    private JButton btnRemover;
    private JButton btnThemeToggle;

    public ListarVeiculoView() {
        super("Lista de Veículos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Aumenta a largura da janela para 1200 pixels
        setSize(1200, 700); // <-- ÚNICA ALTERAÇÃO AQUI
        setLocationRelativeTo(null);
    }

    @Override
    protected void initializeComponents() {
        // CORRIGIDO: Removido "Ano" da tabela, para corresponder ao seu modelo Veiculo
        tableModel = new DefaultTableModel(new Object[]{"ID", "Marca", "Modelo", "Matrícula", "Preço/Dia", "Responsável", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        veiculosTable = createStyledTable(tableModel);

        txtFiltroMarca = createStyledTextField(15);
        txtFiltroModelo = createStyledTextField(15);
        txtFiltroMatricula = createStyledTextField(10);
        btnFiltrar = createStyledButton("Filtrar");
        btnLimparFiltro = createStyledButton("Limpar Filtro");
        btnVoltar = createStyledButton("Voltar");
        btnEditar = createStyledButton("Editar Veículo");
        btnRemover = createStyledButton("Remover Veículo");
        btnThemeToggle = createThemeToggleButton();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.add(createStyledLabel("Marca:"));
        filterPanel.add(txtFiltroMarca);
        filterPanel.add(createStyledLabel("Modelo:"));
        filterPanel.add(txtFiltroModelo);
        filterPanel.add(createStyledLabel("Matrícula:"));
        filterPanel.add(txtFiltroMatricula);
        filterPanel.add(btnFiltrar);
        filterPanel.add(btnLimparFiltro);
        applyThemeToComponent(filterPanel); // Ensure theme is applied to sub-panels

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.add(btnEditar);
        actionPanel.add(btnRemover);
        actionPanel.add(btnVoltar);
        applyThemeToComponent(actionPanel); // Ensure theme is applied to sub-panels

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        JPanel themeButtonWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        themeButtonWrapper.add(btnThemeToggle);
        applyThemeToComponent(themeButtonWrapper); // Ensure theme is applied to sub-panels

        headerPanel.add(themeButtonWrapper, BorderLayout.NORTH);
        applyThemeToComponent(headerPanel); // Ensure theme is applied to sub-panels

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(veiculosTable), BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        this.add(mainPanel);
    }

    public JTable getTabela() {
        return veiculosTable;
    }

    public String getFiltroMarca() {
        return txtFiltroMarca.getText();
    }

    public String getFiltroModelo() {
        return txtFiltroModelo.getText();
    }

    public String getFiltroMatricula() {
        return txtFiltroMatricula.getText();
    }

    public void adicionarAcaoVoltar(ActionListener listener) {
        btnVoltar.addActionListener(listener);
    }

    public void adicionarAcaoFiltrar(ActionListener listener) {
        btnFiltrar.addActionListener(listener);
    }

    public void adicionarAcaoLimparFiltro(ActionListener listener) {
        btnLimparFiltro.addActionListener(listener);
    }

    public void adicionarAcaoEditar(ActionListener listener) {
        btnEditar.addActionListener(listener);
    }

    public void adicionarAcaoRemover(ActionListener listener) {
        btnRemover.addActionListener(listener);
    }

    public void setEditarButtonVisible(boolean visible) {
        if (btnEditar != null) {
            btnEditar.setVisible(visible);
        }
    }

    public void setRemoverButtonVisible(boolean visible) {
        if (btnRemover != null) {
            btnRemover.setVisible(visible);
        }
    }

    public void limparTabela() {
        tableModel.setRowCount(0);
    }

    public void adicionarVeiculoNaTabela(Veiculo veiculo) {
        // CORRIGIDO: Removido veiculo.getAno()
        tableModel.addRow(new Object[]{
                veiculo.getId(),
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getMatricula(),
                veiculo.getPreco(),
                veiculo.getResponsavel() != null ? veiculo.getResponsavel().getNome() : "N/A",
                veiculo.getEstado() != null ? veiculo.getEstado().getDescricao() : "N/A"
        });
    }

    public void setFiltroMarcaField(String text) {
        txtFiltroMarca.setText(text);
    }

    public void setFiltroModeloField(String text) {
        txtFiltroModelo.setText(text);
    }

    public void setFiltroMatriculaField(String text) {
        txtFiltroMatricula.setText(text);
    }

    public void mostrar() {
        SwingUtilities.invokeLater(() -> this.setVisible(true));
    }
}