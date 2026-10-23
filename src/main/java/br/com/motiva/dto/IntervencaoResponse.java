package br.com.motiva.dto;

import br.com.motiva.model.Intervencao;
import java.time.LocalDate;

public record IntervencaoResponse(Long id, String descricao, String status, LocalDate dataPrevista,
                                  Long trechoId, String rodovia, Long equipeId, String equipeNome) {
    public static IntervencaoResponse from(Intervencao i) {
        return new IntervencaoResponse(i.getId(), i.getDescricao(), i.getStatus(), i.getDataPrevista(),
                i.getTrecho().getId(), i.getTrecho().getRodovia(), i.getEquipe().getId(), i.getEquipe().getNome());
    }
}
