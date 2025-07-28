package model;

/**
 * Classe modelo que representa um País.
 */
public class Pais {

    private int id;
    private String nome;

    // Construtor padrão (sem argumentos) - Essencial para JDBC e frameworks
    public Pais() {
    }

    // Construtor com id e nome
    public Pais(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome; // Para exibição em JComboBoxes, etc.
    }

    // Opcional: equals e hashCode baseados no ID para comparação de objetos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pais pais = (Pais) o;
        return id == pais.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
