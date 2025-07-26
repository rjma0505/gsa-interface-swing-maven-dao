package dao;

import util.ConexaoBD;
import java.sql.*;
import model.Utilizador;

public class UtilizadorDAO {

    private final Connection connection;

    public UtilizadorDAO() {
        this.connection = ConexaoBD.getConnection();
    }

    public Utilizador buscarPorCredenciais(String utilizador, String palavraPasse) {
        Utilizador user = null;
        String sql = "SELECT id, nome, utilizador, palavra_passe, id FROM utilizador WHERE utilizador = ? AND palavra_passe = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, utilizador);
            ps.setString(2, palavraPasse);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new Utilizador(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("utilizador"),
                        rs.getString("palavra_passe"),
                        rs.getInt("id")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar utilizador por credenciais: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }
}
