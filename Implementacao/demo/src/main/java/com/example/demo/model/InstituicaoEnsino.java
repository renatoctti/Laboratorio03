package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class InstituicaoEnsino {
   
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   @Column(nullable = false, unique = true)
   private String nome;

   private String endereco;

   @OneToMany(mappedBy = "instituicaoEnsino", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Aluno> alunos;
   
   @OneToMany(mappedBy = "instituicaoEnsino", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Professor> professores;

   public InstituicaoEnsino () {

   }

   public InstituicaoEnsino (String nome, String endereco){
      this.nome = nome;
      this.endereco = endereco;
   }

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

   public String getEndereco() {
      return endereco;
   }

   public void setEndereco(String endereco) {
      this.endereco = endereco;
   }

   public List<Aluno> getAlunos() {
      return alunos;
   }

   public void setAlunos(List<Aluno> alunos) {
      this.alunos = alunos;
   }

   public List<Professor> getProfessores() {
      return professores;
   }

   public void setProfessores(List<Professor> professores) {
      this.professores = professores;
   }

   
}
