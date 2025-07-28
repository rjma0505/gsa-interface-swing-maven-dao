package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import model.Veiculo;
import model.Estado;
import model.Utilizador;
import model.Cidade;
import model.Comprador;
import util.ConexaoBD;

public class VeiculoDAO {

    public VeiculoDAO() {
        // O construtor não precisa mais de inicializar a conexão aqui.
        // A conexão será obtida dentro de cada método usando try-with-resources.
    }

    // Classe interna para validação de matrícula
    public static class MatriculaValidator {

        // Padrões de matrícula portuguesa
        private static final Pattern FORMATO_ATUAL_PT = Pattern.compile("^[A-Z]{2}-\\d{2}-[A-Z]{2}$");      // AA-00-AA
        private static final Pattern FORMATO_ANTIGO_PT = Pattern.compile("^\\d{2}-[A-Z]{2}-\\d{2}$");      // 00-AA-00
        private static final Pattern FORMATO_MUITO_ANTIGO_PT = Pattern.compile("^[A-Z]{2}-\\d{4}$");       // AA-0000

        // Padrões de matrícula francesa
        private static final Pattern FORMATO_ATUAL_FR = Pattern.compile("^[A-Z]{2}-\\d{3}-[A-Z]{2}$");     // AA-000-AA (desde 2009)
        private static final Pattern FORMATO_ANTIGO_FR = Pattern.compile("^\\d{1,4}\\s[A-Z]{1,3}\\s\\d{2}$"); // 0000 AAA 00 (até 2009)

        // --- INÍCIO DAS EDIÇÕES AQUI (MatriculaValidator) ---
        // Padrões de matrícula espanhola AJUSTADOS para aceitar espaço OU hífen
        // Formato atual (desde 2000): 0000 AAA (com espaço) OU 0000-AAA (com hífen)
        private static final Pattern FORMATO_ATUAL_ES = Pattern.compile("^\\d{4}[ -][A-Z]{3}$");            // 0000 AAA ou 0000-AAA
        // Formato antigo (até 2000): A 0000 AA (com espaço) OU A-0000-AA (com hífen)
        private static final Pattern FORMATO_ANTIGO_ES = Pattern.compile("^[A-Z]{1,2}[ -]\\d{4}[ -][A-Z]{1,2}$"); // A 0000 AA ou A-0000-AA
        // --- FIM DAS EDIÇÕES AQUI (MatriculaValidator) ---

        /**
         * Valida se a matrícula segue um formato válido (português, francês ou espanhol)
         * @param matricula A matrícula a validar
         * @return true se válida, false caso contrário
         */
        public static boolean isMatriculaValida(String matricula) {
            if (matricula == null || matricula.trim().isEmpty()) {
                return false;
            }

            String matriculaUpper = matricula.trim().toUpperCase();

            // Verificar formatos portugueses
            if (FORMATO_ATUAL_PT.matcher(matriculaUpper).matches() ||
                    FORMATO_ANTIGO_PT.matcher(matriculaUpper).matches() ||
                    FORMATO_MUITO_ANTIGO_PT.matcher(matriculaUpper).matches()) {
                return true;
            }

            // Verificar formatos franceses
            if (FORMATO_ATUAL_FR.matcher(matriculaUpper).matches() ||
                    FORMATO_ANTIGO_FR.matcher(matriculaUpper).matches()) {
                return true;
            }

            // Verificar formatos espanhóis
            if (FORMATO_ATUAL_ES.matcher(matriculaUpper).matches() ||
                    FORMATO_ANTIGO_ES.matcher(matriculaUpper).matches()) {
                return true;
            }

            return false;
        }

