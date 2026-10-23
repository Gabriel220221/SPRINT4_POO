package br.com.motiva.service;

import br.com.motiva.dto.EquipeRequest;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.EquipeManutencao;
import br.com.motiva.repository.EquipeManutencaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipeService {

    private final EquipeManutencaoRepository repository;

    public EquipeService(EquipeManutencaoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EquipeManutencao> listar(String especialidade, Boolean apenasAtivas) {
        if (especialidade != null) return repository.findByEspecialidade(especialidade.toUpperCase());
        if (Boolean.TRUE.equals(apenasAtivas)) return repository.findByAtivaTrue();
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public EquipeManutencao buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Equipe", id));
    }

    @Transactional
    public EquipeManutencao criar(EquipeRequest req) {
        return repository.save(aplicar(new EquipeManutencao(), req));
    }

    @Transactional
    public EquipeManutencao atualizar(Long id, EquipeRequest req) {
        return repository.save(aplicar(buscar(id), req));
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscar(id));
    }

    private EquipeManutencao aplicar(EquipeManutencao e, EquipeRequest req) {
        if (req.qtdMembros() == null || req.qtdMembros() <= 0) {
            throw new RegraNegocioException("Uma equipe precisa ter ao menos 1 membro");
        }
        e.setNome(req.nome().trim());
        e.setEspecialidade(req.especialidade().trim().toUpperCase());
        e.setQtdMembros(req.qtdMembros());
        e.setAtiva(req.ativa() == null ? Boolean.TRUE : req.ativa());
        return e;
    }
}
