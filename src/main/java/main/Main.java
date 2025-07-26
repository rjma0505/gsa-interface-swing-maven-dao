package main;

import controller.LoginController;

// Declaração da classe principal da aplicação
public class Main {

    // Método principal (main), ponto de entrada da aplicação Java
    // É o primeiro método a ser executado quando se corre o programa
    public static void main(String[] args) {
        // Cria uma nova instância do LoginController
        // Esta ação irá abrir automaticamente a janela de login da aplicação
        new LoginController();
    }
}
