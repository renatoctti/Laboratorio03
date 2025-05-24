package com.example.demo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "vantagens")
public class Vantagem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Column(nullable = false)
    private String descricao;
    
    @Column(columnDefinition = "TEXT")
    private String foto;
    
    @NotNull(message = "Custo é obrigatório")
    @Positive(message = "Custo deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal custoMoedas;
    
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private EmpresaParceira empresa;
    
    // Constructors
    public Vantagem() {}
    
    public Vantagem(String descricao, String foto, BigDecimal custoMoedas, EmpresaParceira empresa) {
        this.descricao = descricao;
        this.foto = foto;
        this.custoMoedas = custoMoedas;
        this.empresa = empresa;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    
    public BigDecimal getCustoMoedas() { return custoMoedas; }
    public void setCustoMoedas(BigDecimal custoMoedas) { this.custoMoedas = custoMoedas; }
    
    public EmpresaParceira getEmpresa() { return empresa; }
    public void setEmpresa(EmpresaParceira empresa) { this.empresa = empresa; }
}
