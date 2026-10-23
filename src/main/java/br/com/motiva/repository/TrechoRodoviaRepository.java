package br.com.motiva.repository;

import br.com.motiva.model.TrechoRodovia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TrechoRodoviaRepository extends JpaRepository<TrechoRodovia, Long> {
    List<TrechoRodovia> findByNivelVegetacaoCmGreaterThanEqual(Double minimo);
    List<TrechoRodovia> findByTipo(String tipo);
    List<TrechoRodovia> findByRodoviaIgnoreCaseOrderByKmInicialAsc(String rodovia);
}