        /**
         * Valida se a matrícula é válida para o país da cidade fornecida.
         * Esta é a validação primária a usar para inserções/atualizações.
         * @param matricula A matrícula a validar.
         * @param cidade A cidade do veículo, usada para determinar o país.
         * @return true se a matrícula é válida para o país da cidade, false caso contrário.
         */
        // --- INÍCIO DAS EDIÇÕES AQUI (MatriculaValidator - isMatriculaValidaPorPais) ---
        public static boolean isMatriculaValidaPorPais(String matricula, Cidade cidade) {
            if (cidade == null || cidade.getDescricao() == null || matricula == null || matricula.trim().isEmpty()) {
                return false;
            }

            String matriculaUpper = matricula.trim().toUpperCase();
            String cidadeNomeUpper = cidade.getDescricao().trim().toUpperCase();

            // Identifica o país com base na descrição da cidade
            if (cidadeNomeUpper.contains("LISBOA") || cidadeNomeUpper.contains("PORTO") || cidadeNomeUpper.contains("PORTUGAL")) {
                return FORMATO_ATUAL_PT.matcher(matriculaUpper).matches() ||
                        FORMATO_ANTIGO_PT.matcher(matriculaUpper).matches() ||
                        FORMATO_MUITO_ANTIGO_PT.matcher(matriculaUpper).matches();
            } else if (cidadeNomeUpper.contains("MADRID") || cidadeNomeUpper.contains("BARCELONA") || cidadeNomeUpper.contains("SEVILHA") || cidadeNomeUpper.contains("VALENCIA") || cidadeNomeUpper.contains("ESPANHA")) {
                return FORMATO_ATUAL_ES.matcher(matriculaUpper).matches() ||
                        FORMATO_ANTIGO_ES.matcher(matriculaUpper).matches();
            } else if (cidadeNomeUpper.contains("PARIS") || cidadeNomeUpper.contains("MARSELHA") || cidadeNomeUpper.contains("LYON") || cidadeNomeUpper.contains("FRANCA") || cidadeNomeUpper.contains("FRANÇA")) {
                return FORMATO_ATUAL_FR.matcher(matriculaUpper).matches() ||
                        FORMATO_ANTIGO_FR.matcher(matriculaUpper).matches();
            }

            // Se o país da cidade não for reconhecido, consideramos inválido para evitar dados incorretos.
            return false;
        }
        // --- FIM DAS EDIÇÕES AQUI (MatriculaValidator - isMatriculaValidaPorPais) ---

        /**
         * Identifica o país da matrícula
         * @param matricula A matrícula a identificar
         * @return String com o país (PORTUGAL, FRANÇA, ESPANHA) ou INVÁLIDA
         */
        public static String identificarPais(String matricula) {
            if (matricula == null || matricula.trim().isEmpty()) {
                return "INVÁLIDA";
            }

            String matriculaUpper = matricula.trim().toUpperCase();

            // Verificar formatos portugueses
            if (FORMATO_ATUAL_PT.matcher(matriculaUpper).matches() ||
                    FORMATO_ANTIGO_PT.matcher(matriculaUpper).matches() ||
                    FORMATO_MUITO_ANTIGO_PT.matcher(matriculaUpper).matches()) {
                return "PORTUGAL";
            }

            // Verificar formatos franceses
            if (FORMATO_ATUAL_FR.matcher(matriculaUpper).matches() ||
                    FORMATO_ANTIGO_FR.matcher(matriculaUpper).matches()) {
                return "FRANÇA";
            }

            // --- INÍCIO DAS EDIÇÕES AQUI (MatriculaValidator - identificarPais) ---
            // Verificar formatos espanhóis (com as novas regexes)
            if (FORMATO_ATUAL_ES.matcher(matriculaUpper).matches() ||
                    FORMATO_ANTIGO_ES.matcher(matriculaUpper).matches()) {
                return "ESPANHA";
            }
            // --- FIM DAS EDIÇÕES AQUI (MatriculaValidator - identificarPais) ---

            return "FORMATO NÃO RECONHECIDO";
        }

        /**
         * Formata a matrícula para o padrão correto (maiúsculas)
         * @param matricula A matrícula a formatar
         * @return A matrícula formatada
         */
        public static String formatarMatricula(String matricula) {
            if (matricula == null) return null;
            return matricula.trim().toUpperCase();
        }

