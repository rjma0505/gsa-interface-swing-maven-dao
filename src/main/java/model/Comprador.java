package model;

/**
 * Classe modelo que representa um Comprador.
 */
public class Comprador {

    private int id;
    private String nomeCompleto; // Nome completo do comprador
    private String telefone;     // Número de telefone
    private String email;        // Email do comprador

    // Construtor padrão (sem argumentos)
    public Comprador() {
    }

    // Construtor com id e nomeCompleto (útil para JComboBoxes)
    public Comprador(int id, String nomeCompleto) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
    }

    // Construtor completo
    public Comprador(int id, String nomeCompleto, String telefone, String email) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nomeCompleto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comprador comprador = (Comprador) o;
        return id == comprador.id;
    }

    // hashCode opcional para coleções
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
