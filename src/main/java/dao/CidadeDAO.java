package dao;

import model.Cidade;
import model.Pais;
import util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CidadeDAO {

    public CidadeDAO() {
        // Construtor padrão
    }

    /**
     * Busca todas as cidades na base de dados, sem JOIN.
     * Evita problemas se alguma cidade não tiver país associado.
     */
    public List<Cidade> buscarTodos() {
        List<Cidade> cidades = new ArrayList<>();
        String sql = "SELECT id, descricao FROM gsa_db.cidade";

        System.out.println("CidadeDAO: Executando SQL para buscar todas as cidades.");

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            int count = 0;
            while (rs.next()) {
                Cidade cidade = new Cidade(rs.getInt("id"), rs.getString("descricao"));
                cidades.add(cidade);
                count++;
                System.out.println("CidadeDAO: Cidade carregada: " + cidade.getDescricao());
            }

            System.out.println("CidadeDAO: Total de cidades encontradas = " + count);

        } catch (SQLException e) {
            System.err.println("CidadeDAO: Erro SQL ao buscar todas as cidades: " + e.getMessage());
            e.printStackTrace();
        }

        return cidades;
    }

    /**
     * Busca uma cidade pela sua descrição (com JOIN ao país).
     */
    public Cidade buscarPorDescricao(String descricao) {
        Cidade cidade = null;
        String sql = "SELECT c.id, c.descricao, p.id AS pais_id, p.descricao AS pais_nome " +
                "FROM cidade c " +
                "JOIN pais p ON c.id_pais = p.id " +
                "WHERE c.descricao = ?";

        System.out.println("CidadeDAO: A tentar buscar cidade por descrição: " + descricao);

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, descricao);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pais pais = new Pais(rs.getInt("pais_id"), rs.getString("pais_nome"));
                    cidade = new Cidade(rs.getInt("id"), rs.getString("descricao"), pais);
                    System.out.println("CidadeDAO: Cidade encontrada: " + cidade.getDescricao());
                } else {
                    System.out.println("CidadeDAO: Nenhuma cidade encontrada para: " + descricao);
                }
            }

        } catch (SQLException e) {
            System.err.println("CidadeDAO: Erro SQL ao buscar cidade por descrição: " + e.getMessage());
            e.printStackTrace();
        }

        return cidade;
    }

    /**
     * Busca uma cidade pelo seu ID.
     */
    public Cidade buscarPorId(int id) {
        Cidade cidade = null;
        String sql = "SELECT c.id, c.descricao, p.id AS pais_id, p.nome AS pais_nome " +
                "FROM gsa_db.cidade c " +
                "JOIN gsa_db.pais p ON c.id_pais = p.id " +
                "WHERE c.id = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pais pais = new Pais(rs.getInt("pais_id"), rs.getString("pais_nome"));
                    cidade = new Cidade(rs.getInt("id"), rs.getString("descricao"), pais);
                }
            }

        } catch (SQLException e) {
            System.err.println("CidadeDAO: Erro SQL ao buscar cidade por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return cidade;
    }
}
