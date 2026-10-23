package br.com.motiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record IntervencaoRequest(
        @NotBlank(message = "descricao é obrigatória") String descricao,
        String status,
        @NotNull(message = "dataPrevista é obrigatória (yyyy-MM-dd)") LocalDate dataPrevista,
        @NotNull(message = "trechoId é obrigatório") Long trechoId,
        @NotNull(message = "equipeId é obrigatório") Long equipeId) {
}
