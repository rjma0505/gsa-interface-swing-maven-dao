package controller;

import dao.VeiculoDAO;
import dao.EstadoDAO;
import dao.CidadeDAO;
import dao.UtilizadorDAO;
import model.Veiculo;
import model.Estado;
import model.Cidade;
import model.Utilizador;
import view.InserirVeiculoView;
import view.MenuView; // Para voltar ao menu
import javax.swing.JOptionPane;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale; // Para formatação de números

public class InserirController {

    private final InserirVeiculoView inserirVeiculoView;
    private final VeiculoDAO veiculoDAO;
    private final EstadoDAO estadoDAO;
    private final CidadeDAO cidadeDAO;
    private final UtilizadorDAO utilizadorDAO;

    public InserirController() {
        this.inserirVeiculoView = new InserirVeiculoView();
        this.veiculoDAO = new VeiculoDAO();
        this.estadoDAO = new EstadoDAO();
        this.cidadeDAO = new CidadeDAO();
        this.utilizadorDAO = new UtilizadorDAO();

        setupView();
        setupButtonActions();
        populateComboBoxes();
        inserirVeiculoView.setVisible(true);
    }

    private void setupView() {
        // Nenhuma lógica específica de visibilidade de botões aqui, pois é uma vista de inserção
    }

    private void setupButtonActions() {
        inserirVeiculoView.adicionarAcaoInserir(e -> inserirVeiculo());
        inserirVeiculoView.adicionarAcaoVoltar(e -> voltarAoMenu());
    }

    private void populateComboBoxes() {
        // Popula o JComboBox de Estados
        List<Estado> estados = estadoDAO.buscarTodos();
        // Mapeia para String para a JComboBox da View
        List<String> descricoesEstados = estados.stream().map(Estado::getDescricao).toList();
        inserirVeiculoView.popularEstados(descricoesEstados);

        // Popula o JComboBox de Cidades
        List<Cidade> cidades = cidadeDAO.buscarTodos();
        // Mapeia para String para a JComboBox da View
        List<String> descricoesCidades = cidades.stream().map(Cidade::getDescricao).toList();
        inserirVeiculoView.popularCidades(descricoesCidades);

        // Popula o JComboBox de Responsáveis
        List<Utilizador> responsaveis = utilizadorDAO.listarTodos(); // Assumindo que listarTodos() retorna todos os utilizadores
        inserirVeiculoView.popularResponsaveis(responsaveis);
    }

    private void inserirVeiculo() {
        String marca = inserirVeiculoView.getMarca();
        String modelo = inserirVeiculoView.getModelo();
        String matricula = inserirVeiculoView.getMatricula();
        String estadoDescricao = inserirVeiculoView.getEstadoSelecionado();
        String precoStr = inserirVeiculoView.getPreco();
        String cidadeDescricao = inserirVeiculoView.getCidadeSelecionada();
        Utilizador responsavel = inserirVeiculoView.getResponsavelSelecionado();

        // Validações básicas
        if (marca.isEmpty() || modelo.isEmpty() || matricula.isEmpty() || estadoDescricao == null || precoStr.isEmpty() || cidadeDescricao == null) {
            inserirVeiculoView.exibirMensagem("Todos os campos obrigatórios (Marca, Modelo, Matrícula, Estado, Preço, Cidade) devem ser preenchidos.", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double preco;
        try {
            // Usar NumberFormat para lidar com vírgulas ou pontos como separadores decimais
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault()); // Ou Locale.forLanguageTag("pt-PT")
            preco = format.parse(precoStr).doubleValue();
        } catch (ParseException e) {
            inserirVeiculoView.exibirMensagem("O preço deve ser um número válido.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obter objetos completos para Estado e Cidade a partir das descrições
        Estado estado = estadoDAO.buscarPorDescricao(estadoDescricao);
        Cidade cidade = cidadeDAO.buscarPorDescricao(cidadeDescricao);

        if (estado == null) {
            inserirVeiculoView.exibirMensagem("Estado selecionado inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cidade == null) {
            inserirVeiculoView.exibirMensagem("Cidade selecionada inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Criar o objeto Veiculo
        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMarca(marca);
        novoVeiculo.setModelo(modelo);
        novoVeiculo.setMatricula(matricula);
        novoVeiculo.setEstado(estado);
        novoVeiculo.setPreco(preco);
        novoVeiculo.setCidade(cidade);
        novoVeiculo.setResponsavel(responsavel); // Pode ser nulo
        // dataVenda e comprador são nulos na inserção inicial, a menos que seja um veículo já vendido

        if (veiculoDAO.inserirVeiculo(novoVeiculo)) {
            inserirVeiculoView.exibirMensagem("Veículo inserido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            // Limpar campos ou fechar a janela
            inserirVeiculoView.dispose();
            new MenuController(); // Volta para o Menu Principal
        } else {
            inserirVeiculoView.exibirMensagem("Erro ao inserir veículo. Verifique os dados e tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarAoMenu() {
        inserirVeiculoView.dispose();
        new MenuController(); // Volta para o Menu Principal
    }
}
