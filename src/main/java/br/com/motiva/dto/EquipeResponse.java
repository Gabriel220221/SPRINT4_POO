package br.com.motiva.dto;

import br.com.motiva.model.EquipeManutencao;

public record EquipeResponse(Long id, String nome, String especialidade, Integer qtdMembros, Boolean ativa) {
    public static EquipeResponse from(EquipeManutencao e) {
        return new EquipeResponse(e.getId(), e.getNome(), e.getEspecialidade(), e.getQtdMembros(), e.getAtiva());
    }
}
