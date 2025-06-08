// src/main/java/com/example/demo/model/Empresa.java
package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn; // Novo import
import jakarta.persistence.Table; // Novo import

@Entity
@Table(name = "empresa") // Mapeia para a tabela 'empresa'
@PrimaryKeyJoinColumn(name = "id") // <-- IMPORTANTE: Indica que o ID da Empresa é o mesmo ID do Usuario
public class Empresa extends Usuario { // <-- Herda de Usuario
    
    @Column(unique = true, nullable = false)
    private String cnpj;
    
    @Column(nullable = false)
    private String nomeFantasia;


    // Getters e Setters (apenas para os campos específicos de Empresa)
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }
}