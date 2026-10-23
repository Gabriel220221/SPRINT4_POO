package br.com.motiva.dto;

import br.com.motiva.model.TrechoRodovia;

public record TrechoResponse(Long id, String rodovia, Double kmInicial, Double kmFinal, String tipo, Double nivelVegetacaoCm) {
    public static TrechoResponse from(TrechoRodovia t) {
        return new TrechoResponse(t.getId(), t.getRodovia(), t.getKmInicial(), t.getKmFinal(), t.getTipo(), t.getNivelVegetacaoCm());
    }
}
