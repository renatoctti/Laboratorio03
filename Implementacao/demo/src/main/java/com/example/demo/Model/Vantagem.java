package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "vantagem")
public class Vantagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(nullable = false)
    private int custoEmMoedas;

    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresaParceira;

    @Column(nullable = false)
    private boolean vendida;

    @Column(nullable = true, length = 1024)
    private String imageUrl;

    // NOVIDADE AQUI: Campo para armazenar o Aluno que comprou a vantagem
    @ManyToOne // Uma vantagem pode ser comprada por um Aluno (ou nenhum)
    @JoinColumn(name = "aluno_comprador_id", nullable = true) // Pode ser nulo se não vendida
    private Aluno alunoComprador;

    // Construtor padrão
    public Vantagem() {
        this.vendida = false;
    }

    // Construtor com todos os campos (atualizado para incluir imageUrl e alunoComprador)
    public Vantagem(String nome, String descricao, int custoEmMoedas, Empresa empresaParceira, String imageUrl, Aluno alunoComprador) {
        this.nome = nome;
        this.descricao = descricao;
        this.custoEmMoedas = custoEmMoedas;
        this.empresaParceira = empresaParceira;
        this.vendida = false; // Inicializa como não vendida
        this.imageUrl = imageUrl;
        this.alunoComprador = alunoComprador; // Pode ser nulo no construtor
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getCustoEmMoedas() {
        return custoEmMoedas;
    }

    public void setCustoEmMoedas(int custoEmMoedas) {
        this.custoEmMoedas = custoEmMoedas;
    }

    public Empresa getEmpresaParceira() {
        return empresaParceira;
    }

    public void setEmpresaParceira(Empresa empresaParceira) {
        this.empresaParceira = empresaParceira;
    }

    public boolean isVendida() {
        return vendida;
    }

    public void setVendida(boolean vendida) {
        this.vendida = vendida;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Aluno getAlunoComprador() {
        return alunoComprador;
    }

    public void setAlunoComprador(Aluno alunoComprador) {
        this.alunoComprador = alunoComprador;
    }
}
