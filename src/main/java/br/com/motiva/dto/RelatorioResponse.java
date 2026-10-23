package br.com.motiva.dto;

import br.com.motiva.model.ItemRelatorio;
import br.com.motiva.model.NivelPrioridade;
import br.com.motiva.model.RelatorioPrioridade;
import java.time.LocalDateTime;
import java.util.List;

public record RelatorioResponse(Long id, LocalDateTime dataGeracao, Integer totalTrechos,
                                Integer urgentes, Integer criticos, Integer atencao, Integer normais,
                                List<Item> itens) {

    public record Item(Long trechoId, String rodovia, Double nivelVegetacaoCm, NivelPrioridade classificacao) {
        static Item from(ItemRelatorio i) {
            return new Item(i.getTrechoId(), i.getRodovia(), i.getNivelVegetacaoCm(), i.getClassificacao());
        }
    }

    public static RelatorioResponse from(RelatorioPrioridade r) {
        return new RelatorioResponse(r.getId(), r.getDataGeracao(), r.getTotalTrechos(),
                r.getQtdUrgente(), r.getQtdCritico(), r.getQtdAtencao(), r.getQtdNormal(),
                r.getItens().stream().map(Item::from).toList());
    }
}