        /**
         * Obtém uma mensagem de erro explicativa sobre os formatos válidos
         * @return String com os formatos aceites
         */
        // --- INÍCIO DAS EDIÇÕES AQUI (MatriculaValidator - getMensagemFormatos) ---
        // Método atualizado para aceitar uma cidade e dar mensagem específica ao país
        public static String getMensagemFormatos(Cidade cidade) {
            if (cidade == null || cidade.getDescricao() == null) {
                return "Formato de matrícula inválido. País da cidade não especificado.\n" +
                        "Formatos válidos gerais:\n" +
                        "\n=== PORTUGAL ===\n" +
                        "- Formato atual: AA-00-AA (ex: AB-12-CD)\n" +
                        "- Formato antigo: 00-AA-00 (ex: 12-AB-34)\n" +
                        "- Formato muito antigo: AA-0000 (ex: AB-1234)\n" +
                        "\n=== FRANÇA ===\n" +
                        "- Formato atual (desde 2009): AA-000-AA (ex: AB-123-CD)\n" +
                        "- Formato antigo (até 2009): 0000 AAA 00 (ex: 1234 ABC 12)\n" +
                        "\n=== ESPANHA ===\n" +
                        "- Formato atual (desde 2000): 0000 AAA ou 0000-AAA (ex: 1234 ABC ou 1234-FGH)\n" +
                        "- Formato antigo (até 2000): A 0000 AA ou A-0000-AA (ex: B 1234 CD ou GR-5678-C)";
            }

            String cidadeNomeUpper = cidade.getDescricao().trim().toUpperCase();

            if (cidadeNomeUpper.contains("LISBOA") || cidadeNomeUpper.contains("PORTO") || cidadeNomeUpper.contains("PORTUGAL")) {
                return "Formatos válidos de matrícula portuguesa:\n" +
                        "- Formato atual: AA-00-AA (ex: AB-12-CD)\n" +
                        "- Formato antigo: 00-AA-00 (ex: 12-AB-34)\n" +
                        "- Formato muito antigo: AA-0000 (ex: AB-1234)";
            } else if (cidadeNomeUpper.contains("MADRID") || cidadeNomeUpper.contains("BARCELONA") || cidadeNomeUpper.contains("SEVILHA") || cidadeNomeUpper.contains("VALENCIA") || cidadeNomeUpper.contains("ESPANHA")) {
                return "Formatos válidos de matrícula espanhola:\n" +
                        "- Formato atual (desde 2000): 0000 AAA ou 0000-AAA (ex: 1234 ABC ou 1234-FGH)\n" +
                        "- Formato antigo (até 2000): A 0000 AA ou A-0000-AA (ex: B 1234 CD ou GR-5678-C)";
            } else if (cidadeNomeUpper.contains("PARIS") || cidadeNomeUpper.contains("MARSELHA") || cidadeNomeUpper.contains("LYON") || cidadeNomeUpper.contains("FRANCA") || cidadeNomeUpper.contains("FRANÇA")) {
                return "Formatos válidos de matrícula francesa:\n" +
                        "- Formato atual (desde 2009): AA-000-AA (ex: AB-123-CD)\n" +
                        "- Formato antigo (até 2009): 0000 AAA 00 (ex: 1234 ABC 12)";
            } else {
                return "Formato de matrícula inválido para o país da cidade selecionada. Por favor, verifique a matrícula e a cidade.\n" +
                        "Formatos válidos gerais (verifique o país da cidade):\n" +
                        "\n=== PORTUGAL ===\n" +
                        "- Formato atual: AA-00-AA\n" +
                        "- Formato antigo: 00-AA-00\n" +
                        "- Formato muito antigo: AA-0000\n" +
                        "\n=== FRANÇA ===\n" +
                        "- Formato atual (desde 2009): AA-000-AA\n" +
                        "- Formato antigo (até 2009): 0000 AAA 00\n" +
                        "\n=== ESPANHA ===\n" +
                        "- Formato atual (desde 2000): 0000 AAA ou 0000-AAA\n" +
                        "- Formato antigo (até 2000): A 0000 AA ou A-0000-AA";
            }
        }

