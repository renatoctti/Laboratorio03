// src/main/java/com/example/demo/model/Professor.java
package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "professor")
@PrimaryKeyJoinColumn(name = "id")
public class Professor extends Usuario {

    @Column(nullable = false)
    private String nome;
    
    @Column(unique = true, nullable = false)
    private String cpf;
    
    @Column(nullable = false)
    private String departamento;

    private String titulacao;

    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    private InstituicaoEnsino instituicaoEnsino;

    // NOVO CAMPO: Moedas para o Professor
    @Column(nullable = false)
    private int moedas; // O professor tem saldo de moedas

    public Professor() {
        super();
        this.moedas = 1000; // Inicializa moedas como 0 por padrão para novos professores
    }

    // Getters e Setters (existentes)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getTitulacao() {
        return titulacao;
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }

    public InstituicaoEnsino getInstituicaoEnsino() {
        return instituicaoEnsino;
    }

    public void setInstituicaoEnsino(InstituicaoEnsino instituicaoEnsino) {
        this.instituicaoEnsino = instituicaoEnsino;
    }

    // NOVO: Getter e Setter para moedas do Professor
    public int getMoedas() {
        return moedas;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }
}