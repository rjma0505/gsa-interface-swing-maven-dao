package model;

import java.util.Date;

/**
 * Classe modelo que representa um Veículo no sistema.
 * Guarda os dados básicos e associações com outras entidades.
 */
public class Veiculo {

    private int id;
    private String marca;
    private String modelo;
    private String matricula;
    private Estado estado;          // Associação com a classe Estado
    private double preco;
    private Cidade cidade;          // Associação com a classe Cidade
    private Utilizador responsavel; // Associação com a classe Utilizador (Responsável)
    private Comprador comprador;    // Associação com a classe Comprador
    private Date dataVenda;         // Data de venda (pode ser nula)

    // Construtor padrão (sem parâmetros) - Essencial para JDBC
    public Veiculo() {}

    // Construtor com parâmetros para inserção (sem ID, responsavel, comprador, dataVenda que podem ser nulos)
    public Veiculo(String marca, String modelo, String matricula, Estado estado, double preco, Cidade cidade) {
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.estado = estado;
        this.preco = preco;
        this.cidade = cidade;
    }

    // Construtor completo com todos os parâmetros
    public Veiculo(int id, String marca, String modelo, String matricula, Estado estado, double preco, Cidade cidade, Utilizador responsavel, Comprador comprador, Date dataVenda) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.estado = estado;
        this.preco = preco;
        this.cidade = cidade;
        this.responsavel = responsavel;
        this.comprador = comprador;
        this.dataVenda = dataVenda;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Utilizador getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Utilizador responsavel) {
        this.responsavel = responsavel;
    }

    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }

    public Date getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Date dataVenda) {
        this.dataVenda = dataVenda;
    }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + matricula + ")";
    }
}
