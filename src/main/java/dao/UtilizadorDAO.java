package dao;

import model.Utilizador;
import model.Perfil; // Importar a classe Perfil
import util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Importar SQLException
import java.sql.Statement; // Importar Statement se usado
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane; // Para mensagens de erro, se necessário

/**
 * DAO para operações sobre a tabela utilizador.
 * Adaptado do projeto anterior, com foco na gestão de utilizadores e perfis.
 */
public class UtilizadorDAO {

    // A conexão é inicializada uma vez no construtor, conforme o seu padrão de trabalho.
    private final Connection connection;
    private final PerfilDAO perfilDAO; // Dependência para PerfilDAO para carregar objetos Perfil

    public UtilizadorDAO() {
        this.connection = ConexaoBD.getConnection(); // Estabelece a conexão com o banco de dados
        this.perfilDAO = new PerfilDAO(); // Inicializa PerfilDAO
    }

    /**
     * Lista todos os utilizadores na base de dados.
     * Carrega também o objeto Perfil associado a cada utilizador.
     *
     * @return Uma lista de objetos Utilizador.
     */
    public List<Utilizador> listarTodos() { // Renomeado de buscarTodos para listarTodos
        List<Utilizador> utilizadores = new ArrayList<>();
        String sql = "SELECT id, nome, utilizador, palavra_passe, id_perfil FROM gsa_db.utilizador";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utilizador u = new Utilizador();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setUtilizador(rs.getString("utilizador"));
                u.setPalavraChave(rs.getString("palavra_passe")); // Apenas o hash
                u.setPerfilId(rs.getInt("id_perfil"));

                // Carregar o objeto Perfil completo e associá-lo
                Perfil perfil = perfilDAO.obterPorId(rs.getInt("id_perfil"));
                u.setPerfil(perfil);

                utilizadores.add(u);
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao listar todos os utilizadores: " + e.getMessage());
            e.printStackTrace();
        }
        return utilizadores;
    }

    /**
     * Insere um novo utilizador na base de dados.
     *
     * @param u O objeto Utilizador a ser inserido.
     * @return true se a inserção foi bem-sucedida, false caso contrário.
     */
    public boolean inserir(Utilizador u) {
        String sql = "INSERT INTO gsa_db.utilizador (nome, utilizador, palavra_passe, id_perfil) VALUES (?,?,?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getUtilizador());
            ps.setString(3, u.getPalavraChave());
            ps.setInt(4, u.getPerfilId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Erro SQL ao inserir utilizador: " + e.getMessage());
            // Pode adicionar uma verificação de SQLIntegrityConstraintViolationException aqui
            // para mensagens mais específicas, se necessário.
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Atualiza os dados de um utilizador existente na base de dados.
     *
     * @param u O objeto Utilizador com os dados atualizados.
     */
    public void atualizar(Utilizador u) {
        String sql = "UPDATE gsa_db.utilizador SET nome=?, utilizador=?, id_perfil=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, u.getNome());
            ps.setString(2, u.getUtilizador());
            ps.setInt(3, u.getPerfilId());
            ps.setInt(4, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro SQL ao atualizar utilizador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Apaga um utilizador da base de dados pelo seu ID.
     *
     * @param id O ID do utilizador a ser apagado.
     */
    public void apagar(int id) {
        String sql = "DELETE FROM gsa_db.utilizador WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro SQL ao apagar utilizador: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Busca um utilizador pelo seu ID.
     * Carrega também o objeto Perfil associado.
     *
     * @param id O ID do utilizador a ser buscado.
     * @return O objeto Utilizador correspondente, ou null se não for encontrado.
     */
    public Utilizador obterPorId(int id) {
        Utilizador u = null;
        String sql = "SELECT id, nome, utilizador, palavra_passe, id_perfil FROM gsa_db.utilizador WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Utilizador();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setUtilizador(rs.getString("utilizador"));
                    u.setPalavraChave(rs.getString("palavra_passe"));
                    u.setPerfilId(rs.getInt("id_perfil"));

                    // Carregar o objeto Perfil completo e associá-lo
                    Perfil perfil = perfilDAO.obterPorId(rs.getInt("id_perfil"));
                    u.setPerfil(perfil);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao obter utilizador por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return u;
    }

    /**
     * Busca um utilizador pelo seu email (campo 'utilizador' na BD).
     * Carrega também o objeto Perfil associado.
     *
     * @param email O email do utilizador a ser buscado.
     * @return O objeto Utilizador correspondente, ou null se não for encontrado.
     */
    public Utilizador obterPorEmail(String email) {
        Utilizador u = null;
        String sql = "SELECT id, nome, utilizador, palavra_passe, id_perfil FROM gsa_db.utilizador WHERE utilizador=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Utilizador();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setUtilizador(rs.getString("utilizador"));
                    u.setPalavraChave(rs.getString("palavra_passe"));
                    u.setPerfilId(rs.getInt("id_perfil"));

                    // Carregar o objeto Perfil completo e associá-lo
                    Perfil perfil = perfilDAO.obterPorId(rs.getInt("id_perfil"));
                    u.setPerfil(perfil);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao obter utilizador por email: " + e.getMessage());
            e.printStackTrace();
        }
        return u;
    }

    /**
     * Verifica se um email já existe na base de dados.
     *
     * @param email O email a ser verificado.
     * @return true se o email já existe, false caso contrário.
     */
    public boolean existeEmail(String email) {
        String sql = "SELECT COUNT(*) FROM gsa_db.utilizador WHERE utilizador=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao verificar existência de email: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // O método 'buscarResponsavelPorNome' do seu outro projeto pode ser adaptado
    // para 'obterPorNome' se for necessário buscar utilizadores por nome em algum ponto.
    // No entanto, para o fluxo atual, 'listarTodos', 'obterPorId', 'obterPorEmail' são os mais críticos.
    // Se precisar:
    /*
    public Utilizador obterPorNome(String nome) {
        Utilizador u = null;
        String sql = "SELECT id, nome, utilizador, palavra_passe, id_perfil FROM gsa_db.utilizador WHERE nome = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Utilizador();
                    u.setId(rs.getInt("id"));
                    u.setNome(rs.getString("nome"));
                    u.setUtilizador(rs.getString("utilizador"));
                    u.setPalavraChave(rs.getString("palavra_passe"));
                    u.setPerfilId(rs.getInt("id_perfil"));
                    Perfil perfil = perfilDAO.obterPorId(rs.getInt("id_perfil"));
                    u.setPerfil(perfil);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro SQL ao obter utilizador por nome: " + e.getMessage());
            e.printStackTrace();
        }
        return u;
    }
    */
}