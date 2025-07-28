package service;

import model.Utilizador;
import model.Perfil;

/**
 * Classe singleton para gerir a sessão do utilizador logado na aplicação.
 * Armazena o objeto Utilizador e o seu Perfil.
 */
public class UserSession {
    private static UserSession instance;
    private Utilizador utilizador;
    private Perfil perfil;

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
        System.out.println("UserSession: Utilizador logado: " + (utilizador != null ? utilizador.getUtilizador() : "N/A") + ", Perfil: " + (perfil != null ? perfil.getDescricao() : "N/A"));
    }

    /**
     * Termina a sessão do utilizador, limpando os dados do utilizador e do perfil.
     * Este método é chamado pelo MenuController.
     */
    public void cleanUserSession() { // MÉTODO ADICIONADO/CORRIGIDO
        this.utilizador = null;
        this.perfil = null;
        System.out.println("UserSession: Sessão do utilizador limpa (cleanUserSession).");
    }

    /**
     * Termina a sessão do utilizador, limpando os dados do utilizador e do perfil.
     * Renomeado de cleanUserSession() para logout() para maior clareza.
     * Este método agora chama cleanUserSession() para consistência.
     */
    public void logout() {
        this.cleanUserSession(); // Chama o método cleanUserSession para limpar a sessão
        System.out.println("UserSession: Sessão do utilizador limpa (logout).");
    }

    /**
     * Obtém o utilizador atualmente logado.
     * @return O objeto Utilizador logado, ou null se ninguém estiver logado.
     */
    public Utilizador getUtilizador() {
        return utilizador;
    }

    /**
     * Obtém o perfil do utilizador atualmente logado.
     * @return O objeto Perfil do utilizador logado, ou null se ninguém estiver logado.
     */
    public Perfil getPerfil() {
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