        // Método original getMensagemFormatos() sem parâmetros (pode ser útil como fallback)
        public static String getMensagemFormatos() {
            return "Formatos válidos de matrícula:\n" +
                    "\n=== PORTUGAL ===\n" +
                    "- Formato atual: AA-00-AA (ex: AB-12-CD)\n" +
                    "- Formato antigo: 00-AA-00 (ex: 12-AB-34)\n" +
                    "- Formato muito antigo: AA-0000 (ex: AB-1234)\n" +
                    "\n=== FRANÇA ===\n" +
                    "- Formato atual (desde 2009): AA-000-AA (ex: AB-123-CD)\n" +
                    "- Formato antigo (até 2009): 0000 AAA 00 (ex: 1234 ABC 12)\n" +
                    "\n=== ESPANHA ===\n" +
                    "- Formato atual (desde 2000): 0000 AAA ou 0000-AAA (ex: 1234 ABC ou 1234-FGH)\n" +
                    "- Formato antigo (até 2000): A 0000 AA ou A-0000-AA (ex: B 1234 CD ou GR-5678-C)";
        }
        // --- FIM DAS EDIÇÕES AQUI (MatriculaValidator - getMensagemFormatos) ---

        /**
         * Obtém informações detalhadas sobre uma matrícula específica
         * @param matricula A matrícula a analisar
         * @return String com informações sobre a matrícula
         */
        public static String getInfoMatricula(String matricula) {
            if (matricula == null || matricula.trim().isEmpty()) {
                return "Matrícula inválida: vazia ou nula";
            }

            String pais = identificarPais(matricula);
            boolean valida = isMatriculaValida(matricula); // Este método agora usa as novas regexes

            StringBuilder info = new StringBuilder();
            info.append("Matrícula: ").append(matricula).append("\n");
            info.append("Formatada: ").append(formatarMatricula(matricula)).append("\n");
            info.append("País: ").append(pais).append("\n");
            info.append("Válida: ").append(valida ? "Sim" : "Não").append("\n");

            if (!valida) {
                // Ao invés de getMensagemFormatos() sem parâmetros, podemos tentar
                // uma mensagem mais específica se tivermos o objeto Cidade.
                // Mas como getInfoMatricula não tem Cidade, manteremos o geral.
                info.append("\n").append(getMensagemFormatos());
            }

            return info.toString();
        }
    }

    // Método para buscar todos os veiculos (sem filtros)
    public List<Veiculo> buscarTodos() {
        return filtrarVeiculos(null, null, null);
    }

