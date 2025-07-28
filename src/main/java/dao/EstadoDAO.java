package dao;

import model.Estado;
import util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para operações relacionadas com a entidade Estado.
 * Gere a persistência e recuperação de dados de estados na base de dados.
 */
public class EstadoDAO {

    public EstadoDAO() {
        // O construtor não precisa inicializar a conexão aqui.
        // A conexão será obtida dentro de cada método usando try-with-resources.
    }

    /**
     * Busca todos os estados na base de dados.
     *
     * @return Uma lista de objetos Estado.
     */
    public List<Estado> buscarTodos() {
        List<Estado> estados = new ArrayList<>();
        String sql = "SELECT id, descricao FROM gsa_db.estado";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estado estado = new Estado(rs.getInt("id"), rs.getString("descricao"));
                estados.add(estado);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os estados: " + e.getMessage());
            e.printStackTrace();
        }
        return estados;
    }

    /**
     * Busca um estado pelo seu ID.
     *
     * @param id O ID do estado a ser buscado.
     * @return O objeto Estado correspondente, ou null se não for encontrado.
     */
    public Estado buscarPorId(int id) {
        Estado estado = null;
        String sql = "SELECT id, descricao FROM gsa_db.estado WHERE id = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estado = new Estado(rs.getInt("id"), rs.getString("descricao"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar estado por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return estado;
    }

    /**
     * Busca um estado pela sua descrição.
     *
     * @param descricao A descrição do estado a ser buscada.
     * @return O objeto Estado correspondente, ou null se não for encontrado.
     */
    public Estado buscarPorDescricao(String descricao) {
        Estado estado = null;
        String sql = "SELECT id, descricao FROM gsa_db.estado WHERE descricao = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, descricao);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estado = new Estado(rs.getInt("id"), rs.getString("descricao"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar estado por descrição: " + e.getMessage());
            e.printStackTrace();
        }
        return estado;
    }
}
