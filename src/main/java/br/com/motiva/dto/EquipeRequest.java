package br.com.motiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EquipeRequest(
        @NotBlank(message = "nome é obrigatório") String nome,
        @NotBlank(message = "especialidade é obrigatória") String especialidade,
        @NotNull(message = "qtdMembros é obrigatório") @Positive(message = "qtdMembros deve ser positivo") Integer qtdMembros,
        Boolean ativa) {
}
