// src/main/java/com/example/demo/model/Usuario.java
package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario") // Garante que a tabela se chama 'usuario'
@Inheritance(strategy = InheritanceType.JOINED) // <-- ESSA É A CHAVE! Define a estratégia de herança
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Email deve ser único e não nulo
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String role; // Ex: "ALUNO", "EMPRESA", "PROFESSOR"

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}