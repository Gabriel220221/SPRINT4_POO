package br.com.motiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TrechoRequest(
        @NotBlank(message = "rodovia é obrigatória") String rodovia,
        @NotNull(message = "kmInicial é obrigatório") @PositiveOrZero(message = "kmInicial não pode ser negativo") Double kmInicial,
        @NotNull(message = "kmFinal é obrigatório") @PositiveOrZero(message = "kmFinal não pode ser negativo") Double kmFinal,
        @NotBlank(message = "tipo é obrigatório") String tipo,
        @NotNull(message = "nivelVegetacaoCm é obrigatório") @PositiveOrZero(message = "nivelVegetacaoCm não pode ser negativo") Double nivelVegetacaoCm) {
}
