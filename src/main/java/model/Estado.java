package model;

public class Estado {

    private int id;
    private String descricao;

    // Construtor com id e descricao
    public Estado(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    // Construtor sem parâmetros (caso você precise instanciar a partir do banco de dados)
    public Estado() {
    }

    // Getters e setters
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

    @Override
    public String toString() {
        return descricao;  // Exibe a descrição no JComboBox ou em outras interações
    }
}
