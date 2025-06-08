package com.example.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank; // Para campos de texto obrigatórios e não vazios
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size; // Para limitar o tamanho da mensagem

public class TransferenciaMoedasDTO {

    @NotNull(message = "O ID do aluno não pode ser nulo.")
    private Long alunoId;

    @NotNull(message = "A quantidade de moedas não pode ser nula.")
    @Min(value = 1, message = "A quantidade de moedas deve ser no mínimo 1.")
    private int quantidade;

    @NotBlank(message = "O motivo da transferência é obrigatório.") // Torna o campo obrigatório e não vazio
    @Size(max = 500, message = "O motivo deve ter no máximo 500 caracteres.") // Limita o tamanho
    private String motivo; // Novo campo

    // Construtor padrão
    public TransferenciaMoedasDTO() {
    }

    // Construtor com todos os campos
    public TransferenciaMoedasDTO(Long alunoId, int quantidade, String motivo) { // Atualizado
        this.alunoId = alunoId;
        this.quantidade = quantidade;
        this.motivo = motivo; // Novo
    }

    // Getters e Setters
    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getMotivo() { // Novo Getter
        return motivo;
    }

    public void setMotivo(String motivo) { // Novo Setter
        this.motivo = motivo;
    }
}