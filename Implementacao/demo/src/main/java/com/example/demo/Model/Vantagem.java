package com.example.demo.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "vantagem")
public class Vantagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 500)
    private String descricao;

    @Column(name = "custo_moedas", nullable = false, precision = 10, scale = 2)
    private BigDecimal custoMoedas;

    @ManyToOne(fetch = FetchType.EAGER) // Mudei para EAGER para evitar LazyInitializationException
    @JoinColumn(name = "empresa_id")
    private EmpresaParceira empresa;

    // Construtores
    public Vantagem() {
    }

    public Vantagem(String nome, String descricao, BigDecimal custoMoedas, EmpresaParceira empresa) {
        this.nome = nome;
        this.descricao = descricao;
        this.custoMoedas = custoMoedas;
        this.empresa = empresa;
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

    public BigDecimal getCustoMoedas() {
        return custoMoedas;
    }

    public void setCustoMoedas(BigDecimal custoMoedas) {
        this.custoMoedas = custoMoedas;
    }

    public EmpresaParceira getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaParceira empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Vantagem{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", custoMoedas=" + custoMoedas +
                ", empresa=" + (empresa != null ? empresa.getNome() : "null") +
                '}';
    }
}
