package model;

/**
 * Classe modelo que representa um utilizador do sistema
 * Guarda os dados básicos de autenticação e associação de perfil
 */
public class Utilizador {

    private int id;
    private String nome;
    private String utilizador; // Este é o email/login
    private String palavraChave; // Hash da senha
    private int perfilId; // ID do perfil (1=Admin, 2=Tecnico, 3=Formador)
    private Perfil perfil; // Objeto Perfil associado

    // Construtor padrão (sem argumentos) - ESSENCIAL PARA RESOLVER O ERRO
    public Utilizador() {
    }

    // Construtor com id e nome (se necessário para a sua lógica)
    public Utilizador(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Construtor completo com todos os campos básicos
    public Utilizador(int id, String nome, String utilizador, String palavraChave, int perfilId) {
        this.id = id;
        this.nome = nome;
        this.utilizador = utilizador;
        this.palavraChave = palavraChave;
        this.perfilId = perfilId;
    }

    // Construtor completo que aceita um objeto Perfil
    public Utilizador(int id, String nome, String utilizador, String palavraChave, Perfil perfil) {
        this.id = id;
        this.nome = nome;
        this.utilizador = utilizador;
        this.palavraChave = palavraChave;
        this.setPerfil(perfil); // Usa o setter que também seta o perfilId
    }


    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getUtilizador() { return utilizador; }
    public void setUtilizador(String utilizador) { this.utilizador = utilizador; }
    public String getPalavraChave() { return palavraChave; }
    public void setPalavraChave(String palavraChave) { this.palavraChave = palavraChave; }
    public int getPerfilId() { return perfilId; }
    public void setPerfilId(int perfilId) { this.perfilId = perfilId; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        if (perfil != null) {
            this.perfilId = perfil.getId(); // Garante que perfilId esteja sincronizado
        }
    }

    // ******* ESSENCIAL PARA O JCOMBOBOX *******
    @Override
    public String toString() {
        return nome; // Retorna o nome para exibição no JComboBox
    }

    // Opcional: para comparação de objetos Utilizador (útil em sets, maps, ou quando removemos itens)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilizador that = (Utilizador) o;
        return id == that.id; // Compara por ID
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
