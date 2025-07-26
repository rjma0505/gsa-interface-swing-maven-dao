package controller;

import javax.swing.*;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;
import model.Utilizador;
import dao.UtilizadorDAO; // Manter se UtilizadorDAO ainda for usado para outras operações
import dao.PerfilDAO;    // Importar PerfilDAO para a autenticação
import view.LoginView;
import view.MenuView; // Alterado para MenuView, a próxima vista principal do frontoffice
// Se houver uma MainView ou FrontOfficeMainView, ajuste o import e a criação

public class LoginController {

    private final LoginView loginView;
    private final PerfilDAO perfilDAO; // Alterado para PerfilDAO para autenticação
    // private final UtilizadorDAO utilizadorDAO; // Removido se não for mais necessário para outras operações aqui

    public LoginController() {
        loginView = new LoginView();
        perfilDAO = new PerfilDAO(); // Instanciar PerfilDAO para usar buscarPorCredenciais
        // utilizadorDAO = new UtilizadorDAO(); // Remover ou manter se necessário para outras finalidades

        loginView.getBtnEntrar().addActionListener(e -> {
            String email = loginView.getUtilizador();
            String senha = loginView.getPalavraChave();

            System.out.println("Tentando login com email: " + email + " e senha: " + senha);

            // Chamar o método buscarPorCredenciais do PerfilDAO
            Utilizador utilizador = perfilDAO.buscarPorCredenciais(email, senha);

            if (utilizador != null) {
                System.out.println("Login aceite para: " + email);

                JOptionPane.showMessageDialog(loginView,
                        "Login bem-sucedido!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                loginView.dispose();
                // Abre a próxima janela principal do frontoffice, que é a MenuView
                new MenuView().mostrar();
            } else {
                JOptionPane.showMessageDialog(loginView,
                        "Credenciais inválidas. Verifique seu email e senha.",
                        "Erro de Autenticação",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        loginView.setVisible(true);
    }

    // O método applyGlobalUITheme() deve ser chamado no Main.java antes de criar o LoginController
    // para garantir que o tema é aplicado a todos os componentes Swing.
    // public static void applyGlobalUITheme() { } // Removido, pois é responsabilidade do Main.java
}
