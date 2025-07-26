package service;

import model.Utilizador;
import model.Perfil;

/**
 * Classe singleton para gerir a sessão do utilizador logado na aplicação.
 * Armazena o objeto Utilizador e o seu Perfil.
 */
public class UserSession {
    private static UserSession instance;
    private Utilizador utilizador; // Mantido o nome da sua variável
    private Perfil perfil;       // Mantido o nome da sua variável

    /**
     * Construtor privado para garantir o padrão Singleton.
     */
    private UserSession() {
        // Construtor privado para prevenir a instanciação direta
    }

    /**
     * Retorna a única instância de UserSession.
     * @return A instância de UserSession.
     */
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Define o utilizador e o seu perfil como logados na sessão.
     * @param utilizador O objeto Utilizador que fez login.
     * @param perfil O objeto Perfil associado ao utilizador.
     */
    public void setLoggedInUser(Utilizador utilizador, Perfil perfil) {
        this.utilizador = utilizador;
        this.perfil = perfil;
    }

    /**
     * Termina a sessão do utilizador, limpando os dados do utilizador e do perfil.
     * Renomeado de cleanUserSession() para logout() para maior clareza.
     */
    public void logout() {
        this.utilizador = null;
        this.perfil = null;
    }

    /**
     * Obtém o utilizador atualmente logado.
     * @return O objeto Utilizador logado, ou null se ninguém estiver logado.
     */
    public Utilizador getUtilizador() { // Mantido o nome do seu método
        return utilizador;
    }

    /**
     * Obtém o perfil do utilizador atualmente logado.
     * @return O objeto Perfil do utilizador logado, ou null se ninguém estiver logado.
     */
    public Perfil getPerfil() { // Mantido o nome do seu método
        return perfil;
    }

    /**
     * Verifica se existe um utilizador logado na sessão.
     * @return true se um utilizador estiver logado, false caso contrário.
     */
    public boolean isLoggedIn() {
        return this.utilizador != null;
    }
}