    // Método corrigido para inserir um veiculo
    public boolean inserirVeiculo(Veiculo veiculo) {
        String sql = "INSERT INTO gsa_db.veiculo (marca, modelo, matricula, id_estado, preco, id_cidade, id_responsavel, id_comprador, data_venda) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Validações básicas antes de inserir
            if (veiculo == null) {
                System.err.println("Erro: Veiculo não pode ser nulo");
                return false;
            }

            if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {
                System.err.println("Erro: Marca é obrigatória");
                return false;
            }

            if (veiculo.getModelo() == null || veiculo.getModelo().trim().isEmpty()) {
                System.err.println("Erro: Modelo é obrigatório");
                return false;
            }

            if (veiculo.getMatricula() == null || veiculo.getMatricula().trim().isEmpty()) {
                System.err.println("Erro: Matrícula é obrigatória");
                return false;
            }

            // --- INÍCIO DAS EDIÇÕES AQUI (inserirVeiculo - Validação por país) ---
            // Validar formato da matrícula especificamente pelo país da cidade
            if (!MatriculaValidator.isMatriculaValidaPorPais(veiculo.getMatricula(), veiculo.getCidade())) {
                System.err.println("Erro: Formato de matrícula inválido para o país da cidade selecionada.");
                System.err.println(MatriculaValidator.getMensagemFormatos(veiculo.getCidade())); // Mensagem específica do país
                System.err.println("\nMatrícula fornecida: " + veiculo.getMatricula());
                System.err.println("País identificado (pela matrícula): " + MatriculaValidator.identificarPais(veiculo.getMatricula()));
                System.err.println("Cidade do veículo: " + (veiculo.getCidade() != null ? veiculo.getCidade().getDescricao() : "NULA"));
                return false;
            }
            // --- FIM DAS EDIÇÕES AQUI (inserirVeiculo - Validação por país) ---

            if (veiculo.getEstado() == null) {
                System.err.println("Erro: Estado é obrigatório");
                return false;
            }

            if (veiculo.getCidade() == null) {
                System.err.println("Erro: Cidade é obrigatória");
                return false;
            }

            // Definir os parâmetros
            ps.setString(1, veiculo.getMarca().trim());
            ps.setString(2, veiculo.getModelo().trim());
            ps.setString(3, MatriculaValidator.formatarMatricula(veiculo.getMatricula()));
            ps.setInt(4, veiculo.getEstado().getId());
            ps.setDouble(5, veiculo.getPreco());
            ps.setInt(6, veiculo.getCidade().getId());

            // Tratar id_responsavel que pode ser nulo
            if (veiculo.getResponsavel() != null && veiculo.getResponsavel().getId() > 0) {
                ps.setInt(7, veiculo.getResponsavel().getId());
            } else {
                ps.setNull(7, Types.INTEGER);
            }

            // Tratar id_comprador que pode ser nulo
            if (veiculo.getComprador() != null && veiculo.getComprador().getId() > 0) {
                ps.setInt(8, veiculo.getComprador().getId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            // Tratar data_venda que pode ser nulo
            if (veiculo.getDataVenda() != null) {
                ps.setDate(9, new java.sql.Date(veiculo.getDataVenda().getTime()));
            } else {
                ps.setNull(9, Types.DATE);
            }

            // Log com informações da matrícula
            String paisMatricula = MatriculaValidator.identificarPais(veiculo.getMatricula());
            System.out.println("Executando SQL: " + sql);
            System.out.println("Parâmetros: marca=" + veiculo.getMarca() +
                    ", modelo=" + veiculo.getModelo() +
                    ", matricula=" + MatriculaValidator.formatarMatricula(veiculo.getMatricula()) +
                    " (" + paisMatricula + ")" +
                    ", estado_id=" + veiculo.getEstado().getId() +
                    ", cidade_id=" + veiculo.getCidade().getId());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Veiculo inserido com sucesso!");
                System.out.println("Matrícula " + veiculo.getMatricula() + " (" + paisMatricula + ") registada com sucesso!");
                return true;
            } else {
                System.err.println("Nenhuma linha foi afetada na inserção");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir veiculo: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());

            // Se for erro de matrícula duplicada, mostrar informação específica
            if (e.getErrorCode() == 1062 && e.getMessage().contains("matricula")) {
                System.err.println("ATENÇÃO: Já existe um veículo com a matrícula: " + veiculo.getMatricula());
                System.err.println(MatriculaValidator.getInfoMatricula(veiculo.getMatricula()));
            }

            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Erro geral ao inserir veiculo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para atualizar um veiculo (também com validação de matrícula)
    public boolean atualizarVeiculo(Veiculo veiculo) {
        String sql = "UPDATE gsa_db.veiculo SET marca = ?, modelo = ?, matricula = ?, id_estado = ?, preco = ?, id_cidade = ?, id_responsavel = ?, id_comprador = ?, data_venda = ? WHERE id = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Validações básicas antes de atualizar
            if (veiculo == null) {
                System.err.println("Erro: Veiculo não pode ser nulo");
                return false;
            }

            if (veiculo.getMarca() == null || veiculo.getMarca().trim().isEmpty()) {
                System.err.println("Erro: Marca é obrigatória");
                return false;
            }

            if (veiculo.getModelo() == null || veiculo.getModelo().trim().isEmpty()) {
                System.err.println("Erro: Modelo é obrigatório");
                return false;
            }

            // --- INÍCIO DAS EDIÇÕES AQUI (atualizarVeiculo - Validação por país) ---
            // Validar matrícula antes de atualizar (agora por país da cidade)
            if (veiculo.getMatricula() == null || veiculo.getMatricula().trim().isEmpty()) {
                System.err.println("Erro: Matrícula é obrigatória para atualização.");
                return false;
            }
            if (!MatriculaValidator.isMatriculaValidaPorPais(veiculo.getMatricula(), veiculo.getCidade())) {
                System.err.println("Erro ao atualizar: Formato de matrícula inválido para o país da cidade selecionada.");
                System.err.println(MatriculaValidator.getMensagemFormatos(veiculo.getCidade())); // Mensagem específica do país
                System.err.println("\nMatrícula fornecida: " + veiculo.getMatricula());
                System.err.println("País identificado (pela matrícula): " + MatriculaValidator.identificarPais(veiculo.getMatricula()));
                System.err.println("Cidade do veículo: " + (veiculo.getCidade() != null ? veiculo.getCidade().getDescricao() : "NULA"));
                return false;
            }
            // --- FIM DAS EDIÇÕES AQUI (atualizarVeiculo - Validação por país) ---

            if (veiculo.getEstado() == null) {
                System.err.println("Erro: Estado é obrigatório");
                return false;
            }

            if (veiculo.getCidade() == null) {
                System.err.println("Erro: Cidade é obrigatória");
                return false;
            }

            stmt.setString(1, veiculo.getMarca().trim());
            stmt.setString(2, veiculo.getModelo().trim());
            stmt.setString(3, MatriculaValidator.formatarMatricula(veiculo.getMatricula()));
            stmt.setInt(4, veiculo.getEstado().getId());
            stmt.setDouble(5, veiculo.getPreco());
            stmt.setInt(6, veiculo.getCidade().getId());

            if (veiculo.getResponsavel() != null && veiculo.getResponsavel().getId() > 0) {
                stmt.setInt(7, veiculo.getResponsavel().getId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            if (veiculo.getComprador() != null && veiculo.getComprador().getId() > 0) {
                stmt.setInt(8, veiculo.getComprador().getId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            if (veiculo.getDataVenda() != null) {
                stmt.setDate(9, new java.sql.Date(veiculo.getDataVenda().getTime()));
            } else {
                stmt.setNull(9, Types.DATE);
            }

            stmt.setInt(10, veiculo.getId());

            // Log com informações da matrícula
            String paisMatricula = MatriculaValidator.identificarPais(veiculo.getMatricula());
            System.out.println("Atualizando veiculo ID: " + veiculo.getId());
            System.out.println("Nova matrícula: " + MatriculaValidator.formatarMatricula(veiculo.getMatricula()) + " (" + paisMatricula + ")");

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Veiculo atualizado com sucesso!");
                return true;
            } else {
                System.err.println("Nenhuma linha foi afetada na atualização");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar veiculo: " + e.getMessage());

            // Se for erro de matrícula duplicada, mostrar informação específica
            if (e.getErrorCode() == 1062 && e.getMessage().contains("matricula")) {
                System.err.println("ATENÇÃO: Já existe um veículo com a matrícula: " + veiculo.getMatricula());
                System.err.println(MatriculaValidator.getInfoMatricula(veiculo.getMatricula()));
            }

            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Erro geral ao atualizar veiculo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Veiculo buscarPorId(int id) {
        Veiculo veiculo = null;
        String sql = "SELECT v.id, v.marca, v.modelo, v.matricula, v.preco, v.data_venda, " +
                "e.id AS estado_id, e.descricao AS estado_descricao, " +
                "u.id AS responsavel_id, u.nome AS responsavel_nome, " +
                "c.id AS cidade_id, c.descricao AS cidade_descricao, " +
                "comp.id AS comprador_id, comp.nome_completo AS comprador_nome " +
                "FROM gsa_db.veiculo v " +
                "JOIN gsa_db.estado e ON v.id_estado = e.id " +
                "JOIN gsa_db.cidade c ON v.id_cidade = c.id " +
                "LEFT JOIN gsa_db.utilizador u ON v.id_responsavel = u.id " +
                "LEFT JOIN gsa_db.comprador comp ON v.id_comprador = comp.id " +
                "WHERE v.id = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    veiculo = new Veiculo();
                    veiculo.setId(rs.getInt("id"));
                    veiculo.setMarca(rs.getString("marca"));
                    veiculo.setModelo(rs.getString("modelo"));
                    veiculo.setMatricula(rs.getString("matricula"));
                    veiculo.setPreco(rs.getDouble("preco"));
                    veiculo.setDataVenda(rs.getDate("data_venda"));

                    Estado estado = new Estado(rs.getInt("estado_id"), rs.getString("estado_descricao"));
                    veiculo.setEstado(estado);

                    Cidade cidade = new Cidade(rs.getInt("cidade_id"), rs.getString("cidade_descricao"));
                    veiculo.setCidade(cidade);

                    if (rs.getObject("responsavel_id") != null) {
                        Utilizador responsavel = new Utilizador(rs.getInt("responsavel_id"), rs.getString("responsavel_nome"));
                        veiculo.setResponsavel(responsavel);
                    }

                    if (rs.getObject("comprador_id") != null) {
                        Comprador comprador = new Comprador(rs.getInt("comprador_id"), rs.getString("comprador_nome"));
                        veiculo.setComprador(comprador);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar veiculo por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return veiculo;
    }

    public boolean removerVeiculoPorId(int id) {
        String sql = "DELETE FROM gsa_db.veiculo WHERE id = ?";
        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Veiculo removido com sucesso! ID: " + id);
                return true;
            } else {
                System.err.println("Nenhum veiculo encontrado com ID: " + id);
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Erro ao remover veiculo por ID: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar veiculos com filtros (marca, modelo, matricula)
    public List<Veiculo> filtrarVeiculos(String marca, String modelo, String matricula) {
        List<Veiculo> veiculos = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT v.id, v.marca, v.modelo, v.matricula, v.preco, v.data_venda, ")
                .append("e.id AS estado_id, e.descricao AS estado_descricao, ")
                .append("u.id AS responsavel_id, u.nome AS responsavel_nome, ")
                .append("c.id AS cidade_id, c.descricao AS cidade_descricao, ")
                .append("comp.id AS comprador_id, comp.nome_completo AS comprador_nome ")
                .append("FROM gsa_db.veiculo v ")
                .append("JOIN gsa_db.estado e ON v.id_estado = e.id ")
                .append("JOIN gsa_db.cidade c ON v.id_cidade = c.id ")
                .append("LEFT JOIN gsa_db.utilizador u ON v.id_responsavel = u.id ")
                .append("LEFT JOIN gsa_db.comprador comp ON v.id_comprador = comp.id ");

        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        if (marca != null && !marca.trim().isEmpty()) {
            conditions.add("v.marca LIKE ?");
            parameters.add("%" + marca.trim() + "%");
        }
        if (modelo != null && !modelo.trim().isEmpty()) {
            conditions.add("v.modelo LIKE ?");
            parameters.add("%" + modelo.trim() + "%");
        }
        if (matricula != null && !matricula.trim().isEmpty()) {
            conditions.add("v.matricula LIKE ?");
            parameters.add("%" + matricula.trim().toUpperCase() + "%");
        }

        if (!conditions.isEmpty()) {
            sqlBuilder.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        sqlBuilder.append(" ORDER BY v.id DESC");

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Veiculo veiculo = new Veiculo();
                    veiculo.setId(rs.getInt("id"));
                    veiculo.setMarca(rs.getString("marca"));
                    veiculo.setModelo(rs.getString("modelo"));
                    veiculo.setMatricula(rs.getString("matricula"));
                    veiculo.setPreco(rs.getDouble("preco"));
                    veiculo.setDataVenda(rs.getDate("data_venda"));

                    Estado estado = new Estado(rs.getInt("estado_id"), rs.getString("estado_descricao"));
                    veiculo.setEstado(estado);

                    Cidade cidade = new Cidade(rs.getInt("cidade_id"), rs.getString("cidade_descricao"));
                    veiculo.setCidade(cidade);

                    if (rs.getObject("responsavel_id") != null) {
                        Utilizador responsavel = new Utilizador(rs.getInt("responsavel_id"), rs.getString("responsavel_nome"));
                        veiculo.setResponsavel(responsavel);
                    }

                    if (rs.getObject("comprador_id") != null) {
                        Comprador comprador = new Comprador(rs.getInt("comprador_id"), rs.getString("comprador_nome"));
                        veiculo.setComprador(comprador);
                    }

                    veiculos.add(veiculo);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar veiculos com filtros: " + e.getMessage());
            e.printStackTrace();
        }
        return veiculos;
    }

    /**
     * Método adicional para testar validação de matrículas
     * @param matricula A matrícula a testar
     */
    public static void testarMatricula(String matricula) {
        System.out.println("=== TESTE DE MATRÍCULA ===");
        // --- INÍCIO DAS EDIÇÕES AQUI (testarMatricula) ---
        // Não temos o objeto Cidade aqui, então usamos o getInfoMatricula sem cidade.
        System.out.println(MatriculaValidator.getInfoMatricula(matricula));
        // --- FIM DAS EDIÇÕES AQUI (testarMatricula) ---
        System.out.println("==========================\n");
    }

    /**
     * Método para validar uma lista de matrículas
     * @param matriculas Array de matrículas para validar
     */
    public static void validarMatriculas(String... matriculas) {
        System.out.println("=== VALIDAÇÃO DE MÚLTIPLAS MATRÍCULAS ===");
        for (String matricula : matriculas) {
            boolean valida = MatriculaValidator.isMatriculaValida(matricula);
            String pais = MatriculaValidator.identificarPais(matricula);
            System.out.printf("%-15s | %-8s | %s%n", matricula, valida ? "VÁLIDA" : "INVÁLIDA", pais);
        }
        System.out.println("==========================================\n");
    }

    /**
     * Método para buscar veículos por país da matrícula
     * @param pais O país a filtrar (PORTUGAL, FRANÇA, ESPANHA)
     * @return Lista de veículos do país especificado
     */
    public List<Veiculo> buscarPorPaisMatricula(String pais) {
        List<Veiculo> todosVeiculos = buscarTodos();
        List<Veiculo> veiculosPais = new ArrayList<>();

        for (Veiculo veiculo : todosVeiculos) {
            String paisMatricula = MatriculaValidator.identificarPais(veiculo.getMatricula());
            if (paisMatricula.equalsIgnoreCase(pais)) {
                veiculosPais.add(veiculo);
            }
        }

        System.out.println("Encontrados " + veiculosPais.size() + " veículos com matrículas de " + pais);
        return veiculosPais;
    }

    /**
     * Método para obter estatísticas de matrículas por país
     */
    public void obterEstatisticasMatriculas() {
        List<Veiculo> todosVeiculos = buscarTodos();
        int portugal = 0, franca = 0, espanha = 0, invalidas = 0;

        System.out.println("=== ESTATÍSTICAS DE MATRÍCULAS ===");
        for (Veiculo veiculo : todosVeiculos) {
            String pais = MatriculaValidator.identificarPais(veiculo.getMatricula());
            switch (pais) {
                case "PORTUGAL":
                    portugal++;
                    break;
                case "FRANÇA":
                    franca++;
                    break;
                case "ESPANHA":
                    espanha++;
                    break;
                default:
                    invalidas++;
                    System.out.println("Matrícula inválida encontrada: " + veiculo.getMatricula() + " (ID: " + veiculo.getId() + ")");
                    break;
            }
        }

        System.out.println("Total de veículos: " + todosVeiculos.size());
        System.out.println("Portugal: " + portugal);
        System.out.println("França: " + franca);
        System.out.println("Espanha: " + espanha);
        System.out.println("Inválidas: " + invalidas);
        System.out.println("==================================\n");
    }
}