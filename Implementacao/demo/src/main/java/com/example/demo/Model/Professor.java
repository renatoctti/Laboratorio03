package com.example.demo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "professores")
public class Professor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true)
    private String cpf;
    
    @NotBlank(message = "Departamento é obrigatório")
    @Column(nullable = false)
    private String departamento;
    
    @Column(nullable = false)
    private String senha;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoMoedas = new BigDecimal("1000.00");
    
    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;
    
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Transacao> transacoes;
    
    // Constructors
    public Professor() {}
    
    public Professor(String nome, String cpf, String departamento, String senha, Instituicao instituicao) {
        this.nome = nome;
        this.cpf = cpf;
        this.departamento = departamento;
        this.senha = senha;
        this.instituicao = instituicao;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public BigDecimal getSaldoMoedas() { return saldoMoedas; }
    public void setSaldoMoedas(BigDecimal saldoMoedas) { this.saldoMoedas = saldoMoedas; }
    
    public Instituicao getInstituicao() { return instituicao; }
    public void setInstituicao(Instituicao instituicao) { this.instituicao = instituicao; }
    
    public List<Transacao> getTransacoes() { return transacoes; }
    public void setTransacoes(List<Transacao> transacoes) { this.transacoes = transacoes; }
}
