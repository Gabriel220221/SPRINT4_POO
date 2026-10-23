package br.com.motiva.service;

import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.ItemRelatorio;
import br.com.motiva.model.NivelPrioridade;
import br.com.motiva.model.RelatorioPrioridade;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.RelatorioPrioridadeRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

/** Motor de prioridade (Sprint 2/3) transformado em serviço Spring. */
@Service
public class RelatorioService {

    // Limiares em cm (ajuste para os da SUA Sprint 2, se forem diferentes)
    public static final double LIMIAR_URGENTE = 80.0;
    public static final double LIMIAR_CRITICO = 50.0;
    public static final double LIMIAR_ATENCAO = 25.0;

    private final RelatorioPrioridadeRepository relatorioRepository;
    private final TrechoRodoviaRepository trechoRepository;

    public RelatorioService(RelatorioPrioridadeRepository relatorioRepository, TrechoRodoviaRepository trechoRepository) {
        this.relatorioRepository = relatorioRepository;
        this.trechoRepository = trechoRepository;
    }

    public NivelPrioridade classificar(double nivelCm) {
        if (nivelCm < 0) throw new RegraNegocioException("O nível de vegetação (cm) não pode ser negativo");
        if (nivelCm >= LIMIAR_URGENTE) return NivelPrioridade.URGENTE;
        if (nivelCm >= LIMIAR_CRITICO) return NivelPrioridade.CRITICO;
        if (nivelCm >= LIMIAR_ATENCAO) return NivelPrioridade.ATENCAO;
        return NivelPrioridade.NORMAL;
    }

    @Transactional
    public RelatorioPrioridade gerar() {
        List<TrechoRodovia> trechos = trechoRepository.findAll();
        if (trechos.isEmpty()) {
            throw new RegraNegocioException("Não há trechos cadastrados para gerar o relatório");
        }
        RelatorioPrioridade rel = new RelatorioPrioridade();
        rel.setDataGeracao(LocalDateTime.now());
        int urg = 0, cri = 0, ate = 0, nor = 0;

        List<TrechoRodovia> ordenados = trechos.stream()
                .sorted(Comparator.comparing(TrechoRodovia::getNivelVegetacaoCm).reversed()).toList();
        for (TrechoRodovia t : ordenados) {
            NivelPrioridade nivel = classificar(t.getNivelVegetacaoCm());
            switch (nivel) {
                case URGENTE -> urg++;
                case CRITICO -> cri++;
                case ATENCAO -> ate++;
                case NORMAL -> nor++;
            }
            ItemRelatorio item = new ItemRelatorio();
            item.setTrechoId(t.getId());
            item.setRodovia(t.getRodovia());
            item.setNivelVegetacaoCm(t.getNivelVegetacaoCm());
            item.setClassificacao(nivel);
            rel.adicionarItem(item);
        }
        rel.setTotalTrechos(trechos.size());
        rel.setQtdUrgente(urg);
        rel.setQtdCritico(cri);
        rel.setQtdAtencao(ate);
        rel.setQtdNormal(nor);
        return relatorioRepository.save(rel);
    }

    @Transactional(readOnly = true)
    public List<RelatorioPrioridade> historico() {
        return relatorioRepository.findAllByOrderByDataGeracaoDesc();
    }

    @Transactional(readOnly = true)
    public List<RelatorioPrioridade> porPeriodo(LocalDate inicio, LocalDate fim) {
        if (fim.isBefore(inicio)) {
            throw new RegraNegocioException("A data 'fim' não pode ser anterior à data 'inicio'");
        }
        return relatorioRepository.findByDataGeracaoBetweenOrderByDataGeracaoDesc(
                inicio.atStartOfDay(), fim.atTime(LocalTime.MAX));
    }
}
