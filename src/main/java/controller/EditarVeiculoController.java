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
import view.EditarVeiculoView;
import view.ListarVeiculoView;

import javax.swing.SwingUtilities;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class EditarVeiculoController {

    private static final Logger LOGGER = Logger.getLogger(EditarVeiculoController.class.getName());
    private static final int MATRICULA_MIN_LENGTH = 6;
    private static final int MATRICULA_MAX_LENGTH = 10;
    private static final String ESTADO_VENDIDO = "Vendido";

    private EditarVeiculoView editarVeiculoView;
    private final VeiculoDAO veiculoDAO;
    private final EstadoDAO estadoDAO;
    private final CidadeDAO cidadeDAO;
    private final UtilizadorDAO utilizadorDAO;
    private final CompradorDAO compradorDAO;
    private final Veiculo veiculoOriginal;

    public EditarVeiculoController(Veiculo veiculoParaEditar, List<Estado> todosEstados,
                                   List<Cidade> todasCidades, List<Utilizador> todosResponsaveis,
                                   List<Comprador> todosCompradores) {

        // Validar parâmetros de entrada
        if (veiculoParaEditar == null) {
            throw new IllegalArgumentException("Veículo para editar não pode ser nulo");
        }

        // Inicializar DAOs
        this.veiculoDAO = new VeiculoDAO();
        this.estadoDAO = new EstadoDAO();
        this.cidadeDAO = new CidadeDAO();
        this.utilizadorDAO = new UtilizadorDAO();
        this.compradorDAO = new CompradorDAO();
        this.veiculoOriginal = veiculoParaEditar;

        // Criar a view no EDT
        SwingUtilities.invokeLater(() -> {
            try {
                this.editarVeiculoView = new EditarVeiculoView(null);
                inicializarView(todosEstados, todasCidades, todosResponsaveis, todosCompradores);
                configurarEventos();
                this.editarVeiculoView.setVisible(true);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao inicializar a view de edição", e);
                // Poderia mostrar uma mensagem de erro ao usuário aqui
            }
        });
    }

    private void inicializarView(List<Estado> todosEstados, List<Cidade> todasCidades,
                                 List<Utilizador> todosResponsaveis, List<Comprador> todosCompradores) {
        // Preencher a vista com os dados do veículo e popula as comboboxes
        editarVeiculoView.setVeiculoData(veiculoOriginal, todosEstados, todasCidades,
                todosResponsaveis, todosCompradores);
    }

    private void configurarEventos() {
        editarVeiculoView.adicionarAcaoSalvar(e -> salvarEdicaoVeiculo());
        editarVeiculoView.adicionarAcaoCancelar(e -> cancelarEdicao());
    }

    private void salvarEdicaoVeiculo() {
        try {
            // Validar dados
            ResultadoValidacao validacao = validarDadosFormulario();
            if (!validacao.isValido()) {
                editarVeiculoView.mostrarErro(validacao.getMensagemErro());
                return;
            }

            // Obter dados validados do formulário
            DadosVeiculo dados = obterDadosFormulario();

            // Criar uma cópia do veículo para rollback em caso de erro
            Veiculo veiculoBackup = criarBackupVeiculo();

            try {
                // Atualizar o objeto Veiculo
                atualizarVeiculoComDados(dados);

                // Salvar no banco de dados
                veiculoDAO.atualizarVeiculo(veiculoOriginal);

                // Mostrar sucesso e fechar
                editarVeiculoView.mostrarSucesso("Veículo atualizado com sucesso!");
                editarVeiculoView.fecharJanela();

                // Voltar para a lista
                new ListarController();

            } catch (Exception dbException) {
                // Restaurar o estado original em caso de erro na base de dados
                restaurarVeiculoDoBackup(veiculoBackup);
                throw dbException;
            }

        } catch (ParseException pe) {
            String mensagemErro = "Erro ao processar dados numéricos: " + pe.getMessage();
            editarVeiculoView.mostrarErro(mensagemErro);
            LOGGER.log(Level.WARNING, "Erro de parsing ao salvar veículo", pe);
        } catch (Exception ex) {
            String mensagemErro = "Erro ao atualizar veículo: " +
                    (ex.getMessage() != null ? ex.getMessage() : "Erro desconhecido");
            editarVeiculoView.mostrarErro(mensagemErro);
            LOGGER.log(Level.SEVERE, "Erro ao salvar edição do veículo", ex);
        }
    }

    private Veiculo criarBackupVeiculo() {
        // Criar uma cópia simples dos campos principais
        Veiculo backup = new Veiculo();
        backup.setMarca(veiculoOriginal.getMarca());
        backup.setModelo(veiculoOriginal.getModelo());
        backup.setMatricula(veiculoOriginal.getMatricula());
        backup.setPreco(veiculoOriginal.getPreco());
        backup.setEstado(veiculoOriginal.getEstado());
        backup.setCidade(veiculoOriginal.getCidade());
        backup.setResponsavel(veiculoOriginal.getResponsavel());
        backup.setComprador(veiculoOriginal.getComprador());
        backup.setDataVenda(veiculoOriginal.getDataVenda());
        return backup;
    }

    private void restaurarVeiculoDoBackup(Veiculo backup) {
        veiculoOriginal.setMarca(backup.getMarca());
        veiculoOriginal.setModelo(backup.getModelo());
        veiculoOriginal.setMatricula(backup.getMatricula());
        veiculoOriginal.setPreco(backup.getPreco());
        veiculoOriginal.setEstado(backup.getEstado());
        veiculoOriginal.setCidade(backup.getCidade());
        veiculoOriginal.setResponsavel(backup.getResponsavel());
        veiculoOriginal.setComprador(backup.getComprador());
        veiculoOriginal.setDataVenda(backup.getDataVenda());
    }

    private ResultadoValidacao validarDadosFormulario() {
        List<String> erros = new ArrayList<>();

        try {
            // Obter dados do formulário
            String marca = editarVeiculoView.getMarca();
            String modelo = editarVeiculoView.getModelo();
            String matricula = editarVeiculoView.getMatricula();
            String precoStr = editarVeiculoView.getPreco();
            Estado estadoSelecionado = editarVeiculoView.getSelectedEstado();
            Cidade cidadeSelecionada = editarVeiculoView.getSelectedCidade();
            Utilizador responsavelSelecionado = editarVeiculoView.getSelectedResponsavel();
            Comprador compradorSelecionado = editarVeiculoView.getSelectedComprador();
            Date dataVenda = editarVeiculoView.getDataVendaDate();

            // Validações de campos obrigatórios
            validarCamposObrigatorios(erros, marca, modelo, matricula, precoStr,
                    estadoSelecionado, cidadeSelecionada);

            // Validação do preço
            validarPreco(erros, precoStr);

            // Validações específicas para estado "Vendido"
            validarEstadoVendido(erros, estadoSelecionado, compradorSelecionado, dataVenda);

            // Validação da matrícula
            validarMatricula(erros, matricula);

        } catch (Exception e) {
            erros.add("Erro interno ao validar formulário: " + e.getMessage());
            LOGGER.log(Level.WARNING, "Erro durante validação do formulário", e);
        }

        return erros.isEmpty() ? ResultadoValidacao.sucesso() :
                ResultadoValidacao.erro(String.join("\n• ", erros));
    }

    private void validarCamposObrigatorios(List<String> erros, String marca, String modelo,
                                           String matricula, String precoStr, Estado estado,
                                           Cidade cidade) {
        if (marca == null || marca.trim().isEmpty()) {
            erros.add("Marca é obrigatória");
        }
        if (modelo == null || modelo.trim().isEmpty()) {
            erros.add("Modelo é obrigatório");
        }
        if (matricula == null || matricula.trim().isEmpty()) {
            erros.add("Matrícula é obrigatória");
        }
        if (precoStr == null || precoStr.trim().isEmpty()) {
            erros.add("Preço é obrigatório");
        }
        if (estado == null) {
            erros.add("Estado é obrigatório");
        }
        if (cidade == null) {
            erros.add("Cidade é obrigatória");
        }
    }

    private void validarPreco(List<String> erros, String precoStr) {
        if (precoStr != null && !precoStr.trim().isEmpty()) {
            try {
                NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                double preco = format.parse(precoStr.trim()).doubleValue();
                if (preco < 0) {
                    erros.add("O preço deve ser um valor positivo");
                }
                if (Double.isInfinite(preco) || Double.isNaN(preco)) {
                    erros.add("O preço deve ser um número válido");
                }
            } catch (ParseException e) {
                erros.add("O preço deve ser um número válido");
            }
        }
    }

    private void validarEstadoVendido(List<String> erros, Estado estadoSelecionado,
                                      Comprador compradorSelecionado, Date dataVenda) {
        if (estadoSelecionado != null) {
            boolean isVendido = ESTADO_VENDIDO.equalsIgnoreCase(estadoSelecionado.getDescricao());

            if (isVendido) {
                if (dataVenda == null) {
                    erros.add("Para o estado 'Vendido', a Data de Venda é obrigatória");
                }
                if (compradorSelecionado == null) {
                    erros.add("Para o estado 'Vendido', um comprador deve ser selecionado");
                }
            } else {
                if (dataVenda != null) {
                    erros.add("A Data de Venda só deve ser preenchida se o estado for 'Vendido'");
                }
                if (compradorSelecionado != null) {
                    erros.add("Um comprador só deve ser selecionado se o estado for 'Vendido'");
                }
            }
        }
    }

    private void validarMatricula(List<String> erros, String matricula) {
        if (matricula != null && !matricula.trim().isEmpty() && !isMatriculaValida(matricula.trim())) {
            erros.add("Formato de matrícula inválido (deve ter entre " +
                    MATRICULA_MIN_LENGTH + " e " + MATRICULA_MAX_LENGTH + " caracteres)");
        }
    }

    private boolean isMatriculaValida(String matricula) {
        if (matricula == null) return false;
        String matriculaLimpa = matricula.trim();
        return matriculaLimpa.length() >= MATRICULA_MIN_LENGTH &&
                matriculaLimpa.length() <= MATRICULA_MAX_LENGTH;
    }

    private DadosVeiculo obterDadosFormulario() throws ParseException {
        String marca = editarVeiculoView.getMarca();
        String modelo = editarVeiculoView.getModelo();
        String matricula = editarVeiculoView.getMatricula();
        String precoStr = editarVeiculoView.getPreco();

        // Limpar e validar strings
        marca = marca != null ? marca.trim() : "";
        modelo = modelo != null ? modelo.trim() : "";
        matricula = matricula != null ? matricula.trim() : "";
        precoStr = precoStr != null ? precoStr.trim() : "";

        // Converter preço
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        double preco = format.parse(precoStr).doubleValue();

        Estado estado = editarVeiculoView.getSelectedEstado();
        Cidade cidade = editarVeiculoView.getSelectedCidade();
        Utilizador responsavel = editarVeiculoView.getSelectedResponsavel();
        Comprador comprador = editarVeiculoView.getSelectedComprador();
        Date dataVenda = editarVeiculoView.getDataVendaDate();

        return new DadosVeiculo(marca, modelo, matricula, preco, estado, cidade,
                responsavel, comprador, dataVenda);
    }

    private void atualizarVeiculoComDados(DadosVeiculo dados) {
        veiculoOriginal.setMarca(dados.getMarca());
        veiculoOriginal.setModelo(dados.getModelo());
        veiculoOriginal.setMatricula(dados.getMatricula());
        veiculoOriginal.setPreco(dados.getPreco());
        veiculoOriginal.setEstado(dados.getEstado());
        veiculoOriginal.setCidade(dados.getCidade());
        veiculoOriginal.setResponsavel(dados.getResponsavel());
        veiculoOriginal.setComprador(dados.getComprador());
        veiculoOriginal.setDataVenda(dados.getDataVenda());
    }

    private void cancelarEdicao() {
        try {
            editarVeiculoView.fecharJanela();
            new ListarController(); // Volta para a lista de veículos sem salvar
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Erro ao cancelar edição", e);
        }
    }

    // Classes auxiliares para melhor organização
    private static class ResultadoValidacao {
        private final boolean valido;
        private final String mensagemErro;

        private ResultadoValidacao(boolean valido, String mensagemErro) {
            this.valido = valido;
            this.mensagemErro = mensagemErro;
        }

        public static ResultadoValidacao sucesso() {
            return new ResultadoValidacao(true, null);
        }

        public static ResultadoValidacao erro(String mensagem) {
            return new ResultadoValidacao(false, "• " + mensagem);
        }

        public boolean isValido() {
            return valido;
        }

        public String getMensagemErro() {
            return mensagemErro;
        }
    }

    private static class DadosVeiculo {
        private final String marca;
        private final String modelo;
        private final String matricula;
        private final double preco;
        private final Estado estado;
        private final Cidade cidade;
        private final Utilizador responsavel;
        private final Comprador comprador;
        private final Date dataVenda;

        public DadosVeiculo(String marca, String modelo, String matricula, double preco,
                            Estado estado, Cidade cidade, Utilizador responsavel,
                            Comprador comprador, Date dataVenda) {
            this.marca = marca;
            this.modelo = modelo;
            this.matricula = matricula;
            this.preco = preco;
            this.estado = estado;
            this.cidade = cidade;
            this.responsavel = responsavel;
            this.comprador = comprador;
            this.dataVenda = dataVenda;
        }

        // Getters
        public String getMarca() { return marca; }
        public String getModelo() { return modelo; }
        public String getMatricula() { return matricula; }
        public double getPreco() { return preco; }
        public Estado getEstado() { return estado; }
        public Cidade getCidade() { return cidade; }
        public Utilizador getResponsavel() { return responsavel; }
        public Comprador getComprador() { return comprador; }
        public Date getDataVenda() { return dataVenda; }
    }
}