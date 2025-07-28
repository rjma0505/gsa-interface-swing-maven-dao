package controller;

import dao.VeiculoDAO;
import dao.EstadoDAO;
import dao.CidadeDAO;
import dao.UtilizadorDAO;
import dao.CompradorDAO;
import model.Veiculo;
import model.Estado;
import model.Cidade;
import model.Utilizador;
import model.Comprador;
import model.Perfil; // Importar a classe Perfil
import service.UserSession; // Importar UserSession
import view.ListarVeiculoView;
import view.MenuView; // Para voltar ao menu
import javax.swing.JOptionPane;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ListarController {

    private final ListarVeiculoView listarVeiculoView;
    private final VeiculoDAO veiculoDAO;
    private final EstadoDAO estadoDAO;
    private final CidadeDAO cidadeDAO;
    private final UtilizadorDAO utilizadorDAO;
    private final CompradorDAO compradorDAO;
    private boolean isSelectionMode; // Indica se o controlador está no modo de seleção para edição

    // Construtor padrão (para listagem geral)
    public ListarController() {
        this(false); // Chama o construtor com isSelectionMode = false
    }

    // Construtor para modo de seleção (usado para edição)
    public ListarController(boolean isSelectionMode) {
        this.listarVeiculoView = new ListarVeiculoView();
        this.veiculoDAO = new VeiculoDAO();
        this.estadoDAO = new EstadoDAO();
        this.cidadeDAO = new CidadeDAO();
        this.utilizadorDAO = new UtilizadorDAO();
        this.compradorDAO = new CompradorDAO();
        this.isSelectionMode = isSelectionMode;

        System.out.println("ListarController: A inicializar...");

        setupView(); // Configura a vista, incluindo visibilidade dos botões
        setupButtonActions();
        loadVeiculos(); // Carrega os veículos inicialmente
        listarVeiculoView.mostrar();
        System.out.println("ListarController: ListarVeiculoView exibida.");
    }

    private void setupView() {
        if (isSelectionMode) {
            listarVeiculoView.setTitle("Selecionar Veículo para Edição");
            // Esconder botões de Editar e Remover quando no modo de seleção direta por clique na tabela
            listarVeiculoView.setEditarButtonVisible(false);
            listarVeiculoView.setRemoverButtonVisible(false);
            System.out.println("ListarController: Modo de seleção ativo. Botões Editar/Remover escondidos.");

            // Adicionar listener para clique na tabela no modo de seleção
            listarVeiculoView.getTabela().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // Deteta duplo clique
                        handleTableSelectionForEdit();
                    }
                }
            });
        } else {
            // Lógica de visibilidade baseada no perfil para o modo normal
            Perfil perfil = UserSession.getInstance().getPerfil();
            if (perfil != null) {
                String descricaoPerfil = perfil.getDescricao();
                System.out.println("ListarController: Perfil do utilizador logado: " + descricaoPerfil);

                // Por padrão, esconde ambos os botões de edição/remoção
                listarVeiculoView.setEditarButtonVisible(false);
                listarVeiculoView.setRemoverButtonVisible(false);

                if (descricaoPerfil.equalsIgnoreCase("Administrador")) {
                    listarVeiculoView.setEditarButtonVisible(true);
                    listarVeiculoView.setRemoverButtonVisible(true);
                    System.out.println("ListarController: Visibilidade: 'Administrador' pode Editar e Remover.");
                } else if (descricaoPerfil.equalsIgnoreCase("Vendedor")) {
                    listarVeiculoView.setEditarButtonVisible(true); // Vendedor pode editar
                    System.out.println("ListarController: Visibilidade: 'Vendedor' pode Editar (não pode Remover).");
                } else if (descricaoPerfil.equalsIgnoreCase("Responsavel de Frota")) {
                    // Responsável de Frota não pode editar nem remover (já estão escondidos por padrão)
                    System.out.println("ListarController: Visibilidade: 'Responsavel de Frota' não pode Editar nem Remover.");
                } else {
                    // Outros perfis (se houver) também não podem editar nem remover
                    System.out.println("ListarController: Visibilidade: Perfil desconhecido ou não autorizado. Botões de edição/remoção escondidos.");
                }
            } else {
                // Se não há perfil na sessão (erro), esconde tudo por segurança
                listarVeiculoView.setEditarButtonVisible(false);
                listarVeiculoView.setRemoverButtonVisible(false);
                System.err.println("ListarController: Erro: Perfil do utilizador não encontrado na sessão. Botões de edição/remoção escondidos.");
            }
        }
    }

    private void setupButtonActions() {
        listarVeiculoView.adicionarAcaoVoltar(e -> voltarAoMenu());
        listarVeiculoView.adicionarAcaoFiltrar(e -> filtrarVeiculos());
        listarVeiculoView.adicionarAcaoLimparFiltro(e -> limparFiltro());

        // Ações para os botões Editar e Remover (apenas no modo normal)
        if (!isSelectionMode) {
            listarVeiculoView.adicionarAcaoEditar(e -> handleEditButtonClick());
            listarVeiculoView.adicionarAcaoRemover(e -> handleRemoverButtonClick());
        }
    }

    private void loadVeiculos() {
        System.out.println("ListarController: A carregar veículos na tabela...");
        listarVeiculoView.limparTabela();
        List<Veiculo> veiculos = veiculoDAO.buscarTodos();
        if (veiculos.isEmpty()) {
            System.out.println("ListarController: Nenhuns veículos encontrados na base de dados.");
        } else {
            System.out.println("ListarController: " + veiculos.size() + " veículos carregados.");
        }
        for (Veiculo veiculo : veiculos) {
            listarVeiculoView.adicionarVeiculoNaTabela(veiculo);
        }
    }

    private void filtrarVeiculos() {
        System.out.println("ListarController: A filtrar veículos...");
        String marca = listarVeiculoView.getFiltroMarca();
        String modelo = listarVeiculoView.getFiltroModelo();
        String matricula = listarVeiculoView.getFiltroMatricula();

        listarVeiculoView.limparTabela();
        List<Veiculo> veiculosFiltrados = veiculoDAO.filtrarVeiculos(marca, modelo, matricula);
        if (veiculosFiltrados.isEmpty()) {
            System.out.println("ListarController: Nenhuns veículos encontrados com os filtros: Marca='" + marca + "', Modelo='" + modelo + "', Matrícula='" + matricula + "'.");
        } else {
            System.out.println("ListarController: " + veiculosFiltrados.size() + " veículos encontrados com os filtros.");
        }
        for (Veiculo veiculo : veiculosFiltrados) {
            listarVeiculoView.adicionarVeiculoNaTabela(veiculo);
        }
    }

    private void limparFiltro() {
        System.out.println("ListarController: A limpar filtros e recarregar veículos.");
        listarVeiculoView.setFiltroMarcaField("");
        listarVeiculoView.setFiltroModeloField("");
        listarVeiculoView.setFiltroMatriculaField("");
        loadVeiculos(); // Recarrega todos os veículos
    }

    private void voltarAoMenu() {
        System.out.println("ListarController: Botão 'Voltar' clicado. A fechar ListarVeiculoView.");
        listarVeiculoView.dispose();
        new MenuController(); // Volta para o Menu Principal
        System.out.println("ListarController: MenuController iniciado.");
    }

    private void handleTableSelectionForEdit() {
        int selectedRow = listarVeiculoView.getTabela().getSelectedRow();
        if (selectedRow >= 0) {
            int veiculoId = (int) listarVeiculoView.getTabela().getModel().getValueAt(selectedRow, 0); // ID está na primeira coluna
            System.out.println("ListarController: Duplo clique na tabela. A abrir edição para Veículo ID: " + veiculoId);
            abrirEditarVeiculo(veiculoId);
        } else {
            JOptionPane.showMessageDialog(listarVeiculoView, "Por favor, selecione um veículo na tabela para editar.", "Nenhum Veículo Selecionado", JOptionPane.WARNING_MESSAGE);
            System.out.println("ListarController: Nenhuma linha selecionada na tabela para edição por duplo clique.");
        }
    }

    private void handleEditButtonClick() {
        int selectedRow = listarVeiculoView.getTabela().getSelectedRow();
        if (selectedRow >= 0) {
            int veiculoId = (int) listarVeiculoView.getTabela().getModel().getValueAt(selectedRow, 0);
            System.out.println("ListarController: Botão 'Editar' clicado. A abrir edição para Veículo ID: " + veiculoId);
            abrirEditarVeiculo(veiculoId);
        } else {
            JOptionPane.showMessageDialog(listarVeiculoView, "Por favor, selecione um veículo na tabela para editar.", "Nenhum Veículo Selecionado", JOptionPane.WARNING_MESSAGE);
            System.out.println("ListarController: Nenhuma linha selecionada para edição via botão.");
        }
    }

    private void abrirEditarVeiculo(int veiculoId) {
        Veiculo veiculoParaEditar = veiculoDAO.buscarPorId(veiculoId);
        if (veiculoParaEditar != null) {
            System.out.println("ListarController: Veículo ID " + veiculoId + " encontrado para edição. A carregar dados para EditarVeiculoView.");
            List<Estado> todosEstados = estadoDAO.buscarTodos();
            List<Cidade> todasCidades = cidadeDAO.buscarTodos();
            List<Utilizador> todosResponsaveis = utilizadorDAO.listarTodos();
            List<Comprador> todosCompradores = compradorDAO.buscarTodos();

            listarVeiculoView.dispose(); // Fecha a janela de listagem
            new EditarVeiculoController(veiculoParaEditar, todosEstados, todasCidades, todosResponsaveis, todosCompradores);
            System.out.println("ListarController: EditarVeiculoController iniciado.");
        } else {
            JOptionPane.showMessageDialog(listarVeiculoView, "Erro: Veículo não encontrado para edição.", "Erro", JOptionPane.ERROR_MESSAGE);
            System.err.println("ListarController: Erro: Veículo com ID " + veiculoId + " não encontrado para edição.");
        }
    }

    private void handleRemoverButtonClick() {
        int selectedRow = listarVeiculoView.getTabela().getSelectedRow();
        if (selectedRow >= 0) {
            int veiculoId = (int) listarVeiculoView.getTabela().getModel().getValueAt(selectedRow, 0);
            System.out.println("ListarController: Botão 'Remover' clicado. A tentar remover Veículo ID: " + veiculoId);

            int confirmResult = JOptionPane.showConfirmDialog(listarVeiculoView,
                    "Tem certeza que deseja remover o veículo selecionado?", "Confirmar Remoção",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirmResult == JOptionPane.YES_OPTION) {
                if (veiculoDAO.removerVeiculoPorId(veiculoId)) {
                    JOptionPane.showMessageDialog(listarVeiculoView, "Veículo removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("ListarController: Veículo ID " + veiculoId + " removido com sucesso.");
                    loadVeiculos(); // Recarrega a tabela após a remoção
                } else {
                    JOptionPane.showMessageDialog(listarVeiculoView, "Erro ao remover o veículo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    System.err.println("ListarController: Falha ao remover veículo ID " + veiculoId + ".");
                }
            } else {
                System.out.println("ListarController: Remoção cancelada pelo utilizador.");
            }
        } else {
            JOptionPane.showMessageDialog(listarVeiculoView, "Por favor, selecione um veículo na tabela para remover.", "Nenhum Veículo Selecionado", JOptionPane.WARNING_MESSAGE);
            System.out.println("ListarController: Nenhuma linha selecionada para remoção.");
        }
    }
}
