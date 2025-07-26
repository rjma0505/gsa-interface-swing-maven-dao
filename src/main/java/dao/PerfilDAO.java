package dao;

import model.Perfil;
import model.Utilizador;
import org.mindrot.jbcrypt.BCrypt;
import service.UserSession;
import util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; // Importar ArrayList para listarTodos
import java.util.List;

/**
 * DAO para operações relacionadas com o perfil, mas que o utilizador adaptou para incluir a lógica de login.
 * NOTA: Para uma melhor separação de responsabilidades (Single Responsibility Principle),
 * a lógica de autenticação (buscarPorCredenciais) seria idealmente colocada numa classe
 * UtilizadorDAO ou numa LoginDAO dedicada.
 */
public class PerfilDAO {

    public PerfilDAO() {
        // O construtor não precisa de inicializar a conexão aqui.
        // A conexão será obtida dentro de cada método usando try-with-resources.
    }

    /**
     * Busca um utilizador pelas suas credenciais (email e senha).
     *
     * @param email Email do utilizador.
     * @param senha Senha em texto simples fornecida pelo utilizador.
     * @return Objeto Utilizador se as credenciais forem válidas e o utilizador for encontrado, caso contrário, null.
     */
    public Utilizador buscarPorCredenciais(String email, String senha) {
        Utilizador utilizador = null;
        // 1. Removido o nome da base de dados hardcoded. A ConexaoBD deve gerir isto.
        // 2. Nomes das colunas corrigidos para `palavra_passe` e `id_perfil` (consistente com o seu SQL).
        String sql = "SELECT u.id, u.nome, u.utilizador, u.palavra_passe, p.id as id_perfil, p.descricao as perfil_descricao " +
                "FROM utilizador u " +
                "JOIN perfil p ON u.id_perfil = p.id " +
                "WHERE u.utilizador = ?";

        try (Connection connection = ConexaoBD.getConnection(); // Obter a conexão dentro do try-with-resources
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("palavra_passe"); // Nome da coluna corrigido

                // Verifica a palavra-passe fornecida contra o hash armazenado
                if (BCrypt.checkpw(senha, storedHash)) {
                    // Crie o objeto Utilizador completo usando os setters
                    utilizador = new Utilizador(); // Agora o construtor padrão existe em model.Utilizador
                    utilizador.setId(rs.getInt("id"));
                    utilizador.setNome(rs.getString("nome"));
                    utilizador.setUtilizador(rs.getString("utilizador"));
                    utilizador.setPalavraChave(storedHash); // Armazenar o hash no modelo
                    utilizador.setPerfilId(rs.getInt("id_perfil")); // Nome da coluna corrigido

                    // Crie o objeto Perfil associado
                    Perfil perfil = new Perfil(rs.getInt("id_perfil"), rs.getString("perfil_descricao")); // Construtor com argumentos
                    utilizador.setPerfil(perfil); // Definir o objeto Perfil no Utilizador

                    // Define o utilizador e o perfil na sessão do utilizador
                    UserSession.getInstance().setLoggedInUser(utilizador, perfil);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar por credenciais: " + e.getMessage());
            e.printStackTrace();
        }
        return utilizador;
    }

    /**
     * Obtém um perfil a partir do seu ID.
     * Este método é típico de um PerfilDAO.
     * @param id identificador do perfil
     * @return objeto Perfil preenchido ou null se não existir
     */
    public Perfil obterPorId(int id) {
        Perfil p = null;
        try (Connection con = ConexaoBD.getConnection()) {
            String sql = "SELECT id, descricao FROM perfil WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new Perfil(); // Agora o construtor padrão existe em model.Perfil
                p.setId(rs.getInt("id"));
                p.setDescricao(rs.getString("descricao"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * Lista todos os perfis disponíveis.
     * Este método é típico de um PerfilDAO.
     * @return lista de objetos Perfil
     */
    public List<Perfil> listarTodos() {
        List<Perfil> lista = new ArrayList<>(); // Usar ArrayList
        try (Connection con = ConexaoBD.getConnection()) {
            String sql = "SELECT id, descricao FROM perfil";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Perfil p = new Perfil(); // Agora o construtor padrão existe em model.Perfil
                p.setId(rs.getInt("id"));
                p.setDescricao(rs.getString("descricao"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Obtém um perfil a partir da descrição.
     * Este método é típico de um PerfilDAO.
     * @param descricao texto descritivo do perfil
     * @return objeto Perfil preenchido ou null se não existir
     */
    public Perfil obterPorDescricao(String descricao) {
        Perfil p = null;
        try (Connection con = ConexaoBD.getConnection()) {
            String sql = "SELECT id, descricao FROM perfil WHERE descricao=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, descricao);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new Perfil(); // Agora o construtor padrão existe em model.Perfil
                p.setId(rs.getInt("id"));
                p.setDescricao(rs.getString("descricao"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }
}
