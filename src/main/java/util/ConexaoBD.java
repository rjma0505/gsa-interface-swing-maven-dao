package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

    // URL da base de dados
    private static final String URL = "jdbc:mysql://localhost:3306/gsa_db";
    private static final String USER = "****";  // Usuário do banco de dados
    private static final String PASSWORD = "****";  // Senha do banco de dados

    // Método para obter a conexão com a base de dados
    public static Connection getConnection() {
        try {
            // Registrar o driver do MySQL (não necessário para versões mais recentes do JDBC)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tentar obter a conexão com o banco
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Imprimir sucesso se a conexão for bem-sucedida
            System.out.println("Conexão bem-sucedida com a base de dados!");
            return conn;
        } catch (ClassNotFoundException e) {
            // Imprimir erro se o driver não for encontrado
            System.out.println("Erro: Driver do MySQL não encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            // Imprimir erro se a conexão falhar
            System.out.println("Erro ao conectar com a base de dados.");
            e.printStackTrace();
        }

        return null;
    }
}
