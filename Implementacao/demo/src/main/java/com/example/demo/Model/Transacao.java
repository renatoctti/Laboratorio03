package com.example.demo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
public class Transacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
    
    @NotBlank(message = "Motivo é obrigatório")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;
    
    @Column(nullable = false)
    private LocalDateTime dataTransacao = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;
    
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;
    
    
    
    // Constructors
    public Transacao() {}
    
    public Transacao(BigDecimal valor, String motivo, Professor professor, Aluno aluno) {
        this.valor = valor;
        this.motivo = motivo;
        this.professor = professor;
        this.aluno = aluno;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    
    public LocalDateTime getDataTransacao() { return dataTransacao; }
    public void setDataTransacao(LocalDateTime dataTransacao) { this.dataTransacao = dataTransacao; }
    
    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }
    
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    
}
