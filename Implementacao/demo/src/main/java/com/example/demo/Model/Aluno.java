// src/main/java/com/example/demo/model/Aluno.java
package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "aluno")
@PrimaryKeyJoinColumn(name = "id")
public class Aluno extends Usuario {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String rg;
    private String endereco;

    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    private InstituicaoEnsino instituicaoEnsino;

    @Column(nullable = false)
    private String curso;

    // NOVO CAMPO: Moedas para o Aluno
    @Column(nullable = false)
    private int moedas; // O aluno tem saldo de moedas

    public Aluno() {
        super();
        this.moedas = 0; // Inicializa moedas como 0 por padrão para novos alunos
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

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public InstituicaoEnsino getInstituicaoEnsino() {
        return instituicaoEnsino;
    }

    public void setInstituicaoEnsino(InstituicaoEnsino instituicaoEnsino) {
        this.instituicaoEnsino = instituicaoEnsino;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    // NOVO: Getter e Setter para moedas do Aluno
    public int getMoedas() {
        return moedas;
    }

    public void setMoedas(int moedas) {
        this.moedas = moedas;
    }
}