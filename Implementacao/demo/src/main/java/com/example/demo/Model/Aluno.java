package com.example.demo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "alunos")
public class Aluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "CPF é obrigatório")
    @Column(nullable = false, unique = true)
    private String cpf;
    
    @NotBlank(message = "RG é obrigatório")
    @Column(nullable = false)
    private String rg;
    
    @NotBlank(message = "Endereço é obrigatório")
    @Column(nullable = false)
    private String endereco;
    
    @NotBlank(message = "Curso é obrigatório")
    @Column(nullable = false)
    private String curso;
    
    @Column(nullable = false)
    private String senha;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoMoedas = BigDecimal.ZERO;
    
    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    private Instituicao instituicao;
    
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<Transacao> transacoes;
    
    // Constructors
    public Aluno() {}
    
    public Aluno(String nome, String email, String cpf, String rg, String endereco, String curso, String senha, Instituicao instituicao) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.curso = curso;
        this.senha = senha;
        this.instituicao = instituicao;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg = rg; }
    
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    
    public BigDecimal getSaldoMoedas() { return saldoMoedas; }
    public void setSaldoMoedas(BigDecimal saldoMoedas) { this.saldoMoedas = saldoMoedas; }
    
    public Instituicao getInstituicao() { return instituicao; }
    public void setInstituicao(Instituicao instituicao) { this.instituicao = instituicao; }
    
    public List<Transacao> getTransacoes() { return transacoes; }
    public void setTransacoes(List<Transacao> transacoes) { this.transacoes = transacoes; }
}
