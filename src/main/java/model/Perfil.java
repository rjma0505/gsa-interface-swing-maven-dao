package model;

/**
 * Classe Perfil representa o tipo ou função de um utilizador no sistema.
 * Exemplo: "Administrador", "Responsável de Frota", "Vendedor", etc.
 */
public class Perfil {

    private int id;           // Identificador único do perfil
    private String descricao; // Descrição textual do perfil

    // Construtor padrão (sem argumentos) - ESSENCIAL para JDBC e frameworks
    public Perfil() {
        // Construtor sem argumentos
    }

    // Construtor completo
    public Perfil(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Representação textual para exibição em JComboBox, tabelas, etc.
    @Override
    public String toString() {
        return descricao;
    }

    // Equals e hashCode baseados no id para consistência (opcional, mas boa prática)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfil)) return false;
        Perfil perfil = (Perfil) o;
        return id == perfil.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}