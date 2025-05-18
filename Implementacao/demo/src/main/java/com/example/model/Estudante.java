package com.example.model;

public class Estudante {
   private int id;
   private String nome;
   private int saldo;

   public Estudante() {
   }

   public Estudante(String nome) {
      this.nome = nome;
   }

   public Estudante(int id, String nome, int saldo) {
      this.id = id;
      this.nome = nome;
      this.saldo = saldo;
   }

   // Getters e Setters
   public int getId() {
      return id;
   }

   public String getNome() {
      return nome;
   }

   public int getSaldo() {
      return saldo;
   }

   public void setId(int id) {
      this.id = id;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public void setSaldo(int saldo) {
      this.saldo = saldo;
   }
}
