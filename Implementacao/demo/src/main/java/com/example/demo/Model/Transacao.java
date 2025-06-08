package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime; // Para registrar a data e hora da transação

@Entity
@Table(name = "transacao")
public class Transacao {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @ManyToOne // Muitas transações para um professor
   @JoinColumn(name = "professor_id", nullable = false)
   private Professor professorOrigem; // Professor que enviou as moedas

   @ManyToOne // Muitas transações para um aluno
   @JoinColumn(name = "aluno_id", nullable = false)
   private Aluno alunoDestino; // Aluno que recebeu as moedas

   @Column(nullable = false)
   private int quantidadeMoedas; // Quantidade de moedas transferidas

   @Column(nullable = false, length = 500) // Mensagem aberta obrigatória
   private String motivo;

   @Column(nullable = false)
   private LocalDateTime dataTransacao; // Data e hora da transação

   // Construtor padrão (necessário para JPA)
   public Transacao() {
      this.dataTransacao = LocalDateTime.now(); // Define a data/hora atual por padrão
   }

   // Construtor com campos obrigatórios
   public Transacao(Professor professorOrigem, Aluno alunoDestino, int quantidadeMoedas, String motivo) {
      this.professorOrigem = professorOrigem;
      this.alunoDestino = alunoDestino;
      this.quantidadeMoedas = quantidadeMoedas;
      this.motivo = motivo;
      this.dataTransacao = LocalDateTime.now(); // Define a data/hora no momento da criação
   }

   // Getters e Setters
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Professor getProfessorOrigem() {
      return professorOrigem;
   }

   public void setProfessorOrigem(Professor professorOrigem) {
      this.professorOrigem = professorOrigem;
   }

   public Aluno getAlunoDestino() {
      return alunoDestino;
   }

   public void setAlunoDestino(Aluno alunoDestino) {
      this.alunoDestino = alunoDestino;
   }

   public int getQuantidadeMoedas() {
      return quantidadeMoedas;
   }

   public void setQuantidadeMoedas(int quantidadeMoedas) {
      this.quantidadeMoedas = quantidadeMoedas;
   }

   public String getMotivo() {
      return motivo;
   }

   public void setMotivo(String motivo) {
      this.motivo = motivo;
   }

   public LocalDateTime getDataTransacao() {
      return dataTransacao;
   }

   public void setDataTransacao(LocalDateTime dataTransacao) {
      this.dataTransacao = dataTransacao;
   }

   @Override
   public String toString() {
      return "Transacao{" +
            "id=" + id +
            ", professorOrigem=" + professorOrigem.getNome() +
            ", alunoDestino=" + alunoDestino.getNome() +
            ", quantidadeMoedas=" + quantidadeMoedas +
            ", motivo='" + motivo + '\'' +
            ", dataTransacao=" + dataTransacao +
            '}';
   }
}
