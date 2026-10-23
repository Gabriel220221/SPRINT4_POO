package br.com.motiva.service;

import br.com.motiva.dto.IntervencaoRequest;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.model.Intervencao;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.EquipeManutencaoRepository;
import br.com.motiva.repository.IntervencaoRepository;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class IntervencaoService {

    private static final Set<String> STATUS_VALIDOS = Set.of("PLANEJADA", "EM_ANDAMENTO", "CONCLUIDA");

    private final IntervencaoRepository repository;
    private final TrechoRodoviaRepository trechoRepository;
    private final EquipeManutencaoRepository equipeRepository;

    public IntervencaoService(IntervencaoRepository repository, TrechoRodoviaRepository trechoRepository,
                              EquipeManutencaoRepository equipeRepository) {
        this.repository = repository;
        this.trechoRepository = trechoRepository;
        this.equipeRepository = equipeRepository;
    }

    @Transactional(readOnly = true)
    public List<Intervencao> listar(String status, Long trechoId) {
        if (status != null) return repository.findByStatus(status.toUpperCase());
        if (trechoId != null) return repository.findByTrechoId(trechoId);
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Intervencao buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Intervenção", id));
    }

    @Transactional
    public Intervencao criar(IntervencaoRequest req) {
        return repository.save(aplicar(new Intervencao(), req));
    }

    @Transactional
    public Intervencao atualizar(Long id, IntervencaoRequest req) {
        return repository.save(aplicar(buscar(id), req));
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscar(id));
    }

    private Intervencao aplicar(Intervencao i, IntervencaoRequest req) {
        TrechoRodovia trecho = trechoRepository.findById(req.trechoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Trecho", req.trechoId()));
        EquipeManutencao equipe = equipeRepository.findById(req.equipeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipe", req.equipeId()));
        if (!Boolean.TRUE.equals(equipe.getAtiva())) {
            throw new RegraNegocioException("A equipe " + equipe.getNome() + " está inativa");
        }
        String status = req.status() == null ? "PLANEJADA" : req.status().trim().toUpperCase();
        if (!STATUS_VALIDOS.contains(status)) {
            throw new RegraNegocioException("Status inválido. Use: " + STATUS_VALIDOS);
        }
        i.setDescricao(req.descricao().trim());
        i.setStatus(status);
        i.setDataPrevista(req.dataPrevista());
        i.setTrecho(trecho);
        i.setEquipe(equipe);
        return i;
    }
}
