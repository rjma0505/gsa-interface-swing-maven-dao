package dao;

import model.Pais;
import util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para operações relacionadas com a entidade País.
 * Gere a persistência e recuperação de dados de países na base de dados.
 */
public class PaisDAO {

    public PaisDAO() {
        // O construtor não precisa inicializar a conexão aqui.
        // A conexão será obtida dentro de cada método usando try-with-resources.
    }

    /**
     * Busca todos os países na base de dados.
     *
     * @return Uma lista de objetos Pais.
     */
    public List<Pais> buscarTodos() {
        List<Pais> paises = new ArrayList<>();
        String sql = "SELECT id, nome FROM gsa_db.pais";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pais pais = new Pais(rs.getInt("id"), rs.getString("nome"));
                paises.add(pais);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os países: " + e.getMessage());
            e.printStackTrace();
        }
        return paises;
    }

    /**
     * Busca um país pelo seu ID.
     *
     * @param id O ID do país a ser buscado.
     * @return O objeto Pais correspondente, ou null se não for encontrado.
     */
    public Pais buscarPorId(int id) {
        Pais pais = null;
        String sql = "SELECT id, nome FROM gsa_db.pais WHERE id = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pais = new Pais(rs.getInt("id"), rs.getString("nome"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar país por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return pais;
    }

    /**
     * Busca um país pelo seu nome.
     *
     * @param nome O nome do país a ser buscado.
     * @return O objeto Pais correspondente, ou null se não for encontrado.
     */
    public Pais buscarPorNome(String nome) {
        Pais pais = null;
        String sql = "SELECT id, nome FROM gsa_db.pais WHERE nome = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nome);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    pais = new Pais(rs.getInt("id"), rs.getString("nome"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar país por nome: " + e.getMessage());
            e.printStackTrace();
        }
        return pais;
    }
}
