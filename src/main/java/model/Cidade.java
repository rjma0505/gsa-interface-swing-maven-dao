package model;

/**
 * Classe modelo que representa uma Cidade.
 */
public class Cidade {

    private int id;
    private String descricao;
    private Pais pais; // Associação com a classe Pais

    // Construtor padrão (sem argumentos) - Essencial para JDBC e frameworks
    public Cidade() {
    }

    // Construtor com id e descricao
    public Cidade(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Construtor com id, descricao e Pais
    public Cidade(int id, String descricao, Pais pais) {
        this.id = id;
        this.descricao = descricao;
        this.pais = pais;
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

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    @Override
    public String toString() {
        return descricao; // Para exibição em JComboBoxes, etc.
    }

    // Opcional: equals e hashCode baseados no ID para comparação de objetos
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cidade cidade = (Cidade) o;
        return id == cidade.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}