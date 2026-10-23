package br.com.motiva.service;

import br.com.motiva.dto.TrechoRequest;
import br.com.motiva.exception.RecursoNaoEncontradoException;
import br.com.motiva.exception.RegraNegocioException;
import br.com.motiva.model.TrechoRodovia;
import br.com.motiva.repository.TrechoRodoviaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrechoService {

    private final TrechoRodoviaRepository repository;

    public TrechoService(TrechoRodoviaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TrechoRodovia> listar(String tipo, Double nivelMinimo) {
        if (tipo != null) return repository.findByTipo(tipo);
        if (nivelMinimo != null) return repository.findByNivelVegetacaoCmGreaterThanEqual(nivelMinimo);
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public TrechoRodovia buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Trecho", id));
    }

    @Transactional
    public TrechoRodovia criar(TrechoRequest req) {
        return repository.save(aplicar(new TrechoRodovia(), req));
    }

    @Transactional
    public TrechoRodovia atualizar(Long id, TrechoRequest req) {
        return repository.save(aplicar(buscar(id), req));
    }

    @Transactional
    public void remover(Long id) {
        repository.delete(buscar(id));
    }

    // Regras de negócio da Sprint 1 vivem aqui (nunca no Controller).
    private TrechoRodovia aplicar(TrechoRodovia t, TrechoRequest req) {
        if (req.nivelVegetacaoCm() == null || req.nivelVegetacaoCm() < 0) {
            throw new RegraNegocioException("O nível de vegetação (cm) não pode ser negativo");
        }
        if (req.kmInicial() == null || req.kmFinal() == null || req.kmInicial() < 0 || req.kmFinal() < req.kmInicial()) {
            throw new RegraNegocioException("Quilometragem inválida: kmFinal deve ser >= kmInicial e ambos >= 0");
        }
        t.setRodovia(req.rodovia().trim());
        t.setKmInicial(req.kmInicial());
        t.setKmFinal(req.kmFinal());
        t.setTipo(req.tipo().trim().toUpperCase());
        t.setNivelVegetacaoCm(req.nivelVegetacaoCm());
        return t;
    }
}
