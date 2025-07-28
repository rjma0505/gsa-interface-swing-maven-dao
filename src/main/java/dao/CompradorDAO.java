package dao;

import model.Comprador;
import util.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompradorDAO {

    public CompradorDAO() {
        // Construtor simples
    }

    public List<Comprador> buscarTodos() {
        List<Comprador> compradores = new ArrayList<>();
        String sql = "SELECT id, nome_completo, telefone, email FROM gsa_db.comprador";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Comprador comprador = new Comprador();
                comprador.setId(rs.getInt("id"));
                comprador.setNomeCompleto(rs.getString("nome_completo"));
                comprador.setTelefone(rs.getString("telefone"));
                comprador.setEmail(rs.getString("email"));
                compradores.add(comprador);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os compradores: " + e.getMessage());
            e.printStackTrace();
        }

        return compradores;
    }

    public Comprador buscarPorId(int id) {
        Comprador comprador = null;
        String sql = "SELECT id, nome_completo, telefone, email FROM gsa_db.comprador WHERE id = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    comprador = new Comprador();
                    comprador.setId(rs.getInt("id"));
                    comprador.setNomeCompleto(rs.getString("nome_completo"));
                    comprador.setTelefone(rs.getString("telefone"));
                    comprador.setEmail(rs.getString("email"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar comprador por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return comprador;
    }

    public Comprador buscarPorEmail(String email) {
        Comprador comprador = null;
        String sql = "SELECT id, nome_completo, telefone, email FROM gsa_db.comprador WHERE email = ?";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    comprador = new Comprador();
                    comprador.setId(rs.getInt("id"));
                    comprador.setNomeCompleto(rs.getString("nome_completo"));
                    comprador.setTelefone(rs.getString("telefone"));
                    comprador.setEmail(rs.getString("email"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar comprador por email: " + e.getMessage());
            e.printStackTrace();
        }

        return comprador;
    }

    public void salvar(Comprador comprador) throws SQLException {
        String sql = "INSERT INTO gsa_db.comprador (nome_completo, telefone, email) VALUES (?, ?, ?)";

        try (Connection connection = ConexaoBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, comprador.getNomeCompleto());
            ps.setString(2, comprador.getTelefone());
            ps.setString(3, comprador.getEmail());

            ps.executeUpdate();
        }
    }
}
